package com.soeguet.gui.reply;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.gui.reply.generated.ReplyPanel;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.util.WrapEditorKit;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.util.EmojiHandler;
import com.soeguet.util.EmojiPopUpMenuHandler;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ReplyPanelImpl extends ReplyPanel {

    private final Point offset = new Point();
    private final MainFrameInterface mainFrame;
    private final BaseModel messageModel;
    private final Border border = this.getBorder();

    public ReplyPanelImpl(MainFrameInterface mainFrame, BaseModel messageModel) {

        this.mainFrame = mainFrame;
        this.messageModel = messageModel;

        populatePanel();
        setPosition();

        requestAllFocus();
    }

    private void populatePanel() {

        getMainQuoteTextField().setEditorKit(new WrapEditorKit());

        new EmojiHandler(mainFrame).replaceEmojiDescriptionWithActualImageIcon(getMainQuoteTextField(), messageModel.getMessage());
        form_quotedSender.setText(messageModel.getSender());
        form_quotedTime.setText(messageModel.getTime());
    }

    private void setPosition() {

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

    private void requestAllFocus() {

        SwingUtilities.invokeLater(() -> {

            this.setBorder(new LineBorder(Color.BLUE));

            this.getReplyTextPane().requestFocus();
            this.getReplyTextPane().grabFocus();
        });
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
    protected void quotePanelEmojiButtonMouseClicked(MouseEvent e) {

        new EmojiPopUpMenuHandler(mainFrame, this.getReplyTextPane(), this.form_quotePanelEmojiButton);
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

        //TODO: implement picture sending as reply
    }

    @Override
    protected void quotePanelSendButtonMouseClicked(MouseEvent e) {

        if (isTextPaneBlank()) return;

        new EmojiHandler(mainFrame).replaceImageIconWithEmojiDescription(this.getReplyTextPane());

        MessageModel sendModel = new MessageModel((byte) MessageTypes.NORMAL, mainFrame.getUsername(), this.getReplyTextPane().getText(), messageModel.getSender(), messageModel.getTime(), messageModel.getMessage());

        try {

            mainFrame.getWebsocketClient().send(mainFrame.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sendModel));

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(ex);
        }

        this.removeAll();
        this.setVisible(false);
    }

    private boolean isTextPaneBlank() {

        if (this.form_replyTextPane.getText().isBlank()) {
            return true;
        }
        return false;
    }
}