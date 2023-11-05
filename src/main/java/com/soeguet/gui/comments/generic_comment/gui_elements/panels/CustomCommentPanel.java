package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.factories.LinkPanelFactory;
import com.soeguet.gui.comments.generic_comment.factories.PicturePanelFactory;
import com.soeguet.gui.comments.generic_comment.factories.TextMessageFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.util.ChatBubblePaintHandler;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;
import java.awt.*;

public class CustomCommentPanel extends JPanel {

    // variables -- start
    private MainFrameGuiInterface mainFrame;
    /**
     Represents a side of the comment - LEFT or RIGHT.
     */
    private Side side;
    /**
     Represents the main information Model of this app.

     @see BaseModel
     */
    private BaseModel baseModel;
    private CommentTypeEnum commentType;
    private CommentSidePanel sidePanel;
    /**
     The top level container will be used for MouseEvents as well as reaction emojis.

     @see JPanel
     */
    private TransparentTopPanel topContainer;
    /**
     The main container panel will be used for the side panel as well as the content container.
     */
    private CustomMainWrapperContainer customMainWrapperContainer;
    /**
     The content container will be used for the text and picture content as well as the quoted comments.
     */
    private CustomContentContainer customContentContainer;
    private JTextPane jTextPane;
    private Color borderColor;
    // variables -- end

    // constructors -- start
    public CustomCommentPanel() {

    }
    // constructors -- end

    public void setLayoutManager() {

        this.setThisComponentLayoutManager();
    }

    private void setThisComponentLayoutManager() {

        this.setLayout(new OverlayLayout(this));
    }

    public void addComponents() {

        /*
        CONSTRAINS EXPLAINED
                cell 1 1 1 2
                colum 1, row 1, span col 1, span row 2
         */

        // MainPanel - MigLayout - wraps SIDE and CONTENT - wrapped by CustomCommentPanel (OverlayLayout)
        final CustomMainWrapperContainer mainPanel = this.getCustomMainWrapperContainer();

        // wrapped by CustomCommentPanel (OverlayLayout)
        final TransparentTopPanel topPanel = this.getTopContainer();

        // wrapped by CustomMainWrapperContainer -- SIDE
        final CommentSidePanel commentSidePanel = this.getSidePanel();

        // wrapped by CustomMainWrapperContainer -- CONTENT
        final CustomContentContainer mainContentPanel = this.getCustomContentContainer();
        mainContentPanel.setTopPanelReference(topPanel); //gets a reference to the TopPanel (resizing events!)

        switch (this.getSide()) {

            case LEFT -> {

                //SIDE
                mainPanel.add(commentSidePanel, "cell 0 0 1 2, dock west");

                //CONTENT
                mainPanel.add(mainContentPanel, "cell 1 0 1 2, dock west, gapleft 5, gapright 50");
                topPanel.setAlignmentX(0.0f);
            }

            case RIGHT -> {

                //SIDE
                mainPanel.add(commentSidePanel, "cell 1 0 1 2, dock east");

                //CONTENT
                mainPanel.add(mainContentPanel, "cell 0 0 1 2, dock east, gapright 5, gapleft 50, grow");
            }
        }

        // this == CustomCommentPanel == Container with OverlayLayout (!) -> wraps TopPanel and MainPanel
        this.add(mainPanel);
        this.add(topPanel);
    }

    public void addContext() {

        //setup
        final CustomContentContainer contentContainer = this.getCustomContentContainer();
        final Side commentSide = this.getSide();

        switch (commentType) {

            case LEFT_TEXT, RIGHT_TEXT -> processIncomingTextMessage(commentSide, contentContainer);

            case LEFT_PICTURE, RIGHT_PICTURE -> processIncomingPictureMessage(commentSide, contentContainer);

            case LEFT_LINK, RIGHT_LINK -> processIncomingLinkMessage(commentSide, contentContainer);
        }

    }

    private void processIncomingLinkMessage(final Side commentSide, final CustomContentContainer contentContainer) {

        CustomLinkPanel customLinkPanel = new LinkPanelFactory(mainFrame,baseModel).create();

        switch (commentSide) {

            case LEFT -> {

                final String leftConstraints = "cell 0 0, gapleft 15, gapright 5";
                contentContainer.add(customLinkPanel, leftConstraints);
            }

            case RIGHT -> {

                final String rightConstraints = "cell 0 0, gapleft 3, gapright 15";
                contentContainer.add(customLinkPanel, rightConstraints);
            }
        }
    }

