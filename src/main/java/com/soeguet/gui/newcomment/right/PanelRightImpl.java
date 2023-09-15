package com.soeguet.gui.newcomment.right;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.right.generated.PanelRight;
import com.soeguet.gui.newcomment.util.QuotePanelImpl;
import com.soeguet.gui.newcomment.util.WrapEditorKit;
import com.soeguet.model.MessageModel;
import com.soeguet.model.PanelTypes;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 Implementation of the PanelRight class that is responsible for populating the chat bubble and handling user interaction.
 */
public class PanelRightImpl extends PanelRight {

    private final Logger logger = Logger.getLogger(PanelRightImpl.class.getName());
    private final JFrame mainFrame;

    private final MessageModel messageModel;
    private final PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;

    public PanelRightImpl(JFrame mainFrame, MessageModel messageModel, PanelTypes panelTyp) {

        this.mainFrame = mainFrame;
        this.messageModel = messageModel;
        this.panelTyp = panelTyp;

        populateChatBubble();
        setPopupMenu();

        setupReplyPanels();
    }

    /**
     Populates the chat bubble by executing the steps of checking for quotes and adding the actual message.
     This method does not return any value.
     */
    private void populateChatBubble() {

        checkForQuotes();
        addActualMessage();
    }

    /**
     Sets up the popup menu.
     The popup menu is a JPopupMenu that contains options for replying, editing, and deleting a message.
     When the "reply" option is selected, a ReplyPanelImpl is added to the main text panel's layered pane.
     This method does not return any value.
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
     This method is used to set up the reply panels based on the panel type.
     If the panel type is normal, the method will return without performing any action.
     Otherwise, it will set the visibility of button1 to false.
     */
    private void setupReplyPanels() {

        if (panelTyp == PanelTypes.NORMAL) {
            return;
        }

        this.getButton1().setVisible(false);
    }

    /**
     Checks if a message has a quoted text and creates a quoted section in the chat bubble.
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
     This method adds the actual message by setting the user message,
     name field, and timestamp field.
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
     Creates a quoted section in a chat bubble.

     @param quotedText            the text to be quoted
     @param quotedChatParticipant the participant whose text is being quoted
     @param quotedTime            the time when the text was quoted
     */
    private void createQuotedSectionInChatBubble(String quotedText, String quotedChatParticipant, String quotedTime) {

        QuotePanelImpl quotedTextPane = new QuotePanelImpl(mainFrame, quotedText, quotedChatParticipant, quotedTime);

        this.getPanel1().add(quotedTextPane, "cell 0 0, wrap");
    }

    /**
     Sets the user message in the GUI.
     <p>
     This method creates a JTextPane and sets its text to the user message retrieved from the message model.
     It then adds the JTextPane to the panel at the specified position.
     */
    private void setUserMessage() {

        JTextPane actualTextPane = createTextPane();

        actualTextPane.setEditorKit(new WrapEditorKit());

        JScrollPane jsp = new JScrollPane(actualTextPane);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        createTextOnPane(actualTextPane);
        this.getPanel1().add(actualTextPane, "cell 0 1, wrap");
    }

    /**
     Sets the name field of the message label.
     This method retrieves the sender's name from the message model
     and displays it in the name label of the message.
     */
    private void setNameField(MainGuiElementsInterface mainFrame) {

        String sender = messageModel.getSender();

        this.getNameLabel().setText(sender);

        if (panelTyp == PanelTypes.NORMAL && sender.equals(mainFrame.getLastMessageSenderName())) {

            this.getNameLabel().setVisible(false);
        }

        if (panelTyp == PanelTypes.NORMAL) {

            mainFrame.setLastMessageSenderName(sender);
        }
    }

    /**
     Sets timestamp field with the value from the message model.
     The timestamp value is set as the text of the time label.
     */
    private void setTimestampField(MainGuiElementsInterface mainFrame) {

        String timeStamp = messageModel.getTime();

        this.getTimeLabel().setText(timeStamp);

        if (panelTyp == PanelTypes.NORMAL && timeStamp.equals(mainFrame.getLastMessageTimeStamp())) {

            this.getTimeLabel().setVisible(false);
        }

        if (panelTyp == PanelTypes.NORMAL) {

            mainFrame.setLastMessageTimeStamp(timeStamp);
        }
    }

    /**
     Creates a JTextPane instance.

     @return a JTextPane instance with set properties.
     */
    private JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

        return jTextPane;
    }

    /**
     Creates text on a JTextPane instance.

     @param actualTextPane the JTextPane instance where the text should be created.
     */
    private void createTextOnPane(JTextPane actualTextPane) {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;
        StyledDocument doc = actualTextPane.getStyledDocument();

        processText(actualTextPane, gui, doc);
    }

    /**
     Processes text in a given JTextPane by splitting the message into words and handling each word accordingly.

     @param actualTextPane The JTextPane instance to process the text.
     @param gui            The main GUI interface that provides access to necessary resources.
     @param doc            The StyledDocument associated with the JTextPane.
     */
    private void processText(JTextPane actualTextPane, MainGuiElementsInterface gui, StyledDocument doc) {

        try {
            for (String word : messageModel.getMessage().split(" ")) {

                if (gui.getEmojiList().containsKey(word)) {
                    processEmoji(actualTextPane, gui, doc, word);

                } else {

                    doc.insertString(doc.getLength(), word + " ", null);
                }

            }

        } catch (Exception e) {

            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     Processes the insertion of an emoji in the given JTextPane.

     @param actualTextPane the JTextPane in which the emoji should be inserted
     @param gui            the MainGuiElementsInterface implementation providing GUI elements
     @param doc            the StyledDocument to insert the emoji into
     @param word           the emoji word to insert
     */
    private void processEmoji(JTextPane actualTextPane, MainGuiElementsInterface gui, StyledDocument doc, String word) {

        Style style = createImageStyle(actualTextPane, gui, word);

        try {

            doc.insertString(doc.getLength(), " ", style);

        } catch (BadLocationException ex) {

            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    /**
     Creates a Style instance for displaying an image in a JTextPane.

     @param actualTextPane the JTextPane instance where the image style will be applied
     @param gui            the MainGuiElementsInterface instance that provides access to emoji images
     @param word           the word associated with the desired image

     @return a Style instance with the image set as an icon
     */
    private static Style createImageStyle(JTextPane actualTextPane, MainGuiElementsInterface gui, String word) {

        Style style = actualTextPane.addStyle("Image", null);

        ImageIcon emojiImage = gui.getEmojiList().get(word);
        ImageIcon imageIcon = new ImageIcon(emojiImage.getImage());
        imageIcon.setDescription(emojiImage.getDescription());
        StyleConstants.setIcon(style, imageIcon);

        return style;
    }

    @Override
    protected void replyButtonClicked(MouseEvent e) {

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }
}
