package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.socket_client.CustomWebsocketClient;
import com.soeguet.util.EmojiHandler;
import com.soeguet.util.EmojiInitializer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.EventObject;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 This class represents a GUI implementation for a chat application. It extends the ChatPanel class
 and implements the MainGuiInterface.
 */
public class ChatMainGuiElementsImpl extends ChatPanel implements MainGuiElementsInterface {

    private final Logger logger = Logger.getLogger(ChatMainGuiElementsImpl.class.getName());

    private GuiFunctionality guiFunctionality;
    private URI serverUri;
    private final ArrayDeque<String> messageQueue = new ArrayDeque<>();
    private int JSCROLLPANE_MARGIN_RIGHT_BORDER;
    private int JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    private HashMap<String, ImageIcon> emojiList;
    private EmojiHandler emojiHandler;
    ObjectMapper objectMapper = new ObjectMapper();
    private CustomWebsocketClient websocketClient;
    private String username = "osman - backoffice";
    private JPanel messagePanel;


    /**
     * Initializes the main GUI elements for the chat application.
     * Sets up the GUI functionality, calculates the margins for the scroll pane,
     * initializes the emoji handler and list, and establishes a WebSocket connection.
     */
    public ChatMainGuiElementsImpl() {
        initGuiFunctionality();
        setScrollPaneMargins();
        initEmojiHandlerAndList();
        initWebSocketClient();
    }

    private void initGuiFunctionality() {
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

        logMethod(e, "ChatGuiImpl.thisPropertyChange");
    }

    /**
     Logs the provided event and method name.

     @param event      The event to be logged.
     @param methodName The name of the method to be logged.
     */
    private void logMethod(EventObject event, String methodName) {

        System.out.println();
        System.out.println("__ " + methodName);
        logger.info(event.toString());
        System.out.println();
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
    }

