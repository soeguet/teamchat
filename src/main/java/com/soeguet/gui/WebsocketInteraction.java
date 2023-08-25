package com.soeguet.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.client.CustomWebsocketClient;
import com.soeguet.gui.newcomment.Comment;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.gui.util.MessageTypes;
import com.soeguet.model.MessageModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class WebsocketInteraction implements Serializable {

    private static final int MAX_WIDTH = 350;
    private static final String PARTICIPANTS_PREFIX = "PARTICIPANTS:";
    private static final String TERMINATION_COMMAND = "/terminateAll";
    private final ChatImpl chatImpl;
    Logger log = Logger.getLogger(WebsocketInteraction.class.getName());

    public WebsocketInteraction(ChatImpl chatImpl) {

        this.chatImpl = chatImpl;
    }

    public void onMessageReceived(String message) {

        createNewMessageOnPane(message);
    }

    /**
     * This method is invoked when a WebSocket connection is closed,
     * and it reconnects to the WebSocket.
     */
    public void onCloseReconnect() {

        CustomWebsocketClient.resetClient();
        connectToWebSocket();
    }

    public void onByteBufferMessageReceived(ByteBuffer bytes) {

        String message = new String(bytes.array());

        if (message.startsWith(PARTICIPANTS_PREFIX)) {
            String participantString = message.replace(PARTICIPANTS_PREFIX, "");
            chatImpl.setParticipantNameArray(participantString.split(","));
            return;
        }

        if (message.equals("X")) {
            chatImpl.getForm_typingLabel()
                    .setText(" ");
            return;
        }

        if (message.equals(TERMINATION_COMMAND)) {
            System.exit(0);
            return;
        }

        if (bytes.array().length > 50) {
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes.array()));
                if (image != null) {
                    processImage(image);
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            return;
        }

        updateTypingLabel(message);
    }

    /**
     * Process the received image and add it to the chat panel.
     *
     * @param image The BufferedImage to be processed.
     */
    private void processImage(BufferedImage image) {

        ImageIcon icon = new ImageIcon((image.getWidth() > MAX_WIDTH) ? resizeImage(image) : image);
        JLabel label = new JLabel(icon);
        addImageClickListener(label, image);
        chatImpl.form_mainTextPanel.add(label, "center, wrap");
        chatImpl.updateFrame();
    }

    /**
     * Resize the given image maintaining the aspect ratio according to the maximum width.
     *
     * @param image The original BufferedImage.
     * @return The resized Image.
     */
    private Image resizeImage(BufferedImage image) {

        double scaleFactor = (double) MAX_WIDTH / image.getWidth();
        int newWidth = (int) (image.getWidth() * scaleFactor);
        int newHeight = (int) (image.getHeight() * scaleFactor);
        return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    private void addImageClickListener(JLabel label, BufferedImage image) {

        label.addMouseListener(new ImageClickListener(image));
    }

    private File createAndWriteTempImageFile(BufferedImage image) throws IOException {

        File tempFile = File.createTempFile("tempImage", ".png");
        ImageIO.write(image, "png", tempFile);
        return tempFile;
    }

    private void openTempImageFile(File tempFile) throws IOException {

        Desktop.getDesktop()
               .open(tempFile);
    }

    private void updateTypingLabel(String message) {

        String labelTxt = chatImpl.getForm_typingLabel()
                                  .getText()
                                  .trim();
        if (labelTxt.isEmpty()) {
            chatImpl.getForm_typingLabel()
                    .setText(message + " typing..");
        } else if (!labelTxt.contains(message)) {
            chatImpl.getForm_typingLabel()
                    .setText(labelTxt.replaceAll(" typing..$", "") + (", " + message + " typing.."));
        }
    }

    void createNewMessageOnPane(String message) {

        SwingUtilities.invokeLater(() -> {

            try {

                MessageModel messageModel = ChatImpl.MAPPER.readValue(message, MessageModel.class);

                if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.DELETED) {

                    Arrays.stream(chatImpl.getForm_mainTextPanel()
                                          .getComponents())
                          .filter(a -> a instanceof Comment)
                          .filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId()))
                          .forEach(c -> {
                              if (c.getClass()
                                   .equals(PanelRightImpl.class)) {

                                  ((PanelRightImpl) c).removeAll();
                                  ((PanelRightImpl) c).add(new JLabel(messageModel.getTime() + " - " + messageModel.getMessage()), "cell 0 1");
                              } else {

                                  ((PanelLeftImpl) c).removeAll();
                                  ((PanelLeftImpl) c).add(new JLabel(messageModel.getTime() + " - " + messageModel.getMessage()), "cell 0 1");
                              }
                          });

                } else if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.INTERACTED) {

                    Arrays.stream(chatImpl.getForm_mainTextPanel()
                                          .getComponents())
                          .filter(a -> a instanceof Comment)
                          .filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId()))
                          .forEach(c -> {
                              if (c.getClass()
                                   .equals(PanelRightImpl.class)) {

                                  ((PanelRightImpl) c).addEmojiInteraction(messageModel);

                              } else {

                                  ((PanelLeftImpl) c).addEmojiInteraction(messageModel);
                              }
                          });

                    SwingUtilities.invokeLater(chatImpl::repaint);

                } else if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.EDITED) {

                    Arrays.stream(chatImpl.getForm_mainTextPanel()
                                          .getComponents())
                          .filter(a -> a instanceof Comment)
                          .filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId()))
                          .forEach(c -> {
                              if (c.getClass()
                                   .equals(PanelRightImpl.class)) {

                                  SwingUtilities.invokeLater(() -> {
                                      ((PanelRightImpl) c).getTextPaneComment()
                                                          .setText("");
                                      ((PanelRightImpl) c).setTime(messageModel.getTime());
                                      EmojiConverter.replaceWithEmoji(((PanelRightImpl) c).getTextPaneComment(), messageModel.getMessage());
                                  });

                              } else {

                                  ((PanelLeftImpl) c).getTextPaneComment()
                                                     .setText("");
                                  EmojiConverter.replaceWithEmoji(((PanelLeftImpl) c).getTextPaneComment(), messageModel.getMessage());
                              }
                          });

                } else {

                    // check if message is from this client
                    if (messageModel.getSender()
                                    .equals(ChatImpl.mapOfIps.get(ChatImpl.client.getLocalSocketAddress()
                                                                                 .getHostString()))) {
                        PanelRightImpl panelRight = new PanelRightImpl(messageModel, chatImpl.getLastMessageFrom(), chatImpl.getLastPostTime());
                        chatImpl.getForm_mainTextPanel()
                                .add(panelRight, "w 80%, trailing, wrap");
                    } else {
                        PanelLeftImpl panelLeft = new PanelLeftImpl(messageModel, chatImpl.getLastMessageFrom(), chatImpl.getLastPostTime());
                        chatImpl.getForm_mainTextPanel()
                                .add(panelLeft, " w 80%, wrap");
                    }

                    if (chatImpl.isStartup() && chatImpl.isVisible()) {
                        chatImpl.incomingMessagePreviewDesktopNotification(messageModel);
                    }
                }

                chatImpl.setLastMessageFrom(messageModel.getSender());
                chatImpl.setLastPostTime((messageModel.getTime()
                                                      .contains("*") ? "" : messageModel.getTime()));

            } catch (JsonProcessingException e) {

                JLabel label = new JLabel(message);
                label.setForeground(Color.RED);
                label.setFont(new Font(label.getFont()
                                            .getName(), Font.BOLD, 15));
                chatImpl.getForm_mainTextPanel()
                        .add(label, "wrap, align center");

                // initial 100 messages should not be sending pop-ups, after that -> ok
                chatImpl.setStartup(false);
                chatImpl.getInitialLoadingStartUpDialog()
                        .dispose();

            }

            chatImpl.updateFrame();
        });
    }

    void connectToWebSocket() {

        chatImpl.loadingInitialMessagesLoadUpDialog();
        ChatImpl.client = CustomWebsocketClient.getInstance();
        ChatImpl.client.connect();
        ChatImpl.client.addListener(chatImpl);
    }

    class ImageClickListener extends MouseAdapter {

        private final BufferedImage image;

        ImageClickListener(BufferedImage image) {

            this.image = image;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            try {
                File tempFile = createAndWriteTempImageFile(image);
                openTempImageFile(tempFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
