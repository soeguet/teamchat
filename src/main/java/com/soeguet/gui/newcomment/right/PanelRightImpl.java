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
 This class represents a right panel implementation that extends PanelRight and implements CommentInterface.
 It provides methods to set up the text panel wrapper, the picture panel, and add right-click options to the panel.
 */
public class PanelRightImpl extends PanelRight implements CommentInterface {

    private final Logger LOGGER = Logger.getLogger(PanelRightImpl.class.getName());
    private final MainFrameInterface mainFrame;
    private final BaseModel baseModel;
    private PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;
    private JTextPane actualTextPane;

    /**
     Constructs a new PanelRightImpl object.

     @param mainFrame the MainFrameInterface object representing the main frame
     @param baseModel the BaseModel object representing the base model
     @param panelTyp  the PanelTypes object representing the panel type
     */
    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel, PanelTypes panelTyp) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.panelTyp = panelTyp;
    }

    /**
     Creates a new instance of PanelRightImpl.

     @param mainFrame the main frame object
     @param baseModel the base model object
     */
    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    /**
     Sets up the text panel wrapper.

     This method is used to set up the text panel wrapper for a message. It performs
     several operations to configure the text panel wrapper, such as checking for
     quotes in the message, adding the actual message, setting up the editor popup
     menu, adding a right-click option to the panel, and setting up reply panels.

     This method should be called when the base model is an instance of the MessageModel
     class. It should be invoked using SwingUtilities.invokeLater() to ensure that the
     operation is executed on the Event Dispatch Thread.
     */
    @Override
    public void setupTextPanelWrapper() {

        if (!(baseModel instanceof MessageModel)) {
            return;
        }

        SwingUtilities.invokeLater(() -> {

            checkForQuotesInMessage();
            addActualMessage();
            setupEditorPopupMenu();
            addRightClickOptionToPanel();

            setupReplyPanels();
        });
    }

    /**
     Sets up the picture panel in the user interface.
     This method is called when a picture message is received and needs to be displayed.
     It performs multiple tasks as a wrapper method.
     */
    @Override
    public void setupPicturePanelWrapper() {

        SwingUtilities.invokeLater(() -> {

            setupEditorPopupMenu();

            extractImageFromMessage();

            if (image == null) {

                LOGGER.log(java.util.logging.Level.SEVERE, "Buffered image is null");
                return;
            }

            JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
            form_panel1.add(imageLabel, "cell 0 0, wrap");

            addMaximizePictureOnClick(imageLabel);

            JTextPane imageCaptionTextPane = createImageCaptionTextPane();
            form_panel1.add(imageCaptionTextPane, "cell 0 1, wrap");

            addRightClickOptionToPanel();

            setNameField(mainFrame);
            setTimestampField(mainFrame);
        });
    }

    /**
     Extracts the image from the received message and stores it in the 'image' variable.
     This method reads the image data from the PictureModel object and uses the ImageIO class to convert it
     into a BufferedImage object.

     If an IOException occurs during the image reading process, an error message is logged.

     Note: This method assumes that the 'baseModel' object is an instance of PictureModel and
     that the 'getPicture()' method returns the byte array representing the image data.
     */
    private void extractImageFromMessage() {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((PictureModel) baseModel).getPicture())) {

            image = ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error reading image", e);
        }
    }

    /**
     Scales the given image if its dimensions exceed certain limits.
     If the width of the image exceeds 500 pixels, it scales the image to have a width of 500 pixels and proportionate height.
     If the height of the image exceeds 350 pixels, it scales the image to have a height of 350 pixels and proportionate width.
     If the image dimensions are within the limits, the original image is returned as an ImageIcon.

     @param bufferedImage The image to scale.

     @return An ImageIcon of the scaled image if it is too big, otherwise an ImageIcon of the original image.
     */
    private ImageIcon scaleImageIfTooBig(BufferedImage bufferedImage) {

        if (bufferedImage.getWidth() > 500) {

            return new ImageIcon(bufferedImage.getScaledInstance(500, -1, Image.SCALE_AREA_AVERAGING));

        } else if (bufferedImage.getHeight() > 350) {

            return new ImageIcon(bufferedImage.getScaledInstance(-1, 350, Image.SCALE_AREA_AVERAGING));

        } else {

            return new ImageIcon(bufferedImage);
        }
    }

    /**
     Adds a maximize picture action to the given image label.
     When the image label is clicked, the picture will be opened in the default image viewer application.

     @param imageLabel The JLabel containing the image to maximize
     */
    private void addMaximizePictureOnClick(JLabel imageLabel) {

        imageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                new Thread(() -> {

                    File imgFile = new File("temp-image.jpg");

                    try {

                        ImageIO.write(image, "png", imgFile);

                    } catch (IOException ex) {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Error writing image", ex);
                    }

                    if (Desktop.isDesktopSupported()) {

                        try {

                            if (imgFile.exists()) {

                                Desktop.getDesktop().open(imgFile);

                            } else {

                                LOGGER.log(java.util.logging.Level.SEVERE, "Image file does not exist");
                                throw new IOException();

                            }

                        } catch (IOException ex) {

                            LOGGER.log(java.util.logging.Level.SEVERE, "Error opening image", ex);
                        }

                    } else {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Desktop not supported");
                    }

                    if (!imgFile.delete()) {

                        LOGGER.log(java.util.logging.Level.SEVERE, "Error deleting temp image file");
                    }
                });
            }
        });
    }

    /**
     Creates a JTextPane to display the image caption.

     @return The created JTextPane.
     */
    private JTextPane createImageCaptionTextPane() {

        // EDT check done!

        if (actualTextPane == null) {

            actualTextPane = new JTextPane();
        }

        actualTextPane.setText(baseModel.getMessage());
        actualTextPane.setEditable(false);
        actualTextPane.setOpaque(false);

        return actualTextPane;
    }

    /**
     Checks if the message contains a quoted text and displays it in a QuotePanelImpl.
     */
    private void checkForQuotesInMessage() {

        // EDT check done!
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
    private void setupEditorPopupMenu() {

        // EDT check done!

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

        // EDT check done!

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyItem = createAndAddMenuItem(popupMenu, "copy");

        addActionListenerToCopyJMenuItem(copyItem);
        addMouseListenerToJTextPane(actualTextPane, popupMenu);

        actualTextPane.setComponentPopupMenu(popupMenu);
    }

    /**
     Sets up the reply panels based on the panel type.

     If the panel type is normal, the method returns without performing any action.
     Otherwise, it sets the visibility of form_panel1 to false.

     This method does not return any value.
     */
    private void setupReplyPanels() {

        // EDT check done!
        if (panelTyp == PanelTypes.NORMAL) {

            return;
        }

        form_panel1.setVisible(false);
    }

    /**
     Sets the user message in the GUI.

     This method creates a JTextPane and sets its text to the user message retrieved from the message model.
     It then adds the JTextPane to the panel at the specified position.

     This method does not return any value.
     */
    private void setUserMessage() {

        // EDT check done!

        actualTextPane = createTextPane();

        actualTextPane.setEditorKit(new WrapEditorKit());

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        this.getPanel1().add(actualTextPane, "cell 0 1, wrap");
    }

    /**
     Sets the name field of the message label.

     This method retrieves the sender's name from the message model and displays it in the name label of the message.
     If the panel type is NORMAL and the sender's name is the same as the last message sender name, the name label is set to invisible.

     @param mainFrame The main frame of the GUI.
     This parameter is required to store the last message sender name.

     @throws IllegalStateException If the base model is not set in the current instance.

     This method does not return any value.
     */
    private void setNameField(MainFrameInterface mainFrame) {

        // EDT check done!
        String sender = baseModel.getSender();
        form_nameLabel.setText(sender);

        if (panelTyp == PanelTypes.NORMAL) {

            if (sender.equals(mainFrame.getLastMessageSenderName())) {

                form_nameLabel.setVisible(false);
            }

            mainFrame.setLastMessageSenderName(sender);
        }
    }

    /**
     Sets the timestamp field of the time label.

     This method retrieves the timestamp value from the message model and displays it in the time label of the message.

     @param mainFrame The main frame of the GUI.
     This parameter is required to store the last message timestamp.

     @throws IllegalStateException If the base model is not set in the current instance.

     This method does not return any value.
     */
    private void setTimestampField(MainFrameInterface mainFrame) {

        // EDT check done!
        String timeStamp = baseModel.getTime();
        form_timeLabel.setText(timeStamp);

        if (panelTyp == PanelTypes.NORMAL) {

            // If timestamp is the same as the last message timestamp, hide the time label
            if (timeStamp.equals(mainFrame.getLastMessageTimeStamp())) {

                form_timeLabel.setVisible(false);
            }

            mainFrame.setLastMessageTimeStamp(timeStamp);
        }
    }

    /**
     Creates a menu item with the given name and adds it to the specified popup menu.

     @param popupMenu    The popup menu to add the menu item to.
     This parameter cannot be null.
     @param menuItemName The name of the menu item to be created.
     This parameter cannot be null.

     @return The created menu item.
     This method returns a JMenuItem object.

     @throws IllegalArgumentException If either popupMenu or menuItemName is null.
     @since 1.0
     */
    private JMenuItem createAndAddMenuItem(JPopupMenu popupMenu, String menuItemName) {

        JMenuItem copyItem = new JMenuItem(menuItemName);
        popupMenu.add(copyItem);
        return copyItem;
    }

    /**
     Adds a MouseListener to the given JMenuItem.

     @param reply The JMenuItem to add the MouseListener to.
     This parameter cannot be null.

     @throws IllegalArgumentException If reply is null.
     */
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

    /**
     Adds an ActionListener to the given JMenuItem.

     @param menuOption The JMenuItem to add the ActionListener to.
     This parameter cannot be null.

     @throws IllegalArgumentException If menuOption is null.
     */
    private void addActionListenerToCopyJMenuItem(JMenuItem menuOption) {

        menuOption.addActionListener(e -> {
            final String selectedText = actualTextPane.getSelectedText();
            if (selectedText != null) {
                StringSelection selection = new StringSelection(selectedText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });
    }

    /**
     Adds a MouseListener to the given JTextPane that opens a JPopupMenu when the right mouse button is clicked.

     @param textPane  The JTextPane to add the MouseListener to.
     This parameter cannot be null.
     @param popupMenu The JPopupMenu to show when the right mouse button is clicked.
     This parameter cannot be null.

     @throws IllegalArgumentException If textPane or popupMenu is null.
     */
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
     Creates a JTextPane instance with set properties.

     @return a JTextPane instance with the following properties:
     - Editable: false
     - Opaque: false
     - Minimum size: 5x5 pixels.
     */
    private JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

        return jTextPane;
    }

    /**
     Overrides the replyButtonClicked method from the parent class.

     @param e The MouseEvent object representing the mouse event that triggered the method.

     The method shows a JPopupMenu at the specified location obtained from the MouseEvent object's
     getX() and getY() methods. The component on which the method is called is obtained from the
     getComponent() method of the MouseEvent object.
     */
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