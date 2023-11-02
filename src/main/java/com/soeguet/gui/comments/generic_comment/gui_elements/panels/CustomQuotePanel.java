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
    private final MessageModel messageModel;
    // variables -- end

    // constructors -- start
    public CustomQuotePanel(final BaseModel baseModel) {

        this.messageModel = (MessageModel) baseModel;
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

        CustomQuoteTopLabel customQuotedTextPane = new CustomQuoteTopLabel(messageModel);
        customQuotedTextPane.create();
        super.add(customQuotedTextPane, "cell 0 0");

        // TODO: 01.11.23 this needs to be rather complex to determine, whether the quoted text is a link or a text
        JTextPane jTextPane = new JTextPane();
        jTextPane.setEditorKit(new WrapEditorKit());
        // FIXME: 02.11.23
        jTextPane.setText("messageModel.getQuotedMessageText())");
        jTextPane.setEnabled(false);
        super.add(jTextPane, "cell 0 1");
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