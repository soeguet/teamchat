package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomLinkTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.*;
import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class CustomQuoteBubblePanel extends JPanel {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final QuoteModel<? extends BaseModel> quoteModel;

    // variables -- end

    // constructors -- start
    public CustomQuoteBubblePanel(
            MainFrameGuiInterface mainFrame, final QuoteModel<? extends BaseModel> quoteModel) {

        this.mainFrame = mainFrame;

        this.quoteModel = quoteModel;
        super.setBackground(new Color(240, 240, 240, 255));
        super.setOpaque(true);
    }

    // constructors -- end

    public void setQuoteBubbleLayoutManager() {

        /*

        SCHEMA: CustomQuoteBubblePanel

        [NAME - TIME ]
        [QUOTED STUFF]
         */

        super.setLayout(
                new MigLayout(
                        "", // Layout Constraints
                        "[fill]", // Column constraints
                        "[fill][fill]" // Row constraints
                        ));
    }

    public void addTopNameAndTimeTextPane() {

        final BaseModel baseModel = quoteModel.t();

        CustomSimpleTextPane nameAndTimeTextPane = new CustomSimpleTextPane(mainFrame);
        // modifications -> maybe custom class
        nameAndTimeTextPane.setEnabled(false);
        nameAndTimeTextPane.setFont(
                new Font(nameAndTimeTextPane.getFont().getName(), Font.ITALIC, 10));

        nameAndTimeTextPane.setText(baseModel.getSender() + " - " + baseModel.getTime());

        super.add(nameAndTimeTextPane, "cell 0 0");
    }

    public void extractQuotedMessage() {

        final BaseModel baseModel = quoteModel.t();

        switch (baseModel) {
            case MessageModel messageModel -> {
                CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane(mainFrame);
                customSimpleTextPane.replaceEmojiDescriptionWithActualImageIcon(
                        messageModel.getMessage());

                super.add(customSimpleTextPane, "cell 0 1");
            }

            case PictureModel pictureModel -> {
                CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane(mainFrame);
                customSimpleTextPane.replaceEmojiDescriptionWithActualImageIcon(
                        pictureModel.getDescription());

                super.add(customSimpleTextPane, "cell 0 1");
            }

            case LinkModel linkModel -> {
                CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(linkModel);
                customLinkTextPane.create();

                super.add(customLinkTextPane, "cell 0 1");
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.DARK_GRAY);

        final int maxWidth = super.getWidth() - 1;
        final int maxHeight = super.getHeight();

        g2d.drawLine(0, 0, 0, maxHeight);
        g2d.drawLine(maxWidth, 0, maxWidth, maxHeight);
    }
}
