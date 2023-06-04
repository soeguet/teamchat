package com.soeguet.gui.newcomment.left;

import static com.soeguet.gui.ChatImpl.*;
import static com.soeguet.gui.util.EmojiConverter.replaceWithEmoji;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.config.Settings;
import com.soeguet.gui.emoji.EmojiImpl;
import com.soeguet.gui.newcomment.Colorpicker;
import com.soeguet.gui.newcomment.Comment;
import com.soeguet.gui.newcomment.pane.TextPaneImpl;
import com.soeguet.gui.reply.ReplyImpl;
import com.soeguet.gui.translate.TranslateDialogImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.gui.util.MessageTypes;
import com.soeguet.model.MessageModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PanelLeftImpl extends PanelLeft implements Comment {

    private final MessageModel messageModel;
    private final Long id;
    private JDialog jDialog;
    private boolean executedFromJar = false;
    private EmojiImpl emojiWindow;
    private TextPaneImpl textPaneComment;

    public PanelLeftImpl(@NotNull MessageModel messageModel) {
        this(messageModel, null, null);
        form_button1.setVisible(false);
    }

    public PanelLeftImpl(@NotNull MessageModel messageModel, String lastMessageFrom, @NotNull String lastPostTime) {

        this.messageModel = messageModel;
        this.id = messageModel.getId();

        customInitialMethods(messageModel, lastMessageFrom, lastPostTime);
    }

    private void customInitialMethods(@NotNull MessageModel messageModel, @Nullable String lastMessageFrom, @NotNull String lastPostTime) {

        if (messageIsDeleted(messageModel)) return;

        setBorderColor(Colorpicker.colorPicker(messageModel.getSender()).getBorderColor());
        Settings settings = Settings.getInstance();
        textPaneComment = new TextPaneImpl();


        addRightClickOptionToPanel();

        this.form_panel1.add(textPaneComment, "cell 1 1,grow");

        addQuoteLayerToCommentIfPresent(messageModel, settings);

        replaceWithEmoji(textPaneComment, messageModel.getMessage());

        form_nameLabel.setText(messageModel.getSender());
        form_timeLabel.setText(messageModel.getTime());

        addingImportantFlagToComment(messageModel);

        //add user interaction emojis to comment
        if (messageModel.getUserInteractions() != null && messageModel.getUserInteractions().size() > 0) {

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
        translateItem.addActionListener(e -> {


            JDialog translateDialog = new TranslateDialogImpl(null, textPaneComment.getSelectedText());
            translateDialog.setVisible(true);
        });

        popupMenu.add(copyItem);
        popupMenu.add(translateItem);

        textPaneComment.setComponentPopupMenu(popupMenu);

        textPaneComment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(textPaneComment, e.getX(), e.getY());
                }
            }
        });
    }

    private void addingImportantFlagToComment(@NotNull MessageModel messageModel) {
        if (messageModel.getMessageType() == 5) {
            add(new JLabel("!!!"), "cell 0 1");
        }
    }

    private void addQuoteLayerToCommentIfPresent(@NotNull MessageModel messageModel, @NotNull Settings settings) {
        if (messageModel.getQuotedMessageText() != null) {

            TextPaneImpl replyTextPane = new TextPaneImpl() {

                @Override
                protected void paintComponent(Graphics grphcs) {

                    Graphics2D g2d = (Graphics2D) grphcs;
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
            this.form_panel1.add(replyTextPane, "cell 1 0");
            replyTextPane.setEnabled(false);
            replyTextPane.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(),
                    messageModel.getQuotedMessageSender() + " - " + messageModel.getQuotedMessageTime(),
                    TitledBorder.LEADING,
                    TitledBorder.ABOVE_TOP, null,
                    new Color(117, 133, 175)));
            replyTextPane.setMinimumSize(new Dimension(150, 50));
        }
    }

    public TextPaneImpl getTextPaneComment() {
        return textPaneComment;
    }

    @Override
    protected void actionLabelMouseEntered(@NotNull MouseEvent e) {
        e.consume();
        form_actionLabel.setForeground(new Color(92, 92, 92, 255));
    }

    @Override
    protected void actionLabelMouseClicked(@NotNull MouseEvent e) {

        e.consume();
        if (emojiWindow != null && emojiWindow.isVisible()) {
            SwingUtilities.invokeLater(() -> emojiWindow.dispose());
        } else {
            SwingUtilities.invokeLater(() -> {
                if (emojiWindow == null) {
                    emojiWindow = new EmojiImpl();
                    initEmojiFrame();
                }
                emojiWindow.pack();
                emojiWindow.repaint();
                emojiWindow.revalidate();
                emojiWindow.setLocation(e.getLocationOnScreen().x,
                        e.getLocationOnScreen().y);
                emojiWindow.setVisible(true);
            });
        }
    }

    @Override
    protected void actionLabelMouseExited(@NotNull MouseEvent e) {

        e.consume();
        form_actionLabel.setForeground(new Color(92, 92, 92, 0));
    }

    private boolean messageIsDeleted(@NotNull MessageModel messageModel) {
        //deleted message
        if (messageModel.getMessageType() == 127) {

            removeAll();
            add(new JLabel(messageModel.getTime() + " - " + messageModel.getMessage()), "cell 0 1");
            return true;
        }
        return false;
    }


    @Override
    protected void replyButtonClicked(@NotNull MouseEvent e) {

        e.consume();
        jDialog = new JDialog();
        jDialog.setModal(true);
        jDialog.setTitle("options");
        jDialog.setLayout(new MigLayout(
                "fillx",
                // columns
                "[grow,fill]",
                // rows
                "[grow,fill]"));


        jDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JButton replyButton = new JButton("reply");
        replyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jDialog.dispose();
                ReplyImpl replyDialog = new ReplyImpl(messageModel);
                replyDialog.setVisible(true);
            }
        });

        jDialog.add(replyButton, "wrap");

        jDialog.pack();
        jDialog.setLocation(e.getLocationOnScreen().x + 20, e.getLocationOnScreen().y);
        jDialog.setVisible(true);
    }

    @Override
    public Long getId() {
        return id;
    }

    @NotNull
    @Override
    public String toString() {
        return "PanelLeftImpl{" +
                "id=" + id +
                '}';
    }

    public void addEmojiInteraction(@NotNull MessageModel messageModel) {

        addEmojiToInteractionPane(messageModel);
    }

    private void addEmojiToInteractionPane(@NotNull MessageModel messageModel) {

        executedFromJar = String.valueOf(PanelLeftImpl.class.getResource("PanelLeftImpl.class")).startsWith("jar:");

        JPanel userInteractionPanel = new JPanel();
        add(userInteractionPanel, "cell 3 2");

        AtomicInteger interactionCount = new AtomicInteger(1);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                messageModel.getUserInteractions().stream().limit(form_layeredPane1.getWidth() / 25).forEach((interaction) -> {

                    ImageIcon icon;

                    if (executedFromJar) {

                        icon = new ImageIcon(Objects.requireNonNull(EmojiConverter.class.getResource("/emojis/" + interaction.getEmoji() + ".png")));

                    } else {

                        icon = new ImageIcon("src/main/resources/emojis/" + interaction.getEmoji() + ".png");
                    }

                    icon.setDescription(interaction.getUsername());

                    int diameter = 25;
                    JLabel jLabel = new JLabel(icon) {
                        @Override
                        protected void paintComponent(Graphics g) {

                            Graphics2D g2d = (Graphics2D) g;
                            g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
                            g2d.setColor(Color.WHITE);
                            g2d.fillRoundRect(1, 1, diameter - 2, diameter - 2, 50, 50);

                            g2d.setColor(Color.DARK_GRAY);
                            g2d.drawRoundRect(0, 0, diameter - 1, diameter - 1, 50, 50);
                            super.paintComponent(g);
                        }
                    };

                    jLabel.setToolTipText(interaction.getUsername());
                    jLabel.setBounds(form_layeredPane1.getWidth() - (interactionCount.get() * 25), form_layeredPane1.getHeight() - 25, 25, 25);

                    jLabel.setVisible(true);
                    form_layeredPane1.add(jLabel);
                    form_layeredPane1.moveToFront(jLabel);

                    interactionCount.getAndIncrement();

                    PanelLeftImpl.this.repaint();
                });
            }
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

                                    String finalFileName = fileName;

                                    jButton.addMouseListener(new MouseAdapter() {
                                        @Override
                                        public void mouseClicked(@NotNull MouseEvent e) {

                                            e.consume();
                                            MessageModel likeModel = new MessageModel(messageModel.getId(),
                                                    MessageTypes.INTERACTED,
                                                    messageModel.addUserInteractions(mapOfIps.get(client.getLocalSocketAddress().getHostString()), finalFileName),
                                                    messageModel.getSender(),
                                                    messageModel.getTime(),
                                                    messageModel.getMessage(),
                                                    messageModel.getQuotedMessageSender(),
                                                    messageModel.getQuotedMessageTime(),
                                                    messageModel.getQuotedMessageText());
                                            likeModel.setSender(messageModel.getSender());
                                            likeModel.setId(messageModel.getId());
                                            likeModel.setMessageType(MessageTypes.INTERACTED);
                                            likeModel.setMessage(messageModel.getMessage());
                                            likeModel.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

                                            try {
                                                client.send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(likeModel));
                                            } catch (JsonProcessingException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            emojiWindow.dispose();
                                        }
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

                            String finalFileName = fileName;
                            jButton.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(@NotNull MouseEvent e) {

                                    e.consume();
                                    MessageModel likeModel = new MessageModel(messageModel.getId(),
                                            MessageTypes.INTERACTED,
                                            messageModel.addUserInteractions(mapOfIps.get(client.getLocalSocketAddress().getHostString()), finalFileName),
                                            messageModel.getSender(),
                                            messageModel.getTime(),
                                            messageModel.getMessage(),
                                            messageModel.getQuotedMessageSender(),
                                            messageModel.getQuotedMessageTime(),
                                            messageModel.getQuotedMessageText());

                                    try {
                                        client.send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(likeModel));
                                    } catch (JsonProcessingException ex) {
                                        throw new RuntimeException(ex);
                                    }

                                    emojiWindow.dispose();
                                }
                            });

                            emojiWindow.add(jButton);
                        }
                    }
                }
            }
        }
    }
}
