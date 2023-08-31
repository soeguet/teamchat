package com.pre.model;

import java.awt.*;
import java.util.Objects;

public class ChatBubbleColor {

    private Color borderColor;
    private Color quoteColor;

    public ChatBubbleColor() {

    }

    public ChatBubbleColor(Color borderColor, Color quoteColor) {

        this.borderColor = borderColor;
        this.quoteColor = quoteColor;
    }

    public Color getBorderColor() {

        return borderColor;
    }

    public void setBorderColor(Color borderColor) {

        this.borderColor = borderColor;
    }

    public Color getQuoteColor() {

        return quoteColor;
    }

    public void setQuoteColor(Color quoteColor) {

        this.quoteColor = quoteColor;
    }

    public ChatBubbleColor borderColor(Color borderColor) {

        setBorderColor(borderColor);
        return this;
    }

    public ChatBubbleColor quoteColor(Color quoteColor) {

        setQuoteColor(quoteColor);
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof ChatBubbleColor)) {
            return false;
        }
        ChatBubbleColor chatBubbleColor = (ChatBubbleColor) o;
        return Objects.equals(borderColor, chatBubbleColor.borderColor)
                && Objects.equals(quoteColor, chatBubbleColor.quoteColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(borderColor, quoteColor);
    }

    @Override
    public String toString() {

        return "{"
                + " borderColor='"
                + getBorderColor()
                + "'"
                + ", quoteColor='"
                + getQuoteColor()
                + "'"
                + "}";
    }
}
