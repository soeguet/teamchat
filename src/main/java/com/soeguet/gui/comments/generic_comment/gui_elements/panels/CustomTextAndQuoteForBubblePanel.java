package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomTextAndQuoteForBubblePanel extends JPanel {

    private final MessageModel messageModel;

    public CustomTextAndQuoteForBubblePanel(MessageModel messageModel) {

        this.messageModel = messageModel;
        super.setBackground(null);
        super.setOpaque(false);
    }

    public void setTextAndQuoteBubbleLayoutManager() {

        super.setLayout(new MigLayout(

                /*

                SCHEMA:

                [quoted stuff] <- this might be another panel
                [text message]

                 */

                "", // Layout Constraints
                "[fill,grow]", // Column constraints
                "[fill][fill]" // Row constraints
        ));
    }

    public void addTextMessage() {

        CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane();
        customSimpleTextPane.setText(messageModel.getMessage());

        super.add(customSimpleTextPane, "cell 0 1");
    }

    public void addQuote() {

        if (messageModel.getQuotedMessage() != null) {


            CustomQuoteBubblePanel customQuoteBubblePanel = new CustomQuoteBubblePanel(messageModel.getQuotedMessage());

            customQuoteBubblePanel.setQuoteBubbleLayoutManager();
            customQuoteBubblePanel.extractQuotedMessage();

            super.add(customQuoteBubblePanel, "cell 0 0");
        }
    }
}