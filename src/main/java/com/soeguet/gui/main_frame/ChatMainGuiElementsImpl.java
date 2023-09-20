package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.gui.image_panel.ImagePanelImpl;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.gui.properties.PropertiesPanelImpl;
import com.soeguet.properties.CustomProperties;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.util.EmojiHandler;
import com.soeguet.util.EmojiInitializer;
import com.soeguet.util.EmojiPopUpMenuHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 This class represents a GUI implementation for a chat application. It extends the ChatPanel class
 and implements the MainGuiInterface.
 */
public class ChatMainGuiElementsImpl extends ChatPanel implements MainGuiElementsInterface {

    private final Logger logger = Logger.getLogger(ChatMainGuiElementsImpl.class.getName());
    private final LinkedBlockingDeque<String> messageQueue;
    private final HashMap<String, CustomUserProperties> chatClientPropertiesHashMap;
    private final LinkedBlockingDeque<String> clientMessageQueue;
    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;
    private GuiFunctionality guiFunctionality;
    private URI serverUri;
    private int JSCROLLPANE_MARGIN_RIGHT_BORDER;
    private int JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    private HashMap<String, ImageIcon> emojiList;
    private EmojiHandler emojiHandler;
    private CustomWebsocketClient websocketClient;
    private String username = "yasman";
    private JPanel messagePanel;


    private AtomicBoolean isProcessingClientMessages = new AtomicBoolean(false);

    /**
     Initializes the main GUI elements for the chat application.
     Sets up the GUI functionality, calculates the margins for the scroll pane,
     initializes the emoji handler and list, and establishes a WebSocket connection.
     */
    public ChatMainGuiElementsImpl() {

        clientMessageQueue = new LinkedBlockingDeque<>();
        messageQueue = new LinkedBlockingDeque<>();
        chatClientPropertiesHashMap = new HashMap<>();
        objectMapper = new ObjectMapper();
        customProperties = new CustomProperties(this);

        initGuiFunctionality();

        setScrollPaneMargins();
        initEmojiHandlerAndList();
        initWebSocketClient();
    }

