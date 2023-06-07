package com.soeguet.gui;

import static com.soeguet.gui.util.EmojiConverter.emojiListInit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.ui.FlatListCellBorder;
import com.formdev.flatlaf.ui.FlatTableCellBorder;
import com.soeguet.Main;
import com.soeguet.client.CustomWebsocketClient;
import com.soeguet.client.WebSocketListener;
import com.soeguet.config.Settings;
import com.soeguet.gui.emoji.EmojiImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.gui.properties.PropertiesImpl;
import com.soeguet.gui.util.EmojiConverter;
import com.soeguet.model.ClientsList;
import com.soeguet.model.MessageModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class ChatImpl extends ChatPanel implements WebSocketListener {

  public static HashMap<String, String> mapOfIps;

  // needed for converting text to emojis
  public static List<String> emojiListFull = new ArrayList<>();
  public static CustomWebsocketClient client = null;
  public static ObjectMapper mapper;
  private final Settings settings;
  private final WebsocketInteraction websocketInteraction = new WebsocketInteraction(this);
  private final JProgressBar progressBar = new JProgressBar(0, 100);
  private final List<JButton> emojiButtonListForFocus = new ArrayList<>();
  private Timer connectionTimer;
  private String[] participantNameArray;
  private JTextPane participantTextArea;
  private EmojiImpl emojiWindow;
  private JFrame participantsFrame;
  private int heightLastPopUp = 30;
  private int openPopUps = 0;
  private int currentEmojiFocus = 0;
  // first 100 messages from server should not be sending pop-ups while initial && loading screen
  private boolean startup = true;

  private PropertiesImpl propertiesFrame;
  private JDialog initialLoadingPanel;

  private String lastMessageFrom = "";
  private String lastPostTime = "";
  private int progressBarValue;
  private int progressbarMaxValue = 1;
  private JLabel loadingMessageLabel;

  private JDialog emojiDialog;
  private JLabel displayFakeLabel;

  public ChatImpl() {
    mapper = new ObjectMapper();
    settings = Settings.getInstance();

    initIpAddressesAsLocalClients();
    emojiListInit();
    websocketInteraction.connectToWebSocket();
    manualInit();

    connectToServerTimer();
  }

  private void connectToServerTimer() {

    connectionTimer =
        new Timer(
            5_000,
            (event) -> {
              log.info("attempting to connect");

              if (!client.isOpen()) {

                websocketInteraction.connectToWebSocket();
              } else {
                log.info("connection already established!");
                connectionTimer.stop();
              }
            });
    connectionTimer.setRepeats(true);
    connectionTimer.setDelay(30_000);

    if (!client.isOpen()) connectionTimer.start();
  }

  private void initIpAddressesAsLocalClients() {

    try {

      String s;
      if (String.valueOf(ChatImpl.class.getResource("ChatImpl.class")).startsWith("jar:")) {

        URI location = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();

        Path path = Paths.get(Paths.get(location).getParent().getParent() + "/bin/conf/ips.txt");

        if (new File(path.toUri()).isFile()) {
          s = new String(Files.readAllBytes(Paths.get(path.toUri())), StandardCharsets.UTF_8);

        } else {

          ClassLoader.getSystemClassLoader();
          InputStream inputStream = ClassLoader.getSystemResourceAsStream("conf/ips.txt");
          InputStreamReader streamReader;
          assert inputStream != null;
          streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
          BufferedReader in = new BufferedReader(streamReader);
          s = in.lines().collect(Collectors.joining());
        }

      } else {
        s =
            new String(
                Files.readAllBytes(Paths.get("src/main/resources/conf/ips.txt")),
                StandardCharsets.UTF_8);
      }

      ClientsList clientsList = mapper.readValue(s, ClientsList.class);
      mapOfIps = new HashMap<>();
      clientsList
          .getClientsList()
          .forEach(a -> mapOfIps.put(a.getLocalIpAddress(), a.getClientName()));
    } catch (IOException | URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void manualInit() {

    loadLogoIconForTitleBarAndSystemTray();

    settings.setMainJFrame(this);

    // JFrame options
    setSize(
        (settings.getMainFrameWidth() < 500 ? 650 : settings.getMainFrameWidth()),
        (settings.getMainFrameHeight() < 500 ? 650 : settings.getMainFrameHeight()));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setTitle("Team-Chat");
    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            setExtendedState(JFrame.ICONIFIED);
          }
        });

    emojiWindow = new EmojiImpl(form_textEditorPane);

    // scroll speed for main text area
    form_mainTextBackgroundScrollPane.getVerticalScrollBar().setUnitIncrement(16);
  }

  private void loadLogoIconForTitleBarAndSystemTray() {
    ClassLoader classLoader = getClass().getClassLoader();
    if (Objects.requireNonNull(classLoader.getResource("icon.png")).toString().contains("jar")) {

      setIconImage(Toolkit.getDefaultToolkit().getImage(classLoader.getResource("icon.png")));
    } else {
      setIconImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png"));
    }
  }

  public void setProgressbarMaxValueInitialMessageLoadUp(int progressbarMaxValue) {

    if (progressbarMaxValue == 0) {
      // max is 100, if anything goes wrong @backend. if 0, the loading screen will disappear
      // immediately anyway
      this.progressbarMaxValue = 100;
      return;
    }
    this.progressbarMaxValue = progressbarMaxValue;
  }

  @Override
  public void onMessageReceived(@NotNull String message) {

    if (startup) {
      // initial row count info from server //need a better way to do this
      if (message.contains("ROWS:")) {
        message = message.replace("ROWS:", "");
        setProgressbarMaxValueInitialMessageLoadUp(Integer.parseInt(message));
        //start at 0, not 1
        progressBarValue--;
        return;
      }

      progressBarValue++;
      progressBar.setValue((progressBarValue) * 100 / progressbarMaxValue);

      try {
        Thread.sleep(25);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (loadingMessageLabel == null) {
        loadingMessageLabel = new JLabel();
      }
      loadingMessageLabel.setText(
          "loading - " + progressBarValue + " of " + progressbarMaxValue + " - estm. "+ (progressbarMaxValue * 25 / 600) +" secs");
    }

    websocketInteraction.onMessageReceived(message);
  }

  @Override
  public void onByteBufferMessageReceived(@NotNull ByteBuffer bytes) {

    websocketInteraction.onByteBufferMessageReceived(bytes);
  }

  @Override
  public void onCloseReconnect() {
    connectToServerTimer();
  }

  protected void updateFrame() {

    revalidate();
    form_mainTextPanel.revalidate();

    SwingUtilities.invokeLater(
        () -> {
          JScrollBar verticalScrollBar = form_mainTextBackgroundScrollPane.getVerticalScrollBar();
          verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
  }

  protected void loadingInitialMessagesPanel() {

    if (initialLoadingPanel != null && initialLoadingPanel.isVisible()) return;

    SwingUtilities.invokeLater(
        () -> {
          initialLoadingPanel = new JDialog();
          initialLoadingPanel.setModal(true);
          initialLoadingPanel.setDefaultCloseOperation(HIDE_ON_CLOSE);
          initialLoadingPanel.setLayout(
              new MigLayout("alignx center, wrap 1", "[center]", "[grow,fill, center]"));
          initialLoadingPanel.add(
              new JLabel(
                  "trying to connect to server -- ip: "
                      + settings.getIp()
                      + " -- "
                      + "port: "
                      + settings.getPort(),
                  SwingConstants.CENTER));
          initialLoadingPanel.add(
              new JLabel("¯\\_(ツ)_/¯   loading messages.. please wait..", SwingConstants.CENTER));
          loadingMessageLabel = new JLabel();
          loadingMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
          loadingMessageLabel.setText("loading..");

          initialLoadingPanel.add(loadingMessageLabel);

          JPanel progressBarPanel = new JPanel();
          progressBarPanel.add(progressBar);

          initialLoadingPanel.add(progressBarPanel);

          initialLoadingPanel.pack();
          initialLoadingPanel.setSize(
              initialLoadingPanel.getWidth() + 150, initialLoadingPanel.getHeight() + 150);
          initialLoadingPanel.setLocationRelativeTo(null);
          initialLoadingPanel.setAlwaysOnTop(true);
          initialLoadingPanel.setVisible(true);
        });
  }

  protected void popUps(@NotNull MessageModel messageModel) {

    if (!this.isFocused() && !startup) {
      SwingUtilities.invokeLater(
          () -> {
            JDialog popUpDialog = new JDialog();
            popUpDialog.addMouseListener(
                new MouseAdapter() {
                  @Override
                  public void mouseClicked(@NotNull MouseEvent e) {
                    e.consume();
                    // bring main frame to front by clicking pop up
                    SwingUtilities.invokeLater(
                        () -> {
                          ChatImpl.super.setExtendedState(JFrame.NORMAL);
                          ChatImpl.super.setAlwaysOnTop(true);
                          ChatImpl.super.requestFocus();
                          ChatImpl.super.setLocationRelativeTo(null);
                          ChatImpl.super.setAlwaysOnTop(false);
                        });
                  }
                });
            popUpDialog.setLayout(
                new MigLayout(
                    "fillx",
                    // columns
                    "[fill]",
                    // rows
                    "[fill]"));

            popUpDialog.add(new PanelRightImpl(messageModel));

            popUpDialog.pack();
            popUpDialog.setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width - popUpDialog.getWidth() - 20,
                heightLastPopUp);
            popUpDialog.setAutoRequestFocus(false);
            heightLastPopUp += popUpDialog.getHeight() + 5;
            openPopUps++;
            popUpDialog.getRootPane().setBorder(new LineBorder(Color.RED, 5));
            popUpDialog.setAlwaysOnTop(true);
            popUpDialog.pack();
            popUpDialog.setVisible(true);

            Timer timerBorder =
                new Timer(
                    1000,
                    e -> {
                      popUpDialog.getRootPane().setBorder(new LineBorder(Color.BLUE, 2));

                      popUpDialog.pack();
                    });
            timerBorder.setRepeats(false);
            timerBorder.start();

            Timer timer =
                new Timer(
                    settings.getMessageDuration() * 1000,
                    e -> {
                      popUpDialog.dispose();
                      openPopUps--;

                      if (openPopUps == 0) {
                        heightLastPopUp = 30;
                      }
                    });
            timer.setRepeats(false);
            timer.start();

            if ((heightLastPopUp > 500) || (openPopUps == 0)) {
              heightLastPopUp = 30;
            }

            super.toFront();
            setFocusableWindowState(false);
            setFocusableWindowState(true);
            Toolkit.getDefaultToolkit().beep();
          });
    }
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
                  populateDialogWithEmojiButton(null, bufferedImage, entryName);
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
              populateDialogWithEmojiButton(file, null, null);
            }
          }
          emojiDialog.pack();
        }
      }
    }
  }

  private void populateDialogWithEmojiButton(
      @Nullable File file, @NotNull BufferedImage bufferedImage, @NotNull String entryName) {

    ImageIcon icon;
    String fileName;

    if (file != null) {

      icon = new ImageIcon(file.getPath());
      fileName = file.getName().replace(".png", "");
    } else {

      icon = new ImageIcon(bufferedImage);
      fileName = entryName.replace(".png", "");
    }

    fileName = fileName.replace("emojis/", "");

    JButton jButton = new JButton();
    jButton.setBorder(new FlatListCellBorder.Default());

    jButton.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            jButton.setBorder(new FlatTableCellBorder.Selected());
          }

          @Override
          public void focusLost(FocusEvent e) {
            jButton.setBorder(new FlatTableCellBorder.Default());
          }
        });

    jButton.setName(fileName);
    icon.setDescription(fileName);
    jButton.setIcon(icon);

    jButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(@NotNull MouseEvent e) {
            e.consume();
            form_textEditorPane.insertIcon(icon);
            form_textEditorPane.insertComponent(new JLabel(""));
            form_textEditorPane.repaint();
          }
        });

    jButton.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyPressed(@NotNull KeyEvent e) {
            if (keyboardTraversFocusViaArrowKeys(e)) return;

            emojiFrameKeyPressed(e, icon);
          }
        });

    emojiDialog.add(jButton);
    emojiButtonListForFocus.add(jButton);
  }

  private boolean keyboardTraversFocusViaArrowKeys(@NotNull KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      int nextButtonIndex = Math.min(emojiButtonListForFocus.size() - 1, currentEmojiFocus + 10);
      emojiButtonListForFocus.get(nextButtonIndex).requestFocus();
      currentEmojiFocus = nextButtonIndex;
      e.consume();
      return true;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      int nextButtonIndex = Math.max(0, currentEmojiFocus - 10);
      emojiButtonListForFocus.get(nextButtonIndex).requestFocus();
      currentEmojiFocus = nextButtonIndex;
      e.consume();
      return true;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      int nextButtonIndex = Math.min(emojiButtonListForFocus.size() - 1, currentEmojiFocus + 1);
      emojiButtonListForFocus.get(nextButtonIndex).requestFocus();
      currentEmojiFocus = nextButtonIndex;
      e.consume();
      return true;
    }
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      int nextButtonIndex = Math.max(0, currentEmojiFocus - 1);
      emojiButtonListForFocus.get(nextButtonIndex).requestFocus();
      currentEmojiFocus = nextButtonIndex;
      e.consume();
      return true;
    }
    return false;
  }

  @Override
  protected void thisPropertyChange(@NotNull PropertyChangeEvent e) {
    log.info(e.toString());
  }

  @Override
  protected void thisComponentResized(ComponentEvent e) {
    settings.setMainFrameWidth(getWidth());
    settings.setMainFrameHeight(getHeight());
    updateFrame();
  }

  @Override
  protected void textEditorPaneMouseClicked(@NotNull MouseEvent e) {
    e.consume();
    if (emojiWindow != null && emojiWindow.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiWindow.dispose());
    }
  }

  @Override
  protected void mainTextPanelMouseClicked(@NotNull MouseEvent e) {
    e.consume();
    if (emojiWindow != null && emojiWindow.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiWindow.dispose());
    }
  }

  @Override
  protected void textEditorPaneKeyReleased(KeyEvent e) {}

  @Override
  protected void propertiesMenuItemMousePressed(@NotNull MouseEvent e) {

    e.consume();
    if (propertiesFrame == null || !propertiesFrame.isVisible()) {
      propertiesFrame = new PropertiesImpl();
      propertiesFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
      propertiesFrame.pack();
      propertiesFrame.setLocationRelativeTo(null);
      propertiesFrame.setVisible(true);
    }
  }

  @Override
  protected void resetConnectionMenuItemMousePressed(@NotNull MouseEvent e) {

    e.consume();
    form_mainTextPanel.removeAll();
    assert client != null;
    client.close();
    startup = true;
    lastMessageFrom = "";
    lastPostTime = "";
    websocketInteraction.onCloseReconnect();
  }

  @Override
  protected synchronized void sendButton(ActionEvent e) {

    if (form_textEditorPane.getText().trim().equals("/terminateAll")) {
      client.send("/terminateAll".getBytes());
      form_textEditorPane.setText("");
      return;
    }

    try {

      MessageModel messageModel =
          new MessageModel(
              mapOfIps.get(client.getLocalSocketAddress().getHostString()),
              EmojiConverter.checkTextForEmojis(form_textEditorPane));

      if (messageModel.getMessage().trim().isEmpty()) {
        form_textEditorPane.setText("");
        return;
      }

      try {
        client.send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageModel));
      } catch (WebsocketNotConnectedException ex) {
        websocketInteraction.createNewMessageOnPane("can't send message, no connection to server");
      }

      form_textEditorPane.setText("");

    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    } catch (NullPointerException ex) {

      // todo error handling not working
      SwingUtilities.invokeLater(
          () -> {
            JLabel label = new JLabel(ex.getMessage());
            label.setForeground(Color.RED);
            label.setFont(new Font(null, Font.BOLD, 20));
            form_mainTextPanel.add(new JLabel().add(label), "wrap, align center");
          });
    }
  }

  @Override
  protected void textEditorPaneKeyPressed(@NotNull KeyEvent e) {

    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
      return;
    }

    // refresh panel on f5
    if (e.getKeyCode() == KeyEvent.VK_F5) {
      e.consume();
      updateFrame();
      return;
    }

    if (!e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
      sendButton(null);
      e.consume();
      return;
    } else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
      form_textEditorPane.setText(form_textEditorPane.getText() + "\n");
      e.consume();
      return;
    }

    // hashtag button
    if (e.getKeyCode() == 520) {
      SwingUtilities.invokeLater(
          () -> form_textEditorPane.setText(StringUtils.chop(form_textEditorPane.getText())));
      e.consume();
      emojiButton(null);
    }

    try {
      assert client != null;
      client.send("write".getBytes());
    } catch (WebsocketNotConnectedException ex) {
      websocketInteraction.createNewMessageOnPane(
          "no connection to server, try to reconnect -> file > reset reconnect");
    }
  }

  @Override
  protected void emojiButton(ActionEvent e) {

    if (e != null && e.getModifiers() == 26) {
      System.exit(0);
    }

    if (emojiDialog != null && emojiDialog.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiDialog.dispose());
    } else {
      if (emojiDialog == null) {
        emojiDialog = new JDialog();
        emojiDialog.setLayout(new GridLayout(0, 10));
        initEmojiFrame();
      }

      emojiDialog.pack();
      emojiDialog.setLocationRelativeTo(this);
      emojiDialog.setVisible(true);
    }
  }

  protected void emojiFrameKeyPressed(@NotNull KeyEvent e, ImageIcon icon) {
    e.consume();

    if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER
        || e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
      form_textEditorPane.insertIcon(icon);
      form_textEditorPane.insertComponent(new JLabel(""));
      form_textEditorPane.repaint();
      return;
    }

    if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {
      emojiDialog.dispose();
      form_textEditorPane.requestFocusInWindow();
    }
  }

  @SneakyThrows
  @Override
  protected void pictureButtonMouseClicked(MouseEvent e) {
    System.setProperty("sun.awt.datatransfer.Logging", "false");

    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    BufferedImage image = null;
    ImageIcon imageIcon = null;
    JTextField textField = new JTextField();

    try {
      image = (BufferedImage) clipboard.getData(DataFlavor.imageFlavor);
      imageIcon = new ImageIcon(image);
      textField.setText("clipboard");
    } catch (UnsupportedFlavorException | NullPointerException ex) {
      textField.setText("");
    }
    displayFakeLabel = new JLabel();
    if (imageIcon != null) {

      if (imageIcon.getIconWidth() > 401) {

        int maxWidth = 400;
        double scaleFactor = (double) maxWidth / image.getWidth();
        int newWidth = (int) (image.getWidth() * scaleFactor);
        int newHeight = (int) (image.getHeight() * scaleFactor);
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon fakeIcon = new ImageIcon(scaledImage);
        displayFakeLabel = new JLabel(fakeIcon);
      } else {
        displayFakeLabel = new JLabel(imageIcon);
      }
    }

    textField.setEditable(false);
    JLabel label = new JLabel(imageIcon);
    label.setMaximumSize(new Dimension(500, 500));

    JDialog pictureDialog = new JDialog(this, "picture", true);
    pictureDialog.setSize(600, 600);
    pictureDialog.setLayout(new MigLayout("", "[grow,fill][fill]", "[grow,fill][fill]"));

    pictureDialog.add(displayFakeLabel, "span 2 0, wrap, center");

    pictureDialog.add(textField);
    JButton addPictureButton = new JButton("add picture");

    addPictureButton.addMouseListener(
        new MouseAdapter() {

          @Override
          public void mouseClicked(MouseEvent e) {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter =
                new FileNameExtensionFilter("pictures", "png", "jpg");
            fileChooser.setFileFilter(fileNameExtensionFilter);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

              textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
              displayFakeLabel.removeAll();

              BufferedImage bufferedImage;
              try {
                bufferedImage =
                    ImageIO.read(new File(fileChooser.getSelectedFile().getAbsolutePath()));
              } catch (IOException ex) {
                throw new RuntimeException(ex);
              }

              ImageIcon imageIcon = null;
              if (bufferedImage != null) {

                if (bufferedImage.getWidth() > 401) {

                  int maxWidth = 400;
                  double scaleFactor = (double) maxWidth / bufferedImage.getWidth();
                  int newWidth = (int) (bufferedImage.getWidth() * scaleFactor);
                  int newHeight = (int) (bufferedImage.getHeight() * scaleFactor);
                  Image scaledImage =
                      bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                  imageIcon = new ImageIcon(scaledImage);
                } else {
                  imageIcon = new ImageIcon(bufferedImage);
                }
              }

              displayFakeLabel.setIcon(imageIcon);
              label.setIcon(new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()));

              pictureDialog.pack();
              pictureDialog.setLocationRelativeTo(null);
            }
          }
        });
    pictureDialog.add(addPictureButton, "wrap");

    JButton sendPictureButton = new JButton("send");
    sendPictureButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {

            Icon labelIcon = label.getIcon();
            BufferedImage image =
                new BufferedImage(
                    labelIcon.getIconWidth(),
                    labelIcon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            labelIcon.paintIcon(null, g, 0, 0);
            g.dispose();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
              ImageIO.write(image, "gif", byteArrayOutputStream);
            } catch (IOException ex) {
              ex.printStackTrace();
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            ByteBuffer buffer = ByteBuffer.wrap(byteArray);

            client.send(buffer);

            pictureDialog.dispose();

            updateFrame();
          }
        });
    pictureDialog.add(sendPictureButton, "span 2 0");

    pictureDialog.pack();
    pictureDialog.setSize(600, pictureDialog.getHeight());
    pictureDialog.setLocationRelativeTo(null);
    pictureDialog.setVisible(true);
  }

  @Override
  protected void participantsMenuItemMousePressed(@Nullable MouseEvent e) {

    // if clicked on update -> new updated frame
    if (e != null) {
      e.consume();
    } else {
      participantsFrame.dispose();
      participantsFrame = null;
    }

    SwingUtilities.invokeLater(
        () -> {
          if (participantTextArea != null) {
            participantTextArea.selectAll();
            participantTextArea.replaceSelection("");
          }

          if (participantsFrame == null) {

            assert client != null;
            client.send("PARTICIPANTLIST".getBytes());
            participantsFrame = new JFrame("participants");
            participantsFrame.setAlwaysOnTop(true);
            participantsFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
            participantsFrame.setLayout(
                new MigLayout("fill,wrap", "[grow, align center]", "[grow, align center]"));

            participantTextArea = new JTextPane();
            participantsFrame.add(participantTextArea, "growx");
            JButton updateButton = new JButton("update");

            updateButton.addMouseListener(
                new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                    client.send("PARTICIPANTLIST".getBytes());
                    participantsMenuItemMousePressed(null);
                  }
                });
            participantsFrame.add(updateButton, "growx");
          }

          if (participantNameArray != null) {
            mapOfIps.forEach(
                (a, b) -> {
                  if (Arrays.stream(participantNameArray).anyMatch(z -> z.contains(a))) {
                    appendToPane(participantTextArea, "•", Color.GREEN);
                  } else {
                    appendToPane(participantTextArea, "•", Color.RED);
                  }

                  appendToPane(participantTextArea, " " + b + " (" + a + ")", Color.BLACK);
                  appendToPane(participantTextArea, "\n", Color.BLACK);
                  participantsFrame.pack();
                });
          }

          assert participantTextArea != null;
          participantsFrame.revalidate();
          participantsFrame.pack();
          participantsFrame.setResizable(false);
          participantsFrame.setLocation(
              this.getBounds().x + this.getBounds().width, this.getBounds().y);
          participantTextArea.setEditable(false);
          participantsFrame.setVisible(true);
        });
  }

  private void appendToPane(@NotNull JTextPane tp, @NotNull String msg, Color c) {
    StyleContext sc = StyleContext.getDefaultStyleContext();
    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

    int len = tp.getDocument().getLength();

    if (msg.equals("•")) {
      aset = sc.addAttribute(aset, StyleConstants.FontSize, 20);
    } else {
      aset = sc.addAttribute(aset, StyleConstants.FontSize, 12);
    }
    tp.setCaretPosition(len);
    tp.setCharacterAttributes(aset, false);
    tp.replaceSelection(msg);
  }

  @Override
  protected void exitMenuItemMousePressed(@NotNull MouseEvent e) {
    e.consume();
    System.exit(0);
  }

  @Override
  protected void thisMouseClicked(MouseEvent e) {}

  public JPanel getForm_mainTextPanel() {
    return form_mainTextPanel;
  }

  public boolean isStartup() {
    return !startup;
  }

  public void setStartup(boolean startup) {

    if (!startup) {
      progressBarValue = 0;
    }
    this.startup = startup;
  }

  public JLabel getForm_typingLabel() {
    return form_typingLabel;
  }

  public String getLastPostTime() {
    return lastPostTime;
  }

  public void setLastPostTime(String lastPostTime) {
    this.lastPostTime = lastPostTime;
  }

  public String getLastMessageFrom() {
    return lastMessageFrom;
  }

  public void setLastMessageFrom(String lastMessageFrom) {
    this.lastMessageFrom = lastMessageFrom;
  }

  public void setParticipantNameArray(String[] participantNameArray) {
    this.participantNameArray = participantNameArray;
  }

  public JDialog getInitialLoadingPanel() {
    return initialLoadingPanel;
  }
}
