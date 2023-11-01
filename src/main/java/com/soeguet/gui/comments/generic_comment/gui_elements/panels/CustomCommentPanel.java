package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.factories.LinkBubbleFactory;
import com.soeguet.gui.comments.generic_comment.factories.PictureBubbleFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomPicturePane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.util.ChatBubblePaintHandler;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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

        this.setMainContainerLayoutManager();
    }

    private void setThisComponentLayoutManager() {

        this.setLayout(new OverlayLayout(this));
    }

    private void setMainContainerLayoutManager() {

        // wraps the side panel and the content container

        /*

        SCHEMA: MAIN CONTAINER -- side panel and content container

        ">>" [side panel][content container] "<<"

         */


    }

    public void addComponents() {

        /*
        cell 1 1 1 2
        colum 1, row 1, span col 1, span row 2
         */

        // init setup
        final JPanel mainPanel = this.getCustomMainWrapperContainer();
        final JPanel topPanel = this.getTopContainer();
        final CommentSidePanel commentSidePanel = this.getSidePanel();
        final JPanel mainContentPanel = this.getCustomContentContainer();

        //container components
        this.add(mainPanel);
        this.add(topPanel);

        switch (this.getSide()) {
            case LEFT -> {
                mainPanel.add(commentSidePanel, "cell 0 0 1 2, dock west");
                mainPanel.add(mainContentPanel, "cell 1 0 1 2, dock west, gapleft 5, gapright 50");
                topPanel.setAlignmentX(0.0f);
            }
            case RIGHT -> {
                mainPanel.add(commentSidePanel, "cell 1 0 1 2, dock east");
                mainPanel.add(mainContentPanel, "cell 0 0 1 2, dock east, gapright 5, gapleft 50, grow");
            }
        }

        this.getCustomContentContainer().addComponentListener(new ComponentAdapter() {

            // overrides -- start
            @Override
            public void componentResized(final ComponentEvent e) {

                getTopContainer().setMaximumSize(new Dimension(getCustomContentContainer().getSize().width + getSidePanel().getWidth(), getCustomContentContainer().getSize().height));
                super.componentResized(e);
            }
        });
    }

    public void addContext() {

        //setup
        final CustomContentContainer contentContainer = this.getCustomContentContainer();
        final JPanel mainPanelContainer = this.getCustomMainWrapperContainer();

        switch (commentType) {

            case LEFT_TEXT, RIGHT_TEXT -> {

                CustomTextPane customTextPane = new CustomTextPane(true, (MessageModel) baseModel);
                customTextPane.create();

                final String layoutConstraints = determineTextPanePaddings();

                contentContainer.add(customTextPane, layoutConstraints);
            }

            case LEFT_PICTURE, RIGHT_PICTURE -> {

                CustomPicturePane pictureLabel = new PictureBubbleFactory(baseModel).create();

                switch (this.getSide()) {
                    case LEFT -> {
                        contentContainer.add(pictureLabel,
                                             "cell 0 0, wrap, gapleft 15, gaptop 10, " + "gapright 5, " + "grow 1.1");
                    }
                    case RIGHT -> {
                        contentContainer.add(pictureLabel,
                                             "cell 0 0, wrap, gapleft 3, gapright 15, " + "gaptop 10, " + "grow 1.1");
                    }
                }
                contentContainer.setSize(pictureLabel.getSize().width * 2, pictureLabel.getSize().height * 2);
                mainPanelContainer.setSize(pictureLabel.getSize().width * 2, pictureLabel.getSize().height * 2);
                this.setSize(pictureLabel.getSize().width * 2, pictureLabel.getSize().height * 2);
                contentContainer.revalidate();
                contentContainer.repaint();
                this.mainFrame.revalidate();
                this.mainFrame.repaint();
            }

            case LEFT_LINK, RIGHT_LINK -> {

                CustomLinkPanel customLinkPanel = new LinkBubbleFactory(baseModel).create();
                switch (this.getSide()) {
                    case LEFT -> {
                        contentContainer.add(customLinkPanel, "cell 0 0, gapleft 15, gaptop 10, gapright 5");
                    }
                    case RIGHT -> {
                        contentContainer.add(customLinkPanel, "cell 0 0, gapleft 3, gapright 15, gaptop 10");
                    }
                }
            }
        }

    }

    private String determineTextPanePaddings() {

        String layoutConstraints;

        switch (this.getSide()) {
            case LEFT -> {
                layoutConstraints = "gapleft 15, grow 1.0";
            }
            case RIGHT -> {
                layoutConstraints = "gapright 15, grow 1.0";
            }
            default -> throw new IllegalStateException("Unexpected value: %s".formatted(this.getSide()));
        }

        return "%s, gaptop 10".formatted(layoutConstraints);
    }

    public void paintChatBubble() {

        ChatBubblePaintHandler chatBubblePaintHandler = new ChatBubblePaintHandler(getCustomContentContainer(),
                                                                                   getSide(), new Color(23, 0, 146));

        chatBubblePaintHandler.setupChatBubble();
    }

    public void setSide() {

        switch (this.getCommentType()) {

            case LEFT_TEXT, LEFT_PICTURE, LEFT_LINK -> this.side = Side.LEFT;

            case RIGHT_TEXT, RIGHT_PICTURE, RIGHT_LINK -> this.side = Side.RIGHT;
        }
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

    public JPanel getTopContainer() {

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