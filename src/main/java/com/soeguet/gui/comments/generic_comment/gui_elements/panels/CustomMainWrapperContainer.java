package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomMainWrapperContainer extends JPanel {

    // variables -- start
    private final Side side;
    // variables -- end

    // constructors -- start
    public CustomMainWrapperContainer(CommentTypeEnum commentType) {

        this.side = determineSide(commentType);
    }
    // constructors -- end

    public void setMainWrapperContainerLayoutManager() {


        /*

        SCHEMA: Main Panel - wraps everything

            LEFT
        >>"[" [Side Panel][Content Panel] "]"<<

                        RIGHT
                    >>"[" [Content Panel][Side Panel] "]"<<
         */

        switch (this.getSide()) {

            case LEFT -> super.setLayout(new MigLayout("insets 0",
                                                       // columns
                                                       "[left,shrink][fill,left]",
                                                       // rows
                                                       "[shrink]"));
            case RIGHT -> super.setLayout(new MigLayout("",
                                                        // columns
                                                        // -> grow is needed -> else right sight will collapse
                                                        "[grow,fill,right]20[shrink,right]",
                                                        // rows
                                                        "[shrink]"));
        }
    }

    private Side determineSide(final CommentTypeEnum commentType) {

        return switch (commentType) {

            case LEFT_LINK, LEFT_PICTURE, LEFT_TEXT -> Side.LEFT;
            case RIGHT_LINK, RIGHT_PICTURE, RIGHT_TEXT -> Side.RIGHT;
        };
    }

    // getter & setter -- start
    public Side getSide() {

        return side;
    }
    // getter & setter -- end
}