    /**
     Handles the event when the mouse clicks the current JFrame.

     @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void thisMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.thisMouseClicked");
    }

    /**
     Called when the mouse is pressed on the property menu item.

     @param e The MouseEvent object representing the event.
     */
    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.propertiesMenuItemMousePressed");
    }

    /**
     Resets the connection when the reset connection menu item is pressed.

     @param e The mouse event that triggered the method.
     */
    @Override
    protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImp.resetConnectionMenuItemMousePressed");

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
     Returns the message queue.

     <p>This method retrieves the current message queue of the ChatMainGuiElementsImpl object.

     @return The message queue.
     */
    public ArrayDeque<String> getMessageQueue() {

        return messageQueue;
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

        logMethod(e, "ChatGuiImpl.participantsMenuItemMousePressed");
    }

    /**
     Called when the main text panel is clicked. Logs the provided event and method name.

     @param e The MouseEvent associated with the click event.
     */
    @Override
    protected void mainTextPanelMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.mainTextPanelMouseClicked");
    }

    /**
     {@inheritDoc}

     <p>This method is called when the mouse is clicked on the text editor pane in the ChatGuiImpl
     class. It logs the method call with the provided MouseEvent object and the method name.

     @param e the MouseEvent object representing the mouse click event on the text editor pane
     */
    @Override
    protected void textEditorPaneMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.textEditorPaneMouseClicked");
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

        logMethod(e, "ChatGuiImpl.pictureButtonMouseClicked");
    }

    /**
     Invoked when the emoji button is clicked in the chat GUI.

     <p>This method is an override of the emojiButton method from the superclass. It is called when
     the emoji button is clicked.

     @param e the ActionEvent object generated when the emoji button is clicked
     */
    @Override
    protected void emojiButton(ActionEvent e) {

        createEmojiPopupMenu();

        logMethod(e, "ChatGuiImpl.emojiButton");
    }

    /**
     Creates a pop-up menu with emojis.

     <p>This method is responsible for creating a pop-up menu and adding emojis to it. The pop-up
     menu is then displayed at the position of the emoji button.
     */
    private void createEmojiPopupMenu() {

        JPopupMenu emojiPopupMenu = new JPopupMenu();
        JPanel emojiPanelWrapper = createEmojiPanel();

        getEmojiList().forEach((key, emoji) -> {
            JPanel emojiPanelForOneEmoji = createEmojiPanelForOneEmoji(key, emoji);
            emojiPanelWrapper.add(emojiPanelForOneEmoji);
        });

        emojiPopupMenu.add(emojiPanelWrapper);
        emojiPopupMenu.show(form_emojiButton, form_emojiButton.getMousePosition().x, form_emojiButton.getMousePosition().y);
    }

    /**
     Creates a panel for displaying emojis.

     <p>This method creates a JPanel and sets its layout to MigLayout with "wrap 10"
     constraints to display emojis in a grid-like fashion. The panel is then returned.

     @return The JPanel for displaying emojis.
     */
    private JPanel createEmojiPanel() {

        JPanel emojiPanelWrapper = new JPanel();
        emojiPanelWrapper.setLayout(new MigLayout("wrap 10", "[center]", "[center]"));
        return emojiPanelWrapper;
    }

    /**
     Returns the list of emoji icons.

     <p>This method retrieves the list of emoji icons that are available for use in the chat GUI.

     @return the list of emoji icons
     */
    public HashMap<String, ImageIcon> getEmojiList() {

        return emojiList;
    }

    /**
     Creates a JPanel for displaying a single emoji.

     @param key   the key associated with the emoji
     @param emoji the ImageIcon representing the emoji

     @return the JPanel containing the emoji
     */
    private JPanel createEmojiPanelForOneEmoji(String key, ImageIcon emoji) {

        JPanel emojiPanelForOneEmoji = new JPanel();
        JLabel emojiLabel = new JLabel(emoji);
        emojiPanelForOneEmoji.add(emojiLabel);
        emojiPanelForOneEmoji.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        addMouseListenerToEmojiPanel(emojiPanelForOneEmoji, key, emoji);

        return emojiPanelForOneEmoji;
    }

    /**
     Adds a mouse listener to the specified emoji panel.

     @param emojiPanelForOneEmoji the JPanel to add the mouse listener to
     @param key                   the key associated with the emoji
     @param emoji                 the ImageIcon representing the emoji
     */
    private void addMouseListenerToEmojiPanel(JPanel emojiPanelForOneEmoji, String key, ImageIcon emoji) {

        emojiPanelForOneEmoji.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                insertEmojiAtCaretPosition(key, emoji);
            }

            public void mouseEntered(MouseEvent evt) {

                emojiPanelForOneEmoji.setBackground(new Color(0, 136, 191));
            }

            public void mouseExited(MouseEvent evt) {

                emojiPanelForOneEmoji.setBackground(null);
            }
        });
    }

    /**
     Inserts an emoji at the current caret position in the text editor pane.

     @param key   the key associated with the emoji
     @param emoji the ImageIcon representing the emoji
     */
    private void insertEmojiAtCaretPosition(String key, ImageIcon emoji) {

        StyledDocument doc = getTextEditorPane().getStyledDocument();
        Style style = getTextEditorPane().addStyle("Image", null);

        ImageIcon imageIcon = new ImageIcon(emoji.getImage());
        imageIcon.setDescription(key);
        StyleConstants.setIcon(style, imageIcon);

        try {

            doc.insertString(getTextEditorPane().getCaretPosition(), " ", style);

        } catch (BadLocationException e) {

            logger.log(java.util.logging.Level.WARNING, e.getMessage(), e);

        }
    }

    /**
     Handles the event when the send button is clicked. Clears the text pane in the GUI and sends
     the message to the socket.

     @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        emojiHandler.replaceImageIconWithEmojiDescription(getTextEditorPane());

        guiFunctionality.clearTextPaneAndSendMessageToSocket();
    }

}
