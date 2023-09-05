package com.soeguet.gui.main_frame;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.behaviour.GuiFunctionality;
import com.soeguet.gui.main_frame.generated.ChatPanel;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EventObject;
import java.util.logging.Logger;

/**
 * This class represents a GUI implementation for a chat application.
 * It extends the ChatPanel class and implements the MainGuiInterface.
 */
public class ChatMainGuiElementsImpl extends ChatPanel implements MainGuiElementsInterface {

    private final Logger logger = Logger.getLogger(ChatMainGuiElementsImpl.class.getName());

    private final GuiFunctionality guiFunctionality;

    private final CustomWebsocketClient websocketClient;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes the ChatMainGuiElementsImpl object.
     *
     * It sets up the GUI functionality required for the chat application and
     * establishes a connection to the WebSocket server.
     *
     * @throws RuntimeException if there is an error in the WebSocket URI syntax
     */
    public ChatMainGuiElementsImpl() {

        guiFunctionality = new GuiFunctionality(this);

        try {
            websocketClient = new CustomWebsocketClient(new URI("ws://127.0.0.1:8100"), this);
            websocketClient.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the GuiFunctionality.
     *
     * @return the GuiFunctionality object.
     */
    public GuiFunctionality getGuiFunctionality() {

        return guiFunctionality;
    }

    /**
     * Gets the ObjectMapper instance used for converting JSON to Java objects and vice versa.
     *
     * @return the ObjectMapper instance
     */
    public ObjectMapper getObjectMapper() {

        return objectMapper;
    }

    /**
     * Retrieves the WebSocket client.
     *
     * @return The WebSocket client.
     */
    public CustomWebsocketClient getWebsocketClient() {

        return websocketClient;
    }

    /**
     * Logs the provided event and method name.
     *
     * @param event      The event to be logged.
     * @param methodName The name of the method to be logged.
     */
    private void logMethod(EventObject event, String methodName) {

        System.out.println();
        System.out.println("__ " + methodName);
        logger.info(event.toString());
        System.out.println();
    }

    /**
     * {@inheritDoc}
     *
     * This method is called when a property change event occurs.
     * It logs the provided event and method name.
     *
     * @param e           The property change event to be handled.
     *                   Must not be null.
     */
    @Override
    protected void thisPropertyChange(PropertyChangeEvent e) {

        logMethod(e, "ChatGuiImpl.thisPropertyChange");
    }

    /**
     * Method called when the component is resized.
     *
     * @param e The ComponentEvent object representing the resize event.
     */
    @Override
    protected void thisComponentResized(ComponentEvent e) {

        SwingUtilities.invokeLater(() -> {
            this.getMainTextBackgroundScrollPane().setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight() - this.getInteractionAreaPanel().getHeight() - 55);
            this.revalidate();
            this.repaint();
        });

//        logMethod(e, "ChatGuiImpl.thisComponentResized");
    }

    /**
     * Called when the mouse is pressed on the properties menu item.
     *
     * @param e The MouseEvent object representing the event.
     */
    @Override
    protected void propertiesMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.propertiesMenuItemMousePressed");
    }

    /**
     * Resets the connection when the reset connection menu item is pressed.
     *
     * @param e The mouse event that triggered the method.
     */
    @Override
    protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImp.resetConnectionMenuItemMousePressed");
    }

    /**
     * Called when the participants menu item is pressed.
     * Logs the provided event and method name.
     *
     * @param e The MouseEvent associated with the button press.
     */
    @Override
    protected void participantsMenuItemMousePressed(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.participantsMenuItemMousePressed");
    }

    /**
     * Called when the main text panel is clicked.
     * Logs the provided event and method name.
     *
     * @param e The MouseEvent associated with the click event.
     */
    @Override
    protected void mainTextPanelMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.mainTextPanelMouseClicked");
    }



    /**
     * {@inheritDoc}
     *
     * This method is called when the mouse is clicked on the text editor pane in the ChatGuiImpl class.
     * It logs the method call with the provided MouseEvent object and the method name.
     *
     * @param e the MouseEvent object representing the mouse click event on the text editor pane
     */
    @Override
    protected void textEditorPaneMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.textEditorPaneMouseClicked");
    }

    /**
     * Called when a key is pressed in the text editor pane.
     * If the pressed key is not the Enter key, the method simply returns.
     * If the pressed key is the Enter key, it consumes the event and performs the appropriate action based on whether the Shift key is pressed or not.
     *
     * @param e The KeyEvent object representing the key press event.
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
     * Appends a new line to the text editor pane.
     * <p>
     * Retrieves the current text in the text editor pane and appends a new line character at the end of it.
     */
    private void appendNewLineToTextEditorPane() {

        String currentText = getTextEditorPane().getText();
        getTextEditorPane().setText(currentText + "\n");
    }

    /**
     * Handles a key press event when the enter key is pressed without pressing the shift key.
     * <p>
     * Retrieves the content of the text editor pane, trims any leading or trailing space, and checks if it is empty.
     * If the content is empty, it clears the text editor pane. Otherwise, it calls the `clearTextPaneAndSendMessageToSocket`
     * method to clear the text pane and send the current content to a socket.
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
     * Invoked when a key is released in the text editor pane.
     * <p>
     * This method is an override of the textEditorPaneKeyReleased method from the superclass.
     * It is called when a key is released in the text editor pane.
     *
     * @param e the KeyEvent object generated when a key is released
     */
    @Override
    protected void textEditorPaneKeyReleased(KeyEvent e) {

    }


    /**
     * Invoked when the emoji button is clicked in the chat GUI.
     * <p>
     * This method is an override of the emojiButton method from the superclass.
     * It is called when the emoji button is clicked.
     *
     * @param e the ActionEvent object generated when the emoji button is clicked
     */
    @Override
    protected void emojiButton(ActionEvent e) {

        logMethod(e, "ChatGuiImpl.emojiButton");
    }

    /**
     * Handles the event when the mouse presses the exit menu item.
     * Sets the default close operation for the current JFrame to EXIT_ON_CLOSE
     * and disposes the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
        System.exit(0);
    }

    /**
     * Handles the event when the mouse clicks the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void thisMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.thisMouseClicked");
    }

    /**
     * Handles the event when the mouse clicks the picture button in the current JFrame.
     *
     * @param e the MouseEvent object that triggered this event
     */
    @Override
    protected void pictureButtonMouseClicked(MouseEvent e) {

        logMethod(e, "ChatGuiImpl.pictureButtonMouseClicked");
    }

    /**
     * Handles the event when the send button is clicked.
     * Clears the text pane in the GUI and sends the message to the socket.
     *
     * @param e the ActionEvent object that triggered this event
     */
    @Override
    protected void sendButton(ActionEvent e) {

        guiFunctionality.clearTextPaneAndSendMessageToSocket();
    }
}
