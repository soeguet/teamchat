package com.soeguet.gui.main_frame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.socket_client.CustomWebsocketClient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayDeque;
import java.util.EventObject;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 This class represents a GUI implementation for a chat application. It extends the ChatPanel class
 and implements the MainGuiInterface.
 */
public class ChatMainGuiElementsImpl extends ChatPanel implements MainGuiElementsInterface {

    private final Logger logger = Logger.getLogger(ChatMainGuiElementsImpl.class.getName());

    private final GuiFunctionality guiFunctionality;
    private final URI serverUri;
    private final ArrayDeque<String> messageQueue = new ArrayDeque<>();
    private final int JSCROLLPANE_MARGIN_RIGHT_BORDER;
    private final int JSCROLLPANE_MARGIN_BOTTOM_BORDER;
    private final HashMap<String, ImageIcon> emojiList = createEmojiList();
    ObjectMapper objectMapper = new ObjectMapper();
    private CustomWebsocketClient websocketClient;
    private String username = "osman - backoffice";
    private JPanel messagePanel;

    /**
     Initializes the ChatMainGuiElementsImpl object.

     <p>It sets up the GUI functionality required for the chat application and establishes a
     connection to the WebSocket server.

     @throws RuntimeException if there is an error in the WebSocket URI syntax
     */
    public ChatMainGuiElementsImpl() {

        guiFunctionality = new GuiFunctionality(this);

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {

            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 63;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 20;

        } else {

            JSCROLLPANE_MARGIN_BOTTOM_BORDER = 56;
            JSCROLLPANE_MARGIN_RIGHT_BORDER = 3;

        }

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

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    @Override
    public JFrame getMainFrame() {

        return this;
    }

    public JPanel getMessagePanel() {

        return messagePanel;
    }

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
        JPanel emojiPanelWrapper = new JPanel();

        emojiPanelWrapper.setLayout(new MigLayout("wrap 10", "[center]", "[center]"));

        getEmojiList().forEach((key, emoji) -> {
            JPanel emojiPanelForOneEmoji = new JPanel();
            JLabel emojiLabel = new JLabel(emoji);
            emojiPanelForOneEmoji.add(emojiLabel);
            emojiPanelForOneEmoji.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            emojiPanelForOneEmoji.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    StyledDocument doc = getTextEditorPane().getStyledDocument();

                    Style style = getTextEditorPane().addStyle("Image", null);

                    ImageIcon imageIcon = new ImageIcon(emoji.getImage());
                    imageIcon.setDescription(key);
                    StyleConstants.setIcon(style, imageIcon);

                    try {

                        doc.insertString(getTextEditorPane().getCaretPosition(), " ", style);

                    } catch (BadLocationException ex) {

                        logger.info(ex.getMessage());
                    }
                }

                public void mouseEntered(java.awt.event.MouseEvent evt) {

                    emojiPanelForOneEmoji.setBackground(new Color(0, 136, 191));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {

                    emojiPanelForOneEmoji.setBackground(null);
                }
            });

            emojiPanelWrapper.add(emojiPanelForOneEmoji);
        });

        emojiPopupMenu.add(emojiPanelWrapper);
        emojiPopupMenu.show(form_emojiButton, form_emojiButton.getMousePosition().x, form_emojiButton.getMousePosition().y);
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
     Handles the event when the send button is clicked. Clears the text pane in the GUI and sends
     the message to the socket.

     @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        testEmojiFunction();

        guiFunctionality.clearTextPaneAndSendMessageToSocket();
    }

    private void testEmojiFunction() {

        Element root = getTextEditorPane().getStyledDocument().getDefaultRootElement();

        try {
            findImagesInElement(root);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private void findImagesInElement(Element element) throws BadLocationException {

        for (int i = 0; i < element.getElementCount(); i++) {
            Element childElement = element.getElement(i);
            AttributeSet attributes = childElement.getAttributes();

            if (StyleConstants.getIcon(attributes) != null) {
                System.out.println("Found an ImageIcon!");

                ImageIcon foundIcon = (ImageIcon) StyleConstants.getIcon(attributes);
                String description = foundIcon.getDescription();
                System.out.println("Description: " + description);

                childElement.getDocument().insertString(childElement.getEndOffset(), description + " ", null);
            }

            findImagesInElement(childElement);
        }
    }

    /**
     Creates a list of ImageIcons for emojis.

     @return an ArrayList of ImageIcons representing emojis
     */
    private HashMap<String, ImageIcon> createEmojiList() {

        HashMap<String, ImageIcon> imageIcons = new HashMap<>();
        CodeSource src = ChatMainGuiElementsImpl.class.getProtectionDomain().getCodeSource();

        if (src != null) {

            try (ZipInputStream zip = new ZipInputStream(src.getLocation().openStream())) {

                processZipEntries(imageIcons, zip);

            } catch (IOException e) {

                logger.info(e.getMessage());
            }
        }

        return imageIcons;
    }

    /**
     Processes the entries in a zip file and adds the emoji images to the provided ArrayList.

     @param imageIcons the ArrayList to store the emoji images
     @param zip        the ZipInputStream representing the zip file to process

     @throws IOException if an I/O error occurs while reading the zip file
     */
    private void processZipEntries(HashMap<String, ImageIcon> imageIcons, ZipInputStream zip) throws IOException {

        ZipEntry ze;

        while ((ze = zip.getNextEntry()) != null) {

            String entryName = ze.getName();

            if (isEmojiEntry(entryName)) {

                createAndAddImageIcon(imageIcons, entryName);
            }
        }
    }

    /**
     Checks if the given entry name is an emoji entry.

     @param entryName the name of the entry to check

     @return true if the entry name starts with "emojis/" and is not equal to "emojis/", false
     otherwise
     */
    private boolean isEmojiEntry(String entryName) {

        return entryName.startsWith("emojis/") && !entryName.equals("emojis/");
    }

    /**
     Creates an ImageIcon from a given entry name and adds it to the given list of ImageIcons.

     @param imageIcons the list of ImageIcons to add the created ImageIcon to
     @param entryName  the name of the entry to create the ImageIcon from
     */
    private void createAndAddImageIcon(HashMap<String, ImageIcon> imageIcons, String entryName) {

        URL emojiUrl = getClass().getResource("/" + entryName);
        assert emojiUrl != null;
        ImageIcon imageIcon = new ImageIcon(emojiUrl);
        imageIcon.setDescription(entryName.replace("emojis/", "").replace(".png", ""));
        imageIcons.put(imageIcon.getDescription(), imageIcon);
    }
}
