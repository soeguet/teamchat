package com.soeguet.gui.comments;

import com.soeguet.gui.comments.generic_comment.dto.CommentGuiDTO;
import com.soeguet.gui.comments.generic_comment.factories.CommentPanelFactory;
import com.soeguet.gui.comments.generic_comment.factories.ContentContainerFactory;
import com.soeguet.gui.comments.generic_comment.factories.SidePanelFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomCommentPanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomContentContainer;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.TransparentTopPanel;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.comments.interfaces.LinkPanelInterface;
import com.soeguet.gui.comments.left.LinkLeftImpl;
import com.soeguet.gui.comments.left.PanelLeftImpl;
import com.soeguet.gui.comments.right.LinkRightImpl;
import com.soeguet.gui.comments.right.PanelRightImpl;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.main_panel.dtos.MessageHandlerDTO;
import com.soeguet.model.MessageTypes;
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

        switch (side) {
            case LEFT -> this.mainFrame.getMainTextPanel().add(commentPanel, "wrap, width 70%");
            case RIGHT -> this.mainFrame.getMainTextPanel().add(commentPanel, "wrap, width 70%, align right");
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

                if (mainFrame.getUsername().equals(messageModel.getSender())) {

                    if (messageModel.getMessageType() == MessageTypes.LINK) {

                        return CommentTypeEnum.RIGHT_LINK;

                    } else {

                        return CommentTypeEnum.RIGHT_TEXT;
                    }

                } else {

                    if (messageModel.getMessageType() == MessageTypes.LINK) {

                        return CommentTypeEnum.LEFT_LINK;

                    } else {

                        return CommentTypeEnum.LEFT_TEXT;
                    }
                }
            }

            case PictureModel pictureModel -> {

                if (mainFrame.getUsername().equals(pictureModel.getSender())) {

                    return CommentTypeEnum.RIGHT_PICTURE;

                } else {

                    return CommentTypeEnum.LEFT_PICTURE;
                }
            }

            case LinkModel link ->{

                throw new RuntimeException("LinkModel not supported yet");
            }
        }
    }

    @Override
    public void setupMessage(final MessageHandlerDTO messageHandlerDTO) {

        CustomCommentPanel customCommentPanel = new CommentPanelFactory(mainFrame, messageHandlerDTO).create();
        this.addCommentPanelToMainChatPanel(customCommentPanel, messageHandlerDTO.commentType());

        repaintMainFrame();
    }
    // overrides -- end
}