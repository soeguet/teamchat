package com.soeguet.gui;

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
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
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
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.soeguet.gui.util.EmojiConverter.emojiListInit;

/**
 Sets up the GUI. Interacts with the websocket (client side) via WebSocketListener.
 */
public class ChatImpl extends ChatPanel implements WebSocketListener {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    // Time limit in milliseconds to establish server connection
    private static final int SERVER_CONNECTION_TIME_LIMIT = 5000;
    private static final String EMOJI_DIRECTORY_PATH = "emojis/";
    private static final String PNG_FILE_EXTENSION = ".png";
    private static final String JAR_PROTOCOL = "jar";
    // extracts preset local ips from ips.txt in resources/conf/
    public static HashMap<String, String> mapOfIps;
    // needed for converting text to emojis
    public static CustomWebsocketClient client;
    private final Settings settings;
    private final WebsocketInteraction websocketInteraction;
    private final JProgressBar formProgressbar = new JProgressBar(0, 100);
    private final List<JButton> emojiButtonListForFocus = new ArrayList<>();
    private final ClassLoader classLoader = getClass().getClassLoader();
    Logger logger = Logger.getLogger(ChatImpl.class.getName());
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

        settings = Settings.getInstance();

        ipAddressesAsLocalClientsInit();
        emojiListInit();
        initializeGui();

