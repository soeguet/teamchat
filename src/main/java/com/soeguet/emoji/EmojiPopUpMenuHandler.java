package com.soeguet.emoji;

import com.soeguet.emoji.interfaces.EmojiPopupInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class EmojiPopUpMenuHandler implements EmojiPopupInterface {

    private final MainFrameGuiInterface mainFrame;
    private final JTextPane textPane;
    private final JButton emojiButton;

    private final Logger logger = Logger.getLogger(EmojiPopUpMenuHandler.class.getName());

    public EmojiPopUpMenuHandler(MainFrameGuiInterface mainFrame, JTextPane textPane, JButton emojiButton) {

        this.mainFrame = mainFrame;
        this.textPane = textPane;
        this.emojiButton = emojiButton;
    }

    /**
     Creates a pop-up menu with emojis.

     <p>This method is responsible for creating a pop-up menu and adding emojis to it. The pop-up
     menu is then displayed at the position of the emoji button.
     */
    @Override
    public void createEmojiPopupMenu() {

        JPopupMenu emojiPopupMenu = new JPopupMenu();
        JPanel emojiPanelWrapper = createEmojiPanel();

        mainFrame.getEmojiList().forEach((key, emoji) -> {
            JPanel emojiPanelForOneEmoji = createEmojiPanelForOneEmoji(key, emoji);
            emojiPanelWrapper.add(emojiPanelForOneEmoji);
        });

        emojiPopupMenu.add(emojiPanelWrapper);
        emojiPopupMenu.show(emojiButton, emojiButton.getMousePosition().x, emojiButton.getMousePosition().y);
    }

    /**
     Creates a panel for displaying emojis.

     <p>This method creates a JPanel and sets its layout to MigLayout with "wrap 10"
     constraints to display emojis in a grid-like fashion. The panel is then returned.

     @return The JPanel for displaying emojis.
     */
    private JPanel createEmojiPanel() {

        JPanel emojiPanelWrapper = new JPanel();
        emojiPanelWrapper.setLayout(new MigLayout("wrap 10", "[center]", "[center]"));
        return emojiPanelWrapper;
    }

    /**
     Creates a JPanel for displaying a single emoji.

     @param key   the key associated with the emoji
     @param emoji the ImageIcon representing the emoji

     @return the JPanel containing the emoji
     */
    private JPanel createEmojiPanelForOneEmoji(String key, ImageIcon emoji) {

        JPanel emojiPanelForOneEmoji = new JPanel();
        JLabel emojiLabel = new JLabel(emoji);
        emojiPanelForOneEmoji.add(emojiLabel);
        emojiPanelForOneEmoji.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        addMouseListenerToEmojiPanel(emojiPanelForOneEmoji, key, emoji);

        return emojiPanelForOneEmoji;
    }

    /**
     Adds a mouse listener to the specified emoji panel.

     @param emojiPanelForOneEmoji the JPanel to add the mouse listener to
     @param key                   the key associated with the emoji
     @param emoji                 the ImageIcon representing the emoji
     */
    private void addMouseListenerToEmojiPanel(JPanel emojiPanelForOneEmoji, String key, ImageIcon emoji) {

        emojiPanelForOneEmoji.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                insertEmojiAtCaretPosition(key, emoji);
            }

            public void mouseEntered(MouseEvent evt) {

                emojiPanelForOneEmoji.setBackground(new Color(0, 136, 191));
            }

            public void mouseExited(MouseEvent evt) {

                emojiPanelForOneEmoji.setBackground(null);
            }
        });
    }

    /**
     Inserts an emoji at the current caret position in the text editor pane.

     @param key   the key associated with the emoji
     @param emoji the ImageIcon representing the emoji
     */
    private void insertEmojiAtCaretPosition(String key, ImageIcon emoji) {

        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("Image", null);

        ImageIcon imageIcon = new ImageIcon(emoji.getImage());
        imageIcon.setDescription(key);
        StyleConstants.setIcon(style, imageIcon);

        try {

            doc.insertString(textPane.getCaretPosition(), " ", style);

        } catch (BadLocationException e) {

            logger.log(java.util.logging.Level.WARNING, e.getMessage(), e);
            throw new RuntimeException();

        }
    }
}