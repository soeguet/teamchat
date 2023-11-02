package com.soeguet.gui.comments;

import com.soeguet.gui.comments.generic_comment.factories.CommentPanelFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomCommentPanel;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.main_panel.dtos.MessageHandlerDTO;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CommentManagerImpl implements CommentManager {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    // variables -- end

    // constructors -- start
    public CommentManagerImpl(final MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }
    // constructors -- end

    private void addCommentPanelToMainChatPanel(final JPanel commentPanel, final CommentTypeEnum commentType) {

        Side side = new SideHandler().determineSide(commentType);

        /*
        wrap -> else all fields will not stack on top of each other
        width -> else each panel will grow endlessly -> can't go past 95%
        align right -> else the panel will be aligned left
         */

        switch (side) {
            case LEFT -> {
                final String constraintsLeft = "width 95%, wrap";
                this.mainFrame.getMainTextPanel().add(commentPanel, constraintsLeft);
            }
            case RIGHT -> {
                final String constraintsRight = "width 95%, wrap, align right";
                this.mainFrame.getMainTextPanel().add(commentPanel, constraintsRight);
            }
        }
    }

    private void repaintMainFrame() {

        this.mainFrame.revalidate();
        this.mainFrame.repaint();
    }

    private int generateRandomRgbIntValue() {

        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b).getRGB();
    }

    // overrides -- start

    /**
     Categorizes messages received from a socket based on the given BaseModel.

     @param baseModel
     the BaseModel representing the message received from the socket

     @return an integer value representing the category of the message
     */
    @Override
    public CommentTypeEnum categorizeMessageFromSocket(final BaseModel baseModel) {

        //TEST this

        switch (baseModel) {

            case MessageModel messageModel -> {

                if (doesClientMatchSender(messageModel.getSender())) {

                    return CommentTypeEnum.RIGHT_TEXT;

                } else {

                    return CommentTypeEnum.LEFT_TEXT;
                }
            }

            case PictureModel pictureModel -> {

                if (doesClientMatchSender(pictureModel.getSender())) {

                    return CommentTypeEnum.RIGHT_PICTURE;

                } else {

                    return CommentTypeEnum.LEFT_PICTURE;
                }
            }

            case LinkModel linkModel -> {

                if (doesClientMatchSender(linkModel.getSender())) {

                    return CommentTypeEnum.RIGHT_LINK;

                } else {

                    return CommentTypeEnum.LEFT_LINK;
                }
            }
        }
    }

    private boolean doesClientMatchSender(final String messageModel) {

        return mainFrame.getUsername().equals(messageModel);
    }

    @Override
    public void setupMessage(final MessageHandlerDTO messageHandlerDTO) {

        CustomCommentPanel customCommentPanel = new CommentPanelFactory(mainFrame, messageHandlerDTO).create();
        this.addCommentPanelToMainChatPanel(customCommentPanel, messageHandlerDTO.commentType());

        repaintMainFrame();
    }
    // overrides -- end
}