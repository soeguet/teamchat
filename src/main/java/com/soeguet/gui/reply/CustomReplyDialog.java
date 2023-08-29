package com.soeguet.gui.reply;

import com.soeguet.model.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CustomReplyDialog extends CustomReply{

    private final MessageModel messageModel;
    private JTextPane jTextPane;

    public CustomReplyDialog(MessageModel messageModel) {

        super(null);

        this.messageModel = messageModel;
//        initUi();
    }

    private void initUi() {

        this.setLayout(new MigLayout("debug, fill, gap 5"));

        addComponentsToDialog();
    }

    private void addComponentsToDialog() {

        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.BLUE);
        this.add(jPanel, "cell 0 0, grow, span 3");
        JTextPane jTextPane1 = new JTextPane();
        this.add(jTextPane1, "cell 0 1, grow, span 3");
        JButton imageButton = new JButton("SEND");
        this.add(imageButton, "cell 0 2, grow, height ::40");
        JButton emojiButton = new JButton("SEND");
        this.add(emojiButton, "cell 1 2, grow, height ::40");
        JButton sendButton = new JButton("SEND");
        this.add(sendButton, "cell 2 2, grow, height ::40");
        this.setSize(new Dimension(300,300));
    }
}
