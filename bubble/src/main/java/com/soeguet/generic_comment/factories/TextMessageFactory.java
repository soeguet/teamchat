package com.soeguet.generic_comment.factories;

import com.soeguet.generic_comment.gui_elements.panels.CustomTextAndQuoteForBubblePanel;
import com.soeguet.model.jackson.MessageModel;

/**
 Produces a CustomTextAndQuoteForBubblePanel which contains the text panel as well as the quote panel.

 <p>SCHEMA: <br>
 CustomTextAndQuoteForBubblePanel:<br> __________________<br> | [quoted stuff] | <- this is what we produce<br> | [text
 message] |<br> ------------------

 @param MainFrameGuiInterface
 {@link MainFrameGuiInterface}
 @param MessageModel
 {@link MessageModel}
 */
public class TextMessageFactory {

    private final MessageModel messageModel;

    /**
     Produces a CustomTextAndQuoteForBubblePanel which contains the text panel as well as the quote panel.

     @param MainFrameGuiInterface
     mainFrame {@link MainFrameGuiInterface}
     @param MessageModel
     messageModel {@link MessageModel}
     */
    public TextMessageFactory(final MessageModel messageModel) {

        this.messageModel = messageModel;
    }

    /**
     Creates a CustomTextAndQuoteForBubblePanel which contains the text panel as well as the quote panel.

     @return CustomTextAndQuoteForBubblePanel {@link CustomTextAndQuoteForBubblePanel}
     */
    public CustomTextAndQuoteForBubblePanel create() {

        CustomTextAndQuoteForBubblePanel customTextAndQuoteForBubblePanel =
                new CustomTextAndQuoteForBubblePanel(messageModel);

        customTextAndQuoteForBubblePanel.setTextAndQuoteBubbleLayoutManager();

        customTextAndQuoteForBubblePanel.addQuote();
        customTextAndQuoteForBubblePanel.addTextMessage();

        return customTextAndQuoteForBubblePanel;
    }
}