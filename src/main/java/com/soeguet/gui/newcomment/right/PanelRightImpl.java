package com.soeguet.gui.newcomment.right;

import static com.soeguet.gui.ChatImpl.MAPPER;
import static com.soeguet.gui.ChatImpl.client;
import static com.soeguet.gui.ChatImpl.mapOfIps;
import static com.soeguet.gui.util.EmojiConverter.emojiListFull;
import static com.soeguet.gui.util.EmojiConverter.replaceWithEmoji;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.soeguet.config.Settings;
import com.soeguet.gui.ChatImpl;
import com.soeguet.gui.newcomment.Colorpicker;
import com.soeguet.gui.newcomment.Comment;
import com.soeguet.gui.newcomment.pane.TextPaneImpl;
import com.soeguet.gui.reply.EditImpl;
import com.soeguet.gui.reply.ReplyImpl;
import com.soeguet.gui.translate.TranslateDialogImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.gui.util.MessageTypes;
import com.soeguet.model.MessageModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.jetbrains.annotations.Nullable;

public class PanelRightImpl extends PanelRight implements Comment {

  private final MessageModel messageModel;
  private final Long id;
  private TextPaneImpl textPaneComment;
  private boolean executedFromJar = false;
  private JsonNode root;

  // for replies
  public PanelRightImpl(MessageModel messageModel) {
    this(messageModel, null, null);
    form_button1.setVisible(false);
  }

