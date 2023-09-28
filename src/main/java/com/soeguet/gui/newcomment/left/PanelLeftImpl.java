package com.soeguet.gui.newcomment.left;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.images.CustomImagePanel;
import com.soeguet.gui.newcomment.left.generated.PanelLeft;
import com.soeguet.gui.newcomment.util.QuotePanelImpl;
import com.soeguet.gui.newcomment.util.WrapEditorKit;
import com.soeguet.model.PanelTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.EmojiHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelLeftImpl extends PanelLeft implements CommentInterface {

    private final MainFrameInterface mainFrame;
    private final BaseModel baseModel;
    private PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;
    private JTextPane actualTextPane;

    public PanelLeftImpl(MainFrameInterface mainFrame, BaseModel baseModel, PanelTypes panelTyp) {

        this.baseModel = baseModel;
        this.mainFrame = mainFrame;
        this.panelTyp = panelTyp;
    }

    /**
     Ensures that the method is running on the Event Dispatch Thread (EDT).
     If the method is not running on the EDT, an IllegalStateException is thrown.

     This method does not return any value.

     @throws IllegalStateException if the method is not running on the EDT
     */
    private void ensureEDT() {

        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("This should run on the EDT");
        }
    }

    public PanelLeftImpl(MainFrameInterface mainFrame, BaseModel baseModel) {

        this.baseModel = baseModel;
        this.mainFrame = mainFrame;
    }

    @Override
    public void setupTextPanelWrapper() {

        if (!(baseModel instanceof MessageModel)) {
            return;
        }

        SwingUtilities.invokeLater(() -> {

            checkForQuotesInMessage();
            addActualMessage();
            setupEditorPopupMenu(mainFrame, baseModel, jPopupMenu);
            addRightClickOptionToPanel();

            setupReplyPanels();
        });

    }

    @Override
    public void setupPicturePanelWrapper() {

        SwingUtilities.invokeLater(() -> {

            CustomImagePanel customImagePanel = new CustomImagePanel(mainFrame, this, (PictureModel) baseModel);
            customImagePanel.addImageLabelToPanel("cell 1 0, wrap");

            actualTextPane = createImageCaptionTextPane();
            getPanel1().add(actualTextPane, "cell 1 1, wrap");

            setupEditorPopupMenu(mainFrame, baseModel, jPopupMenu);

            this.addRightClickOptionToPanel();

            setNameField(mainFrame);
            setTimestampField(mainFrame);
        });
    }

    public void addRightClickOptionToPanel() {

        // EDT check done!

        if (actualTextPane == null) {

            actualTextPane = createTextPane();
        }

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyItem = createAndAddMenuItem(popupMenu, "copy");

        addActionListenerToCopyJMenuItem(copyItem);
        addMouseListenerToJTextPane(actualTextPane, popupMenu);

        actualTextPane.setComponentPopupMenu(popupMenu);
    }

    /**
     * Creates a JTextPane for displaying image captions.
     * If the actualTextPane is null, a new JTextPane is created using the createTextPane() method.
     * The text content of the JTextPane is set to the message obtained from the baseModel.
     * The JTextPane is set to non-editable and non-opaque.
     * Returns the JTextPane.
     *
     * @return the created JTextPane
     */
    private JTextPane createImageCaptionTextPane() {

        // EDT check done!

        if (actualTextPane == null) {

            actualTextPane = createTextPane();
        }

        actualTextPane.setText(baseModel.getMessage());
        actualTextPane.setEditable(false);
        actualTextPane.setOpaque(false);

        return actualTextPane;
    }

    /**
     Checks if a message has a quoted text and creates a quoted section in the chat bubble.

     If the message does not have a quoted text, the method will return without performing any action.
     Otherwise, it will create a QuotePanelImpl and add it to the panel1.

     @see MessageModel#getQuotedMessageText()
     @see MessageModel#getQuotedMessageSender()
     @see MessageModel#getQuotedMessageTime()
     @see QuotePanelImpl
     */
    private void checkForQuotesInMessage() {

        // EDT check done!

        MessageModel messageModel = getMessageModel();

        String quotedText = messageModel.getQuotedMessageText();

        if (quotedText == null) {
            return;
        }

        String quotedChatParticipant = messageModel.getQuotedMessageSender();
        String quotedTime = messageModel.getQuotedMessageTime();

        QuotePanelImpl quotedSectionPanel = new QuotePanelImpl(mainFrame, quotedText, quotedChatParticipant, quotedTime);
        this.getPanel1().add(quotedSectionPanel, "cell 1 0, wrap");
    }

    private MessageModel getMessageModel() {

        return (MessageModel) baseModel;
    }

    /**
     Adds the actual message to the chat bubble.

     This method sets the user message, name field, and timestamp field in the main GUI elements.
     */
    private void addActualMessage() {

        // EDT check done!
        setUserMessage();
        setNameField(mainFrame);
        setTimestampField(mainFrame);
    }

    /**
     Sets up the popup menu for the editor.
     The popup menu is a JPopupMenu that contains options for replying, editing, and deleting a message.
     When the "reply" option is selected, a ReplyPanelImpl is added to the main text panel's layered pane.
     This method does not return any value.
     */
    protected void setupEditorPopupMenu(final MainFrameInterface mainFrame, final BaseModel baseModel, final JPopupMenu jPopupMenu) {

        // EDT check done!

        this.jPopupMenu = new JPopupMenu();

        JMenuItem reply = createAndAddMenuItem(this.jPopupMenu, "reply");
        addMouseListenerToReplyMenuItem(reply);

        this.jPopupMenu.addSeparator();
        createAndAddMenuItem(this.jPopupMenu, "edit");
        //TODO add action listener to edit menu item
        createAndAddMenuItem(this.jPopupMenu, "delete");
        //TODO add action listener to delete menu item
    }

    /**
     This method is used to set up the reply panels based on the panel type.
     If the panel type is normal, the method will return without performing any action.
     Otherwise, it will set the visibility of button1 to false.
     */
    private void setupReplyPanels() {

        // EDT check done!

        if (panelTyp == PanelTypes.NORMAL) {

            return;
        }

        this.getButton1().setVisible(false);
    }

    /**
     Sets the user message in the GUI.
     <p>
     This method creates a JTextPane and sets its text to the user message retrieved from the message model.
     It then adds the JTextPane to the panel at the specified position.
     */
    private void setUserMessage() {

        ensureEDT();

        actualTextPane = createTextPane();

        actualTextPane.setEditorKit(new WrapEditorKit());

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        this.getPanel1().add(actualTextPane, "cell 1 1, wrap");
    }

    /**
     Sets the name field of the message label.
     This method retrieves the sender's name from the message model
     and displays it in the name label of the message.
     */
    private void setNameField(MainFrameInterface mainFrame) {

        // EDT check done!

        String sender = baseModel.getSender();

        form_nameLabel.setText(sender);

        if (panelTyp == PanelTypes.NORMAL && sender.equals(mainFrame.getLastMessageSenderName())) {

            form_nameLabel.setVisible(false);
        }

        if (panelTyp == PanelTypes.NORMAL) {

            mainFrame.setLastMessageSenderName(sender);
        }
    }

    /**
     Sets timestamp field with the value from the message model.
     The timestamp value is set as the text of the time label.
     */
    private void setTimestampField(MainFrameInterface mainFrame) {

        // EDT check done!

        String timeStamp = baseModel.getTime();

        form_timeLabel.setText(timeStamp);

        if (panelTyp == PanelTypes.NORMAL && timeStamp.equals(mainFrame.getLastMessageTimeStamp())) {

            form_timeLabel.setVisible(false);
        }

        if (panelTyp == PanelTypes.NORMAL) {

            mainFrame.setLastMessageTimeStamp(timeStamp);
        }
    }

    /**
     Creates a menu item with the given name and adds it to the specified popup menu.

     @param popupMenu    the popup menu to add the menu item to
     @param menuItemName the name of the menu item to be created

     @return the created menu item
     */
    protected JMenuItem createAndAddMenuItem(JPopupMenu popupMenu, String menuItemName) {

        ensureEDT();

        JMenuItem copyItem = new JMenuItem(menuItemName);
        popupMenu.add(copyItem);
        return copyItem;
    }

    private void addMouseListenerToReplyMenuItem(JMenuItem reply) {

        ensureEDT();

        reply.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                super.mouseReleased(e);

                ReplyPanelImpl replyPanel = new ReplyPanelImpl(mainFrame, baseModel);
                mainFrame.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
            }
        });
    }

    private void addActionListenerToCopyJMenuItem(JMenuItem menuOption) {

        ensureEDT();

        menuOption.addActionListener(e -> {
            final String selectedText = actualTextPane.getSelectedText();
            if (selectedText != null) {
                StringSelection selection = new StringSelection(selectedText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });
    }

    private void addMouseListenerToJTextPane(JTextPane textPane, JPopupMenu popupMenu) {

        ensureEDT();

        textPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(PanelLeftImpl.this, e.getX(), e.getY());
                }
            }
        });
    }

    /**
     Creates a JTextPane instance.

     @return a JTextPane instance with set properties.
     */
    protected JTextPane createTextPane() {

        ensureEDT();

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

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

        ensureEDT();

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }
}