    private void initGuiFunctionality() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        guiFunctionality = new GuiFunctionality(this);
    }

    private void setScrollPaneMargins() {

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 63;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 20;
        } else {
            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 56;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 3;
        }
    }

    private void initEmojiHandlerAndList() {

        emojiHandler = new EmojiHandler(this);
        emojiList = new EmojiInitializer().createEmojiList();
    }

    private void initWebSocketClient() {

        try {
            serverUri = new URI("ws://127.0.0.1:8100");
            websocketClient = new CustomWebsocketClient(serverUri, this);
            websocketClient.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     {@inheritDoc}

     <p>This method is called when a property change event occurs. It logs the provided event and
     method name.

     @param e The property change event to be handled. Must not be null.
     */
    @Override
    protected void thisPropertyChange(PropertyChangeEvent e) {

    }

    /**
     Method called when the component is resized.

     @param e The ComponentEvent object representing the resize event.
     */
    @Override
    protected void thisComponentResized(ComponentEvent e) {

        SwingUtilities.invokeLater(() -> {

            this.getMainTextBackgroundScrollPane().setBounds(1, 1, e.getComponent().getWidth() - JSCROLLPANE_MARGIN_RIGHT_BORDER, e.getComponent().getHeight() - this.getInteractionAreaPanel().getHeight() - JSCROLLPANE_MARGIN_BOTTOM_BORDER);
            this.revalidate();
            this.repaint();
        });
    }    public AtomicBoolean getIsProcessingClientMessages() {

        return isProcessingClientMessages;
    }

    /**
     Handles the event when the mouse clicks the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void thisMouseClicked(MouseEvent e) {

    }

    /**
     Called when the mouse is pressed on the property menu item.

     @param e The MouseEvent object representing the event.
     */
    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        new PropertiesPanelImpl(this);
    }    public void setIsProcessingClientMessages(AtomicBoolean isProcessingClientMessages) {

        this.isProcessingClientMessages = isProcessingClientMessages;
    }

    /**
     Resets the connection when the reset connection menu item is pressed.

     @param e The mouse event that triggered the method.
     */
    @Override
    protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

        form_mainTextPanel.removeAll();
        form_mainTextPanel.revalidate();
        form_mainTextPanel.repaint();

        if (getWebsocketClient().isOpen()) {

            logger.info("Closing websocket client");
            getWebsocketClient().close();
        }

        new Timer(1000, event -> {

            if (getWebsocketClient().isClosed()) {

                logger.info("Reconnecting websocket client");

                setWebsocketClient(new CustomWebsocketClient(serverUri, this));
                getWebsocketClient().connect();
            }

        }).start();
    }

    /**
     Retrieves the WebSocket client.

     @return The WebSocket client.
     */
    public CustomWebsocketClient getWebsocketClient() {

        return websocketClient;
    }

    /**
     Gets the ObjectMapper instance used for converting JSON to Java objects and vice versa.

     @return the ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }

    /**
     Retrieves the GuiFunctionality.

     @return the GuiFunctionality object.
     */
    public GuiFunctionality getGuiFunctionality() {

        return guiFunctionality;
    }

    /**
     Retrieves the username.

     @return the username as a String.
     */
    public String getUsername() {

        return username;
    }

    /**
     Sets the username.

     @param username the username to set.
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     Retrieves the main JFrame of the application.

     @return the main JFrame object.
     */
    @Override
    public JFrame getMainFrame() {

        return this;
    }

    /**
     Retrieves the message panel.

     @return the JPanel object representing the message panel.
     */
    public JPanel getMessagePanel() {

        return messagePanel;
    }

    /**
     Sets the message panel for displaying messages.

     @param messagePanel the JPanel to set as the message panel.
     */
    public void setMessagePanel(JPanel messagePanel) {

        this.messagePanel = messagePanel;
    }

    /**
     Retrieves the message queue containing the messages.

     @return the LinkedBlockingDeque<String> representing the message queue.
     */
    public LinkedBlockingDeque<String> getMessageQueue() {

        return messageQueue;
    }

    /**
     Returns a HashMap containing the list of emojis and their corresponding image icons.

     <p>This method returns the emojiList HashMap, which contains the emojis and their corresponding image icons.
     The key in the HashMap represents the emoji text, and the value represents the corresponding image icon.

     @return the HashMap containing the list of emojis and their corresponding image icons
     */
    public HashMap<String, ImageIcon> getEmojiList() {

        return emojiList;
    }

    public HashMap<String, CustomUserProperties> getChatClientPropertiesHashMap() {

        return chatClientPropertiesHashMap;
    }

    /**
     Returns the custom properties object.

     @return the custom properties object.
     */
    public CustomProperties getCustomProperties() {

        return customProperties;
    }

    /**
     Retrieves the client message queue.

     @return A LinkedBlockingDeque object representing the client message queue.
     */
    public LinkedBlockingDeque<String> getClientMessageQueue() {

        return clientMessageQueue;
    }

    public void setWebsocketClient(CustomWebsocketClient websocketClient) {

        this.websocketClient = websocketClient;
    }

    /**
     Handles the event when the mouse presses the exit menu item. Sets the default close operation
     for the current JFrame to EXIT_ON_CLOSE and disposes the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();

        System.exit(0);
    }

    /**
     Called when the participant menu item is pressed. Logs the provided event and method name.

     @param e The MouseEvent associated with the button press.
     */
    @Override
    protected void participantsMenuItemMousePressed(MouseEvent e) {

    }

    /**
     Called when the main text panel is clicked. Logs the provided event and method name.

     @param e The MouseEvent associated with the click event.
     */
    @Override
    protected void mainTextPanelMouseClicked(MouseEvent e) {

    }

    /**
     {@inheritDoc}

     <p>This method is called when the mouse is clicked on the text editor pane in the ChatGuiImpl
     class. It logs the method call with the provided MouseEvent object and the method name.

     @param e the MouseEvent object representing the mouse click event on the text editor pane
     */
    @Override
    protected void textEditorPaneMouseClicked(MouseEvent e) {

    }

    /**
     Called when a key is pressed in the text editor pane. If the pressed key is not the Enter key,
     the method simply returns. If the pressed key is the Enter key, it consumes the event and
     performs the appropriate action based on whether the Shift key is pressed or not.

     @param e The KeyEvent object representing the key press event.
     */
    @Override
    protected void textEditorPaneKeyPressed(KeyEvent e) {

        if (e.getKeyCode() != KeyEvent.VK_ENTER) {

            return;
        }

        e.consume();

        if (e.isShiftDown()) {

            appendNewLineToTextEditorPane();

            return;
        }

        handleNonShiftEnterKeyPress();
    }

    /**
     Appends a new line to the text editor pane.

     <p>Retrieves the current text in the text editor pane and appends a new line character at the
     end of it.
     */
    private void appendNewLineToTextEditorPane() {

        String currentText = getTextEditorPane().getText();
        getTextEditorPane().setText(currentText + "\n");
    }

    /**
     Handles a key press event when the enter key is pressed without pressing the shift key.

     <p>Retrieves the content of the text editor pane, trims any leading or trailing space, and
     checks if it is empty. If the content is empty, it clears the text editor pane. Otherwise, it
     calls the `clearTextPaneAndSendMessageToSocket` method to clear the text pane and send the
     current content to a socket.
     */
    private void handleNonShiftEnterKeyPress() {

        String textPaneContent = getTextEditorPane().getText().trim();

        if (textPaneContent.isEmpty()) {

            getTextEditorPane().setText("");

        } else {

            guiFunctionality.clearTextPaneAndSendMessageToSocket();

        }

    }

    /**
     Invoked when a key is released in the text editor pane.

     <p>This method is an override of the textEditorPaneKeyReleased method from the superclass. It
     is called when a key is released in the text editor pane.

     @param e the KeyEvent object generated when a key is released
     */
    @Override
    protected void textEditorPaneKeyReleased(KeyEvent e) {

    }

    /**
     Handles the event when the mouse clicks the picture button in the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        new ImagePanelImpl(this);
    }

    /**
     Invoked when the emoji button is clicked in the chat GUI.

     <p>This method is an override of the emojiButton method from the superclass. It is called when
     the emoji button is clicked.

     @param e the ActionEvent object generated when the emoji button is clicked
     */
    @Override
    protected void emojiButton(ActionEvent e) {

        new EmojiPopUpMenuHandler(this, this.getTextEditorPane(), this.getEmojiButton());
    }

    /**
     Handles the event when the send-button is clicked.
     Clears the text pane in the GUI and sends
     the message to the socket.

     @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        emojiHandler.replaceImageIconWithEmojiDescription(getTextEditorPane());

        if (getTextEditorPane().getText().trim().isEmpty()) {

            return;
        }

        guiFunctionality.clearTextPaneAndSendMessageToSocket();
    }





}