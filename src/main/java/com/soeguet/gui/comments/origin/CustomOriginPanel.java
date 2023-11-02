package com.soeguet.gui.comments.origin;

import com.soeguet.emoji.EmojiHandler;
import com.soeguet.gui.comments.util.QuotePanelImpl;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.reply.ReplyPanelImpl;
import com.soeguet.gui.reply.interfaces.ReplyInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.model.jackson.QuoteModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 The CustomOriginPanel class extends JPanel and provides additional functionality for displaying custom origin panels.
 */
public class CustomOriginPanel extends JPanel {

    private final MainFrameGuiInterface mainFrame;
    protected Logger LOGGER = Logger.getLogger(CustomOriginPanel.class.getName());
    /**
     Creates a new CustomOriginPanel object with the given MainFrameInterface object.

     @param mainFrame
     the MainFrameInterface object that will be used by the CustomOriginPanel
     */
    public CustomOriginPanel(MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    /**
     * Passes the given MouseEvent to the JTextPane component.
     *
     * @param e
     *      The MouseEvent object that needs to be passed to the JTextPane component.
     * @param source
     *      The JComponent where the MouseEvent originated from.
     * @param destination
     *      The JComponent where the MouseEvent should be passed to.
     */
    protected void passMouseEventToJTextPane(final MouseEvent e, JComponent source, JComponent destination) {

        //pass event through to the text pane, else you cannot select the text
        Point point = SwingUtilities.convertPoint(source, e.getPoint(), destination);
        Component target = SwingUtilities.getDeepestComponentAt(destination, point.x, point.y);

        if (target instanceof JTextPane) {

            MouseEvent convertedEvent = SwingUtilities.convertMouseEvent(source, e, target);
            target.dispatchEvent(convertedEvent);
        }
    }

    /**
     Extracts an image from a message.

     @param baseModel
     the base model containing the image data

     @return the extracted image as a BufferedImage, or null if an error occurs
     */
    protected BufferedImage extractImageFromMessage(BaseModel baseModel) {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((PictureModel) baseModel).getPicture())) {

            return ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error reading image", e);
            LOGGER.log(java.util.logging.Level.SEVERE, "CustomOriginPanel > extractImageFromMessage()");
        }

