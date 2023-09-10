package com.soeguet.gui.newcomment.left;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.util.QuotePanelImpl;
import com.soeguet.gui.newcomment.util.WrapEditorKit;
import com.soeguet.model.MessageModel;
import com.soeguet.model.PanelTypes;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelLeftImpl extends PanelLeft {

    private final MessageModel messageModel;

    private JPopupMenu jPopupMenu;
    private final JFrame mainFrame;
    private final PanelTypes panelTyp;

    public PanelLeftImpl(JFrame mainFrame, MessageModel messageModel, PanelTypes panelTyp) {

        this.messageModel = messageModel;
        this.mainFrame = mainFrame;
        this.panelTyp = panelTyp;

        populateChatBubble();
        setPopupMenu();

        setupReplyPanels();
    }

    /**
     * This method is used to set up the reply panels based on the panel type.
     * If the panel type is normal, the method will return without performing any action.
     * Otherwise, it will set the visibility of button1 to false.
     */
    private void setupReplyPanels() {

        if (panelTyp == PanelTypes.NORMAL) {
            return;
        }

        this.getButton1().setVisible(false);
    }

    /**
     * Sets up the popup menu.
     * The popup menu is a JPopupMenu that contains options for replying, editing, and deleting a message.
     * When the "reply" option is selected, a ReplyPanelImpl is added to the main text panel's layered pane.
     * This method does not return any value.
     */
    private void setPopupMenu() {

        jPopupMenu = new JPopupMenu();
        JMenuItem reply = new JMenuItem("reply");

        reply.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (!(mainFrame instanceof MainGuiElementsInterface)) {
                    return;
                }
                MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

                ReplyPanelImpl replyPanel = new ReplyPanelImpl(mainFrame, messageModel);
                gui.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
            }
        });

        jPopupMenu.add(reply);

        jPopupMenu.addSeparator();
        jPopupMenu.add(new JMenuItem("edit"));
        jPopupMenu.add(new JMenuItem("delete"));
    }

    /**
     * Populates the chat bubble by executing the steps of checking for quotes and adding the actual message.
     * This method does not return any value.
     */
    private void populateChatBubble() {

        checkForQuotes();
        addActualMessage();
    }

    /**
     * This method adds the actual message by setting the user message,
     * name field, and timestamp field.
     */
    private void addActualMessage() {

        if (mainFrame instanceof MainGuiElementsInterface) {

            MainGuiElementsInterface mainGui = (MainGuiElementsInterface) mainFrame;
            setUserMessage();
            setNameField(mainGui);
            setTimestampField(mainGui);
        }
    }

    /**
     * Sets the user message in the GUI.
     * <p>
     * This method creates a JTextPane and sets its text to the user message retrieved from the message model.
     * It then adds the JTextPane to the panel at the specified position.
     */
    private void setUserMessage() {

        JTextPane actualTextPane = createTextPane();


        actualTextPane.setEditorKit(new WrapEditorKit());

        JScrollPane jsp = new JScrollPane(actualTextPane);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        actualTextPane.setText(messageModel.getMessage());
        this.getPanel1().add(actualTextPane, "cell 1 1, wrap");
    }

    /**
     * Sets the name field of the message label.
     * This method retrieves the sender's name from the message model
     * and displays it in the name label of the message.
     */
    private void setNameField(MainGuiElementsInterface mainFrame) {

        String sender = messageModel.getSender();

        if (panelTyp == PanelTypes.NORMAL && sender.equals(mainFrame.getLastMessageSenderName())) {

            this.getNameLabel().setText("");

        } else {

            this.getNameLabel().setText(sender);
        }

        mainFrame.setLastMessageSenderName(sender);
    }


    /**
     * Sets timestamp field with the value from the message model.
     * The timestamp value is set as the text of the time label.
     */
    private void setTimestampField(MainGuiElementsInterface mainFrame) {

        String timeStamp = messageModel.getTime();

        if (panelTyp == PanelTypes.NORMAL && timeStamp.equals(mainFrame.getLastMessageTimeStamp())) {

            this.getTimeLabel().setText("");

        } else {

            this.getTimeLabel().setText(timeStamp);
        }

        mainFrame.setLastMessageTimeStamp(timeStamp);
    }

    /**
     * Checks if a message has a quoted text and creates a quoted section in the chat bubble.
     */
    private void checkForQuotes() {

        String quotedText = messageModel.getQuotedMessageText();

        if (quotedText == null) {
            return;
        }

        String quotedChatParticipant = messageModel.getQuotedMessageSender();
        String quotedTime = messageModel.getQuotedMessageTime();

        createQuotedSectionInChatBubble(quotedText, quotedChatParticipant, quotedTime);
    }

    /**
     * Creates a quoted section in a chat bubble.
     *
     * @param quotedText            the text to be quoted
     * @param quotedChatParticipant the participant whose text is being quoted
     * @param quotedTime            the time when the text was quoted
     */
    private void createQuotedSectionInChatBubble(String quotedText, String quotedChatParticipant, String quotedTime) {

        QuotePanelImpl quotedTextPane = new QuotePanelImpl(quotedText, quotedChatParticipant, quotedTime);

        this.getPanel1().add(quotedTextPane, "cell 1 0, wrap");
    }

    /**
     * Creates a JTextPane instance.
     *
     * @return a JTextPane instance with set properties.
     */
    private JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setEditorKit(new WrapEditorKit());

        JScrollPane jsp = new JScrollPane(jTextPane);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return jTextPane;
    }

    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }

    @Override
    protected void replyButtonClicked(MouseEvent e) {

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }
}
