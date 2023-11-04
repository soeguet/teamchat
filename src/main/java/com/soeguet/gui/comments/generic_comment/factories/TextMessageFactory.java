package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomTextAndQuoteForBubblePanel;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.MessageModel;

public class TextMessageFactory {

    private final MainFrameGuiInterface mainFrame;
    private final MessageModel messageModel;

    public TextMessageFactory(MainFrameGuiInterface mainFrame, final MessageModel messageModel) {

        this.mainFrame = mainFrame;

        this.messageModel = messageModel;
    }

    public CustomTextAndQuoteForBubblePanel create() {

        CustomTextAndQuoteForBubblePanel customTextAndQuoteForBubblePanel =
                new CustomTextAndQuoteForBubblePanel(mainFrame, messageModel);

        customTextAndQuoteForBubblePanel.setTextAndQuoteBubbleLayoutManager();
        customTextAndQuoteForBubblePanel.addQuote();
        customTextAndQuoteForBubblePanel.addTextMessage();


        return customTextAndQuoteForBubblePanel;
    }
}