    private void processIncomingPictureMessage(final Side commentSide, final CustomContentContainer contentContainer) {

        CustomPictureWrapperPanel pictureLabel = new PicturePanelFactory(mainFrame,baseModel).create();

        switch (commentSide) {

            case LEFT -> {

                final String leftConstraints = "cell 0 0, wrap, gapleft 15, gapright 5, grow 1.1";
                contentContainer.add(pictureLabel, leftConstraints);
            }

            case RIGHT -> {

                final String rightConstraints = "cell 0 0, wrap, gapleft 3, gapright 15, grow 1.1";
                contentContainer.add(pictureLabel, rightConstraints);
            }
        }
    }

    private void processIncomingTextMessage(final Side commentSide, final CustomContentContainer contentContainer) {

        //TODO factory method maybe?
        if (baseModel instanceof MessageModel messageModel) {

            CustomTextAndQuoteForBubblePanel customTextAndQuoteForBubblePanel =
                    new TextMessageFactory(mainFrame,messageModel).create();

            switch (commentSide) {

                case LEFT -> {

                    String leftConstraints = "gapleft 15, grow";
                    contentContainer.add(customTextAndQuoteForBubblePanel, leftConstraints);
                }

                case RIGHT -> {

                    String rightConstraints = "gapright 15, grow";
                    contentContainer.add(customTextAndQuoteForBubblePanel, rightConstraints);
                }
            }
        }
    }

    public void paintChatBubble() {

        final Side commentSide = this.getSide();
        // TODO: 02.11.23 border color implementation
        final Color borderColor = new Color(23, 0, 146);
        ChatBubblePaintHandler chatBubblePaintHandler = new ChatBubblePaintHandler(getCustomContentContainer(),
                                                                                   commentSide, borderColor);

        chatBubblePaintHandler.setupChatBubble();
    }

    public void setSide() {

        switch (this.getCommentType()) {

            case LEFT_TEXT, LEFT_PICTURE, LEFT_LINK -> this.side = Side.LEFT;

            case RIGHT_TEXT, RIGHT_PICTURE, RIGHT_LINK -> this.side = Side.RIGHT;
        }
    }

    public void prepareInteractionButtons() {

    }

    // getter & setter -- start
    public BaseModel getBaseModel() {

        return baseModel;
    }

    public void setBaseModel(final BaseModel baseModel) {

        this.baseModel = baseModel;
    }

    public Color getBorderColor() {

        return borderColor;
    }

    public void setBorderColor(final Color borderColor) {

        this.borderColor = borderColor;
    }

    public CommentTypeEnum getCommentType() {

        return commentType;
    }

    public void setCommentType(final CommentTypeEnum commentType) {

        this.commentType = commentType;
    }

    public CustomContentContainer getCustomContentContainer() {

        return customContentContainer;
    }

    public void setCustomContentContainer(final CustomContentContainer customContentContainer) {

        this.customContentContainer = customContentContainer;
    }

    public CustomMainWrapperContainer getCustomMainWrapperContainer() {

        return customMainWrapperContainer;
    }

    public void setCustomMainWrapperContainer(final CustomMainWrapperContainer customMainWrapperContainer) {

        this.customMainWrapperContainer = customMainWrapperContainer;
    }

    public JPanel getLayeredContainer() {

        return topContainer;
    }

    public MainFrameGuiInterface getMainFrame() {

        return mainFrame;
    }

    public void setMainFrame(final MainFrameGuiInterface mainFrame) {

        this.mainFrame = mainFrame;
    }

    public Side getSide() {

        return side;
    }

    public CommentSidePanel getSidePanel() {

        return sidePanel;
    }

    public void setSidePanel(final CommentSidePanel sidePanel) {

        this.sidePanel = sidePanel;
    }

    public TransparentTopPanel getTopContainer() {

        return topContainer;
    }

    public void setTopContainer(final TransparentTopPanel topContainer) {

        this.topContainer = topContainer;
    }

    public JTextPane getjTextPane() {

        return jTextPane;
    }

    public void setjTextPane(final JTextPane jTextPane) {

        this.jTextPane = jTextPane;
    }

    public void setTextPane(final JTextPane jTextPane) {

        this.jTextPane = jTextPane;
    }
    // getter & setter -- end
}