package com.soeguet.gui.reply;

import static com.soeguet.gui.ChatImpl.client;
import static com.soeguet.gui.ChatImpl.mapOfIps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.client.CustomWebsocketClient;
import com.soeguet.config.Settings;
import com.soeguet.gui.emoji.EmojiImpl;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.model.MessageModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.apache.commons.lang3.StringUtils;

public class ReplyImpl extends FeedbackDialog {

  private final MessageModel messageModel;
  private EmojiImpl emojiWindow;
  private boolean controlButtonPressed;

  public ReplyImpl(MessageModel messageModel) {

    super();

    Settings settings = Settings.getInstance();
    this.messageModel = messageModel;
    this.setFont(new Font(getFont().getFontName(), getFont().getStyle(), settings.getFontSize()));

    if (Objects.equals(messageModel.getSender(), client.getLocalSocketAddress().getHostName())) {

      PanelRightImpl replyContent = new PanelRightImpl(messageModel);
      form_contentPanel.add(replyContent, "cell 0 0, wrap");
    } else {

      PanelLeftImpl replyContent = new PanelLeftImpl(messageModel);
      form_contentPanel.add(replyContent, "cell 0 0, wrap");
    }

    pack();
    setTitle("reply to message");
    setLocationRelativeTo(null);

    form_replyTextPane.requestFocusInWindow();
  }

