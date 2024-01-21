package com.soeguet.generic_comment.gui_elements.buttons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.generic_comment.gui_elements.panels.CustomReplyPanel;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.model.jackson.QuoteModel;
import com.soeguet.properties.PropertiesRegister;
import com.soeguet.socket_client.ClientRegister;
import com.soeguet.socket_client.CustomWebsocketClient;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CustomReplySendButton extends JButton implements MouseListener {

    // variables -- start
    private final BaseModel baseModel;
    private final CustomReplyPanel customReplyPanel;

    // variables -- end

    // constructors -- start
    public CustomReplySendButton(BaseModel baseModel, CustomReplyPanel customReplyPanel) {

        this.baseModel = baseModel;
        this.customReplyPanel = customReplyPanel;

        super.setFocusable(false);
        this.setCustomIcon();

        super.addMouseListener(this);
    }

    // constructors -- end

    private void setCustomIcon() {

        // TODO 1
        //        URL sendUrl = ChatMainFrameImpl.class.getResource("/emojis/$+1f4e8$+.png");
        //
        //        if (sendUrl != null) {
        //
        //            super.setIcon(new ImageIcon(sendUrl));
        //        }
    }

    private MessageModel createMessageModel() {

        final QuoteModel<BaseModel> quoteModel = this.createQuoteModel();

        MessageModel messageModel = new MessageModel();

        // FEATURE -> can't quote pictures -> they clutter the database this way -> need to find a
        // way to reference them
        if (quoteModel.t() instanceof PictureModel pictureModel) {

            pictureModel.setPicture(null);
        }

        messageModel.setQuotedMessage(quoteModel);
        messageModel.setMessage(customReplyPanel.getTextPane().getText());
        messageModel.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        messageModel.setSender(PropertiesRegister.getCustomUserPropertiesInstance().getUsername());
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

        // make main textPanel editable again
        // TODO 1
        //        mainFrame.getTextEditorPane().setFocusable(true);
        //        mainFrame.getTextEditorPane().requestFocus();

        final CustomWebsocketClient websocketClient =
                ClientRegister.getWebSocketClientInstance().getCustomWebsocketClient();
        final ObjectMapper objectMapper = new ObjectMapper();

        final MessageModel messageModel = this.createMessageModel();

        try {

            final String serializedMessageModel = objectMapper.writeValueAsString(messageModel);
            // TODO 1
//            websocketClient.send(serializedMessageModel);

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