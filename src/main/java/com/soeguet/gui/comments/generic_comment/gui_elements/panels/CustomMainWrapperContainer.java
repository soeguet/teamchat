package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.util.CommentTypeEnum;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;

public class CustomMainWrapperContainer extends JPanel {

    // variables -- start
    private final Side side;
    private Consumer<Graphics> customPaint;
    // variables -- end

    // constructors -- start
    public CustomMainWrapperContainer(CommentTypeEnum commentType) {

        this.side = determineSide(commentType);
    }
    // constructors -- end

    private Side determineSide(final CommentTypeEnum commentType) {

        return switch (commentType) {

            case LEFT_LINK, LEFT_PICTURE, LEFT_TEXT -> Side.LEFT;
            case RIGHT_LINK, RIGHT_PICTURE, RIGHT_TEXT -> Side.RIGHT;
        };
    }

    public void setMainWrapperContainerLayoutManager() {

        this.setBorder(new LineBorder(Color.BLACK, 1));

        /*

        SCHEMA: Main Panel - wraps everything

            LEFT
        >>"[" [Side Panel][Content Panel] "]"<<

                        RIGHT
                    >>"[" [Content Panel][Side Panel] "]"<<
         */

        switch (this.getSide()) {
            case LEFT -> super.setLayout(new MigLayout("",
                                                       // columns
                                                       "[fill,left]20[fill,left]",
                                                       // rows
                                                       "[shrink]"));
            case RIGHT -> super.setLayout(new MigLayout("",
                                                        // columns
                                                        // -> grow is needed -> else right sight will collapse
                                                        "[grow,fill,right]20[fill,right]",
                                                        // rows
                                                        "[shrink]"));
        }
    }

    // overrides -- start
    @Override
    protected void paintComponent(Graphics g) {

        if (customPaint != null) {
            customPaint.accept(g);
        }
    }
    // overrides -- end

    // getter & setter -- start
    public Consumer<Graphics> getCustomPaint() {

        return customPaint;
    }

    public void setCustomPaint(final Consumer<Graphics> customPaint) {

        this.customPaint = customPaint;
        repaint();
    }

    public Side getSide() {

        return side;
    }
    // getter & setter -- end
}