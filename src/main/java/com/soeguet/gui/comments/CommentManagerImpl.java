package com.soeguet.gui.comments;

import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.comments.interfaces.LinkPanelInterface;
import com.soeguet.gui.comments.left.PanelLeftImpl;
import com.soeguet.gui.comments.right.LinkRightImpl;
import com.soeguet.gui.comments.right.PanelRightImpl;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;
import com.soeguet.util.MessageCategory;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CommentManagerImpl implements CommentManager {

    private final MainFrameInterface mainFrame;

    public CommentManagerImpl(final MainFrameInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    @Override
    public void setupLinkRightSite(final BaseModel baseModel) {

        if (baseModel instanceof MessageModel messageModel) {

            LinkPanelInterface linkRight = new LinkRightImpl(this.mainFrame);

            //create and set up the editor pane for the link
            final JEditorPane jEditorPane = linkRight.createEditorPaneForLinks(messageModel);

            linkRight.addHyperlinkListener(jEditorPane);
            linkRight.setBorderColor(determineBorderColor("own"));

            linkRight.implementComment(jEditorPane);

            addMessagePanelToMainChatPanel((JPanel) linkRight, "trailing");

            repaintMainFrame();
        }
    }

    @Override
    public int categorizeMessageFromSocket(final BaseModel baseModel) {

        switch (baseModel) {

            case MessageModel messageModel -> {

                if (messageModel.getSender().equals("own")) {

                    if (messageModel.getMessageType() == MessageTypes.LINK) {

                        return MessageCategory.RIGHT_SIDE_LINK_MESSAGE;

                    } else {

                        return MessageCategory.RIGHT_SIDE_TEXT_MESSAGE;
                    }

                } else {

                    if (messageModel.getMessageType() == MessageTypes.LINK) {

                        return MessageCategory.LEFT_SIDE_LINK_MESSAGE;

                    } else {

                        return MessageCategory.LEFT_SIDE_TEXT_MESSAGE;
                    }
                }
            }

            case PictureModel pictureModel -> {

                if (pictureModel.getSender().equals("own")) {

                    return MessageCategory.RIGHT_SIDE_PICTURE_MESSAGE;

                } else {

                    return MessageCategory.LEFT_SIDE_PICTURE_MESSAGE;
                }
            }

        }
    }

    @Override
    public void setupPicturesRightSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor("own");

        CommentInterface panelRight = new PanelRightImpl(this.mainFrame, messageModel);
        this.mainFrame.getCommentsHashMap().put(messageModel.getId(), panelRight);
        panelRight.setupPicturePanelWrapper();
        panelRight.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelRight);
        addMessagePanelToMainChatPanel((JPanel) panelRight, "trailing");
    }

    @Override
    public void setupMessagesLeftSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(messageModel.getSender());

        CommentInterface panelLeft = new PanelLeftImpl(this.mainFrame, messageModel);
        this.mainFrame.getCommentsHashMap().put(messageModel.getId(), panelLeft);
        panelLeft.setupTextPanelWrapper();
        panelLeft.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelLeft);
        addMessagePanelToMainChatPanel((JPanel) panelLeft, "leading");
    }

    @Override
    public void setupPicturesLeftSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor(messageModel.getSender());

        CommentInterface panelLeft = new PanelLeftImpl(this.mainFrame, messageModel);
        this.mainFrame.getCommentsHashMap().put(messageModel.getId(), panelLeft);
        panelLeft.setupPicturePanelWrapper();
        panelLeft.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelLeft);
        addMessagePanelToMainChatPanel((JPanel) panelLeft, "leading");
    }

    @Override
    public void setupLinkLeftSide(final BaseModel baseModel) {

    }

    @Override
    public void setupMessagesRightSide(final BaseModel messageModel, final String nickname) {

        Color borderColor = determineBorderColor("own");

        CommentInterface panelRight = new PanelRightImpl(this.mainFrame, messageModel);
        this.mainFrame.getCommentsHashMap().put(messageModel.getId(), panelRight);
        panelRight.setupTextPanelWrapper();
        panelRight.setBorderColor(borderColor);
        displayNicknameInsteadOfUsername(nickname, panelRight);
        addMessagePanelToMainChatPanel((JPanel) panelRight, "trailing");
    }
    /**
     Displays the nickname instead of the username in a comment.

     If the nickname parameter is not null and not empty after trimming,
     it sets the text of the name label in the comment to the nickname.

     @param nickname the nickname to be displayed
     @param comment  the comment object containing the name label
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

        return new Color(getRandomRgbIntValue());
    }

    private void addMessagePanelToMainChatPanel(JPanel message, String alignment) {

        this.mainFrame.getMainTextPanel().add(message, "w 70%, " + alignment + ", wrap");
    }

    private void repaintMainFrame() {

        ((JFrame) this.mainFrame).revalidate();
        ((JFrame) this.mainFrame).repaint();
    }

    private int getRandomRgbIntValue() {

        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b).getRGB();
    }
}