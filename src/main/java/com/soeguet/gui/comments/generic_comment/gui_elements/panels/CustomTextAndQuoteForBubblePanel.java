package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
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
    private final MainFrameGuiInterface mainFrame;
    private final MessageModel messageModel;

    // variables -- end

    // constructors -- start
    public CustomTextAndQuoteForBubblePanel(
            MainFrameGuiInterface mainFrame, MessageModel messageModel) {

        this.mainFrame = mainFrame;

        this.messageModel = messageModel;
        super.setBackground(null);
        super.setOpaque(false);
    }

    // constructors -- end

    public void setTextAndQuoteBubbleLayoutManager() {

        super.setLayout(
                new MigLayout(
                        "", // Layout Constraints
                        "[fill,grow]", // Column constraints
                        "[fill,grow][fill,grow]" // Row constraints
                        ));
    }

    public void addQuote() {

        if (messageModel.getQuotedMessage() != null) {

            CustomQuoteBubblePanel customQuoteBubblePanel =
                    new CustomQuoteBubblePanel(mainFrame, messageModel.getQuotedMessage());

            customQuoteBubblePanel.addTopNameAndTimeTextPane();
            customQuoteBubblePanel.setQuoteBubbleLayoutManager();
            customQuoteBubblePanel.extractQuotedMessage();

            super.add(customQuoteBubblePanel, "cell 0 0");
        }
    }

    public void addTextMessage() {

        CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane(mainFrame);
        customSimpleTextPane.replaceEmojiDescriptionWithActualImageIcon(messageModel.getMessage());

        super.add(customSimpleTextPane, "cell 0 1");
    }
}
