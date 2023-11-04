package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomTextAndQuoteForBubblePanel;
import com.soeguet.model.jackson.MessageModel;

public class TextMessageFactory {

    private final MessageModel messageModel;

    public TextMessageFactory(final MessageModel messageModel) {

        this.messageModel = messageModel;
    }

    public CustomTextAndQuoteForBubblePanel create() {

        CustomTextAndQuoteForBubblePanel customTextAndQuoteForBubblePanel = new CustomTextAndQuoteForBubblePanel(messageModel);

        customTextAndQuoteForBubblePanel.setTextAndQuoteBubbleLayoutManager();
        customTextAndQuoteForBubblePanel.addQuote();
        customTextAndQuoteForBubblePanel.addTextMessage();


        return customTextAndQuoteForBubblePanel;
    }
}