        return null;
    }

    /**
     Scales an image if it is too big.

     @param bufferedImage
     The image to be scaled.

     @return The scaled image as an ImageIcon.
     */
    protected ImageIcon scaleImageIfTooBig(BufferedImage bufferedImage) {

        if (bufferedImage.getWidth() > 500) {

            return new ImageIcon(bufferedImage.getScaledInstance(500, -1, Image.SCALE_AREA_AVERAGING));

        } else if (bufferedImage.getHeight() > 350) {

            return new ImageIcon(bufferedImage.getScaledInstance(-1, 350, Image.SCALE_AREA_AVERAGING));

        } else {

            return new ImageIcon(bufferedImage);
        }
    }

    protected void addMaximizePictureOnClick(JLabel imageLabel, BufferedImage image) {

        imageLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                final JMenuItem menuItem = buildPopupMenu(e, imageLabel);

                addMenuItemClickListener(menuItem, image);
            }
        });
    }

    private JMenuItem buildPopupMenu(final MouseEvent e, final JLabel imageLabel) {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("maximize");
        popupMenu.add(menuItem);
        popupMenu.show(imageLabel, e.getX(), e.getY());
        return menuItem;
    }

    private void addMenuItemClickListener(final JMenuItem menuItem, final BufferedImage image) {

        menuItem.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {

                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                    executor.submit(() -> openImageInExternalImageViewer(image));
                }
            }
        });
    }

    private void openImageInExternalImageViewer(BufferedImage image) {

        try {

            File tempFile = File.createTempFile("tempImage", ".png");
            ImageIO.write(image, "png", tempFile);
            Desktop.getDesktop().open(tempFile);

        } catch (IOException ex) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error opening image", ex);
            throw new RuntimeException();
        }
    }

    protected QuotePanelImpl checkForQuotesInMessage(MessageModel messageModel) {

        // FIXME: 02.11.23
        final QuoteModel<? extends BaseModel> quotedMessage = messageModel.getQuotedMessage();

        if (quotedMessage == null) {
            return null;
        }

        return new QuotePanelImpl(this.mainFrame, "quotedText", "quotedChatParticipant", "quotedTime");
    }

    /**
     Sets up the time field for a given base model and time label.

     @param baseModel
     The base model containing the time to be set.
     @param timeLabel
     The label where the time will be displayed.
     */
    protected void setupTimeField(BaseModel baseModel, JLabel timeLabel) {

        if (setTimestampFieldInvisible(baseModel)) {
            timeLabel.setVisible(false);
        }
        timeLabel.setText(baseModel.getTime());
    }

    /**
     Sets the timestamp field of the base model to be invisible.

     @param baseModel
     The base model containing the timestamp to be set. (NonNull)

     @return true if the timestamp field was set to invisible, false otherwise.
     */
    protected boolean setTimestampFieldInvisible(BaseModel baseModel) {

        String timeStamp = baseModel.getTime();

        final String lastMessageTimestamp = this.mainFrame.getLastMessageTimeStamp();
        final String previousMessageSenderName = this.mainFrame.getLastMessageSenderName();

        this.mainFrame.setLastMessageTimeStamp(timeStamp);

        // null value -> time
        if (previousMessageSenderName == null || lastMessageTimestamp == null) {
            return false;
        }

        // different sender -> time
        if (!previousMessageSenderName.equals(baseModel.getSender())) {
            return false;
        }

        // same sender, same time -> remove time
        return lastMessageTimestamp.equals(timeStamp);

        //just in case
    }

    /**
     Sets up the name field based on the given base model.

     @param baseModel
     The base model containing the information for setting up the name field. (NonNull)
     @param nameLabel
     The JLabel representing the name field to be set up. (NonNull)
     */
    protected void setupNameField(BaseModel baseModel, JLabel nameLabel) {

        if (setNameFieldInvisible(baseModel)) {
            nameLabel.setVisible(false);
        }

        if (nameLabel.getText().isBlank()) {

            nameLabel.setText(baseModel.getSender());
        }
    }

    /**
     Sets the visibility of the name field based on the given base model.

     @param baseModel
     The base model containing the information for setting up the name field. (NonNull)

     @return True if the name field should be set to invisible, false otherwise.
     */
    protected boolean setNameFieldInvisible(BaseModel baseModel) {

        String sender = baseModel.getSender();

        final String previousMessageSenderName = this.mainFrame.getLastMessageSenderName();

        // return no name and don't set a "new" sender
        if (previousMessageSenderName != null && previousMessageSenderName.equals(sender)) {
            return true;
        }

        this.mainFrame.setLastMessageSenderName(sender);
        return false;
    }


    protected JTextPane setUserMessage(BaseModel baseModel) {

        JTextPane actualTextPane = createTextPane();
        actualTextPane.setEditorKit(new WrapEditorKit());

        addRightClickOptionToPanel(actualTextPane);

        if (baseModel instanceof MessageModel messageModel) {

            new EmojiHandler(this.mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, messageModel.getMessage());
        }

        return actualTextPane;
    }

    /**
     Creates a JTextPane with specific settings.

     @return The JTextPane object.
     */
    protected JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

        return jTextPane;
    }

    /**
     Adds a right-click option to the panel.

     @param actualTextPane
     The JTextPane to add the right-click option to.
     */
    protected void addRightClickOptionToPanel(final JTextPane actualTextPane) {

        //return if no text is present
        if (actualTextPane == null) {
            return;
        }

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyItem = createAndAddMenuItem(popupMenu, "copy");

        addActionListenerToCopyJMenuItem(actualTextPane, copyItem);
        addMouseListenerToJTextPane(actualTextPane, popupMenu);

        actualTextPane.setComponentPopupMenu(popupMenu);
    }

    /**
     Creates and adds a menu item to the given popup menu.

     @param popupMenu
     The JPopupMenu to add the menu item to.
     @param menuItemName
     The name of the menu item to create.

     @return The created JMenuItem object.
     */
    protected JMenuItem createAndAddMenuItem(JPopupMenu popupMenu, String menuItemName) {

        JMenuItem copyItem = new JMenuItem(menuItemName);
        popupMenu.add(copyItem);
        return copyItem;
    }

    /**
     Adds an action listener to the given JMenuItem to copy the selected text from the given JTextPane to the system clipboard.

     @param actualTextPane
     The JTextPane containing the text to be copied.
     @param menuOption
     The JMenuItem to add the action listener to.
     */
    private void addActionListenerToCopyJMenuItem(JTextPane actualTextPane, JMenuItem menuOption) {

        menuOption.addActionListener(e -> {
            final String selectedText = actualTextPane.getSelectedText();
            if (selectedText != null) {
                StringSelection selection = new StringSelection(selectedText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });
    }

    /**
     Adds a mouse listener to the given JTextPane to show a popup menu when the right mouse button is clicked.

     @param textPane
     The JTextPane to add the mouse listener to.
     @param popupMenu
     The JPopupMenu to be shown when the right mouse button is clicked.
     */
    protected void addMouseListenerToJTextPane(JTextPane textPane, JPopupMenu popupMenu) {

        textPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(CustomOriginPanel.super.getComponent(0), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     Creates a JTextPane with the provided message as its text. The JTextPane is set to be non-editable and has a transparent background.

     @param baseModel
     The BaseModel object containing the message to be displayed.

     @return The created JTextPane with the message as its text, or null if the message is blank.
     */
    protected JTextPane createImageCaptionTextPane(BaseModel baseModel) {

        JTextPane actualTextPane = new JTextPane();

        if (baseModel instanceof PictureModel pictureModel) {

            if (pictureModel.getDescription().isBlank()) {

                //remove spacing if the message is black, which was reserved for the message
                return null;

            } else {

                actualTextPane.setText(pictureModel.getDescription());
                actualTextPane.setEditable(false);
                actualTextPane.setOpaque(false);
            }
        }

        return actualTextPane;
    }

    /**
     Creates and sets up a JPopupMenu for an editor.

     @param baseModel
     The BaseModel object containing the associated data.

     @return The created and configured JPopupMenu.
     */
    protected JPopupMenu setupEditorPopupMenu(final BaseModel baseModel) {

        JPopupMenu jPopupMenu = new JPopupMenu();

        JMenuItem reply = createAndAddMenuItem(jPopupMenu, "reply");
        addMouseListenerToReplyMenuItem(baseModel, reply);

        jPopupMenu.addSeparator();
        createAndAddMenuItem(jPopupMenu, "edit");
        //FEATURE add action listener to edit menu item
        createAndAddMenuItem(jPopupMenu, "delete");
        //FEATURE add action listener to delete menu item

        return jPopupMenu;
    }

    /**
     Adds a mouse listener to the "reply" menu item in a JPopupMenu. When the menu item is clicked, it creates and displays a ReplyPanelImpl with the
     specified baseModel and adds it to the mainTextPanel's layered pane.

     @param baseModel
     The BaseModel object containing the associated data.
     @param reply
     The "reply" menu item to which the mouse listener is added.
     */
    protected void addMouseListenerToReplyMenuItem(BaseModel baseModel, JMenuItem reply) {

        reply.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                super.mouseReleased(e);

                ReplyInterface replyPanel = new ReplyPanelImpl(mainFrame, baseModel);
                replyPanel.populatePanel();
                replyPanel.setPosition();
                replyPanel.requestAllFocus();
                replyPanel.addPanelToMainFrame();
            }
        });
    }
}