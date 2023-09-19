package com.soeguet.gui.newcomment.left;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.left.generated.PanelLeft;
import com.soeguet.gui.newcomment.util.QuotePanelImpl;
import com.soeguet.gui.newcomment.util.WrapEditorKit;
import com.soeguet.model.PanelTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.EmojiHandler;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class PanelLeftImpl extends PanelLeft implements CommentInterface {

    private final Logger logger = Logger.getLogger(PanelLeftImpl.class.getName());
    private final BaseModel baseModel;
    private final JFrame mainFrame;
    private PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;

    public PanelLeftImpl(JFrame mainFrame, MessageModel baseModel, PanelTypes panelTyp) {

        this.baseModel = baseModel;
        this.mainFrame = mainFrame;
        this.panelTyp = panelTyp;
    }

    @Override
    public void setupTextPanel() {

        assert baseModel instanceof MessageModel;

        populateChatBubble();
        setPopupMenu();

        setupReplyPanels();
    }

    @Override
    public void setupPicturePanel() {

    }

    public PanelLeftImpl(JFrame mainFrame, PictureModel baseModel) {

        this.baseModel = baseModel;
        this.mainFrame = mainFrame;
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

                ReplyPanelImpl replyPanel = new ReplyPanelImpl(mainFrame, baseModel);
                gui.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
            }
        });

        jPopupMenu.add(reply);
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

        assert baseModel instanceof MessageModel;

        String quotedText = ((MessageModel) baseModel).getQuotedMessageText();

        if (quotedText == null) {
            return;
        }

        String quotedChatParticipant = ((MessageModel) baseModel).getQuotedMessageSender();
        String quotedTime = ((MessageModel) baseModel).getQuotedMessageTime();

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

            //time before name, since name is also checked in time -> else time will not be displayed in certain cases
            setTimestampField(mainGui);
            setNameField(mainGui);
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

        this.getPanel1().add(quotedTextPane, "cell 1 0, wrap");
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

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        this.getPanel1().add(actualTextPane, "cell 1 1, wrap");
    }

    /**
     Sets timestamp field with the value from the message model.
     The timestamp value is set as the text of the time label.
     */
    private void setTimestampField(MainGuiElementsInterface mainFrame) {

        String timeStamp = baseModel.getTime();
        String sender = baseModel.getSender();

        this.getTimeLabel().setText(timeStamp);

        //also check for sender; if new sender, the time should always be visible
        if (panelTyp == PanelTypes.NORMAL && timeStamp.equals(mainFrame.getLastMessageTimeStamp())) {

            if (sender.equals(mainFrame.getLastMessageSenderName())) {

                this.getTimeLabel().setVisible(false);
            }
        }

        if (panelTyp == PanelTypes.NORMAL) {

            mainFrame.setLastMessageTimeStamp(timeStamp);
        }
    }

    /**
     Sets the name field of the message label.
     This method retrieves the sender's name from the message model
     and displays it in the name label of the message.
     */
    private void setNameField(MainGuiElementsInterface mainFrame) {

        String sender = baseModel.getSender();
        this.getNameLabel().setText(sender);

        if (panelTyp == PanelTypes.NORMAL && sender.equals(mainFrame.getLastMessageSenderName())) {

            this.getNameLabel().setVisible(false);
        }

        if (panelTyp == PanelTypes.NORMAL) {

            mainFrame.setLastMessageSenderName(sender);
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