  // for normal chat messages on panel
  public PanelRightImpl(MessageModel messageModel, String lastMessageFrom, String lastPostTime) {

    this.messageModel = messageModel;
    this.id = messageModel.getId();

    try {
      root = ChatImpl.MAPPER.readTree(
          new File(System.getProperty("user.home") + "/.teamchat/settings.json"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    customInitialMethods(messageModel, lastMessageFrom, lastPostTime);
  }

  private void customInitialMethods(
      MessageModel messageModel, @Nullable String lastMessageFrom, String lastPostTime) {
    if (messageIsDeleted(messageModel))
      return;

    setBorderColor(Colorpicker.colorPicker(messageModel.getSender()).getBorderColor());
    // TODO add ip to messageModel and use it here
    // setBorderColor(new
    // Color(root.get("chatParticipants").get(messageModel.get).get("uiColor").asInt()));
    Settings settings = Settings.getInstance();
    textPaneComment = new TextPaneImpl();

    addRightClickOptionToPanel();

    form_panel1.add(textPaneComment, "cell 0 1,grow");

    addQuoteLayerToCommentIfPresent(messageModel, settings);

    replaceWithEmoji(textPaneComment, messageModel.getMessage());

    form_nameLabel.setText(messageModel.getSender());
    form_timeLabel.setText(messageModel.getTime());

    addingImportantFlagToComment(messageModel);

    // add user interaction emojis to comment
    if (messageModel.getUserInteractions() != null
        && messageModel.getUserInteractions().size() > 0) {

      addEmojiToInteractionPane(messageModel);
    }

    if (lastMessageFrom != null && lastMessageFrom.equals(messageModel.getSender())) {
      form_nameLabel.setVisible(false);

      if (lastPostTime.equals(messageModel.getTime())) {
        form_timeLabel.setVisible(false);
      }
    }
  }

  private void addRightClickOptionToPanel() {
    JPopupMenu popupMenu = new JPopupMenu();
    popupMenu.setBackground(Color.WHITE);
    JMenuItem copyItem = new JMenuItem("copy");
    JMenuItem translateItem = new JMenuItem("translate");

    copyItem.addActionListener(e -> textPaneComment.copy());
    translateItem.addActionListener(
        e -> {
          JDialog translateDialog = new TranslateDialogImpl(null, textPaneComment.getSelectedText());
          translateDialog.setVisible(true);
        });

    popupMenu.add(copyItem);
    popupMenu.add(translateItem);

    textPaneComment.setComponentPopupMenu(popupMenu);

    textPaneComment.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
              popupMenu.show(textPaneComment, e.getX(), e.getY());
            }
          }
        });
  }

  private void addQuoteLayerToCommentIfPresent(MessageModel messageModel, Settings settings) {
    if (messageModel.getQuotedMessageText() != null) {

      TextPaneImpl replyTextPane = new TextPaneImpl() {

        @Override
        protected void paintComponent(Graphics grphcs) {

          Graphics2D g2d = (Graphics2D) grphcs;
          g2d.setRenderingHints(
              new RenderingHints(
                  RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

          g2d.setColor(Colorpicker.colorPicker(messageModel.getSender()).getBorderColor());
          g2d.drawLine(0, 0, 0, getHeight());
          g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());

          g2d.setColor(Colorpicker.colorPicker(messageModel.getSender()).getQuoteColor());
          g2d.fillRect(1, 0, getWidth() - 2, getHeight());

          super.paintComponent(grphcs);
        }
      };

      replaceWithEmoji(replyTextPane, messageModel.getQuotedMessageText());
      replyTextPane.setDisabledTextColor(settings.getTextColor());
      this.form_panel1.add(replyTextPane, "cell 0 0,grow");
      replyTextPane.setEnabled(false);
      replyTextPane.setBorder(
          new TitledBorder(
              BorderFactory.createEmptyBorder(),
              messageModel.getQuotedMessageSender() + " - " + messageModel.getQuotedMessageTime(),
              TitledBorder.LEADING,
              TitledBorder.ABOVE_TOP,
              null,
              new Color(117, 133, 175)));
      replyTextPane.setMinimumSize(new Dimension(150, 50));
    }
  }

  private void addingImportantFlagToComment(MessageModel messageModel) {
    if (messageModel.getMessageType() == 5) {
      add(new JLabel("!!!"), "cell 0 1");
    }
  }

  public TextPaneImpl getTextPaneComment() {
    return textPaneComment;
  }

  public void setTime(String time) {

    form_timeLabel.setText(time);
    form_timeLabel.setVisible(true);
  }

  private void addEmojiToInteractionPane(MessageModel messageModel) {
    executedFromJar = String.valueOf(PanelRightImpl.class.getResource("PanelRightImpl.class")).startsWith("jar:");

    JPanel userInteractionPanel = new JPanel();
    add(userInteractionPanel, "cell 1 2");

    AtomicInteger interactionCount = new AtomicInteger(0);

    SwingUtilities.invokeLater(
        () -> messageModel.getUserInteractions().stream()
            .limit(form_layeredPane1.getWidth() / 25)
            .forEach(
                (interaction) -> {
                  ImageIcon icon;

                  if (executedFromJar) {

                    icon = new ImageIcon(
                        Objects.requireNonNull(
                            EmojiConverter.class.getResource(
                                "/emojis/" + interaction.getEmoji() + ".png")));

                  } else {

                    icon = new ImageIcon(
                        "src/main/resources/emojis/" + interaction.getEmoji() + ".png");
                  }

                  icon.setDescription(interaction.getUsername());

                  int diameter = 25;

                  JLabel jLabel = new JLabel(icon) {
                    @Override
                    protected void paintComponent(Graphics g) {

                      Graphics2D g2d = (Graphics2D) g;
                      g2d.setRenderingHints(
                          new RenderingHints(
                              RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON));
                      g2d.setColor(Color.WHITE);
                      g2d.fillRoundRect(1, 1, diameter - 2, diameter - 2, 50, 50);

                      g2d.setColor(Color.DARK_GRAY);
                      g2d.drawRoundRect(0, 0, diameter - 1, diameter - 1, 50, 50);
                      super.paintComponent(g);
                    }
                  };

                  jLabel.setToolTipText(interaction.getUsername());
                  jLabel.setBounds(
                      (interactionCount.get() * diameter),
                      form_layeredPane1.getHeight() - diameter,
                      diameter,
                      diameter);

                  jLabel.setVisible(true);
                  form_layeredPane1.add(jLabel);
                  form_layeredPane1.moveToFront(jLabel);

                  interactionCount.getAndIncrement();

                  repaint();
                }));
  }

  public void addEmojiInteraction(MessageModel messageModel) {

    addEmojiToInteractionPane(messageModel);
  }

  @Override
  protected void actionLabelMouseEntered(MouseEvent e) {
    e.consume();

    form_actionLabel.setForeground(new Color(root.get("userPreferences").get("uiColor").asInt()));
  }

  private JPopupMenu likePopup;

  @Override
  protected void actionLabelMouseClicked(MouseEvent e) {

    if (likePopup == null) {

      likePopup = new JPopupMenu();
      likePopup.setLayout(new GridLayout(10, 10));

      emojiListFull.forEach(
          emoji -> {
            ImageIcon icon;
            if (executedFromJar) {
              icon = new ImageIcon(
                  Objects.requireNonNull(
                      EmojiConverter.class.getResource("/emojis/" + emoji + ".png")));
            } else {
              icon = new ImageIcon("src/main/resources/emojis/" + emoji + ".png");
            }

            icon.setDescription(emoji);

            JButton jButton = new JButton(icon);
            jButton.setName(emoji);
            jButton.setPreferredSize(new Dimension(25, 25));
            jButton.setBorderPainted(false);
            jButton.setContentAreaFilled(false);
            jButton.setFocusPainted(false);
            jButton.setOpaque(false);

            jButton.addMouseListener(
                new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {

                    e.consume();
                    MessageModel likeModel = new MessageModel(
                        messageModel.getId(),
                        MessageTypes.INTERACTED,
                        messageModel.addUserInteractions(
                            mapOfIps.get(client.getLocalSocketAddress().getHostString()),
                            jButton.getName()),
                        messageModel.getLocalIp(),
                        messageModel.getSender(),
                        messageModel.getTime(),
                        messageModel.getMessage(),
                        messageModel.getQuotedMessageSender(),
                        messageModel.getQuotedMessageTime(),
                        messageModel.getQuotedMessageText());

                    try {
                      client.send(
                          MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(likeModel));
                    } catch (JsonProcessingException ex) {
                      throw new RuntimeException(ex);
                    }

                    likePopup.setVisible(false);
                  }
                });

            likePopup.add(jButton);
          });
    }
    likePopup.show(e.getComponent(), e.getX(), e.getY());
  }

  @Override
  protected void actionLabelMouseExited(MouseEvent e) {
    e.consume();
    // revert ui element back to transparent
    form_actionLabel.setForeground(new Color(0, 0, 0, 0));
  }

  private boolean messageIsDeleted(MessageModel messageModel) {
    // deleted message
    if (messageModel.getMessageType() == 127) {

      removeAll();
      add(new JLabel(messageModel.getTime() + " - " + messageModel.getMessage()), "cell 0 1");
      return true;
    }
    return false;
  }

  @Override
  protected void replyButtonClicked(MouseEvent e) {

    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem reply = new JMenuItem("reply");
    reply.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {
            SwingUtilities.invokeLater(
                () -> {
                  ReplyImpl replyDialog = new ReplyImpl(messageModel);
                  replyDialog.setVisible(true);
                });
          }
        });
    JMenuItem edit = new JMenuItem("edit");
    edit.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {
            SwingUtilities.invokeLater(
                () -> {
                  EditImpl reply = new EditImpl(messageModel);
                  reply.setVisible(true);
                });
          }
        });
    JMenuItem delete = new JMenuItem("delete");
    delete.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {

            MessageModel modelDelete = new MessageModel();
            modelDelete.setSender(messageModel.getSender());
            modelDelete.setId(messageModel.getId());
            modelDelete.setMessageType(MessageTypes.DELETED);
            modelDelete.setMessage("deleted by user");

            modelDelete.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

            try {

              client.send(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(modelDelete));
            } catch (JsonProcessingException ex) {
              throw new RuntimeException(ex);
            }

            removeAll();
            SwingUtilities.invokeLater(
                () -> {
                  add(new JLabel("deleted comment"));
                  repaint();
                  revalidate();
                });
          }
        });
    popupMenu.add(reply);
    popupMenu.add(edit);
    popupMenu.add(delete);

    popupMenu.addSeparator();

    JMenuItem config = new JMenuItem("config");

    config.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {
            SwingUtilities.invokeLater(
                () -> {
                  int x = (int) e.getPoint().getX();
                  int y = (int) e.getPoint().getY();
                  JPopupMenu configPopup = new JPopupMenu("config");
                  configPopup.add(new JMenuItem("config"));
                  configPopup.show(null,x,y);
                });
          }
        });

    popupMenu.add(config);

    popupMenu.show(e.getComponent(), e.getX(), e.getY());
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "PanelRightImpl{" + "id=" + id + '}';
  }
}
