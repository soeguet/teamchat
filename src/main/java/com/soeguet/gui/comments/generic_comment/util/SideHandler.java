package com.soeguet.gui.comments.generic_comment.util;

import com.soeguet.gui.comments.util.CommentTypeEnum;

/**
 * This class represents a SideHandler, which is responsible for determining the side of a comment
 * based on its type.
 */
public class SideHandler {

    /** Creates a new instance of the SideHandler class. */
    public SideHandler() {}

    /**
     * Determines the side of a comment based on the given comment type.
     *
     * @param commentType the type of the comment
     * @return the side of the comment (LEFT or RIGHT)
     * @throws IllegalArgumentException if the comment type is unknown
     */
    public Side determineSide(final CommentTypeEnum commentType) {

        switch (commentType) {
            case LEFT_TEXT, LEFT_LINK, LEFT_PICTURE -> {
                return Side.LEFT;
            }
            case RIGHT_TEXT, RIGHT_LINK, RIGHT_PICTURE -> {
                return Side.RIGHT;
            }
            default -> throw new IllegalArgumentException("Unknown comment type: " + commentType);
        }
    }
}
