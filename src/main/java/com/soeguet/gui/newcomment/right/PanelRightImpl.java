package com.soeguet.gui.newcomment.right;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
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
    private final JFrame mainFrame;
    private final BaseModel baseModel;
    private PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;
    private JTextPane actualTextPane;

    public PanelRightImpl(JFrame mainFrame, BaseModel baseModel, PanelTypes panelTyp) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.panelTyp = panelTyp;
    }

    public PanelRightImpl(JFrame mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    @Override
    public void setupTextPanel() {

        assert baseModel instanceof MessageModel;

        populateChatBubble();
        setPopupMenu();
        addRightClickOptionToPanel();

        setupReplyPanels();
    }

    @Override
    public void setupPicturePanel() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        setPopupMenu();

        extractImageFromMessage();

        JLabel imageLabel = new JLabel(scaleImageIfTooBig());
        this.getPanel1().add(imageLabel, "cell 0 0, wrap");

        addMaximizePictureOnClick(imageLabel);

        JTextPane imageCaptionTextPane = getImageCaptionTextPane();
        this.getPanel1().add(imageCaptionTextPane, "cell 0 1, wrap");


        addRightClickOptionToPanel();

        setNameField(gui);
        setTimestampField(gui);

    }

    private void addMaximizePictureOnClick(JLabel imageLabel) {

        imageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                File imgFile = new File("temp-image.jpg");

                try {
                    ImageIO.write(image, "png", imgFile);
                } catch (IOException ex) {
                    logger.log(java.util.logging.Level.SEVERE, "Error writing image", ex);
                }

                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(imgFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Desktop not supported");
                }
            }
        });
    }

    private void extractImageFromMessage() {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((PictureModel) baseModel).getPicture())) {

            image = ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            logger.log(java.util.logging.Level.SEVERE, "Error reading image", e);
        }
    }

    private ImageIcon scaleImageIfTooBig() {

        ImageIcon imageIcon = new ImageIcon(image);

        if (imageIcon.getIconWidth() > 500) {

            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(500, -1, Image.SCALE_AREA_AVERAGING));
        } else if (imageIcon.getIconHeight() > 350) {

            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(-1, 350, Image.SCALE_AREA_AVERAGING));
        }

        return imageIcon;
    }

    private JTextPane getImageCaptionTextPane() {

        actualTextPane = new JTextPane();
        actualTextPane.setText(baseModel.getMessage());
        actualTextPane.setEditable(false);
        actualTextPane.setOpaque(false);
        return actualTextPane;
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

                MainGuiElementsInterface gui = getMainFrame();
                assert gui != null;

                ReplyPanelImpl replyPanel = new ReplyPanelImpl(mainFrame, baseModel);
                gui.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
            }
        });

        jPopupMenu.add(reply);

        jPopupMenu.addSeparator();
        jPopupMenu.add(new JMenuItem("edit"));
        jPopupMenu.add(new JMenuItem("delete"));
    }

    private void addRightClickOptionToPanel() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("copy");

        copyItem.addActionListener(e -> {
            final String selectedText = actualTextPane.getSelectedText();
            if (selectedText != null) {
                StringSelection selection = new StringSelection(selectedText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });


        popupMenu.add(copyItem);

        actualTextPane.setComponentPopupMenu(popupMenu);

        actualTextPane.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            popupMenu.show(PanelRightImpl.this, e.getX(), e.getY());
                        }
                    }
                });
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

            MainGuiElementsInterface mainGui = getMainFrame();
            setUserMessage();
            setNameField(mainGui);
            setTimestampField(mainGui);
        }
    }

    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {

            return null;
        }

        return (MainGuiElementsInterface) mainFrame;
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

        actualTextPane = createTextPane();

        actualTextPane.setEditorKit(new WrapEditorKit());

        JScrollPane jsp = new JScrollPane(actualTextPane);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        this.getPanel1().add(actualTextPane, "cell 0 1, wrap");
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
     Sets timestamp field with the value from the message model.
     The timestamp value is set as the text of the time label.
     */
    private void setTimestampField(MainGuiElementsInterface mainFrame) {

        String timeStamp = baseModel.getTime();

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