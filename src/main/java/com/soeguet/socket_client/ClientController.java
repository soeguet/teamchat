package com.soeguet.socket_client;

import com.soeguet.behaviour.SocketToGuiInterface;
import com.soeguet.gui.main_frame.MainFrameInterface;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Logger;

public class ClientController {

    private final Logger logger = Logger.getLogger(ClientController.class.getName());
    private final MainFrameInterface mainFrame;
    private URI serverUri;
    private CustomWebsocketClient websocketClient;
    private final SocketToGuiInterface guiFunctionality;

    public ClientController(final MainFrameInterface mainFrame, final SocketToGuiInterface guiFunctionality) {

        this.mainFrame = mainFrame;
        this.guiFunctionality = guiFunctionality;
    }

    public void initClient() {

        determineWebsocketURI();
        connectToWebsocket();
    }

    protected void determineWebsocketURI() {

        if (readServerIp().isEmpty() || readServerPort().isEmpty()) {

            serverInformationOptionPane();
            return;
        }

        try {

            serverUri = createUri(readServerIp().get(), readServerPort().get());

        } catch (URISyntaxException e) {

            logger.severe("Error creating URI: " + e.getMessage());
        }
    }

    public void connectToWebsocket() {

        websocketClient = new CustomWebsocketClient(serverUri, mainFrame, guiFunctionality);
        websocketClient.connect();
    }

    private Optional<String> readServerIp() {

        return Optional.ofNullable(System.getenv("CHAT_IP"));
    }

    private Optional<String> readServerPort() {

        return Optional.ofNullable(System.getenv("CHAT_PORT"));
    }

    public void serverInformationOptionPane() {

        JTextField serverIpTextField = new JTextField(7);
        JTextField serverPortTextField = new JTextField(7);

        final JPanel serverInfoPanel = createServerInfoPanel(serverIpTextField, serverPortTextField);

        int result = JOptionPane.showConfirmDialog(null, serverInfoPanel, "please enter ip and port values", JOptionPane.OK_CANCEL_OPTION);

        validateServerInformationInputByUser(serverIpTextField.getText(), serverPortTextField.getText());

        if (result == JOptionPane.OK_OPTION) {

            processValidatedServerInformation(serverIpTextField, serverPortTextField);
        }
    }

    private URI createUri(String serverIp, String serverPort) throws URISyntaxException {

        return new URI("ws://" + serverIp + ":" + serverPort);
    }

    private JPanel createServerInfoPanel(final JTextField serverIpTextField, final JTextField serverPortTextField) {

        JPanel myPanel = new JPanel(new MigLayout("wrap 2"));

        //port information
        myPanel.add(new JLabel("Port:"));
        String defaultServerIp = "127.0.0.1";
        serverIpTextField.setText(readServerIp().isEmpty() ? defaultServerIp : readServerIp().get());
        myPanel.add(serverIpTextField);

        //ip information
        myPanel.add(new JLabel("Ip:"));
        String defaultServerPort = "8100";
        serverPortTextField.setText(readServerPort().isEmpty() ? defaultServerPort : readServerPort().get());

        myPanel.add(serverPortTextField);

        serverIpTextField.requestFocus();

        return myPanel;
    }

    private void validateServerInformationInputByUser(final String serverIpText, final String serverPortText) {

        StringBuilder errorMessage = new StringBuilder();

        if (serverIpText.isEmpty() || serverPortText.isEmpty()) {
            errorMessage.append("Server IP or port is empty.\n");
        }

        if (!serverIpText.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            errorMessage.append("Server IP is invalid.\n");
        }

        if (!serverPortText.matches("^[0-9]+$")) {
            errorMessage.append("Server port is invalid.\n");
        }

        if (!errorMessage.isEmpty()) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE));
        }
    }

    private void processValidatedServerInformation(final JTextField serverIpTextField, final JTextField serverPortTextField) {

        String serverIp = serverIpTextField.getText();
        String serverPort = serverPortTextField.getText();

        try {

            serverUri = new URI("ws://" + serverIp + ":" + serverPort);

        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }
    }

    public CustomWebsocketClient getWebsocketClient() {

        return websocketClient;
    }

    public void closeConnection() {

        if (websocketClient != null) {

            websocketClient.close();
        }
    }


    /**
     Prepares the application for reconnection.

     Resets the websocket client to null, sets the last message sender name and timestamp to null,
     and sets the startUp flag to true to disable notifications during initial message flood.
     */
    public void prepareReconnection() {

        this.websocketClient = null;

        //new evaluation of last sender and time
        mainFrame.setLastMessageSenderName(null);
        mainFrame.setLastMessageTimeStamp(null);

        //no notifications during initial message flood
        mainFrame.setStartUp(true);
    }
}