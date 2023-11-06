package com.soeguet.gui.comments.generic_comment.gui_elements.buttons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomReplyPanel;
import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.*;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomReplySendButton extends JButton implements MouseListener {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    private final CustomReplyPanel customReplyPanel;
    // variables -- end

    // constructors -- start
    public CustomReplySendButton(MainFrameGuiInterface mainFrame, BaseModel baseModel,
                                 CustomReplyPanel customReplyPanel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.customReplyPanel = customReplyPanel;

        super.setFocusable(false);
        this.setCustomIcon();

        super.addMouseListener(this);
    }
    // constructors -- end

    private void setCustomIcon() {

        URL sendUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f4e8$+.png");

        if (sendUrl != null) {

            super.setIcon(new ImageIcon(sendUrl));
        }
    }

    private MessageModel createMessageModel() {

        final QuoteModel<BaseModel> quoteModel = this.createQuoteModel();

        MessageModel messageModel = new MessageModel();

        //FEATURE -> can't quote pictures -> they clutter the database this way -> need to find a way to reference them
        if (quoteModel.t() instanceof PictureModel pictureModel) {

            pictureModel.setPicture(null);
        }

        messageModel.setQuotedMessage(quoteModel);
        messageModel.setMessage(customReplyPanel.getTextPane().getText());
        messageModel.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        messageModel.setSender(mainFrame.getUsername());
        messageModel.setMessageType(MessageTypes.REPLY);

        return messageModel;
    }

    private QuoteModel<BaseModel> createQuoteModel() {

        QuoteModel<BaseModel> quoteModel;

        switch (baseModel) {
            case MessageModel messageModel -> quoteModel = new QuoteModel<>(messageModel);
            case PictureModel pictureModel -> quoteModel = new QuoteModel<>(pictureModel);
            case LinkModel linkModel -> quoteModel = new QuoteModel<>(linkModel);
        }
        return quoteModel;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        final CustomWebsocketClient websocketClient = mainFrame.getWebsocketClient();
        final ObjectMapper objectMapper = mainFrame.getObjectMapper();

        final MessageModel messageModel = this.createMessageModel();

        try {

            final String serializedMessageModel = objectMapper.writeValueAsString(messageModel);
            websocketClient.send(serializedMessageModel);

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(ex);
        }

        customReplyPanel.removeAll();
        customReplyPanel.setVisible(false);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}