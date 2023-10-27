package com.soeguet.gui.main_panel;

import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.gui.comments.interfaces.CommentManager;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.main_panel.interfaces.MessageDisplayHandlerInterface;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.util.interfaces.MessageCategory;

public class MessageDisplayHandler implements MessageDisplayHandlerInterface {

    private final MainFrameGuiInterface mainFrame;
    private final CommentManager commentManager;
    private CacheManager cacheManager;

    public MessageDisplayHandler(final MainFrameGuiInterface mainFrame, final CommentManager commentManager) {

        this.mainFrame = mainFrame;
        this.commentManager = commentManager;
    }

    public void setCacheManager(final CacheManager cacheManager) {

        this.cacheManager = cacheManager;
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
     This method processes and displays a message based on the message model, timeAndUsername, and nickname.

     @param baseModel The message model representing the message.
     @param nickname  The nickname of the client.
     */
    @Override
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