package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.dto.CommentGuiDTO;
import com.soeguet.gui.comments.generic_comment.factories.LinkBubbleFactory;
import com.soeguet.gui.comments.generic_comment.factories.PictureBubbleFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomPicturePane;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomTextPane;
import com.soeguet.gui.comments.generic_comment.gui_elements.util.ChatBubblePaintHandler;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CommentMainPanel extends JPanel {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    /**
     Represents a side of the comment - LEFT or RIGHT.
     */
    private final Side side;
    /**
     Represents the main information Model of this app.

     @see BaseModel
     */
    private final BaseModel baseModel;
    private final CommentSidePanel sidePanel;
    /**
     The top level container will be used for MouseEvents as well as reaction emojis.

     @see JPanel
     */
    private final TransparentTopPanel topContainer;
    /**
     The main container panel will be used for the side panel as well as the content container.
     */
    private final JPanel mainContainer;
    /**
     The content container will be used for the text and picture content as well as the quoted comments.
     */
    private final CustomContentContainer customContentContainer;
    private JTextPane jTextPane;
    private Color borderColor;
    private final CommentTypeEnum commentType;
    // variables -- end

    // constructors -- start
    public CommentMainPanel(CommentGuiDTO commentGuiDTO) {

        this.mainFrame = commentGuiDTO.mainFrame();
        this.commentType = commentGuiDTO.commentType();
        this.side = new SideHandler().determineSide(commentGuiDTO.commentType());
        this.baseModel = commentGuiDTO.baseModel();
        this.sidePanel = commentGuiDTO.sidePanel();
        this.topContainer = commentGuiDTO.topContainer();

        this.customContentContainer = commentGuiDTO.customContentContainer();
        this.mainContainer = commentGuiDTO.mainContainer();
    }
    // constructors -- end

    public void setLayoutManager() {

        this.setThisComponentLayoutManager();

        this.setMainContainerLayoutManager();
        this.setMainContentContainerLayoutManager();
    }

    private void setThisComponentLayoutManager() {

        this.setLayout(new OverlayLayout(this));
    }

    private void setMainContainerLayoutManager() {

        // wraps the side panel and the content container

        final JPanel mainPanel = this.getMainContainer();
        switch (this.getSide()) {
            case LEFT -> mainPanel.setLayout(new MigLayout("",
                                                           // columns
                                                           "[][]",
                                                           // rows
                                                           "[][]"));
            case RIGHT -> mainPanel.setLayout(new MigLayout("",
                                                            // columns
                                                            "[grow][]",
                                                            // rows
                                                            "[][]"));
        }
    }

    private void setMainContentContainerLayoutManager() {

        // wraps the text and picture content as well as the quoted comments

        final JPanel mainContentPanel = this.getCustomContentContainer();

        mainContentPanel.setLayout(new MigLayout("",
                                                 // columns
                                                 "[fill,grow]",
                                                 // rows
                                                 "[][fill][]"));
    }

    public void setupSidePanel(final BaseModel baseModel) {

        // wraps the sender name, time and interaction button

        final CommentSidePanel commentSidePanel = this.getSidePanel();

        commentSidePanel.getNameLabel().setText(baseModel.getSender());
        commentSidePanel.getTimeLabel().setText("10:00");
        commentSidePanel.setLayoutManager();
        commentSidePanel.addComponents();
    }

    public void addComponents() {

        /*
        cell 1 1 1 2
        colum 1, row 1, span col 1, span row 2
         */

        // init setup
        final JPanel mainPanel = this.getMainContainer();
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
        final JPanel mainPanelContainer = this.getMainContainer();

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
                        contentContainer.add(pictureLabel, "cell 0 0, wrap, gapleft 15, gaptop 10, gapright 5");
                    }
                    case RIGHT -> {
                        contentContainer.add(pictureLabel, "cell 0 0, wrap, gapleft 3, gapright 15, gaptop 10");
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
                        contentContainer.add(customLinkPanel, "cell 0 0, wrap, gapleft 15, gaptop 10, gapright 5");
                    }
                    case RIGHT -> {
                        contentContainer.add(customLinkPanel, "cell 0 0, wrap, gapleft 3, gapright 15, gaptop 10");
                    }
                }
            }
        }


    }

    private String determineTextPanePaddings() {

        String layoutConstraints;

        switch (this.getSide()) {
            case LEFT -> {
                layoutConstraints = "gapleft 15";
            }
            case RIGHT -> {
                layoutConstraints = "gapright 15";
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

    // getter & setter -- start
    public BaseModel getBaseModel() {

        return baseModel;
    }

    public Color getBorderColor() {

        return borderColor;
    }

    public void setBorderColor(final Color borderColor) {

        this.borderColor = borderColor;
    }

    public CustomContentContainer getCustomContentContainer() {

        return customContentContainer;
    }

    public JPanel getLayeredContainer() {

        return topContainer;
    }

    public JPanel getMainContainer() {

        return mainContainer;
    }

    public MainFrameGuiInterface getMainFrame() {

        return mainFrame;
    }

    public Side getSide() {

        return side;
    }

    public CommentSidePanel getSidePanel() {

        return sidePanel;
    }

    public JPanel getTopContainer() {

        return topContainer;
    }

    public JTextPane getjTextPane() {

        return jTextPane;
    }

    public void setTextPane(final JTextPane jTextPane) {

        this.jTextPane = jTextPane;
    }
    // getter & setter -- end
}