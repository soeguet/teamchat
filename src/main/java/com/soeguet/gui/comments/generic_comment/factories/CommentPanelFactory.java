package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.*;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.main_panel.dtos.MessageHandlerDTO;

//THIS IS THE MAIN CHAT PANEL FACTORY
public class CommentPanelFactory {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final MessageHandlerDTO messageHandlerDTO;
    // variables -- end

    // constructors -- start
    public CommentPanelFactory(final MainFrameGuiInterface mainFrame, final MessageHandlerDTO messageHandlerDTO) {

        this.mainFrame = mainFrame;
        this.messageHandlerDTO = messageHandlerDTO;
    }
    // constructors -- end

    public CustomCommentPanel create() {

        CommentSidePanel commentSidePanel = new SidePanelFactory(messageHandlerDTO.baseModel(),
                                                                 messageHandlerDTO.commentType()).create();

        //setup of content container -> will not be populated with content yet! #generateCommentPanel
        CustomContentContainer customContentContainer = new ContentContainerFactory().create();

        CustomMainWrapperContainer customMainWrapperContainer = new CustomMainWrapperContainer(messageHandlerDTO.commentType());
        customMainWrapperContainer.setMainWrapperContainerLayoutManager();

        return generateCommentPanel(commentSidePanel, customContentContainer, customMainWrapperContainer);
    }

    /**
     * Generates a custom comment panel with the specified dependencies and components.
     *
     * @param commentSidePanel The comment side panel to set for the comment panel.
     * @param customContentContainer The custom content container to set for the comment panel.
     * @param customMainWrapperContainer The custom main wrapper container to set for the comment panel.
     * @return The generated custom comment panel.
     */
    private CustomCommentPanel generateCommentPanel(final CommentSidePanel commentSidePanel,
                                                    final CustomContentContainer customContentContainer,
                                                    final CustomMainWrapperContainer customMainWrapperContainer) {

        CustomCommentPanel customCommentPanel = new CustomCommentPanel();

        //set dependencies
        customCommentPanel.setMainFrame(mainFrame);
        customCommentPanel.setCommentType(messageHandlerDTO.commentType());
        customCommentPanel.setSide();
        customCommentPanel.setBaseModel(messageHandlerDTO.baseModel());
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