package com.soeguet.comments;

import com.soeguet.comments.interfaces.CommentManager;
import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.factories.MainChatPanelFactory;
import com.soeguet.generic_comment.gui_elements.panels.CustomCommentPanel;
import com.soeguet.generic_comment.util.Side;
import com.soeguet.generic_comment.util.SideHandler;
import com.soeguet.main_frame.ChatMainFrameImpl;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.LinkModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;

public class CommentManagerImpl implements CommentManager {

    // constructors -- start
    public CommentManagerImpl() {

    }
    // constructors -- end

    private void addCommentPanelToMainChatPanel(final JPanel commentPanel, final CommentTypeEnum commentType) {

        final Side side = new SideHandler().determineSide(commentType);
        final ChatMainFrameImpl mainFrame = ChatMainFrameImpl.getMainFrameInstance();

        /*
        wrap -> else all fields will not stack on top of each other
        width -> else each panel will grow endlessly -> can't go past 95%
        align right -> else the panel will be aligned left
         */

        switch (side) {
            case LEFT -> {
                final String constraintsLeft = "width 95%, wrap";
                mainFrame.getMainTextPanel().add(commentPanel, constraintsLeft);
            }
            case RIGHT -> {
                final String constraintsRight = "width 95%, wrap, align right";
                mainFrame.getMainTextPanel().add(commentPanel, constraintsRight);
            }
        }
    }

    private void repaintMainFrame() {

        final ChatMainFrameImpl mainFrame = ChatMainFrameImpl.getMainFrameInstance();

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private boolean doesClientMatchSender(final String messageModel) {

        final ChatMainFrameImpl mainFrame = ChatMainFrameImpl.getMainFrameInstance();

        return mainFrame.getUsername().equals(messageModel);
    }

    /**
     Categorizes messages received from a socket based on the given BaseModel.

     @param baseModel
     the BaseModel representing the message received from the socket

     @return an integer value representing the category of the message
     */
    @Override
    public CommentTypeEnum categorizeMessageFromSocket(final BaseModel baseModel) {

        // TEST this
        if (baseModel instanceof MessageModel messageModel) {

            if (doesClientMatchSender(messageModel.getSender())) {

                return CommentTypeEnum.RIGHT_TEXT;

            } else {

                return CommentTypeEnum.LEFT_TEXT;
            }

        } else if (baseModel instanceof PictureModel pictureModel) {

            if (doesClientMatchSender(pictureModel.getSender())) {

                return CommentTypeEnum.RIGHT_PICTURE;

            } else {

                return CommentTypeEnum.LEFT_PICTURE;
            }

        } else if (baseModel instanceof LinkModel linkModel) {

            if (doesClientMatchSender(linkModel.getSender())) {

                return CommentTypeEnum.RIGHT_LINK;

            } else {

                return CommentTypeEnum.LEFT_LINK;
            }

        } else {

            throw new IllegalArgumentException("The baseModel is null!");
        }
    }

    /**
     Sets up a message on the main chat panel based on the given MessageHandlerDTO. Core Method!
     */
    @Override
    public void setupMessage(final BaseModel baseModel, final CommentTypeEnum commentType) {

        final CustomCommentPanel customCommentPanel = new MainChatPanelFactory(baseModel, commentType).create();

        // finally the created chat bubble panel is added to the main frame
        this.addCommentPanelToMainChatPanel(customCommentPanel, commentType);

        repaintMainFrame();
    }
}