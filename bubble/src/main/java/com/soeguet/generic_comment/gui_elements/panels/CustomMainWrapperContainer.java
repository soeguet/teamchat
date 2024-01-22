package com.soeguet.generic_comment.gui_elements.panels;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.util.Side;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Wrapper for Sidepanel and Contentpanel<br>
 * Is set on top of the CustomCommentPanel {@link CustomCommentPanel}<br>
 * <br>
 * SCHEMA: Main Panel - wraps everything<br>
 * <br>
 * LEFT >>"[" [Side Panel][Content Panel] "]"<<<br>
 * <br>
 * RIGHT >>"[" [Content Panel][Side Panel] "]"<<<br>
 * <br>
 * ## search for: TEXT MESSAGES -> CustomTextAndQuoteForBubblePanel &<br>
 * TextMessageFactory<br>
 * PICTURES -> CustomPictureWrapperPanel & PicturePanelFactory LINKS -><br>
 * CustomLinkWrapperPanel & LinkPanelFactory<br>
 *
 * @see CustomCommentPanel {@link CustomCommentPanel}
 * @see CommentTypeEnum {@link CommentTypeEnum}
 */
public class CustomMainWrapperContainer extends JPanel {

    // variables -- start
    private final Side side;

    // variables -- end

    // constructors -- start
    /**
     * Wrapper for Sidepanel and Contentpanel<br>
     * Is set on top of the CustomCommentPanel {@link CustomCommentPanel}<br>
     * <br>
     *
     * @param commentType {@link CommentTypeEnum}
     */
    public CustomMainWrapperContainer(CommentTypeEnum commentType) {

        this.side = determineSide(commentType);
    }

    // constructors -- end

    /** Sets the LayoutManager for the MainWrapperContainer */
    public void setMainWrapperContainerLayoutManager() {

        switch (this.getSide()) {
            case LEFT -> super.setLayout(
                    new MigLayout(
                            "insets 0",
                            // columns
                            "[left,shrink][fill,left]",
                            // rows
                            "[shrink]"));
            case RIGHT -> super.setLayout(
                    new MigLayout(
                            "",
                            // columns
                            // -> grow is needed -> else right sight will collapse
                            "[grow,fill,right]20[shrink,right]",
                            // rows
                            "[shrink]"));
        }
    }

    /**
     * Check on which side the incoming message will be displayed.
     *
     * @param commentType {@link CommentTypeEnum}
     * @return Side {@link Side}
     */
    private Side determineSide(final CommentTypeEnum commentType) {

        return switch (commentType) {
            case LEFT_LINK, LEFT_PICTURE, LEFT_TEXT -> Side.LEFT;
            case RIGHT_LINK, RIGHT_PICTURE, RIGHT_TEXT -> Side.RIGHT;
        };
    }

    // getter & setter -- start
    /**
     * Getter for Side parameter. <br>
     *
     * @return Side {@link Side}
     */
    public Side getSide() {

        return side;
    }
    // getter & setter -- end
}