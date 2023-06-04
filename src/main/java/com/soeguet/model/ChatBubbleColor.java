package com.soeguet.model;

import java.awt.*;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ChatBubbleColor {

  private Color borderColor;
  private Color quoteColor;

  public ChatBubbleColor() {}

  public ChatBubbleColor(Color borderColor, Color quoteColor) {
    this.borderColor = borderColor;
    this.quoteColor = quoteColor;
  }

  public Color getBorderColor() {
    return this.borderColor;
  }

  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }

  public Color getQuoteColor() {
    return this.quoteColor;
  }

  public void setQuoteColor(Color quoteColor) {
    this.quoteColor = quoteColor;
  }

  @NotNull
  public ChatBubbleColor borderColor(Color borderColor) {
    setBorderColor(borderColor);
    return this;
  }

  @NotNull
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

  @NotNull
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