        websocketInteraction = new WebsocketInteraction(this);
        websocketInteraction.connectToWebSocket();
    }

    public JDialog getInitialLoadingStartUpDialog() {

        return initialLoadingStartUpDialog;
    }

    public String getLastMessageFrom() {

        return lastMessageFrom;
    }

    public void setLastMessageFrom(String lastMessageFrom) {

        this.lastMessageFrom = lastMessageFrom;
    }

    public String getLastPostTime() {

        return lastPostTime;
    }

    public void setLastPostTime(String lastPostTime) {

        this.lastPostTime = lastPostTime;
    }

    @Override
    public void onMessageReceived(String message) {

        if (startup) {
            boolean isInitialRowCountInfoProcessed = processInitialRowCountInfo(message);

            if (!isInitialRowCountInfoProcessed) {
                updateProgressBarAndShowStatus();
            }
            addDelay(1050);
        }

        websocketInteraction.onMessageReceived(message);
    }

    private boolean processInitialRowCountInfo(String message) {

        if (message.contains("ROWS:")) {
            progressbarMaxValue = 100;
            // start at 0, not 1
            progressBarLiveValue--;
            return true;
        }
        return false;
    }

    private void updateProgressBarAndShowStatus() {

        progressBarLiveValue++;
        formProgressbar.setValue((progressBarLiveValue) * 100 / progressbarMaxValue);

        if (loadingMessageLabelOnStartUp == null) {
            loadingMessageLabelOnStartUp = new JLabel();
        }

        String statusMessage = String.format("loading - %d of %d - estm. %d secs", progressBarLiveValue, progressbarMaxValue, progressbarMaxValue * 25 / 600);
        loadingMessageLabelOnStartUp.setText(statusMessage);
    }

    private void addDelay(int milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onByteBufferMessageReceived(ByteBuffer bytes) {

        websocketInteraction.onByteBufferMessageReceived(bytes);
    }

    @Override
    public void onCloseReconnect() {

        connectToServerTimer();
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

    public void setParticipantNameArray(String[] participantNameArray) {

        this.participantNameArray = participantNameArray;
    }

    protected void updateFrame() {

        revalidate();
        form_mainTextPanel.revalidate();

        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalScrollBar = form_mainTextBackgroundScrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }

    protected void loadingInitialMessagesLoadUpDialog() {

        if (isDialogVisible()) {
            return;
        }

        SwingUtilities.invokeLater(this::initializeDialog);
    }

    private boolean isDialogVisible() {

        return initialLoadingStartUpDialog != null && initialLoadingStartUpDialog.isVisible();
    }

    private void initializeDialog() {

        initialLoadingStartUpDialog = createDialog();
        initialLoadingStartUpDialog.setVisible(true);
    }

    private JDialog createDialog() {

        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(HIDE_ON_CLOSE);
        dialog.setLayout(new MigLayout("alignx center, wrap 1", "[center]", "[grow,fill, center]"));

        dialog.add(createConnectionStatusLabel());
        dialog.add(createLoadingStatusLabel());
        dialog.add(createProgressBarPanel());

        dialog.pack();
        dialog.setSize(dialog.getWidth() + 150, dialog.getHeight() + 150);
        dialog.setLocationRelativeTo(this);
        dialog.setAlwaysOnTop(true);

        return dialog;
    }

    private JLabel createConnectionStatusLabel() {

        return new JLabel("trying to connect to server -- ip: " + settings.getIp() + " -- " + "port: " + settings.getPort(), SwingConstants.CENTER);
    }

    private JLabel createLoadingStatusLabel() {

        JLabel label = new JLabel("¯\\_(ツ)_/¯   loading messages.. please wait..", SwingConstants.CENTER);
        loadingMessageLabelOnStartUp = new JLabel();
        loadingMessageLabelOnStartUp.setHorizontalAlignment(SwingConstants.CENTER);
        loadingMessageLabelOnStartUp.setText("loading..");

        return label;
    }

    private JPanel createProgressBarPanel() {

        JPanel progressBarPanel = new JPanel();
        progressBarPanel.add(formProgressbar);

        return progressBarPanel;
    }

    protected void incomingMessagePreviewDesktopNotification(MessageModel messageModel) {

        if (!this.isFocused() && !startup) {

            SwingUtilities.invokeLater(() -> {

                // create popup dialog, attach mouse click event and arrange layout
                JDialog popUpDialog = createPopUpDialog();

                // adding a new Panel with given message model to popUpDialog
                addMessageToPopUpDialog(messageModel, popUpDialog);

                // tweak dialog's location and properties like focus, border etc.
                setPopUpDialogProperties(popUpDialog);

                // initiate timers for changing dialog border and auto disposing
                initiateDialogTimers(popUpDialog);

                // beep sound to alert user about the incoming message
                Toolkit.getDefaultToolkit().beep();
            });
        }
    }

    private JDialog createPopUpDialog() {

        JDialog popUpDialog = new JDialog();
        addMouseClickAdapterToPanel(popUpDialog);
        popUpDialog.setLayout(new MigLayout("fillx", "[fill]", "[fill]"));
        return popUpDialog;
    }

    private void addMessageToPopUpDialog(MessageModel messageModel, JDialog popUpDialog) {
        // Add comment panel to dialog
        PanelRightImpl comment = new PanelRightImpl(messageModel);
        addMouseClickAdapterToPanel(comment);
        popUpDialog.add(comment);
    }

    private void setPopUpDialogProperties(JDialog popUpDialog) {

        popUpDialog.pack();
        popUpDialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - popUpDialog.getWidth() - 20, heightLastPopUp);
        popUpDialog.setAutoRequestFocus(false);
        heightLastPopUp += popUpDialog.getHeight() + 5;
        visibleDesktopNotificationCount++;
        popUpDialog.getRootPane().setBorder(new LineBorder(Color.RED, 5));
        popUpDialog.setAlwaysOnTop(true);
        popUpDialog.pack();
        popUpDialog.setVisible(true);
    }

    private void initiateDialogTimers(JDialog popUpDialog) {
        // Timer for changing dialog border
        Timer timerBorder = new Timer(1000, e -> {
            popUpDialog.getRootPane().setBorder(new LineBorder(Color.BLUE, 2));
            popUpDialog.pack();
        });
        timerBorder.setRepeats(false);
        timerBorder.start();

        // Timer for closing dialog
        Timer timer = createClosingTimer(popUpDialog);
        timer.start();

        if ((heightLastPopUp > 500) || (visibleDesktopNotificationCount == 0)) {
            heightLastPopUp = 30;
        }

        super.toFront();
        setFocusableWindowState(false);
        setFocusableWindowState(true);
    }

    private Timer createClosingTimer(JDialog popUpDialog) {

        return new Timer(settings.getMessageDuration() * 1000, e -> {
            popUpDialog.dispose();
            visibleDesktopNotificationCount--;

            if (visibleDesktopNotificationCount == 0) {
                heightLastPopUp = 30;
            }
        });
    }

    @Override
    protected void thisPropertyChange(PropertyChangeEvent e) {

        logger.info(e.toString());
    }

    @Override
    protected void thisComponentResized(ComponentEvent e) {

        settings.setMainFrameWidth(getWidth());
        settings.setMainFrameHeight(getHeight());
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
        websocketInteraction.onCloseReconnect();
    }

    /**
     Function to send button operations

     @param e ActionEvent e
     */
    @Override
    protected synchronized void sendButton(ActionEvent e) {

        String text = form_textEditorPane.getText().trim();

        // emergency command -> shuts down all clients
        if (isTerminateAllCommand(text)) {
            sendTerminateAllCommand();
            return;
        }

        try {
            createAndSendMessageModel(text);

            // close emoji pop up while sending
            closeEmojiPopUp();

        } catch (WebsocketNotConnectedException ex) {
            websocketInteraction.createNewMessageOnPane("can't send message, no connection to server");

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);

        } catch (NullPointerException ex) {
            // todo error handling not working
            handleError(ex);
        }

        form_textEditorPane.setText("");
    }

    private boolean isTerminateAllCommand(String text) {

        return text.equals("/terminateAll");
    }

    private void sendTerminateAllCommand() {

        client.send("/terminateAll".getBytes());
        form_textEditorPane.setText("");
    }

    private void createAndSendMessageModel(String text) throws JsonProcessingException {

        MessageModel messageModel = new MessageModel(mapOfIps.get(client.getLocalSocketAddress().getHostString()), EmojiConverter.checkTextForEmojis(form_textEditorPane));

        if (isMessageEmpty(messageModel)) {
            return;
        }

        messageModel.setLocalIp(client.getLocalSocketAddress().getHostString());
        client.send(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(messageModel));
    }

    private boolean isMessageEmpty(MessageModel messageModel) {

        return messageModel.getMessage().trim().isEmpty();
    }

    private void closeEmojiPopUp() {

        if (emojiSelectionPopUp != null && emojiSelectionPopUp.isVisible()) {
            emojiWindow.dispose();
            emojiSelectionPopUp.dispose();
        }
    }

    private void handleError(NullPointerException ex) {

        SwingUtilities.invokeLater(() -> {
            JLabel label = new JLabel(ex.getMessage());
            label.setForeground(Color.RED);
            label.setFont(new Font(null, Font.BOLD, 20));
            form_mainTextPanel.add(new JLabel().add(label), "wrap, align center");
        });
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
                } else {
                    form_textEditorPane.setText(form_textEditorPane.getText() + "\n");
                }
                e.consume();
                return;

            // TODO test this: Hashtag on ISO DE Keyboard
            case 520:
            case 51:
                SwingUtilities.invokeLater(() -> form_textEditorPane.setText(StringUtils.chop(form_textEditorPane.getText())));
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
            websocketInteraction.createNewMessageOnPane("no connection to server, try to reconnect -> file > reset reconnect");
        }
    }

    @Override
    protected void emojiButton(ActionEvent e) {

        if (e == null) {
            return;
        }

        // emergency exit -> shift or ctrl and emoji button click
        if (e.getModifiers() == 17 || e.getModifiers() == 18) {
            System.exit(0);
        }

        if (emojiSelectionPopUp != null && emojiSelectionPopUp.isVisible()) {
            SwingUtilities.invokeLater(() -> emojiSelectionPopUp.dispose());
        } else {
            if (emojiSelectionPopUp == null) {
                emojiSelectionPopUp = new JDialog();
                emojiSelectionPopUp.addWindowFocusListener(new WindowAdapter() {

                    public void windowLostFocus(WindowEvent e) {

                        emojiSelectionPopUp.dispose();
                    }
                });
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

        if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER || e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
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

        addPictureButton.addMouseListener(new MouseAdapter() {

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
        sendPictureButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                Icon labelIcon = label.getIcon();
                BufferedImage image = new BufferedImage(labelIcon.getIconWidth(), labelIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
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

        if (e != null) {
            e.consume();
            disposeParticipantFrame();
            SwingUtilities.invokeLater(this::initializeParticipantsFrame);
        }
    }

    private void disposeParticipantFrame() {

        if (participantsFrame != null) {
            participantsFrame.dispose();
            participantsFrame = null;
        }
    }

    private void initializeParticipantsFrame() {

        clearParticipantTextArea();
        createParticipantFrameIfNeeded();
        updateParticipantFrame();
    }

    private void clearParticipantTextArea() {

        if (participantTextArea != null) {
            participantTextArea.selectAll();
            participantTextArea.replaceSelection("");
        }
    }

    private void createParticipantFrameIfNeeded() {

        assert client != null;
        if (participantsFrame == null) {
            initializeNewParticipantFrame();
        }
    }

    private void initializeNewParticipantFrame() {

        client.send("PARTICIPANTLIST".getBytes());
        participantsFrame = new JFrame("participants");
        setupParticipantFrameProperties();
        participantTextArea = new JTextPane();
        participantsFrame.add(participantTextArea, "growx");
        addButtonToFrame();
    }

    private void setupParticipantFrameProperties() {

        participantsFrame.setAlwaysOnTop(true);
        participantsFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        participantsFrame.setLayout(new MigLayout("fill,wrap", "[grow, align center]", "[grow, align center]"));
    }

    private void addButtonToFrame() {

        JButton updateButton = new JButton("update");
        updateButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                client.send("PARTICIPANTLIST".getBytes());
                participantsMenuItemMousePressed(null);
            }
        });
        participantsFrame.add(updateButton, "growx");
    }

    private void updateParticipantFrame() {

        populateParticipantTextArea();
        revalidateAndRepaintParticipantFrame();
    }

    private void populateParticipantTextArea() {

        if (participantNameArray != null) {
            for (Entry<String, String> entry : mapOfIps.entrySet()) {
                Color color = Arrays.stream(participantNameArray).anyMatch(z -> z.contains(entry.getKey())) ? Color.GREEN : Color.RED;
                appendToPane(participantTextArea, "•", color);
                appendToPane(participantTextArea, " " + entry.getValue() + " (" + entry.getKey() + ")", Color.BLACK);
                appendToPane(participantTextArea, "\n", Color.BLACK);
            }
        }
    }

    private void revalidateAndRepaintParticipantFrame() {

        assert participantTextArea != null;
        participantsFrame.revalidate();
        participantsFrame.pack();
        participantsFrame.setResizable(false);
        participantTextArea.setEditable(false);
        setParticipantFrameLocation();
        participantsFrame.setVisible(true);
    }

    private void setParticipantFrameLocation() {

        participantsFrame.setLocation(this.getBounds().x + this.getBounds().width, this.getBounds().y);
    }

    @Override
    protected void exitMenuItemMousePressed(MouseEvent e) {

        e.consume();
        System.exit(0);
    }

    @Override
    protected void thisMouseClicked(MouseEvent e) {

    }

    /**
     Initiates a countdown to establish a connection to the server before the client gets stuck on the connection pop up.
     */
    private void connectToServerTimer() {

        Timer connectionTimer = createConnectionTimer();
        startConnectingIfClientNotOpen(connectionTimer);
    }

    /**
     Creates a timer that disposes the initial loading dialog and checks for connection.

     @return A defined Timer instance.
     */
    private Timer createConnectionTimer() {

        return new Timer(SERVER_CONNECTION_TIME_LIMIT, e -> connectionTimerAction());
    }

    /**
     Performs the actions to be carried out when the connection timer fires.
     */
    private void connectionTimerAction() {
        // avoid client being stuck on connection pop up
        disposeInitialLoadingDialog();

        if (!isClientConnected()) {
            notifyConnectionFailure();
        }
    }

    /**
     Disposes the initial loading start up dialog.
     */
    private void disposeInitialLoadingDialog() {

        initialLoadingStartUpDialog.dispose();
    }

    /**
     Checks if the client socket is connected.

     @return <code>true</code> if client is connected, otherwise <code>false</code>.
     */
    private boolean isClientConnected() {

        return client.getSocket().isConnected();
    }

    /**
     Notifies the client when a connection to the server was not successful.
     */
    private void notifyConnectionFailure() {

        JOptionPane.showMessageDialog(this, "Connection failed! Server seems down!");
    }

    /**
     Starts the connection timer if the client is not yet open.

     @param connectionTimer The timer to start.
     */
    private void startConnectingIfClientNotOpen(Timer connectionTimer) {

        if (!client.isOpen()) {
            connectionTimer.start();
        }
    }

    private void ipAddressesAsLocalClientsInit() {

        try {
            String ipAddressConfig = loadIPAddressConfig();
            processIPAddressConfig(ipAddressConfig);
        } catch (IOException | URISyntaxException ex) {
            handleException(ex);
        }
    }

    private String loadIPAddressConfig() throws IOException, URISyntaxException {

        if (isRunningFromJar()) {
            Path path = locateConfigInJar();
            if (Files.exists(path)) {
                return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            } else {
                return loadConfigAsResource();
            }
        } else {
            return loadConfigFromFileSystem();
        }
    }

    private boolean isRunningFromJar() {

        return String.valueOf(ChatImpl.class.getResource("ChatImpl.class")).startsWith("jar:");
    }

    private Path locateConfigInJar() throws URISyntaxException {

        URI location = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        return Paths.get(location).getParent().getParent().resolve("bin/conf/ips.txt");
    }

    private String loadConfigAsResource() {

        InputStream inputStream = ClassLoader.getSystemResourceAsStream("conf/ips.txt");
        assert inputStream != null;
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
    }

    private String loadConfigFromFileSystem() throws IOException {

        return new String(Files.readAllBytes(Paths.get("src/main/resources/conf/ips.txt")), StandardCharsets.UTF_8);
    }

    private void processIPAddressConfig(String ipAddressConfig) throws IOException {

        ClientsList clientsList = MAPPER.readValue(ipAddressConfig, ClientsList.class);
        mapOfIps = new HashMap<>();
        clientsList.getClientsList().forEach(client -> mapOfIps.put(client.getLocalIpAddress(), client.getClientName()));
    }

    private void handleException(Exception ex) {

        throw new RuntimeException(ex);
    }

    private void initializeGui() {

        loadTitleBarAndSystemTrayLogo();

        assignMainFrame();

        instantiateEmojiWindow();

        configureScrollBar();

        configureJFrameOptions();

        handleWindowClosingEvent();

    }

    private void loadTitleBarAndSystemTrayLogo() {

        loadLogoIconForTitleBarAndSystemTray();
    }

    private void assignMainFrame() {

        settings.setMainJFrame(this);
    }

    private void instantiateEmojiWindow() {

        emojiWindow = new EmojiImpl(form_textEditorPane);
    }

    private void configureScrollBar() {

        form_mainTextBackgroundScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void configureJFrameOptions() {

        int frameWidth = (settings.getMainFrameWidth() < 500 ? 650 : settings.getMainFrameWidth());
        int frameHeight = (settings.getMainFrameHeight() < 500 ? 650 : settings.getMainFrameHeight());
        this.setSize(frameWidth, frameHeight);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Dev-Chat");
    }

    private void handleWindowClosingEvent() {

        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {

                setExtendedState(JFrame.ICONIFIED);
            }
        });
    }

    private void loadLogoIconForTitleBarAndSystemTray() {

        if (Objects.requireNonNull(classLoader.getResource("icon.png")).toString().contains("jar")) {

            setIconImage(Toolkit.getDefaultToolkit().getImage(classLoader.getResource("icon.png")));
        } else {
            // if built from IDE
            setIconImage(Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png"));
        }
    }

    private void addMouseClickAdapterToPanel(Component window) {

        window.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                e.consume();
                // bring the main frame to front by clicking pop up
                SwingUtilities.invokeLater(() -> {
                    ChatImpl.super.setExtendedState(JFrame.NORMAL);
                    ChatImpl.super.setAlwaysOnTop(true);
                    ChatImpl.super.requestFocus();
                    ChatImpl.super.setLocationRelativeTo(null);
                    ChatImpl.super.setAlwaysOnTop(false);
                });
            }
        });
    }

    private void initEmojiFrame() {

        Enumeration<URL> resources = getResources();

        while (resources.hasMoreElements()) {
            URL resourceUrl = resources.nextElement();

            if (resourceUrl.getProtocol().equals(JAR_PROTOCOL)) {
                handleJarResources(resourceUrl);
            } else {
                handleFileResources();
            }
        }
    }

    private Enumeration<URL> getResources() {

        try {
            return classLoader.getResources(EMOJI_DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleJarResources(URL resourceUrl) {

        try {
            JarURLConnection jarUrlConnection = (JarURLConnection) resourceUrl.openConnection();
            try (JarFile jarFile = jarUrlConnection.getJarFile()) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();

                    if (entryName.startsWith(EMOJI_DIRECTORY_PATH) && entryName.endsWith(PNG_FILE_EXTENSION)) {
                        try (InputStream inputStream = classLoader.getResourceAsStream(entryName)) {

                            assert inputStream != null;
                            BufferedImage bufferedImage = ImageIO.read(inputStream);
                            populateDialogWithEmojiButton(null, bufferedImage, entryName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private void handleFileResources() {

        File folder = new File("./src/main/resources/" + EMOJI_DIRECTORY_PATH);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(PNG_FILE_EXTENSION)) {
                    populateDialogWithEmojiButton(file, null, null);
                }
            }
            emojiSelectionPopUp.pack();
        }
    }

    private void populateDialogWithEmojiButton(File file, BufferedImage bufferedImage, String entryName) {

        ImageIcon icon;
        String fileName;

        if (file != null) {
            icon = createImageIconFromFile(file);
            fileName = getFileNameWithoutExtension(file.getName());
        } else {
            icon = createImageIconFromBufferedImage(bufferedImage);
            fileName = getFileNameWithoutExtension(entryName);
        }

        fileName = removeEmojisDirectoryPrefix(fileName);
        createJButtonWithIconAndName(fileName, icon);
    }

    /**
     Creates an image icon from a file.

     @param file The file from which the image icon will be created.

     @return The image icon created from the file.
     */
    private ImageIcon createImageIconFromFile(File file) {

        return new ImageIcon(file.getPath());
    }

    /**
     Creates an image icon from a buffered image.

     @param bufferedImage The buffered image from which the image icon will be created.

     @return The image icon created from the buffered image.
     */
    private ImageIcon createImageIconFromBufferedImage(BufferedImage bufferedImage) {

        return new ImageIcon(bufferedImage);
    }

    /**
     Returns the file name without the extension.

     @param fileName The file name which contains the extension.

     @return The file name without the extension.
     */
    private String getFileNameWithoutExtension(String fileName) {

        return fileName.replace(".png", "");
    }

    /**
     Removes the directory prefix from the given file name.

     @param fileName The file name with the directory prefix.

     @return The file name without the directory prefix.
     */
    private String removeEmojisDirectoryPrefix(String fileName) {

        return fileName.replace("emojis/", "");
    }

    private void addJButtonToViews(JButton jbutton) {

        emojiSelectionPopUp.add(jbutton);
        emojiButtonListForFocus.add(jbutton);
    }

    private void createJButtonWithIconAndName(String name, ImageIcon icon) {

        JButton jbutton = new JButton();
        jbutton.setBorder(new FlatListCellBorder.Default());

        addFocusListenerToJButton(jbutton);
        icon.setDescription(name);
        jbutton.setName(name);
        jbutton.setIcon(icon);

        addEventListenersToJButton(jbutton, icon);
        addJButtonToViews(jbutton);
    }

    private void addEventListenersToJButton(JButton jbutton, ImageIcon icon) {

        addMouseListenerToJButton(jbutton, icon);
        addKeyListenerToJButton(jbutton, icon);
    }

    private void addFocusListenerToJButton(JButton jButton) {

        jButton.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {

                jButton.setBorder(new FlatTableCellBorder.Selected());
            }

            @Override
            public void focusLost(FocusEvent e) {

                jButton.setBorder(new FlatTableCellBorder.Default());
            }
        });
    }

    private void addMouseListenerToJButton(JButton jButton, ImageIcon icon) {

        jButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                e.consume();
                form_textEditorPane.insertIcon(icon);
                form_textEditorPane.insertComponent(new JLabel(""));
                form_textEditorPane.repaint();
            }
        });
    }

    private void addKeyListenerToJButton(JButton jButton, ImageIcon icon) {

        jButton.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (keyboardTraversFocusViaArrowKeys(e)) {

                    return;
                }

                emojiFrameKeyPressed(e, icon);
            }

        });
    }

    private int getNextButtonIndex(int keyCode, int step, int min, int max) {

        switch (keyCode) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_RIGHT:
                return Math.min(max, currentEmojiFocus + step);
            case KeyEvent.VK_UP:
            case KeyEvent.VK_LEFT:
                return Math.max(min, currentEmojiFocus - step);
            default:
                return currentEmojiFocus;
        }
    }

    /**
     Switches the focus to the specified emoji button and consumes the KeyEvent.

     @param nextButtonIndex The index of the emoji button to switch the focus to.
     @param e               The KeyEvent to consume.
     */
    private void switchFocusAndConsumeEvent(int nextButtonIndex, KeyEvent e) {

        emojiButtonListForFocus.get(nextButtonIndex).requestFocus();
        currentEmojiFocus = nextButtonIndex;
        e.consume();
    }

    private boolean keyboardTraversFocusViaArrowKeys(KeyEvent e) {

        int nextButtonIndex;
        int step = (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) ? 10 : 1;
        int min = 0;
        int max = emojiButtonListForFocus.size() - 1;

        nextButtonIndex = getNextButtonIndex(e.getKeyCode(), step, min, max);

        if (nextButtonIndex != currentEmojiFocus) {
            switchFocusAndConsumeEvent(nextButtonIndex, e);
            return true;
        }

        return false;
    }

    private AttributeSet createAttributeSet(StyleContext sc, Color c, String msg) {

        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.FontSize, msg.equals("•") ? 20 : 12);

        return aset;
    }

    private void appendMessageToPane(JTextPane tp, String msg, AttributeSet attributeSet) {

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(attributeSet, false);
        tp.replaceSelection(msg);
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = createAttributeSet(sc, c, msg);
        appendMessageToPane(tp, msg, attributeSet);
    }

}
