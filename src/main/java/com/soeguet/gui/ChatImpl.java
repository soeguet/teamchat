package com.soeguet.gui;

import static com.soeguet.gui.util.EmojiConverter.emojiListInit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jetbrains.annotations.Nullable;

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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

/*
 * sets up the GUI
 * interacts with the websocket (client side) via WebSocketListener
 */
@Slf4j
public class ChatImpl extends ChatPanel implements WebSocketListener {

  public static final ObjectMapper MAPPER = new ObjectMapper();

  private final Settings SETTTINGS;
  private final WebsocketInteraction WEBSOCKET_INTERACTION;
  private final JProgressBar FORM_PROGRESSBAR = new JProgressBar(0, 100);
  private final List<JButton> EMOJI_BUTTON_LIST_FOR_FOCUS = new ArrayList<>();
  private final ClassLoader CLASS_LOADER = getClass().getClassLoader();

  // extracts preset local ips from ips.txt in resources/conf/
  public static HashMap<String, String> mapOfIps;

  // needed for converting text to emojis
  public static CustomWebsocketClient client;

  // TODO needs to be extracted -> used for connected Clients
  private String[] participantNameArray;
  private JTextPane participantTextArea;
  private JFrame participantsFrame;

  private EmojiImpl emojiWindow;

  // used to avoid overlapping of desktop notifications
  private int heightLastPopUp = 30;
  private int visibleDesktopNotificationCount = 0;

  private int currentEmojiFocus = 0;

  // first 100 messages from server should not be sending pop-ups while initial &&
  // loading screen
  private boolean startup = true;

  private PropertiesImpl settingsWindow;
  private JDialog initialLoadingStartUpDialog;

  // utility variables for the main chat panel (avoid duplicate printing of name
  // or timestamp)
  private String lastMessageFrom = "";
  private String lastPostTime = "";

  // TODO extraction -> progressbar
  private JLabel loadingMessageLabelOnStartUp;
  private int progressBarLiveValue;
  private int progressbarMaxValue = 1;

  private JDialog emojiSelectionPopUp;

  // for the time being we need this label to display the selected and ready to
  // send picture in picture panel
  private JLabel displayFakePictureLabel;

  public ChatImpl() {

    SETTTINGS = Settings.getInstance();
    WEBSOCKET_INTERACTION = new WebsocketInteraction(this);

    initIpAddressesAsLocalClients();
    emojiListInit();
    WEBSOCKET_INTERACTION.connectToWebSocket();
    manualInit();
  }

  // prevent client to be stuck on connection pop up
  private void connectToServerTimer() {
    Timer connectionTimer = new Timer(
        5_000,
        e -> {
          if (!client.isOpen()) {
            initialLoadingStartUpDialog.dispose();
            JOptionPane.showMessageDialog(this, "Connection failed! Server seems down!");
          }
        });
    connectionTimer.setRepeats(false);

    if (!client.isOpen())
      connectionTimer.start();
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
        s = new String(
            Files.readAllBytes(Paths.get("src/main/resources/conf/ips.txt")),
            StandardCharsets.UTF_8);
      }

      ClientsList clientsList = MAPPER.readValue(s, ClientsList.class);
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

    SETTTINGS.setMainJFrame(this);

    emojiWindow = new EmojiImpl(form_textEditorPane);

    // scroll speed for main text area
    form_mainTextBackgroundScrollPane.getVerticalScrollBar().setUnitIncrement(16);

