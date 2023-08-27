package com.soeguet.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.client.CustomWebsocketClient;
import com.soeguet.gui.newcomment.Comment;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.gui.util.MessageTypes;
import com.soeguet.model.MessageModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class WebsocketInteraction implements Serializable {

    private static final int MAX_WIDTH = 350;
    private static final String PARTICIPANTS_PREFIX = "PARTICIPANTS:";
    private static final String TERMINATION_COMMAND = "/terminateAll";
    private static final String TYPING_MESSAGE = " typing..";
    private final ChatImpl chatImpl;
    Logger log = Logger.getLogger(WebsocketInteraction.class.getName());

    public WebsocketInteraction(ChatImpl chatImpl) {

        this.chatImpl = chatImpl;
    }

    public void onMessageReceived(String message) {

        createNewMessageOnPane(message);
    }

    /**
     This method is invoked when a WebSocket connection is closed,
     and it reconnects to the WebSocket.
     */
    public void onCloseReconnect() {

        CustomWebsocketClient.resetClient();
        connectToWebSocket();
    }

    public void onByteBufferMessageReceived(ByteBuffer bytes) {

        String message = new String(bytes.array());

        if (message.startsWith(PARTICIPANTS_PREFIX)) {
            String participantString = message.replace(PARTICIPANTS_PREFIX, "");
            chatImpl.setParticipantNameArray(participantString.split(","));
            return;
        }

        if (message.equals("X")) {
            chatImpl.getForm_typingLabel().setText(" ");
            return;
        }

        if (message.equals(TERMINATION_COMMAND)) {
            System.exit(0);
            return;
        }

        if (bytes.array().length > 50) {
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes.array()));
                if (image != null) {
                    processImage(image);
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            return;
        }

        updateTypingLabel(message);
    }

    /**
     Process the received image and add it to the chat panel.

     @param image The BufferedImage to be processed.
     */
    private void processImage(BufferedImage image) {

        ImageIcon icon = new ImageIcon((image.getWidth() > MAX_WIDTH) ? resizeImage(image) : image);
        JLabel label = new JLabel(icon);
        addImageClickListener(label, image);
        chatImpl.form_mainTextPanel.add(label, "center, wrap");
        chatImpl.updateFrame();
    }

    /**
     Resize the given image maintaining the aspect ratio according to the maximum width.

     @param image The original BufferedImage.

     @return The resized Image.
     */
    private Image resizeImage(BufferedImage image) {

        double scaleFactor = (double) MAX_WIDTH / image.getWidth();
        int newWidth = (int) (image.getWidth() * scaleFactor);
        int newHeight = (int) (image.getHeight() * scaleFactor);
        return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    private void addImageClickListener(JLabel label, BufferedImage image) {

        label.addMouseListener(new ImageClickListener(image));
    }

    private File createAndWriteTempImageFile(BufferedImage image) throws IOException {

        File tempFile = File.createTempFile("tempImage", ".png");
        ImageIO.write(image, "png", tempFile);
        return tempFile;
    }

    private void openTempImageFile(File tempFile) throws IOException {

        Desktop.getDesktop().open(tempFile);
    }

    private String getLabel() {

        return chatImpl.getForm_typingLabel().getText().trim();
    }

    private void setLabel(String text) {

        chatImpl.getForm_typingLabel().setText(text);
    }

    private void updateTypingLabel(String message) {

        String labelTxt = getLabel();
        StringBuilder newLabelTxt = new StringBuilder();

        if (!labelTxt.isEmpty() && !labelTxt.contains(message)) {

            newLabelTxt.append(labelTxt.replaceAll(TYPING_MESSAGE + "$", "")).append(", ");
        }

        newLabelTxt.append(message).append(TYPING_MESSAGE);

        setLabel(newLabelTxt.toString());
    }

    void createNewMessageOnPane(String message) {

        SwingUtilities.invokeLater(() -> {

            try {

                MessageModel messageModel = ChatImpl.MAPPER.readValue(message, MessageModel.class);

                if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.DELETED) {

                    Arrays.stream(chatImpl.getForm_mainTextPanel().getComponents()).filter(a -> a instanceof Comment).filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId())).forEach(c -> {
                        if (c.getClass().equals(PanelRightImpl.class)) {

                            ((PanelRightImpl) c).removeAll();
                            ((PanelRightImpl) c).add(new JLabel(messageModel.getTime() + " - " + messageModel.getMessage()), "cell 0 1");
                        } else {

                            ((PanelLeftImpl) c).removeAll();
                            ((PanelLeftImpl) c).add(new JLabel(messageModel.getTime() + " - " + messageModel.getMessage()), "cell 0 1");
                        }
                    });

                } else if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.INTERACTED) {

                    Arrays.stream(chatImpl.getForm_mainTextPanel().getComponents()).filter(a -> a instanceof Comment).filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId())).forEach(c -> {
                        if (c.getClass().equals(PanelRightImpl.class)) {

                            ((PanelRightImpl) c).addEmojiInteraction(messageModel);

                        } else {

                            ((PanelLeftImpl) c).addEmojiInteraction(messageModel);
                        }
                    });

                    SwingUtilities.invokeLater(chatImpl::repaint);

                } else if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.EDITED) {

                    Arrays.stream(chatImpl.getForm_mainTextPanel().getComponents()).filter(a -> a instanceof Comment).filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId())).forEach(c -> {
                        if (c.getClass().equals(PanelRightImpl.class)) {

                            SwingUtilities.invokeLater(() -> {
                                ((PanelRightImpl) c).getTextPaneComment().setText("");
                                ((PanelRightImpl) c).setTime(messageModel.getTime());
                                EmojiConverter.replaceWithEmoji(((PanelRightImpl) c).getTextPaneComment(), messageModel.getMessage());
                            });

                        } else {

                            ((PanelLeftImpl) c).getTextPaneComment().setText("");
                            EmojiConverter.replaceWithEmoji(((PanelLeftImpl) c).getTextPaneComment(), messageModel.getMessage());
                        }
                    });

                } else {

                    // check if message is from this client
                    if (messageModel.getSender().equals(ChatImpl.mapOfIps.get(ChatImpl.client.getLocalSocketAddress().getHostString()))) {
                        PanelRightImpl panelRight = new PanelRightImpl(messageModel, chatImpl.getLastMessageFrom(), chatImpl.getLastPostTime());
                        chatImpl.getForm_mainTextPanel().add(panelRight, "w 80%, trailing, wrap");
                    } else {
                        PanelLeftImpl panelLeft = new PanelLeftImpl(messageModel, chatImpl.getLastMessageFrom(), chatImpl.getLastPostTime());
                        chatImpl.getForm_mainTextPanel().add(panelLeft, " w 80%, wrap");
                    }

                    if (chatImpl.isStartup() && chatImpl.isVisible()) {
                        chatImpl.incomingMessagePreviewDesktopNotification(messageModel);
                    }
                }

                chatImpl.setLastMessageFrom(messageModel.getSender());
                chatImpl.setLastPostTime((messageModel.getTime().contains("*") ? "" : messageModel.getTime()));

            } catch (JsonProcessingException e) {

                JLabel label = new JLabel(message);
                label.setForeground(Color.RED);
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, 15));
                chatImpl.getForm_mainTextPanel().add(label, "wrap, align center");

                // initial 100 messages should not be sending pop-ups, after that -> ok
                chatImpl.setStartup(false);
                chatImpl.getInitialLoadingStartUpDialog().dispose();

            }

            chatImpl.updateFrame();
        });
    }

    /**
     Connects to a WebSocket server.

     This method performs the following steps:
     1. Displays a loading dialog.
     2. Initializes the WebSocket client.
     3. Connects the WebSocket client to the server.
     4. Adds the chat implementation as a listener to the WebSocket client.
     */
    void connectToWebSocket() {

        displayLoadingDialog();
        initializeWebSocketClient();
        connectWebSocketClient();
        addChatImplAsWebSocketListener();
    }

    /**
     Displays a loading dialog.

     This method is called to display a loading dialog during the process of connecting to a WebSocket server.
     The loading dialog is used to inform the user that the connection is being established and that they should wait.
     */
    private void displayLoadingDialog() {

        chatImpl.loadingInitialMessagesLoadUpDialog();
    }

    /**
     Initializes the WebSocket client.

     This method is called to initialize the WebSocket client. It creates an instance of the WebSocket client
     and assigns it to the 'client' variable in the ChatImpl class.
     */
    private void initializeWebSocketClient() {

        ChatImpl.client = getWebSocketInstance();
    }

    /**
     Returns an instance of the WebSocket client.

     This method is used to obtain the instance of the WebSocket client. It checks if an instance of the WebSocket client
     already exists and returns it. If no instance exists, a new instance of the WebSocket client is created using the
     CustomWebsocketClient.getInstance() method.

     @return an instance of the WebSocket client
     */
    private CustomWebsocketClient getWebSocketInstance() {

        return CustomWebsocketClient.getInstance();
    }

    /**
     Connects the WebSocket client to the server.

     This method is used to establish a connection between the WebSocket client and the server. It calls the
     connect() method on the client object to initiate the connection.

     The WebSocket client object must be initialized before calling this method.
     */
    private void connectWebSocketClient() {

        ChatImpl.client.connect();
    }

    /**
     Adds the ChatImpl instance as a WebSocket listener to the WebSocket client.

     This method is used to register the ChatImpl instance as a listener for WebSocket events on the WebSocket client.
     It calls the addListener() method on the client object and passes the ChatImpl instance as the listener.

     The ChatImpl instance and the WebSocket client object must be initialized before calling this method.
     */
    private void addChatImplAsWebSocketListener() {

        ChatImpl.client.addListener(chatImpl);
    }

    /**
     This class is an implementation of a MouseAdapter that handles the click
     event on an image.

     It provides a method to handle the image click event by creating a temporary file
     with the image and opening it.

     Please note that this class extends MouseAdapter and overrides the mouseClicked
     method to handle the image click event.
     */
    class ImageClickListener extends MouseAdapter {

        private final BufferedImage image;

        ImageClickListener(BufferedImage image) {

            this.image = image;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            handleImageClick();
        }

        private void handleImageClick() {

            try {
                processImageClick();
            } catch (IOException ex) {
                printTrace(ex);
            }
        }

        private void processImageClick() throws IOException {

            File tempFile = createAndWriteTempImageFile(image);
            openTempImageFile(tempFile);
        }

        private void printTrace(IOException ex) {

            log.info(ex.getMessage());
        }
    }

}
