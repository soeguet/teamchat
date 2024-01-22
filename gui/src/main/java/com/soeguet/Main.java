package com.soeguet;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.factories.MainChatPanelFactory;
import com.soeguet.generic_comment.gui_elements.panels.CustomCommentPanel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

// this file is only for development purposes and is not part of the actual application
public class Main {

    public static void main(String[] args) {

        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(600, 800);
        testFrame.setVisible(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        scrollPane.setViewportView(panel);

        testFrame.add(scrollPane);

        for (int i = 0; i < 5; i++) {

            addRandomComment(10, panel);
        }

        testFrame.revalidate();
        testFrame.repaint();

    }

    private static void addRandomComment(int commentCount, JPanel panel) {

        String[] bookLines = {

                "It was the best of times, it was the worst of times", "Call me Ishmael", "The quick brown fox jumps " +
                                                                                          "over the lazy dog", "To be" +
                                                                                                               " or " +
                                                                                                               "not " +
                                                                                                               "to be, that is the question", "A long time ago in a galaxy far, far away", "Elementary, my dear Watson", "You can't handle the truth", "May the Force be with you", "I think, therefore I am", "To have and have not", "A tale of two cities", "The call of the wild", "The sun also rises", "The grapes of wrath", "The sound and the fury"};

        ArrayList<String> randomLines = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            int index = random.nextInt(bookLines.length);
            randomLines.add(bookLines[index]);
        }

        // Print random lines to check
        for (int i = 0; i < commentCount; i++) {

            // Print messages to check
            MessageModel messageModel = new MessageModel();
            messageModel.setMessage(randomLines.get(i));
            messageModel.setSender("test" + i);
            messageModel.setTime("12:00");
            final CustomCommentPanel customCommentPanel = new MainChatPanelFactory(messageModel,
                                                                                   CommentTypeEnum.RIGHT_TEXT).create();

            panel.add(customCommentPanel, "width 95%, wrap, align right");
        }

    }
}