package com.soeguet.gui.comments.generic_comment.gui_elements.textpanes;

import com.soeguet.emoji.EmojiHandler;
import com.soeguet.gui.comments.generic_comment.gui_elements.interfaces.ContentInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;

import javax.swing.*;
import javax.swing.text.*;

public class CustomTextPane extends JTextPane implements ContentInterface {

    private final MainFrameGuiInterface mainFrame;
    private final boolean lineWrap;
    private final String message;

    public CustomTextPane(MainFrameGuiInterface mainFrame, final boolean lineWrap, final String message) {

        this.mainFrame = mainFrame;

        this.lineWrap = lineWrap;
        this.message = message;

        if (lineWrap) {

            setEditorKit(new WrapEditorKit());
        }
        super.setEditable(false);
        super.setOpaque(false);
    }

    public void create() {

        super.setText(message);
        EmojiHandler emojiHandler = new EmojiHandler(mainFrame);
        emojiHandler.replaceImageIconWithEmojiDescription(this);
    }

    private static class WrapEditorKit extends StyledEditorKit {

        private final ViewFactory defaultFactory = new WrapColumnFactory();

        @Override
        public ViewFactory getViewFactory() {

            return defaultFactory;
        }
    }

    private static class WrapColumnFactory implements ViewFactory {

        @Override
        public View create(final Element element) {

            final String kind = element.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(element);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(element);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(element, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(element);
                    case StyleConstants.IconElementName:
                        return new IconView(element);
                }
            }

            // Default to text display.
            return new LabelView(element);
        }
    }

    private static class WrapLabelView extends LabelView {

        public WrapLabelView(final Element element) {

            super(element);
        }

        @Override
        public float getMinimumSpan(final int axis) {

            return switch (axis) {
                case View.X_AXIS -> 0;
                case View.Y_AXIS -> super.getMinimumSpan(axis);
                default -> throw new IllegalArgumentException("Invalid axis: " + axis);
            };
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {

        if (lineWrap) {

            return super.getScrollableTracksViewportWidth();

        } else {

            return getParent() == null || getUI().getPreferredSize(this).width <= getParent().getSize().width;
        }
    }
}
