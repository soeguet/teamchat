package com.soeguet.gui.newcomment.util;

import javax.swing.text.*;
import java.io.Serializable;

/**
 * The WrapEditorKit class extends the StyledEditorKit class and provides a custom implementation
 * for wrapping text in the editor component. It includes a factory class, WrapColumnFactory,
 * that creates specific views based on the element type, and a customized view class, WrapLabelView,
 * that wraps the label text when it exceeds the available width.
 */
public class WrapEditorKit extends StyledEditorKit {

    public ViewFactory defaultFactory = new WrapColumnFactory();

    public WrapEditorKit() {
    }

    /**
     * Gets the ViewFactory.
     *
     * @return the ViewFactory associated with this object
     */
    public ViewFactory getViewFactory() {

        return defaultFactory;
    }


    /**
     * A factory class that creates specific views based on the element type.
     * Implements the ViewFactory interface.
     */
    class WrapColumnFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }

    /**
     * This class is a customized implementation of the LabelView class that provides
     * the ability to wrap the label text when it exceeds the available width.
     */
    class WrapLabelView extends LabelView {

        public WrapLabelView(Element elem) {
            super(elem);
        }

        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }
}

