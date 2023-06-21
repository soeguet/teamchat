package com.soeguet.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.client.CustomWebsocketClient;
import com.soeguet.gui.newcomment.Comment;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.gui.util.MessageTypes;
import com.soeguet.model.MessageModel;
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
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class WebsocketInteraction implements Serializable {
  private final ChatImpl chatImpl;

  public WebsocketInteraction(ChatImpl chatImpl) {
    this.chatImpl = chatImpl;
  }

  public void onMessageReceived(String message) {

    createNewMessageOnPane(message);
  }

  public void onCloseReconnect() {

    CustomWebsocketClient.resetClient();
    connectToWebSocket();
  }

  public void onByteBufferMessageReceived(@NotNull ByteBuffer bytes) {
    if (new String(bytes.array()).startsWith("PARTICIPANTS:")) {

      String participantString = new String(bytes.array());
      participantString = participantString.replace("PARTICIPANTS:", "");

      chatImpl.setParticipantNameArray(participantString.split(","));

      return;
    }

    if (new String(bytes.array()).equals("X")) {
      chatImpl.getForm_typingLabel().setText(" ");

    } else if (new String(bytes.array()).equals("/terminateAll")) {

      System.exit(0);
    }

    // add picture to chat panel
    else if (bytes.array().length > 50) {

      BufferedImage image;
      try {
        image = ImageIO.read(new ByteArrayInputStream(bytes.array()));

        if (image != null) {

          JLabel label;
          if (image.getWidth() > 351) {

            int maxWidth = 350;
            double scaleFactor = (double) maxWidth / image.getWidth();
            int newWidth = (int) (image.getWidth() * scaleFactor);
            int newHeight = (int) (image.getHeight() * scaleFactor);
            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            ImageIcon imageIcon = new ImageIcon(scaledImage);

            label = new JLabel(imageIcon);
          } else {
            label = new JLabel(new ImageIcon(image));
          }
          label.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  try {
                    File tempFile = File.createTempFile("tempImage", ".png");
                    ImageIO.write(image, "png", tempFile);

                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(tempFile);
                  } catch (IOException ex) {
                    ex.printStackTrace();
                  }
                }
              });
          chatImpl.form_mainTextPanel.add(label, "center, wrap");
          chatImpl.updateFrame();
        }

      } catch (IOException e) {
        e.printStackTrace();
      }

    } else {

      if (!chatImpl.getForm_typingLabel().getText().trim().equals("")) {

        if (!chatImpl.getForm_typingLabel().getText().contains(new String(bytes.array()))) {

          chatImpl
              .getForm_typingLabel()
              .setText(
                  chatImpl.getForm_typingLabel().getText().replace(" typing..", "")
                      + (", " + new String(bytes.array()) + " typing.."));
        }
      } else {

        chatImpl.getForm_typingLabel().setText(new String(bytes.array()) + " typing..");
      }
    }
  }

  void createNewMessageOnPane(String message) {

    SwingUtilities.invokeLater(
        () -> {
          try {
            MessageModel messageModel = ChatImpl.MAPPER.readValue(message, MessageModel.class);

            if (chatImpl.isStartup() && messageModel.getMessageType() == MessageTypes.DELETED) {

              Arrays.stream(chatImpl.getForm_mainTextPanel().getComponents())
                  .filter(a -> a instanceof Comment)
                  .filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId()))
                  .forEach(
                      c -> {
                        if (c.getClass().equals(PanelRightImpl.class)) {

                          ((PanelRightImpl) c).removeAll();
                          ((PanelRightImpl) c)
                              .add(
                                  new JLabel(
                                      messageModel.getTime() + " - " + messageModel.getMessage()),
                                  "cell 0 1");
                        } else {

                          ((PanelLeftImpl) c).removeAll();
                          ((PanelLeftImpl) c)
                              .add(
                                  new JLabel(
                                      messageModel.getTime() + " - " + messageModel.getMessage()),
                                  "cell 0 1");
                        }
                      });

            } else if (chatImpl.isStartup()
                && messageModel.getMessageType() == MessageTypes.INTERACTED) {

              Arrays.stream(chatImpl.getForm_mainTextPanel().getComponents())
                  .filter(a -> a instanceof Comment)
                  .filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId()))
                  .forEach(
                      c -> {
                        if (c.getClass().equals(PanelRightImpl.class)) {

                          ((PanelRightImpl) c).addEmojiInteraction(messageModel);

                        } else {

                          ((PanelLeftImpl) c).addEmojiInteraction(messageModel);
                        }
                      });

              SwingUtilities.invokeLater(chatImpl::repaint);

            } else if (chatImpl.isStartup()
                && messageModel.getMessageType() == MessageTypes.EDITED) {

              Arrays.stream(chatImpl.getForm_mainTextPanel().getComponents())
                  .filter(a -> a instanceof Comment)
                  .filter(b -> Objects.equals(((Comment) b).getId(), messageModel.getId()))
                  .forEach(
                      c -> {
                        if (c.getClass().equals(PanelRightImpl.class)) {

                          SwingUtilities.invokeLater(
                              () -> {
                                ((PanelRightImpl) c).getTextPaneComment().setText("");
                                ((PanelRightImpl) c).setTime(messageModel.getTime());
                                EmojiConverter.replaceWithEmoji(
                                    ((PanelRightImpl) c).getTextPaneComment(),
                                    messageModel.getMessage());
                              });

                        } else {

                          ((PanelLeftImpl) c).getTextPaneComment().setText("");
                          EmojiConverter.replaceWithEmoji(
                              ((PanelLeftImpl) c).getTextPaneComment(), messageModel.getMessage());
                        }
                      });

            } else {

              // check if message is from this client
              if (messageModel
                  .getSender()
                  .equals(
                      ChatImpl.mapOfIps.get(
                          ChatImpl.client.getLocalSocketAddress().getHostString()))) {
                PanelRightImpl panelRight =
                    new PanelRightImpl(
                        messageModel, chatImpl.getLastMessageFrom(), chatImpl.getLastPostTime());
                chatImpl.getForm_mainTextPanel().add(panelRight, "w 80%, trailing, wrap");
              } else {
                PanelLeftImpl panelLeft =
                    new PanelLeftImpl(
                        messageModel, chatImpl.getLastMessageFrom(), chatImpl.getLastPostTime());
                chatImpl.getForm_mainTextPanel().add(panelLeft, " w 80%, wrap");
              }

              if (chatImpl.isStartup() && chatImpl.isVisible()) {
                chatImpl.incomingMessagePreviewDesktopNotification(messageModel);
              }
            }

            chatImpl.setLastMessageFrom(messageModel.getSender());
            chatImpl.setLastPostTime(
                (messageModel.getTime().contains("*") ? "" : messageModel.getTime()));

          } catch (JsonProcessingException e) {

            JLabel label = new JLabel(message);
            label.setForeground(Color.RED);
            label.setFont(new Font(label.getFont().getName(), Font.BOLD, 15));
            chatImpl.getForm_mainTextPanel().add(label, "wrap, align center");

            // initial 100 messages should not be sending pop-ups, after that -> ok
            chatImpl.setStartup(false);
            chatImpl.getInitialLoadingStartUpDialog().dispose();

          } catch (NullPointerException e) {

            log.info(Arrays.toString(e.getStackTrace()));
          }

          chatImpl.updateFrame();
        });
  }

  void connectToWebSocket() {
    chatImpl.loadingInitialMessagesLoadUpPanel();
    ChatImpl.client = CustomWebsocketClient.getInstance();
    ChatImpl.client.connect();
    ChatImpl.client.addListener(chatImpl);
  }
}
