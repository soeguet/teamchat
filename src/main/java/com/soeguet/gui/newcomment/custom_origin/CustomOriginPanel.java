package com.soeguet.gui.newcomment.custom_origin;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class CustomOriginPanel extends JPanel {

    protected Logger LOGGER = Logger.getLogger(CustomOriginPanel.class.getName());

    /**
     Extracts an image from a message.

     @param baseModel The base model containing the image data.

     @return The extracted image as a BufferedImage. Returns null if an error occurs.
     */
    protected BufferedImage extractImageFromMessage(BaseModel baseModel) {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((PictureModel) baseModel).getPicture())) {

            return ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error reading image", e);
        }

        return null;
    }

    /**
     Scales an image if it is too big.

     @param bufferedImage The image to be scaled.

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

                try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                    executor.submit(() -> {

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
                                throw new RuntimeException(ex);
                            }

                        } else {

                            LOGGER.log(java.util.logging.Level.SEVERE, "Desktop not supported");
                        }

                        if (!imgFile.delete()) {

                            LOGGER.log(java.util.logging.Level.SEVERE, "Error deleting temp image file");
                            throw new RuntimeException();
                        }
                    });
                }
            }
        });
    }

    /**
     * Checks if a message contains a quote and creates a QuotePanel with the quote information.
     *
     * @param mainFrame     The main frame object.
     * @param messageModel  The message model object.
     * @return The created QuotePanel, or null if the message does not contain a quote.
     */
    protected QuotePanelImpl checkForQuotesInMessage(MainFrameInterface mainFrame, MessageModel messageModel) {

        // EDT check done!
        String quotedText = messageModel.getQuotedMessageText();

        if (quotedText == null) {
            return null;
        }

        String quotedChatParticipant = messageModel.getQuotedMessageSender();
        String quotedTime = messageModel.getQuotedMessageTime();

        return new QuotePanelImpl(mainFrame, quotedText, quotedChatParticipant, quotedTime);
    }

    /**
     * Sets the name field in a form with the sender's name from a BaseModel object.
     *
     * @param mainFrame       The main frame object.
     * @param baseModel       The base model object.
     * @param form_nameLabel  The label to display the sender's name.
     * @param panelTyp        The type of panel.
     */
    protected void setNameField(MainFrameInterface mainFrame, BaseModel baseModel, JLabel form_nameLabel, PanelTypes panelTyp) {

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
     * Sets the timestamp field in a form with the timestamp from a BaseModel object.
     *
     * @param mainFrame       The main frame object.
     * @param baseModel       The base model object.
     * @param form_timeLabel  The label to display the timestamp.
     * @param panelTyp        The type of panel.
     */
    protected void setTimestampField(MainFrameInterface mainFrame, BaseModel baseModel, JLabel form_timeLabel, PanelTypes panelTyp) {

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
     * Sets the user message in a text pane, replacing emoji descriptions with actual image icons.
     *
     * @param mainFrame  The main frame object.
     * @param baseModel  The base model object.
     * @return The text pane containing the user message.
     */
    protected JTextPane setUserMessage(MainFrameInterface mainFrame, BaseModel baseModel) {

        // EDT check done!
        JTextPane actualTextPane = createTextPane();
        actualTextPane.setEditorKit(new WrapEditorKit());

        addRightClickOptionToPanel(actualTextPane);

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        return actualTextPane;
    }

    /**
     * Creates a JTextPane with specific settings.
     *
     * @return The JTextPane object.
     */
    protected JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

        return jTextPane;
    }

    /**
     * Adds a right-click option to the panel.
     *
     * @param actualTextPane The JTextPane to add the right-click option to.
     */
    protected void addRightClickOptionToPanel(final JTextPane actualTextPane) {

        // EDT check done!
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
     * Creates and adds a menu item to the given popup menu.
     *
     * @param popupMenu The JPopupMenu to add the menu item to.
     * @param menuItemName The name of the menu item to create.
     * @return The created JMenuItem object.
     */
    protected JMenuItem createAndAddMenuItem(JPopupMenu popupMenu, String menuItemName) {

        JMenuItem copyItem = new JMenuItem(menuItemName);
        popupMenu.add(copyItem);
        return copyItem;
    }

    /**
     * Adds an action listener to the given JMenuItem to copy the selected text from the given JTextPane
     * to the system clipboard.
     *
     * @param actualTextPane The JTextPane containing the text to be copied.
     * @param menuOption The JMenuItem to add the action listener to.
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
     * Adds a mouse listener to the given JTextPane to show a popup menu when the right mouse button is clicked.
     *
     * @param textPane The JTextPane to add the mouse listener to.
     * @param popupMenu The JPopupMenu to be shown when the right mouse button is clicked.
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
     * Creates a JTextPane with the provided message as its text. The JTextPane is set to be non-editable
     * and has a transparent background.
     *
     * @param baseModel The BaseModel object containing the message to be displayed.
     * @return The created JTextPane with the message as its text, or null if the message is blank.
     */
    protected JTextPane createImageCaptionTextPane(BaseModel baseModel) {

        // EDT check done!

        JTextPane actualTextPane = new JTextPane();

        if (baseModel.getMessage().isBlank()) {

            //remove spacing if the message is black, which was reserved for the message
            return null;

        } else {

            actualTextPane.setText(baseModel.getMessage());
            actualTextPane.setEditable(false);
            actualTextPane.setOpaque(false);
        }

        return actualTextPane;
    }

    /**
     * Sets up the editor's popup menu with various menu items, such as "Reply", "Edit", and "Delete".
     *
     * @param mainFrame The MainFrameInterface object representing the main frame of the application.
     * @param baseModel The BaseModel object to which the popup menu is being set up.
     * @return The JPopupMenu object containing the setup menu items.
     */
    protected JPopupMenu setupEditorPopupMenu(final MainFrameInterface mainFrame, final BaseModel baseModel) {

        // EDT check done!

        JPopupMenu jPopupMenu = new JPopupMenu();

        JMenuItem reply = createAndAddMenuItem(jPopupMenu, "reply");
        addMouseListenerToReplyMenuItem(mainFrame, baseModel, reply);

        jPopupMenu.addSeparator();
        createAndAddMenuItem(jPopupMenu, "edit");
        //TODO add action listener to edit menu item
        createAndAddMenuItem(jPopupMenu, "delete");
        //TODO add action listener to delete menu item

        return jPopupMenu;
    }


    /**
     * Adds a mouse listener to the specified "Reply" menu item.
     * When the menu item is clicked, it displays a reply panel in the main frame's layered pane.
     *
     * @param mainFrame The MainFrameInterface object representing the main frame of the application.
     * @param baseModel The BaseModel object associated with the menu item.
     * @param reply The JMenuItem object representing the "Reply" menu item.
     */
    protected void addMouseListenerToReplyMenuItem(MainFrameInterface mainFrame, BaseModel baseModel, JMenuItem reply) {

        reply.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                super.mouseReleased(e);

                ReplyPanelImpl replyPanel = new ReplyPanelImpl(mainFrame, baseModel);
                mainFrame.getMainTextPanelLayeredPane().add(replyPanel, JLayeredPane.MODAL_LAYER);
            }
        });
    }

}