package com.soeguet.gui.main_panel;

import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.main_panel.interfaces.MessageDisplayHandlerInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.util.interfaces.MessageCategory;

public class MessageDisplayHandler implements MessageDisplayHandlerInterface {

    private final MainFrameInterface mainFrame;
    private final CommentManager commentManager;
    private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();

    public MessageDisplayHandler(final MainFrameInterface mainFrame, final CommentManager commentManager) {

        this.mainFrame = mainFrame;
        this.commentManager = commentManager;
    }

    /**
     Retrieves and removes the first message from the message queue cache.

     @return The first message in the message queue, or null if the cache is empty.
     */
    @Override
    public String pollMessageFromCache() {

        if (cacheManager.getCache("messageQueue") instanceof MessageQueue messageQueue) {

            return messageQueue.pollFirst();
        }

        return null;
    }

    /**
     This method processes and displays a message based on the message model, username, and nickname.

     @param baseModel The message model representing the message.
     @param nickname  The nickname of the client.
     */
    public void processAndDisplayMessage(final BaseModel baseModel, final String nickname) {

        int messageCategory = commentManager.categorizeMessageFromSocket(baseModel);

        switch (messageCategory) {

            case MessageCategory.RIGHT_SIDE_TEXT_MESSAGE -> commentManager.setupMessagesRightSide(baseModel, nickname);

            case MessageCategory.RIGHT_SIDE_PICTURE_MESSAGE -> commentManager.setupPicturesRightSide(baseModel, nickname);

            case MessageCategory.RIGHT_SIDE_LINK_MESSAGE -> commentManager.setupLinkRightSite(baseModel);

            case MessageCategory.LEFT_SIDE_TEXT_MESSAGE -> commentManager.setupMessagesLeftSide(baseModel, nickname);

            case MessageCategory.LEFT_SIDE_PICTURE_MESSAGE -> commentManager.setupPicturesLeftSide(baseModel, nickname);

            case MessageCategory.LEFT_SIDE_LINK_MESSAGE -> commentManager.setupLinkLeftSide(baseModel);
        }
    }

}