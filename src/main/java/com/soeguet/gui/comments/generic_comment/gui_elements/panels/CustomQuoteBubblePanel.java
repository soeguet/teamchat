package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomLinkTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.model.jackson.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CustomQuoteBubblePanel extends JPanel {

    private final QuoteModel<? extends BaseModel> quoteModel;

    public CustomQuoteBubblePanel(final QuoteModel<? extends BaseModel> quoteModel) {

        this.quoteModel = quoteModel;
        super.setBackground(new Color(240, 240, 240, 255));
        super.setOpaque(true);
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

    public void setQuoteBubbleLayoutManager() {

        super.setLayout(new MigLayout(

                "", // Layout Constraints
                "[fill]", // Column constraints
                "[fill]" // Row constraints
        ));
    }


    public void extractQuotedMessage() {

        final BaseModel baseModel = quoteModel.t();

        switch (baseModel) {
            case MessageModel messageModel -> {

                CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane();
                customSimpleTextPane.setText(messageModel.getMessage());

                super.add(customSimpleTextPane, "cell 0 0");
            }

            case PictureModel pictureModel -> {

                CustomSimpleTextPane customSimpleTextPane = new CustomSimpleTextPane();
                customSimpleTextPane.setText(pictureModel.getDescription());

                super.add(customSimpleTextPane, "cell 0 0");
            }

            case LinkModel linkModel -> {

                CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(linkModel);
                customLinkTextPane.create();

                super.add(customLinkTextPane, "cell 0 0");
            }
        }


    }
}