  @Override
  protected void sendMessageReply() {

    try {
      ObjectMapper mapper = new ObjectMapper();
      CustomWebsocketClient client = CustomWebsocketClient.getInstance();

      MessageModel messageModel =
          new MessageModel(
              mapOfIps.get(client.getLocalSocketAddress().getHostString()),
              EmojiConverter.checkTextForEmojis(form_replyTextPane),
              this.messageModel.getSender(),
              this.messageModel.getTime(),
              this.messageModel.getMessage());

      client.send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageModel));
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }

    SwingUtilities.invokeLater(
        () -> {
          if (emojiWindow != null && emojiWindow.isVisible()) {

            emojiWindow.dispose();
          }
          this.dispose();
        });
  }

  private void initEmojiFrame() {

    ClassLoader classLoader = getClass().getClassLoader();
    Enumeration<URL> resources; // search for all resources in the "emoji" folder
    try {
      resources = classLoader.getResources("emojis/");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    while (resources.hasMoreElements()) {

      URL resourceUrl = resources.nextElement();

      // if resources are WITHIN .jar
      if (resourceUrl.getProtocol().equals("jar")) {

        try {

          JarURLConnection jarURLConnection = (JarURLConnection) resourceUrl.openConnection();
          try (JarFile jarFile = jarURLConnection.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {

              JarEntry entry = entries.nextElement();
              String entryName = entry.getName();

              if (entryName.startsWith("emojis/") && entryName.endsWith(".png")) {

                try (InputStream inputStream = classLoader.getResourceAsStream(entryName)) {

                  assert inputStream != null;
                  BufferedImage bufferedImage = ImageIO.read(inputStream);

                  ImageIcon icon = new ImageIcon(bufferedImage);

                  String fileName = entryName.replace(".png", "");
                  fileName = fileName.replace("emojis/", "");

                  JButton jButton = new JButton();
                  jButton.setName(fileName);
                  icon.setDescription(fileName);
                  jButton.setIcon(icon);

                  jButton.addMouseListener(
                      new MouseListener() {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                          form_replyTextPane.insertIcon(icon);
                          form_replyTextPane.insertComponent(new JLabel(""));
                          form_replyTextPane.repaint();
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {}

                        @Override
                        public void mouseReleased(MouseEvent e) {}

                        @Override
                        public void mouseEntered(MouseEvent e) {}

                        @Override
                        public void mouseExited(MouseEvent e) {}
                      });

                  jButton.addKeyListener(
                      new KeyListener() {

                        @Override
                        public void keyTyped(KeyEvent e) {}

                        @Override
                        public void keyPressed(KeyEvent e) {

                          if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER
                              || e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {

                            form_replyTextPane.insertIcon(icon);
                            form_replyTextPane.insertComponent(new JLabel(""));
                            form_replyTextPane.repaint();
                          }

                          if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {

                            if (emojiWindow != null && emojiWindow.isVisible()) {

                              emojiWindow.dispose();
                              form_replyTextPane.requestFocusInWindow();
                            }
                          }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {}
                      });

                  emojiWindow.add(jButton);
                }
              }
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      // if resources are OUTSIDE .jar
      else {
        File folder2 = new File("./src/main/resources/emojis");
        File[] files = folder2.listFiles();
        if (files != null) {

          for (File file : files) {

            if (file.getName().endsWith(".png")) {

              ImageIcon icon = new ImageIcon(file.getPath());

              String fileName = file.getName().replace(".png", "");
              fileName = fileName.replace("emojis/", "");

              JButton jButton = new JButton();
              jButton.setName(fileName);
              icon.setDescription(fileName);
              jButton.setIcon(icon);

              jButton.addMouseListener(
                  new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                      form_replyTextPane.insertIcon(icon);
                      form_replyTextPane.insertComponent(new JLabel(""));
                      form_replyTextPane.repaint();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {}

                    @Override
                    public void mouseReleased(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseExited(MouseEvent e) {}
                  });

              jButton.addKeyListener(
                  new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {}

                    @Override
                    public void keyPressed(KeyEvent e) {

                      if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER
                          || e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {

                        form_replyTextPane.insertIcon(icon);
                        form_replyTextPane.insertComponent(new JLabel(""));
                        form_replyTextPane.repaint();
                      }

                      if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {

                        if (emojiWindow != null && emojiWindow.isVisible()) {

                          emojiWindow.dispose();
                          form_replyTextPane.requestFocusInWindow();
                        }
                      }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {}
                  });

              emojiWindow.add(jButton);
            }
          }
        }
      }
    }
  }

  @Override
  protected void dialogPaneKeyPressed(KeyEvent e) {

    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      SwingUtilities.invokeLater(this::dispose);
    }
  }

  @Override
  protected void sendButton(ActionEvent e) {
    sendMessageReply();
  }

  @Override
  protected void thisComponentHidden(ComponentEvent e) {

    if (emojiWindow != null && emojiWindow.isVisible()) {
      SwingUtilities.invokeLater(emojiWindow::dispose);
    }
  }

  @Override
  protected void replyTextPaneKeyReleased(KeyEvent e) {

    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      controlButtonPressed = false;
    }
  }

  @Override
  protected void replyTextPaneKeyPressed(KeyEvent e) {

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

      return;
    }

    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      SwingUtilities.invokeLater(this::dispose);
      e.consume();
    }

    // hashtag button
    if ((e.getKeyCode() == KeyEvent.VK_NUMBER_SIGN)
        || (e.getExtendedKeyCode() == KeyEvent.VK_NUMBER_SIGN)) {
      SwingUtilities.invokeLater(
          () -> {
            emojiButtonMouseClicked(null);
            form_replyTextPane.setText(StringUtils.chop(form_replyTextPane.getText()));
          });

      e.consume();
      return;
    }

    if ((e.getKeyCode() == KeyEvent.VK_CONTROL)
        || (e.getExtendedKeyCode() == KeyEvent.VK_CONTROL)) {
      controlButtonPressed = true;
      e.consume();
      return;
    }

    if (!controlButtonPressed && e.getKeyCode() == KeyEvent.VK_ENTER) {
      sendButton(null);

      e.consume();
    } else if (controlButtonPressed && e.getKeyCode() == KeyEvent.VK_ENTER) {
      form_replyTextPane.setText(form_replyTextPane.getText() + "\n");

      e.consume();
    }
  }

  @Override
  protected void emojiButtonMouseClicked(MouseEvent e) {

    if (emojiWindow != null && emojiWindow.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiWindow.dispose());
    } else {

      emojiWindow = new EmojiImpl(form_replyTextPane);
      initEmojiFrame();
      emojiWindow.pack();
      emojiWindow.repaint();
      emojiWindow.revalidate();
      emojiWindow.setLocation(
          this.getBounds().x + this.getBounds().width,
          this.getBounds().y + this.getBounds().height - emojiWindow.getHeight());
      emojiWindow.setVisible(true);
    }
  }
}
