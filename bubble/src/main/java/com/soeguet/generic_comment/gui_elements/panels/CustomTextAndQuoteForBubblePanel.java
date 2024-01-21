package com.soeguet.generic_comment.gui_elements.panels;

import com.soeguet.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;

import com.soeguet.model.jackson.MessageModel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * This is the main panel of every chat bubble housing messages, quotes and alike.
 *
 * <p>SCHEMA: <br>
 * __________________<br>
 * | [quoted stuff] | <- this is where we are<br>
 * | [text message] |<br>
 * ------------------<br>
 */
public class CustomTextAndQuoteForBubblePanel extends JPanel {

    // variables -- start
    private final MessageModel messageModel;

    // variables -- end

    // constructors -- start
    public CustomTextAndQuoteForBubblePanel(
             final MessageModel messageModel) {


        this.messageModel = messageModel;
        super.setBackground(null);
        super.setOpaque(false);
    }

    // constructors -- end

    public void setTextAndQuoteBubbleLayoutManager() {

        super.setLayout(
                new MigLayout(
                        "", // Layout Constraints
                        "[shrink 0,fill,grow][10]", // Column constraints
                        "[shrink 0,fill,grow][shrink 0,fill,grow]" // Row constraints
                        ));
    }

    public void addQuote() {

        if (this.messageModel.getQuotedMessage() != null) {

            final CustomQuoteBubblePanel customQuoteBubblePanel =
                    new CustomQuoteBubblePanel( this.messageModel.getQuotedMessage());

            customQuoteBubblePanel.addTopNameAndTimeTextPane();
            customQuoteBubblePanel.setQuoteBubbleLayoutManager();
            customQuoteBubblePanel.extractQuotedMessage();

            super.add(customQuoteBubblePanel, "cell 0 0");
        }
    }

    public void addTextMessage() {

        final CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane();
        customSimpleTextPane.replaceEmojiDescriptionWithActualImageIcon(
                this.messageModel.getMessage());

        super.add(customSimpleTextPane, "cell 0 1");
    }
}