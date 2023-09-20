package com.soeguet.gui.newcomment.right;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.right.generated.PanelRight;
import com.soeguet.gui.newcomment.util.QuotePanelImpl;
import com.soeguet.gui.newcomment.util.WrapEditorKit;
import com.soeguet.model.PanelTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.EmojiHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 Implementation of the PanelRight class that is responsible for populating the chat bubble and handling user interaction.
 */
public class PanelRightImpl extends PanelRight implements CommentInterface {

    private final Logger logger = Logger.getLogger(PanelRightImpl.class.getName());
    private final MainFrameInterface mainFrame;
    private final BaseModel baseModel;
    private PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;
    private JTextPane actualTextPane;

    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel, PanelTypes panelTyp) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.panelTyp = panelTyp;
    }

    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    @Override
    public void setupTextPanel() {

        if (!(baseModel instanceof MessageModel)) {
            return;
        }

        checkForQuotesInMessage();
        addActualMessage();
        setupEditorPopupMenu();
        addRightClickOptionToPanel();

        setupReplyPanels();
    }

    @Override
    public void setupPicturePanel() {

        setupEditorPopupMenu();

        extractImageFromMessage();

        JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
        form_panel1.add(imageLabel, "cell 0 0, wrap");

        addMaximizePictureOnClick(imageLabel);

        JTextPane imageCaptionTextPane = createImageCaptionTextPane();
        form_panel1.add(imageCaptionTextPane, "cell 0 1, wrap");

        addRightClickOptionToPanel();

        setNameField(mainFrame);
        setTimestampField(mainFrame);

    }

    private void extractImageFromMessage() {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((PictureModel) baseModel).getPicture())) {

            image = ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "Error reading image", e);
        }
    }

    private ImageIcon scaleImageIfTooBig(BufferedImage bufferedImage) {

        if (bufferedImage == null) {

            logger.log(java.util.logging.Level.SEVERE, "Buffered image is null");
            return null;
        }

        ImageIcon imageIcon;

        if (image.getWidth() > 500) {

            imageIcon = new ImageIcon(image.getScaledInstance(500, -1, Image.SCALE_AREA_AVERAGING));

        } else if (image.getHeight() > 350) {

            imageIcon = new ImageIcon(image.getScaledInstance(-1, 350, Image.SCALE_AREA_AVERAGING));

        } else {

            imageIcon = new ImageIcon(image);
        }

        return imageIcon;
    }

    @Override
    public void addMaximizePictureOnClick(JLabel imageLabel) {

        imageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                new Thread(() -> {

                    File imgFile = new File("temp-image.jpg");

                    try {

                        ImageIO.write(image, "png", imgFile);

                    } catch (IOException ex) {

                        logger.log(java.util.logging.Level.SEVERE, "Error writing image", ex);
                    }

                    if (Desktop.isDesktopSupported()) {

                        try {

                            if (imgFile.exists()) {

                                Desktop.getDesktop().open(imgFile);

                            } else {

                                logger.log(java.util.logging.Level.SEVERE, "Image file does not exist");
                                throw new IOException();

                            }

                        } catch (IOException ex) {

                            logger.log(java.util.logging.Level.SEVERE, "Error opening image", ex);
                        }

                    } else {

                        logger.log(java.util.logging.Level.SEVERE, "Desktop not supported");
                    }

                    if (!imgFile.delete()) {

                        logger.log(java.util.logging.Level.SEVERE, "Error deleting temp image file");
                    }
                });
            }
        });
    }

    private JTextPane createImageCaptionTextPane() {

        actualTextPane = new JTextPane();
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

        MessageModel messageModel = (MessageModel) baseModel;

        String quotedText = messageModel.getQuotedMessageText();

        if (quotedText == null) {
            return;
        }

        String quotedChatParticipant = messageModel.getQuotedMessageSender();
        String quotedTime = messageModel.getQuotedMessageTime();

        QuotePanelImpl quotedSectionPanel = new QuotePanelImpl(mainFrame, quotedText, quotedChatParticipant, quotedTime);
        this.getPanel1().add(quotedSectionPanel, "cell 0 0, wrap");
    }

    /**
     Adds the actual message to the chat bubble.

     This method sets the user message, name field, and timestamp field in the main GUI elements.
     */
    private void addActualMessage() {

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
    private void setupEditorPopupMenu() {

        jPopupMenu = new JPopupMenu();

        JMenuItem reply = createAndAddMenuItem(jPopupMenu, "reply");
        addMouseListenerToReplyMenuItem(reply);

        jPopupMenu.addSeparator();
        createAndAddMenuItem(jPopupMenu, "edit");
        //TODO add action listener to edit menu item
        createAndAddMenuItem(jPopupMenu, "delete");
        //TODO add action listener to delete menu item
    }

    public void addRightClickOptionToPanel() {

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyItem = createAndAddMenuItem(popupMenu, "copy");

        addActionListenerToCopyJMenuItem(copyItem);
        addMouseListenerToJTextPane(actualTextPane, popupMenu);

        actualTextPane.setComponentPopupMenu(popupMenu);
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
     Sets the user message in the GUI.
     <p>
     This method creates a JTextPane and sets its text to the user message retrieved from the message model.
     It then adds the JTextPane to the panel at the specified position.
     */
    private void setUserMessage() {

        actualTextPane = createTextPane();

        actualTextPane.setEditorKit(new WrapEditorKit());

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        this.getPanel1().add(actualTextPane, "cell 0 1, wrap");
    }

    /**
     Sets the name field of the message label.
     This method retrieves the sender's name from the message model
     and displays it in the name label of the message.
     */
    private void setNameField(MainFrameInterface mainFrame) {

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
    private JMenuItem createAndAddMenuItem(JPopupMenu popupMenu, String menuItemName) {

        JMenuItem copyItem = new JMenuItem(menuItemName);
        popupMenu.add(copyItem);
        return copyItem;
    }

    private void addMouseListenerToReplyMenuItem(JMenuItem reply) {

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

        menuOption.addActionListener(e -> {
            final String selectedText = actualTextPane.getSelectedText();
            if (selectedText != null) {
                StringSelection selection = new StringSelection(selectedText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });
    }

    private void addMouseListenerToJTextPane(JTextPane textPane, JPopupMenu popupMenu) {

        textPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(PanelRightImpl.this, e.getX(), e.getY());
                }
            }
        });
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

    @Override
    protected void thisComponentResized(ComponentEvent e) {

    }
}