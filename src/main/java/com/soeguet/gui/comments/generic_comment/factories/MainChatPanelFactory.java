package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomCommentPanel;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomContentContainer;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomMainWrapperContainer;
import com.soeguet.gui.comments.generic_comment.gui_elements.panels.TransparentTopPanel;
import com.soeguet.gui.comments.reaction_panel.dtos.ReactionPanelDTO;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;

/**
 * THIS IS THE MAIN CHAT PANEL FACTORY<br>
 * <br>
 * CustomCommentPanel (1) -> CustomMainWrapperContainer (2)<br>
 * CustomMainWrapperContainer -> [CommentSidePanel (3) + CustomContentContainer
 * (4)]<br>
 * <br>
 * (1) CustomCommentPanel: Panel, first layer on GUI<br>
 * (2) CustomMainWrapperContainer: Panel, as wrapper for side and content
 * panel<br>
 * (3) CommentSidePanel: Panel, for the side of the comment, with name, time and
 * menu<br>
 * (4) CustomContentContainer: Panel, for the content
 *
 * @see CustomCommentPanel {@link CustomCommentPanel}
 * @see CustomMainWrapperContainer {@link CustomMainWrapperContainer}
 * @see CommentSidePanel {@link CommentSidePanel}
 * @see CustomContentContainer {@link CustomContentContainer}
 */
public class MainChatPanelFactory {

        // variables -- start
        private final MainFrameGuiInterface mainFrame;
        private final BaseModel baseModel;
        private final CommentTypeEnum commentType;
        // variables -- end

        // constructors -- start
        /**
         * THIS IS THE MAIN CHAT PANEL FACTORY<br>
         * <br>
         * CustomCommentPanel (1) -> CustomMainWrapperContainer (2)<br>
         * CustomMainWrapperContainer -> [CommentSidePanel (3) + CustomContentContainer
         * (4)]<br>
         *
         * @param mainFrame   {@link MainFrameGuiInterface}
         * @param baseModel   {@link BaseModel}
         */
        public MainChatPanelFactory(final MainFrameGuiInterface mainFrame, BaseModel baseModel,
                        CommentTypeEnum commentType) {

                this.mainFrame = mainFrame;
                this.baseModel = baseModel;
                this.commentType = commentType;
        }
        // constructors -- end

        /**
         * Creates a CustomCommentPanel with the necessary components and dependencies.
         *
         * @return CustomCommentPanel {@link CustomCommentPanel}
         */
        public CustomCommentPanel create() {

                // SIDE
                CommentSidePanel commentSidePanel = new SidePanelFactory(mainFrame, baseModel, commentType).create();

                // NEXT TO SIDE -> CONTENT
                // setup of content container -> will not be populated with content yet --
                // #generateCommentPanel
                CustomContentContainer customContentContainer = new ContentContainerFactory().create();

                // WRAPS SIDE AND CONTENT!
                // CustomCommentPanel can't since the LayoutManger needs to be OverlayLayout
                // -> it needs to bind TransparentTopPanel and CustomMainWrapperContainer
                CustomMainWrapperContainer customMainWrapperContainer = new CustomMainWrapperContainer(commentType);
                customMainWrapperContainer.setMainWrapperContainerLayoutManager();

                // set dependencies
                final CustomCommentPanel customCommentPanel = createCustomCommentPanel(commentSidePanel,
                                customContentContainer, customMainWrapperContainer);

                return customCommentPanel;
        }

        /**
         * Assembles CustomCommentPanel with the necessary components and dependencies.
         *
         * @param CommentSidePanel commentSidePanel {@link CommentSidePanel}
         * @param CustomContentContainer customContentContainer {@link CustomContentContainer}
         * @param CustomMainWrapperContainer customMainWrapperContainer {@link CustomMainWrapperContainer}
         * @return
         */
        private CustomCommentPanel createCustomCommentPanel(final CommentSidePanel commentSidePanel,
                        final CustomContentContainer customContentContainer,
                        final CustomMainWrapperContainer customMainWrapperContainer) {

                // TODO 06.11. this panel is not needed anymore. added a panel for the content
                // panel itself
                // -> topPanel
                // floating over it
                CustomCommentPanel customCommentPanel = new CustomCommentPanel();

                customCommentPanel.setMainFrame(mainFrame);
                customCommentPanel.setCommentType(commentType);
                customCommentPanel.setSide();
                customCommentPanel.setBaseModel(baseModel);
                customCommentPanel.setSidePanel(commentSidePanel);
                customCommentPanel.setCustomContentContainer(customContentContainer);
                customCommentPanel.setCustomMainWrapperContainer(customMainWrapperContainer);

                ReactionPanelDTO reactionPanelDTO = new ReactionPanelDTO(baseModel, mainFrame.getUsername(),
                                mainFrame.getWebsocketClient(), mainFrame.getObjectMapper());

                customCommentPanel.setTopContainer(
                                new TransparentTopPanel(this.mainFrame, customCommentPanel, reactionPanelDTO));

                generateCommentPanel(customCommentPanel);

                return customCommentPanel;
        }

        /**
         * Setup comment panel. 
         *
         * @param CustomCommentPanel customCommentPanel {@link CustomCommentPanel}
         */
        private void generateCommentPanel(CustomCommentPanel customCommentPanel) {

                // assemble -> this is where basemodel is differentiated -> message, link,
                // picture
                customCommentPanel.prepareInteractionButtons(); // will be needed in .addComponents()
                customCommentPanel.setLayoutManager();
                customCommentPanel.addComponents();
                customCommentPanel.addContext();
                customCommentPanel.paintChatBubble();
        }
}
