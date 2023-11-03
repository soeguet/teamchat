package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.labels.CustomQuoteTopLabel;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CustomQuotePanel extends JPanel {

    // variables -- start
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public CustomQuotePanel(final BaseModel baseModel) {

        this.baseModel = baseModel;
    }
    // constructors -- end

    public void setLayoutManager() {

        /*

        SCHEMA: QUOTE PANEL

        [name - time]
        [quoted text]

        */

        super.setLayout(new MigLayout("",
                                      //columns
                                      "[]",
                                      //rows
                                      "[][]"));
    }

    public void createQuotedTextPane() {
        // TODO: 01.11.23 this needs to be rather complex to determine, whether the quoted text is a link or a text

        if (baseModel instanceof MessageModel messageModel) {

            //TODO change this to be a TextPane?
            CustomQuoteTopLabel customQuotedTextPane = new CustomQuoteTopLabel(messageModel);
            customQuotedTextPane.create();
            super.add(customQuotedTextPane, "cell 0 0");

            JTextPane jTextPane = new JTextPane();
            jTextPane.setEditorKit(new WrapEditorKit());

            jTextPane.setText("messageModel.getQuotedMessageText())");
            jTextPane.setEnabled(false);
            super.add(jTextPane, "cell 0 1");
        }
    }

    // overrides -- start
    @Override
    protected void paintComponent(final Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 10));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawLine(0, 0, 0, this.getHeight() - 1);
        g2d.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1);
    }
    // overrides -- end
}