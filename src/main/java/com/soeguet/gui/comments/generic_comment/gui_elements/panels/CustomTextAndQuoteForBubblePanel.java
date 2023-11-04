package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomTextAndQuoteForBubblePanel extends JPanel {

    private final MainFrameGuiInterface mainFrame;
    private final MessageModel messageModel;

    public CustomTextAndQuoteForBubblePanel(MainFrameGuiInterface mainFrame,MessageModel messageModel) {

        this.mainFrame = mainFrame;

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

        CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane(mainFrame);
        customSimpleTextPane.setText(messageModel.getMessage());
        customSimpleTextPane.convertTextToEmojis();

        super.add(customSimpleTextPane, "cell 0 1");
    }

    public void addQuote() {

        if (messageModel.getQuotedMessage() != null) {


            CustomQuoteBubblePanel customQuoteBubblePanel =
                    new CustomQuoteBubblePanel(mainFrame, messageModel.getQuotedMessage());

            customQuoteBubblePanel.setQuoteBubbleLayoutManager();
            customQuoteBubblePanel.extractQuotedMessage();

            super.add(customQuoteBubblePanel, "cell 0 0");
        }
    }
}