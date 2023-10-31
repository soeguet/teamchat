package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CustomContentContainer extends JPanel {

    // variables -- start
    private Consumer<Graphics> customPaint;
    // variables -- end

    // overrides -- start
    @Override
    protected void paintComponent(Graphics graphics) {

        if (customPaint != null) {
            customPaint.accept(graphics);
        }
    }

    // getter & setter -- start
    public void setCustomPaint(final Consumer<Graphics> customPaint) {

        this.customPaint = customPaint;
        repaint();
    }
    // getter & setter -- end
}