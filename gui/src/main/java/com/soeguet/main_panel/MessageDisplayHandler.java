package com.soeguet.main_panel;

import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.interfaces.CommentInterface;
import com.soeguet.main_panel.interfaces.MessageDisplayHandlerInterface;
import com.soeguet.model.jackson.BaseModel;
import java.awt.Component;
import java.awt.Toolkit;

public class MessageDisplayHandler implements MessageDisplayHandlerInterface {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final CommentManager commentManager;
    private CacheManager cacheManager;

    // variables -- end

    // constructors -- start
    public MessageDisplayHandler(
            final MainFrameGuiInterface mainFrame, final CommentManager commentManager) {

        this.mainFrame = mainFrame;
        this.commentManager = commentManager;
    }

    // constructors -- end

    /**
     * Retrieves and removes the first message from the message queue cache.
     *
     * @return The first message in the message queue, or null if the cache is empty.
     */
    @Override
    public String pollMessageFromCache() {

        if (cacheManager.getCache("messageQueue") instanceof MessageQueue messageQueue) {

            return messageQueue.pollFirst();
        }

        return null;
    }

    /**
     * Processes and displays a message based on the message model, nickname, and comment type.
     *
     * @param baseModel The message model representing the message.
     */
    @Override
    public void processAndDisplayMessage(final BaseModel baseModel) {

        CommentTypeEnum commentType = commentManager.categorizeMessageFromSocket(baseModel);
        commentManager.setupMessage(baseModel, commentType);
    }

    public void setCacheManager(final CacheManager cacheManager) {

        this.cacheManager = cacheManager;
    }

    @Override
    public void updateExistingMessage(final BaseModel baseModel) {

        final Component[] mainTextPanelComponents = mainFrame.getMainTextPanel().getComponents();

        for (final Component mainTextPanelComponent : mainTextPanelComponents) {

            if (mainTextPanelComponent instanceof CommentInterface commentInterface) {

                if (commentInterface.getBaseModel().getId().equals(baseModel.getId())) {

                    commentInterface.setBaseModel(baseModel);

                    commentInterface.initializeReactionStickerHandler(
                            baseModel.getUserInteractions());

                    // TODO implement notification for client
                    Toolkit.getDefaultToolkit().beep();

                    mainFrame.revalidate();
                    mainFrame.repaint();
                }
            }
        }
    }
}