    // JFrame options
    this.setSize(
        (SETTTINGS.getMainFrameWidth() < 500 ? 650 : SETTTINGS.getMainFrameWidth()),
        (SETTTINGS.getMainFrameHeight() < 500 ? 650 : SETTTINGS.getMainFrameHeight()));
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setTitle("Dev-Chat");
    this.addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            setExtendedState(JFrame.ICONIFIED);
          }
        });
  }

  private void loadLogoIconForTitleBarAndSystemTray() {
    if (Objects.requireNonNull(CLASS_LOADER.getResource("icon.png")).toString().contains("jar")) {

      setIconImage(Toolkit.getDefaultToolkit().getImage(CLASS_LOADER.getResource("icon.png")));
    }
    // if built from IDE
    else {
      setIconImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png"));
    }
  }

  public void setProgressbarMaxValueInitialMessageLoadUp(int progressbarMaxValue) {

    if (progressbarMaxValue == 0 || progressbarMaxValue > 100) {
      // max is 100, if anything goes wrong @backend. if 0, the loading screen will
      // disappear immediately anyway
      this.progressbarMaxValue = 100;
      return;
    }
    this.progressbarMaxValue = progressbarMaxValue;
  }

  @Override
  public void onMessageReceived(String message) {

    if (startup) {
      // initial row count info from server //need a better way to do this
      if (message.contains("ROWS:")) {
        message = message.replace("ROWS:", "");
        setProgressbarMaxValueInitialMessageLoadUp(Integer.parseInt(message));
        // start at 0, not 1
        progressBarLiveValue--;
        return;
      }

      progressBarLiveValue++;
      FORM_PROGRESSBAR.setValue((progressBarLiveValue) * 100 / progressbarMaxValue);

      // a little delay to make the loading a tad more dramatic
      try {
        Thread.sleep(25);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      if (loadingMessageLabelOnStartUp == null) {
        loadingMessageLabelOnStartUp = new JLabel();
      }

      loadingMessageLabelOnStartUp.setText(
          "loading - "
              + progressBarLiveValue
              + " of "
              + progressbarMaxValue
              + " - estm. "
              + (progressbarMaxValue * 25 / 600)
              + " secs");
    }

    WEBSOCKET_INTERACTION.onMessageReceived(message);
  }

  @Override
  public void onByteBufferMessageReceived(ByteBuffer bytes) {
    WEBSOCKET_INTERACTION.onByteBufferMessageReceived(bytes);
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

  protected void loadingInitialMessagesLoadUpDialog() {

    if (initialLoadingStartUpDialog != null && initialLoadingStartUpDialog.isVisible())
      return;

    SwingUtilities.invokeLater(
        () -> {
          initialLoadingStartUpDialog = new JDialog();
          initialLoadingStartUpDialog.setModal(true);
          initialLoadingStartUpDialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
          initialLoadingStartUpDialog.setLayout(
              new MigLayout("alignx center, wrap 1", "[center]", "[grow,fill, center]"));
          initialLoadingStartUpDialog.add(
              new JLabel(
                  "trying to connect to server -- ip: "
                      + SETTTINGS.getIp()
                      + " -- "
                      + "port: "
                      + SETTTINGS.getPort(),
                  SwingConstants.CENTER));
          initialLoadingStartUpDialog.add(
              new JLabel("¯\\_(ツ)_/¯   loading messages.. please wait..", SwingConstants.CENTER));
          loadingMessageLabelOnStartUp = new JLabel();
          loadingMessageLabelOnStartUp.setHorizontalAlignment(SwingConstants.CENTER);
          loadingMessageLabelOnStartUp.setText("loading..");

          initialLoadingStartUpDialog.add(loadingMessageLabelOnStartUp);

          JPanel progressBarPanel = new JPanel();
          progressBarPanel.add(FORM_PROGRESSBAR);

          initialLoadingStartUpDialog.add(progressBarPanel);

          initialLoadingStartUpDialog.pack();
          initialLoadingStartUpDialog.setSize(
              initialLoadingStartUpDialog.getWidth() + 150, initialLoadingStartUpDialog.getHeight() + 150);
          initialLoadingStartUpDialog.setLocationRelativeTo(this);
          initialLoadingStartUpDialog.setAlwaysOnTop(true);
          initialLoadingStartUpDialog.setVisible(true);
        });
  }

  private void addMouseClickAdapterToPanel(Component window) {

    window.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
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
  }

  protected void incomingMessagePreviewDesktopNotification(MessageModel messageModel) {

    if (!this.isFocused() && !startup) {
      SwingUtilities.invokeLater(
          () -> {
            // reusing the same dialog is not an option, it's up to the garbage collector I
            // guess
            JDialog popUpDialog = new JDialog();

            addMouseClickAdapterToPanel(popUpDialog);

            popUpDialog.setLayout(
                new MigLayout(
                    "fillx",
                    // columns
                    "[fill]",
                    // rows
                    "[fill]"));

            PanelRightImpl comment = new PanelRightImpl(messageModel);
            addMouseClickAdapterToPanel(comment);
            popUpDialog.add(comment);

            popUpDialog.pack();
            popUpDialog.setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width - popUpDialog.getWidth() - 20,
                heightLastPopUp);
            popUpDialog.setAutoRequestFocus(false);
            heightLastPopUp += popUpDialog.getHeight() + 5;
            visibleDesktopNotificationCount++;
            popUpDialog.getRootPane().setBorder(new LineBorder(Color.RED, 5));
            popUpDialog.setAlwaysOnTop(true);
            popUpDialog.pack();
            popUpDialog.setVisible(true);

            Timer timerBorder = new Timer(
                1000,
                e -> {
                  popUpDialog.getRootPane().setBorder(new LineBorder(Color.BLUE, 2));

                  popUpDialog.pack();
                });
            timerBorder.setRepeats(false);
            timerBorder.start();

            Timer timer = new Timer(
                SETTTINGS.getMessageDuration() * 1000,
                e -> {
                  popUpDialog.dispose();
                  visibleDesktopNotificationCount--;

                  if (visibleDesktopNotificationCount == 0) {
                    heightLastPopUp = 30;
                  }
                });
            timer.setRepeats(false);
            timer.start();

            if ((heightLastPopUp > 500) || (visibleDesktopNotificationCount == 0)) {
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
    // search for all resources in the "emoji" folder
    Enumeration<URL> resources;
    try {
      resources = CLASS_LOADER.getResources("emojis/");
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

                try (InputStream inputStream = CLASS_LOADER.getResourceAsStream(entryName)) {

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
          emojiSelectionPopUp.pack();
        }
      }
    }
  }

  private void populateDialogWithEmojiButton(File file, BufferedImage bufferedImage, String entryName) {

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
          public void mouseClicked(MouseEvent e) {
            e.consume();
            form_textEditorPane.insertIcon(icon);
            form_textEditorPane.insertComponent(new JLabel(""));
            form_textEditorPane.repaint();
          }
        });

    jButton.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            if (keyboardTraversFocusViaArrowKeys(e))
              return;

            emojiFrameKeyPressed(e, icon);
          }
        });

    emojiSelectionPopUp.add(jButton);
    EMOJI_BUTTON_LIST_FOR_FOCUS.add(jButton);
  }

  private boolean keyboardTraversFocusViaArrowKeys(KeyEvent e) {

    int nextButtonIndex;

    switch (e.getKeyCode()) {

      case KeyEvent.VK_DOWN:
        nextButtonIndex = Math.min(EMOJI_BUTTON_LIST_FOR_FOCUS.size() - 1, currentEmojiFocus + 10);
        EMOJI_BUTTON_LIST_FOR_FOCUS.get(nextButtonIndex).requestFocus();
        currentEmojiFocus = nextButtonIndex;
        e.consume();
        return true;

      case KeyEvent.VK_UP:
        nextButtonIndex = Math.max(0, currentEmojiFocus - 10);
        EMOJI_BUTTON_LIST_FOR_FOCUS.get(nextButtonIndex).requestFocus();
        currentEmojiFocus = nextButtonIndex;
        e.consume();
        return true;

      case KeyEvent.VK_RIGHT:
        nextButtonIndex = Math.min(EMOJI_BUTTON_LIST_FOR_FOCUS.size() - 1, currentEmojiFocus + 1);
        EMOJI_BUTTON_LIST_FOR_FOCUS.get(nextButtonIndex).requestFocus();
        currentEmojiFocus = nextButtonIndex;
        e.consume();
        return true;

      case KeyEvent.VK_LEFT:
        nextButtonIndex = Math.max(0, currentEmojiFocus - 1);
        EMOJI_BUTTON_LIST_FOR_FOCUS.get(nextButtonIndex).requestFocus();
        currentEmojiFocus = nextButtonIndex;
        e.consume();
        return true;

      default:
        return false;
    }
  }

  @Override
  protected void thisPropertyChange(PropertyChangeEvent e) {
    log.info(e.toString());
  }

  @Override
  protected void thisComponentResized(ComponentEvent e) {
    SETTTINGS.setMainFrameWidth(getWidth());
    SETTTINGS.setMainFrameHeight(getHeight());
    updateFrame();
  }

  @Override
  protected void textEditorPaneMouseClicked(MouseEvent e) {
    e.consume();
    if (emojiWindow != null && emojiWindow.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiWindow.dispose());
    }
  }

  @Override
  protected void mainTextPanelMouseClicked(MouseEvent e) {
    e.consume();
    if (emojiWindow != null && emojiWindow.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiWindow.dispose());
    }
  }

  @Override
  protected void textEditorPaneKeyReleased(KeyEvent e) {
  }

  @Override
  protected void propertiesMenuItemMousePressed(MouseEvent e) {

    e.consume();
    if (settingsWindow == null || !settingsWindow.isVisible()) {
      settingsWindow = new PropertiesImpl();
      settingsWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
      settingsWindow.pack();
      settingsWindow.setLocationRelativeTo(null);
      settingsWindow.setVisible(true);
    }
  }

  @Override
  protected void resetConnectionMenuItemMousePressed(MouseEvent e) {

    e.consume();
    form_mainTextPanel.removeAll();
    assert client != null;
    client.close();
    startup = true;
    lastMessageFrom = "";
    lastPostTime = "";
    WEBSOCKET_INTERACTION.onCloseReconnect();
  }

  @Override
  protected synchronized void sendButton(ActionEvent e) {

    // emergency command -> shuts down all clients
    if (form_textEditorPane.getText().trim().equals("/terminateAll")) {
      client.send("/terminateAll".getBytes());
      form_textEditorPane.setText("");
      return;
    }

    try {

      MessageModel messageModel = new MessageModel(
          mapOfIps.get(client.getLocalSocketAddress().getHostString()),
          EmojiConverter.checkTextForEmojis(form_textEditorPane));

      if (messageModel.getMessage().trim().isEmpty()) {
        form_textEditorPane.setText("");
        return;
      }

      try {
        client.send(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(messageModel));

        // close emoji pop up while sending
        if(emojiSelectionPopUp != null && emojiSelectionPopUp.isVisible()){
          emojiWindow.dispose();
          emojiSelectionPopUp.dispose();
        }

      } catch (WebsocketNotConnectedException ex) {
        WEBSOCKET_INTERACTION.createNewMessageOnPane("can't send message, no connection to server");
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
  protected void textEditorPaneKeyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {

      case KeyEvent.VK_BACK_SPACE:
        return;

      // refresh on F5
      case KeyEvent.VK_F5:
        e.consume();
        updateFrame();
        return;

      case KeyEvent.VK_ENTER:
        if (!e.isShiftDown()) {
          sendButton(null);
        }else{
          form_textEditorPane.setText(form_textEditorPane.getText() + "\n");
        }
        e.consume();
        return;

      //TODO test this: Hashtag on ISO DE Keyboard
      case 520:
        SwingUtilities.invokeLater(
          () -> form_textEditorPane.setText(StringUtils.chop(form_textEditorPane.getText())));
        e.consume();
        emojiButton(null);
        break;

      default:
        break;
    }

    // is typing.. indicator
    try {
      assert client != null;
      client.send("write".getBytes());
    } catch (WebsocketNotConnectedException ex) {
      WEBSOCKET_INTERACTION.createNewMessageOnPane(
          "no connection to server, try to reconnect -> file > reset reconnect");
    }
  }

  @Override
  protected void emojiButton(ActionEvent e) {

    // emergency exit -> shift and emoji button click
    if (e != null && (e.getModifiers() == 26 || e.getModifiers() == 17)) {
      System.exit(0);
    }

    if (emojiSelectionPopUp != null && emojiSelectionPopUp.isVisible()) {
      SwingUtilities.invokeLater(() -> emojiSelectionPopUp.dispose());
    } else {
      if (emojiSelectionPopUp == null) {
        emojiSelectionPopUp = new JDialog();
        emojiSelectionPopUp.setLayout(new GridLayout(0, 10));
        initEmojiFrame();
      }

      emojiSelectionPopUp.pack();
      emojiSelectionPopUp.setLocationRelativeTo(this);
      emojiSelectionPopUp.setVisible(true);
    }
  }

  protected void emojiFrameKeyPressed(KeyEvent e, ImageIcon icon) {
    e.consume();

    if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER
        || e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
      form_textEditorPane.insertIcon(icon);
      form_textEditorPane.insertComponent(new JLabel(""));
      form_textEditorPane.repaint();
      return;
    }

    if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {
      emojiSelectionPopUp.dispose();
      form_textEditorPane.requestFocusInWindow();
    }
  }

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
    } catch (UnsupportedFlavorException | NullPointerException | IOException ex) {
      textField.setText("");
    }

    displayFakePictureLabel = new JLabel();

    if (imageIcon != null) {

      if (imageIcon.getIconWidth() > 401) {

        int maxWidth = 400;
        double scaleFactor = (double) maxWidth / image.getWidth();
        int newWidth = (int) (image.getWidth() * scaleFactor);
        int newHeight = (int) (image.getHeight() * scaleFactor);
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon fakeIcon = new ImageIcon(scaledImage);
        displayFakePictureLabel = new JLabel(fakeIcon);
      } else {
        displayFakePictureLabel = new JLabel(imageIcon);
      }
    }

    textField.setEditable(false);
    JLabel label = new JLabel(imageIcon);
    label.setMaximumSize(new Dimension(500, 500));

    JDialog pictureDialog = new JDialog(this, "picture", true);
    pictureDialog.setSize(600, 600);
    pictureDialog.setLayout(new MigLayout("", "[grow,fill][fill]", "[grow,fill][fill]"));

    pictureDialog.add(displayFakePictureLabel, "span 2 0, wrap, center");

    pictureDialog.add(textField);
    JButton addPictureButton = new JButton("add picture");

    addPictureButton.addMouseListener(
        new MouseAdapter() {

          @Override
          public void mouseClicked(MouseEvent e) {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("pictures", "png", "jpg");
            fileChooser.setFileFilter(fileNameExtensionFilter);
            int returnVal = fileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

              textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
              displayFakePictureLabel.removeAll();

              BufferedImage bufferedImage;
              try {
                bufferedImage = ImageIO.read(new File(fileChooser.getSelectedFile().getAbsolutePath()));
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
                  Image scaledImage = bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                  imageIcon = new ImageIcon(scaledImage);
                } else {
                  imageIcon = new ImageIcon(bufferedImage);
                }
              }

              displayFakePictureLabel.setIcon(imageIcon);
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
            BufferedImage image = new BufferedImage(
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

  private void appendToPane(JTextPane tp, String msg, Color c) {
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
  protected void exitMenuItemMousePressed(MouseEvent e) {
    e.consume();
    System.exit(0);
  }

  @Override
  protected void thisMouseClicked(MouseEvent e) {
  }

  public JPanel getForm_mainTextPanel() {
    return form_mainTextPanel;
  }

  public boolean isStartup() {
    return !startup;
  }

  public void setStartup(boolean startup) {

    if (!startup) {
      progressBarLiveValue = 0;
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

  public JDialog getInitialLoadingStartUpDialog() {
    return initialLoadingStartUpDialog;
  }
}
