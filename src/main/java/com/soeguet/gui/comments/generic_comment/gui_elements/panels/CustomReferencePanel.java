package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomLinkTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomReplyPreviewTopInformationTextPane;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CustomReferencePanel extends JPanel {

    // variables -- start
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public CustomReferencePanel(final BaseModel baseModel) {

        this.baseModel = baseModel;

        this.setBackground(null);
        this.setOpaque(false);
    }
    // constructors -- end

    public void setLayoutManager() {

        /*

        SCHEMA: QUOTE PANEL

        [[name - time]]
        [[quoted text]]

        */

        super.setLayout(new MigLayout("wrap",
                                      //columns
                                      "[fill,grow]",
                                      //rows
                                      "[fill][fill,grow]"));
    }

    public void createQuotedTextPane() {
        // TODO: 01.11.23 this needs to be rather complex to determine, whether the quoted text is a link or a text

        if (baseModel instanceof MessageModel messageModel) {

            //TODO change this to be a TextPane?
            CustomReplyPreviewTopInformationTextPane customQuotedTextPane =
                    new CustomReplyPreviewTopInformationTextPane(messageModel);
            customQuotedTextPane.createQuoteTopTextPane();
            super.add(customQuotedTextPane, "cell 0 0");

            JTextPane jTextPane = new JTextPane();
            jTextPane.setEditorKit(new WrapEditorKit());

            //FIXME: 02.11.23
            //            jTextPane.setText("messageModel.getQuotedMessageText())");
            jTextPane.setEnabled(false);
            super.add(jTextPane, "cell 0 1");
        }
    }

    public void populateReferencePanel() {

        if (baseModel instanceof MessageModel messageModel) {

            JScrollPane jScrollPane = new JScrollPane();
            final JTextPane referenceMessageTextPane = createReferenceMessageTextPane(messageModel);

            jScrollPane.setViewportView(referenceMessageTextPane);
            jScrollPane.setOpaque(false);
            jScrollPane.getViewport().setOpaque(false);
            jScrollPane.setBorder(null);

            super.add(jScrollPane, "cell 0 1, grow, width ::500, h :600:800");

        } else if (baseModel instanceof LinkModel linkModel) {

            JScrollPane jScrollPane = new JScrollPane();

            // LINK
            CustomLinkTextPane customLinkTextPane = new CustomLinkTextPane(linkModel);
            customLinkTextPane.create();

            // COMMENT
            if (!linkModel.getComment().isEmpty()) {

                String textWithComment = """
                        <a href="%s"
                        style="text-decoration:underline; color:blue; font-size:15;">
                        %s
                        </a>
                        <p>%s</p>
                        """.formatted(linkModel.getLink(), linkModel.getLink(), linkModel.getComment());

                customLinkTextPane.setText(textWithComment);
            }

            jScrollPane.setViewportView(customLinkTextPane);
            jScrollPane.setOpaque(false);
            jScrollPane.getViewport().setOpaque(false);
            jScrollPane.setBorder(null);

            super.add(jScrollPane, "cell 0 1, grow, width ::500, h :600:800");
        }
    }

    private JTextPane createReferenceMessageTextPane(final MessageModel messageModel) {

        JTextPane referenceMessageTextPane = new JTextPane();

        referenceMessageTextPane.setEditorKit(new WrapEditorKit());
        referenceMessageTextPane.setText(messageModel.getMessage());

        referenceMessageTextPane.setEnabled(false);
        referenceMessageTextPane.setBackground(null);
        referenceMessageTextPane.setOpaque(false);

        return referenceMessageTextPane;
    }

    public void setupNameAndTimeTopPanel() {

        final JTextPane nameAndTimeTextPane = createNameAndTimeTextPane();
        super.add(nameAndTimeTextPane, "cell 0 0");
    }

    private JTextPane createNameAndTimeTextPane() {

        JTextPane nameAndTimeTextPane = new JTextPane();

        nameAndTimeTextPane.setEditorKit(new WrapEditorKit());
        nameAndTimeTextPane.setText("%s - %s".formatted(baseModel.getSender(), baseModel.getTime()));
        nameAndTimeTextPane.setFont(new Font(nameAndTimeTextPane.getFont().getName(), Font.ITALIC, 11));
        nameAndTimeTextPane.setBackground(null);
        nameAndTimeTextPane.setOpaque(false);
        nameAndTimeTextPane.setEnabled(false);
        nameAndTimeTextPane.setAlignmentY(Component.CENTER_ALIGNMENT);

        return nameAndTimeTextPane;
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