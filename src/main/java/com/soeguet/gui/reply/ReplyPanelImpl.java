package com.soeguet.gui.reply;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.emoji.EmojiHandler;
import com.soeguet.emoji.EmojiPopUpMenuHandler;
import com.soeguet.emoji.interfaces.EmojiPopupInterface;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.reply.generated.ReplyPanel;
import com.soeguet.gui.reply.interfaces.ReplyInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ReplyPanelImpl extends ReplyPanel implements ReplyInterface {

    private final Point offset = new Point();
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    private final Border border = this.getBorder();

    public ReplyPanelImpl(MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    @Override
    public void populatePanel() {

        getMainQuoteTextField().setEditorKit(new WrapEditorKit());

        this.setBorder(new LineBorder(Color.BLACK, 1));


        if (baseModel instanceof MessageModel messageModel) {

            new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(getMainQuoteTextField(), messageModel.getMessage());
            form_quotedSender.setText(baseModel.getSender());
            form_quotedTime.setText(baseModel.getTime());
        }
    }

    @Override
    public void setPosition() {

        int textPaneWidth = mainFrame.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = mainFrame.getMainTextPanelLayeredPane().getHeight();

        int height = (int) this.getPreferredSize().getHeight();

        //make it look a little less stuffed
        if (this.getPreferredSize().getWidth() > 500) {
            height += 100;
        } else {
            height += 30;
        }

        if (height > 500) {
            height = 500;
        }

        this.setBounds((textPaneWidth - 500) / 2, (textPaneHeight - height) / 2, 500, height);
    }

    @Override
    public void requestAllFocus() {

        SwingUtilities.invokeLater(() -> {

            this.setBorder(new LineBorder(Color.BLUE));

            this.getReplyTextPane().requestFocus();
            this.getReplyTextPane().grabFocus();
        });
    }

    @Override
    public void addPanelToMainFrame() {

        this.mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    @Override
    protected void thisMousePressed(MouseEvent e) {

        offset.setLocation(e.getX(), e.getY());
    }

    @Override
    protected void thisMouseDragged(MouseEvent e) {

        int x = e.getX() - offset.x + this.getX();
        int y = e.getY() - offset.y + this.getY();
        this.setLocation(x, y);
    }

    @Override
    protected void closeReplyPanelButtonMouseReleased(MouseEvent e) {

        this.removeAll();
        this.setVisible(false);
    }

    @Override
    protected void thisMouseEntered(MouseEvent e) {

        this.setBorder(new LineBorder(Color.BLUE));
    }

    @Override
    protected void thisMouseExited(MouseEvent e) {

        if (!getReplyTextPane().hasFocus()) {

            if (e.getX() < 0 || e.getY() < 0 || e.getX() > this.getWidth() - 1 || e.getY() > this.getHeight() - 1) {

                this.setBorder(border);
            }
        }
    }

    @Override
    protected void thisFocusLost(FocusEvent e) {

    }

    @Override
    protected void replyTextPaneFocusLost(FocusEvent e) {

    }

    @Override
    protected void replyTextPaneKeyPressed(final KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (e.isShiftDown()) {

                this.getReplyTextPane().setText(this.getReplyTextPane().getText() + System.lineSeparator());

            } else {

                quotePanelSendButtonMouseClicked(null);
            }
        }
    }

    @Override
    protected void quotePanelPictureButtonMouseClicked(final MouseEvent e) {

        //FEATURE implement picture sending as reply
    }

    @Override
    protected void quotePanelEmojiButtonMouseClicked(MouseEvent e) {

        EmojiPopupInterface emojiPopup = new EmojiPopUpMenuHandler(mainFrame, this.getReplyTextPane(), this.form_quotePanelEmojiButton);
        emojiPopup.createEmojiPopupMenu();
    }

    @Override
    protected void quotePanelSendButtonMouseClicked(MouseEvent e) {

        new EmojiHandler(mainFrame).replaceImageIconWithEmojiDescription(this.getReplyTextPane());

        //FIXME is this right?
        if (isTextPaneBlank()) {return;}

        MessageModel sendModel = new MessageModel();

        //type
        sendModel.setMessageType(MessageTypes.NORMAL);
        //quoted message
        // FIXME: 02.11.23
//        sendModel.setQuotedMessageSender(this.mainFrame.getUsername());
//        sendModel.setQuotedMessageText(this.getReplyTextPane().getText());
//        sendModel.setQuotedMessageTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        //this client
        sendModel.setSender(this.baseModel.getSender());

        if (baseModel instanceof MessageModel messageModel) {

            sendModel.setMessage(messageModel.getMessage());
        }

        sendModel.setTime(baseModel.getTime());

        try {

            final String serializedMessage = mainFrame.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sendModel);

            mainFrame.getWebsocketClient().send(serializedMessage);

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(ex);
        }

        this.removeAll();
        this.setVisible(false);
    }

    private boolean isTextPaneBlank() {

        return this.form_replyTextPane.getText().isBlank();
    }
}
