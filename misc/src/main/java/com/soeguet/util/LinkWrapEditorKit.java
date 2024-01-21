package com.soeguet.util;

import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class LinkWrapEditorKit extends HTMLEditorKit {

    private final ViewFactory defaultFactory = new WrapColumnFactory();

    public LinkWrapEditorKit() {}

    @Override
    public ViewFactory getViewFactory() {

        return defaultFactory;
    }

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

    class WrapColumnFactory implements ViewFactory {

        private final ViewFactory htmlFactory = new HTMLEditorKit().getViewFactory();

        public WrapColumnFactory() {
            // FIXME make css work -- not working right now
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            StyleSheet styles = htmlKit.getStyleSheet();
            styles.addRule("a { text-decoration: underline; color: red; }");
        }

        @Override
        public View create(Element elem) {

            String kind = elem.getName();
            if ("content".equals(kind)) {
                return new WrapLabelView(elem);
            }

            // Delegieren Sie HTML-Elemente an die HTML-Factory
            AttributeSet attrs = elem.getAttributes();
            Object o = attrs.getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                return htmlFactory.create(elem);
            }

            return new LabelView(elem);
        }
    }
}