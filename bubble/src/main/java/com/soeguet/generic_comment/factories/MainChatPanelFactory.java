package com.soeguet.generic_comment.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.dtos.ReactionPanelDTO;
import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.gui_elements.panels.CommentSidePanel;
import com.soeguet.generic_comment.gui_elements.panels.CustomCommentPanel;
import com.soeguet.generic_comment.gui_elements.panels.CustomContentContainer;
import com.soeguet.generic_comment.gui_elements.panels.CustomMainWrapperContainer;
import com.soeguet.generic_comment.gui_elements.panels.TransparentTopPanel;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.properties.PropertiesRegister;
import com.soeguet.socket_client.ClientRegister;

import java.util.Properties;
import java.util.function.ObjDoubleConsumer;

/**
 * THIS IS THE MAIN CHAT PANEL FACTORY<br>
 * <br>
 * CustomCommentPanel (1) -> CustomMainWrapperContainer (2)<br>
 * CustomMainWrapperContainer -> [CommentSidePanel (3) + CustomContentContainer (4)]<br>
 * <br>
 * (1) CustomCommentPanel: Panel, first layer on GUI<br>
 * (2) CustomMainWrapperContainer: Panel, as wrapper for side and content panel<br>
 * (3) CommentSidePanel: Panel, for the side of the comment, with name, time and menu<br>
 * (4) CustomContentContainer: Panel, for the content
 *
 * @see CustomCommentPanel {@link CustomCommentPanel}
 * @see CustomMainWrapperContainer {@link CustomMainWrapperContainer}
 * @see CommentSidePanel {@link CommentSidePanel}
 * @see CustomContentContainer {@link CustomContentContainer}
 */
public class MainChatPanelFactory {

    // variables -- start
    private final BaseModel baseModel;
    private final CommentTypeEnum commentType;

    // variables -- end

    // constructors -- start
    /**
     * THIS IS THE MAIN CHAT PANEL FACTORY<br>
     * <br>
     * CustomCommentPanel (1) -> CustomMainWrapperContainer (2)<br>
     * CustomMainWrapperContainer -> [CommentSidePanel (3) + CustomContentContainer (4)]<br>
     *
     * @param mainFrame {@link MainFrameGuiInterface}
     * @param baseModel {@link BaseModel}
     */
    public MainChatPanelFactory(
            BaseModel baseModel,
            CommentTypeEnum commentType) {

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
        CommentSidePanel commentSidePanel =
                new SidePanelFactory(baseModel, commentType).create();

        // NEXT TO SIDE -> CONTENT
        // setup of content container -> will not be populated with content yet --
        // #generateCommentPanel
        CustomContentContainer customContentContainer = new ContentContainerFactory().create();

        // WRAPS SIDE AND CONTENT!
        // CustomCommentPanel can't since the LayoutManger needs to be OverlayLayout
        // -> it needs to bind TransparentTopPanel and CustomMainWrapperContainer
        CustomMainWrapperContainer customMainWrapperContainer =
                new CustomMainWrapperContainer(commentType);
        customMainWrapperContainer.setMainWrapperContainerLayoutManager();

        // set dependencies
        final CustomCommentPanel customCommentPanel =
                createCustomCommentPanel(
                        commentSidePanel, customContentContainer, customMainWrapperContainer);

        return customCommentPanel;
    }

    /**
     * Assembles CustomCommentPanel with the necessary components and dependencies.
     *
     * @param CommentSidePanel commentSidePanel {@link CommentSidePanel}
     * @param CustomContentContainer customContentContainer {@link CustomContentContainer}
     * @param CustomMainWrapperContainer customMainWrapperContainer {@link
     *     CustomMainWrapperContainer}
     * @return
     */
    private CustomCommentPanel createCustomCommentPanel(
            final CommentSidePanel commentSidePanel,
            final CustomContentContainer customContentContainer,
            final CustomMainWrapperContainer customMainWrapperContainer) {

        // TODO 06.11. this panel is not needed anymore. added a panel for the content
        // panel itself
        // -> topPanel
        // floating over it
        CustomCommentPanel customCommentPanel = new CustomCommentPanel();

        // TODO 1

//        customCommentPanel.setMainFrame(mainFrame);
        customCommentPanel.setCommentType(commentType);
        customCommentPanel.setSide();
        customCommentPanel.setBaseModel(baseModel);
        customCommentPanel.setSidePanel(commentSidePanel);
        customCommentPanel.setCustomContentContainer(customContentContainer);
        customCommentPanel.setCustomMainWrapperContainer(customMainWrapperContainer);

        PropertiesRegister propertiesRegister = PropertiesRegister.getCustomUserPropertiesInstance();
        ClientRegister clientRegister = ClientRegister.getWebSocketClientInstance();

        ReactionPanelDTO reactionPanelDTO = new ReactionPanelDTO(baseModel, propertiesRegister.getUsername(),
                                                                 mainFrame.getWebsocketClient(),
                                                                 new ObjectMapper());

        customCommentPanel.setTopContainer(
                new TransparentTopPanel( customCommentPanel, reactionPanelDTO));

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