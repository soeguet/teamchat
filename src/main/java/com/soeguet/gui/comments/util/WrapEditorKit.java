package com.soeguet.gui.comments.util;

import javax.swing.text.*;

/**
 The WrapEditorKit class extends the StyledEditorKit class and provides a custom implementation for wrapping text in the editor component. It includes
 a factory class, WrapColumnFactory, that creates specific views based on the element type, and a customized view class, WrapLabelView, that wraps the
 label text when it exceeds the available width.
 */
public class WrapEditorKit extends StyledEditorKit {

    public ViewFactory defaultFactory = new WrapColumnFactory();

    public WrapEditorKit() {

    }

    /**
     Gets the ViewFactory.

     @return the ViewFactory associated with this object
     */
    @Override
    public ViewFactory getViewFactory() {

        return defaultFactory;
    }

    /**
     This class is a customized implementation of the LabelView class that provides the ability to wrap the label text when it exceeds the available
     width.
     */
    private static class WrapLabelView extends LabelView {

        public WrapLabelView(Element elem) {

            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {

            return switch (axis) {

                case View.X_AXIS -> 0;

                case View.Y_AXIS -> super.getMinimumSpan(axis);

                default -> throw new IllegalArgumentException("Invalid axis: " + axis);
            };
        }
    }

    /**
     A factory class that creates specific views based on the element type. Implements the ViewFactory interface.
     */
    private static class WrapColumnFactory implements ViewFactory {

        @Override
        public View create(Element elem) {

            String kind = elem.getName();

            if (kind != null) {

                switch (kind) {

                    case AbstractDocument.ContentElementName -> {
                        return new WrapLabelView(elem);
                    }

                    case AbstractDocument.ParagraphElementName -> {
                        return new ParagraphView(elem);
                    }

                    case AbstractDocument.SectionElementName -> {
                        return new BoxView(elem, View.Y_AXIS);
                    }

                    case StyleConstants.ComponentElementName -> {
                        return new ComponentView(elem);
                    }

                    case StyleConstants.IconElementName -> {
                        return new IconView(elem);
                    }
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }
}