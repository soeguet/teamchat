package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.*;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

//THIS IS THE MAIN CHAT PANEL FACTORY
public class MainChatPanelFactory {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    private final CommentTypeEnum commentType;
    // variables -- end

    // constructors -- start
    public MainChatPanelFactory(final MainFrameGuiInterface mainFrame, BaseModel baseModel,
                                CommentTypeEnum commentType) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.commentType = commentType;
    }
    // constructors -- end

    /**
     Creates a custom comment panel.

     @return the custom comment panel
     */
    public CustomCommentPanel create() {

        //SIDE
        CommentSidePanel commentSidePanel = new SidePanelFactory(baseModel, commentType).create();

        //NEXT TO SIDE -> CONTENT
        // setup of content container -> will not be populated with content yet -- #generateCommentPanel
        CustomContentContainer customContentContainer = new ContentContainerFactory().create();

        //WRAPS SIDE AND CONTENT! CustomCommentPanel can't since the LayoutManger needs to be OverlayLayout
        // -> it needs to bind TransparentTopPanel and CustomMainWrapperContainer
        CustomMainWrapperContainer customMainWrapperContainer = new CustomMainWrapperContainer(commentType);
        customMainWrapperContainer.setMainWrapperContainerLayoutManager();

        return generateCommentPanel(commentSidePanel, customContentContainer, customMainWrapperContainer);
    }

    private CustomCommentPanel generateCommentPanel(final CommentSidePanel commentSidePanel,
                                                    final CustomContentContainer customContentContainer,
                                                    final CustomMainWrapperContainer customMainWrapperContainer) {

        CustomCommentPanel customCommentPanel = new CustomCommentPanel();

        //set dependencies
        customCommentPanel.setMainFrame(mainFrame);
        customCommentPanel.setCommentType(commentType);
        customCommentPanel.setSide();
        customCommentPanel.setBaseModel(baseModel);
        customCommentPanel.setSidePanel(commentSidePanel);
        customCommentPanel.setCustomContentContainer(customContentContainer);
        customCommentPanel.setCustomMainWrapperContainer(customMainWrapperContainer);
        customCommentPanel.setTopContainer(new TransparentTopPanel());

        //assemble -> this is where basemodel is differentiated -> message, link, picture
        customCommentPanel.setLayoutManager();
        customCommentPanel.addComponents();
        customCommentPanel.addContext();
        customCommentPanel.paintChatBubble();

        return customCommentPanel;
    }
}