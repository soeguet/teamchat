package com.soeguet.generic_comment.gui_elements.panels;

import java.awt.Graphics;
import java.util.function.Consumer;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class CustomContentContainer extends JPanel {

  // variables -- start
  private Consumer<Graphics> customPaint;

  // variables -- end

  // constructors -- start
  public CustomContentContainer() {}

  // constructors -- end

  public void setContentContainerLayoutManager() {

    /*

    SCHEMA: MAIN CONTENT PANEL -- text, picture, link as well as quoted messages

    [top: quoted message]
    [main: text, picture, link]
    [reactions: emojis]

    ___
    [ [SIDEPANEL] >>"MAIN CONTENT PANEL"<< ]

     */

    super.setLayout(
        new MigLayout(
            "",
            // columns
            "[grow,fill]",
            // rows
            "[fill]"));
  }

  public void overrideCustomPaint(final Consumer<Graphics> customPaint) {

    this.customPaint = customPaint;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics graphics) {

    if (customPaint != null) {
      customPaint.accept(graphics);
    }
  }
}
