package com.soeguet.gui.comments;

import com.soeguet.gui.comments.generic_comment.dto.CommentGuiDTO;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CommentMainPanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CommentSidePanel;
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
            case LEFT -> this.mainFrame.getMainTextPanel().add(commentPanel, "w 70%, leading, wrap");
            case RIGHT -> this.mainFrame.getMainTextPanel().add(commentPanel, "w 70%, trailing, wrap");
        }
    }

    /**
     Displays the nickname instead of the timeAndUsername in a comment.
     <p>
     If the nickname parameter is not null and not empty after trimming, it sets the text of the name label in the
     comment to the nickname.

     @param nickname
     the nickname to be displayed
     @param comment
     the comment object containing the name label
     */
    private void displayNicknameInsteadOfUsername(String nickname, CommentInterface comment) {

        if (nickname != null && !nickname.trim().isEmpty()) {
            comment.getNameLabel().setText(nickname);
            comment.getNameLabel().revalidate();
            comment.getNameLabel().repaint();
        }
    }

    private Color determineBorderColor(String sender) {

        if (this.mainFrame.getChatClientPropertiesHashMap().containsKey(sender)) {

            return new Color(this.mainFrame.getChatClientPropertiesHashMap().get(sender).getBorderColor());
        }

        return new Color(generateRandomRgbIntValue());
    }

    private void addMessagePanelToMainChatPanel(JPanel message, String alignment) {

        this.mainFrame.getMainTextPanel().add(message, "w 70%, " + alignment + ", wrap");
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

        }
    }

    @Override
    public void setupLinkRightSite(final BaseModel baseModel) {

        //LINK - RIGHT
        if (baseModel instanceof MessageModel messageModel) {

            LinkPanelInterface linkRight = new LinkRightImpl(this.mainFrame);
            linkRight.setCursorOnLink(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            //create and set up the editor pane for the link
            final JEditorPane jEditorPane = linkRight.createEditorPaneForLinks(messageModel);

            linkRight.addHyperlinkListener(jEditorPane);
            linkRight.setBorderColor(determineBorderColor("own"));

            linkRight.implementComment(jEditorPane);
            linkRight.setupTimeField(messageModel);

            addMessagePanelToMainChatPanel((JPanel) linkRight, "trailing");

            repaintMainFrame();
        }
    }

    @Override
    public void setupPicturesRightSide(final BaseModel baseModel, final String nickname) {

        //PICTURE - RIGHT
        if (baseModel instanceof PictureModel pictureModel) {

            Color borderColor = determineBorderColor("own");

            CommentInterface panelRight = new PanelRightImpl(this.mainFrame, pictureModel);
            this.mainFrame.getCommentsHashMap().put(pictureModel.getId(), panelRight);
            panelRight.setupPicturePanelWrapper();
            panelRight.setBorderColor(borderColor);
            displayNicknameInsteadOfUsername(nickname, panelRight);
            addMessagePanelToMainChatPanel((JPanel) panelRight, "trailing");
        }
    }

    @Override
    public void setupPicturesLeftSide(final BaseModel baseModel, final String nickname) {

        //PICTURE - LEFT
        if (baseModel instanceof PictureModel pictureModel) {

            Color borderColor = determineBorderColor(pictureModel.getSender());

            CommentInterface panelLeft = new PanelLeftImpl(this.mainFrame, pictureModel);
            this.mainFrame.getCommentsHashMap().put(pictureModel.getId(), panelLeft);
            panelLeft.setupPicturePanelWrapper();
            panelLeft.setBorderColor(borderColor);
            displayNicknameInsteadOfUsername(nickname, panelLeft);
            addMessagePanelToMainChatPanel((JPanel) panelLeft, "leading");
        }
    }

    @Override
    public void setupLinkLeftSide(final BaseModel baseModel) {

        //LINK - LEFT
        if (baseModel instanceof MessageModel messageModel) {

            LinkPanelInterface linkLeft = new LinkLeftImpl(this.mainFrame);
            linkLeft.setCursorOnLink(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            //create and set up the editor pane for the link
            final JEditorPane jEditorPane = linkLeft.createEditorPaneForLinks(messageModel);

            linkLeft.addHyperlinkListener(jEditorPane);
            linkLeft.setBorderColor(determineBorderColor(messageModel.getSender()));

            linkLeft.implementComment(jEditorPane);
            linkLeft.setupTimeField(messageModel);

            addMessagePanelToMainChatPanel((JPanel) linkLeft, "leading");

            repaintMainFrame();
        }
    }

    @Override
    public void setupMessage(final MessageHandlerDTO messageHandlerDTO) {

        //TODO WORKING ON THIS
        //FEATURE WORKING ON THIS
        BaseModel baseModel = messageHandlerDTO.baseModel();
        CommentTypeEnum commentType = messageHandlerDTO.commentType();

        CommentSidePanel commentSidePanel = new CommentSidePanel(commentType);
        final CommentGuiDTO commentGuiDto = new CommentGuiDTO(mainFrame, commentType, baseModel, commentSidePanel,
                                                              new TransparentTopPanel(), new CustomContentContainer()
                , new JPanel());
        CommentMainPanel commentPanel = new CommentMainPanel(commentGuiDto);

        commentPanel.setLayoutManager();
        commentPanel.setupSidePanel(baseModel);
        commentPanel.addComponents();
        commentPanel.addContext();
        commentPanel.paintChatBubble();
        this.addCommentPanelToMainChatPanel(commentPanel, commentType);
        repaintMainFrame();

    }
    // overrides -- end
}