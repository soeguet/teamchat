package com.soeguet.gui.newcomment.custom_origin;

import com.soeguet.gui.interaction.ReplyPanelImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
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
     Sets up the reply panel based on the specified panel type.
     If the panel type is NORMAL, no action is taken.
     Otherwise, the specified form_panel1 is set to be invisible.

     @param panelTyp    the PanelTypes enum specifying the type of panel
     @param form_panel1 the JPanel to be set to invisible if the panel type is not NORMAL
     */
    protected void setupReplyPanels(PanelTypes panelTyp, JPanel form_panel1) {

        // EDT check done!
        if (panelTyp == PanelTypes.NORMAL) {

            return;
        }

        form_panel1.setVisible(false);
    }

    protected BufferedImage extractImageFromMessage(BaseModel baseModel) {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((PictureModel) baseModel).getPicture())) {

            return ImageIO.read(byteArrayInputStream);

        } catch (IOException e) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Error reading image", e);
        }

        return null;
    }

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

//                        if (Desktop.isDesktopSupported()) {

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

//                        } else {
//
//                            LOGGER.log(java.util.logging.Level.SEVERE, "Desktop not supported");
//                        }

                        if (!imgFile.delete()) {

                            LOGGER.log(java.util.logging.Level.SEVERE, "Error deleting temp image file");
                            throw new RuntimeException();
                        }
                    });
                }
            }
        });
    }

    protected void checkForQuotesInMessage(MainFrameInterface mainFrame, BaseModel baseModel, JPanel panel1) {

        // EDT check done!
        MessageModel messageModel = (MessageModel) baseModel;

        String quotedText = messageModel.getQuotedMessageText();

        if (quotedText == null) {
            return;
        }

        String quotedChatParticipant = messageModel.getQuotedMessageSender();
        String quotedTime = messageModel.getQuotedMessageTime();

        QuotePanelImpl quotedSectionPanel = new QuotePanelImpl(mainFrame, quotedText, quotedChatParticipant, quotedTime);
        panel1.add(quotedSectionPanel, "cell 0 0, wrap");
    }

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

    protected JTextPane setUserMessage(MainFrameInterface mainFrame, BaseModel baseModel, JPanel panel1) {

        // EDT check done!
        JTextPane actualTextPane = createTextPane();

        actualTextPane.setEditorKit(new WrapEditorKit());

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(actualTextPane, baseModel.getMessage());

        panel1.add(actualTextPane, "cell 0 1, wrap");

        return actualTextPane;
    }

    protected JTextPane createTextPane() {

        JTextPane jTextPane = new JTextPane();

        jTextPane.setEditable(false);
        jTextPane.setOpaque(false);
        jTextPane.setMinimumSize(new Dimension(5, 5));

        return jTextPane;
    }

    protected void addRightClickOptionToPanel(CommentInterface commentInterface, final JTextPane actualTextPane) {

        // EDT check done!
        //return if no text is present
        if (actualTextPane == null) {
            return;
        }

        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyItem = createAndAddMenuItem(popupMenu, "copy");

        addActionListenerToCopyJMenuItem(actualTextPane, copyItem);
        addMouseListenerToJTextPane(actualTextPane, popupMenu, commentInterface);

        actualTextPane.setComponentPopupMenu(popupMenu);
    }

    protected JMenuItem createAndAddMenuItem(JPopupMenu popupMenu, String menuItemName) {

        JMenuItem copyItem = new JMenuItem(menuItemName);
        popupMenu.add(copyItem);
        return copyItem;
    }

    private void addActionListenerToCopyJMenuItem(JTextPane actualTextPane, JMenuItem menuOption) {

        menuOption.addActionListener(e -> {
            final String selectedText = actualTextPane.getSelectedText();
            if (selectedText != null) {
                StringSelection selection = new StringSelection(selectedText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            }
        });
    }

    protected void addMouseListenerToJTextPane(JTextPane textPane, JPopupMenu popupMenu, CommentInterface commentInterface) {

        textPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show((Component) commentInterface, e.getX(), e.getY());
                }
            }
        });
    }

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
     Adds a MouseListener to the given JMenuItem to handle the mouseReleased event.
     When the mouseReleased event occurs, a ReplyPanelImpl object is created and added to the MainTextPanelLayeredPane of the specified MainFrameInterface.

     @param mainFrame the MainFrameInterface object to access the MainTextPanelLayeredPane
     @param baseModel the BaseModel object for creating the ReplyPanelImpl
     @param reply     the JMenuItem to which the MouseListener will be added
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