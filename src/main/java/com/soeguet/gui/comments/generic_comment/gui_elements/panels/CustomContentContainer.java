package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CustomContentContainer extends JPanel {

    private Color borderColor;
    private Consumer<Graphics> customPaint;

    public Consumer<Graphics> getCustomPaint() {

        return customPaint;
    }

    public void setCustomPaint(final Consumer<Graphics> customPaint) {

        this.customPaint = customPaint;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {

        System.out.println("paintComponent");
        if (customPaint != null) {
            customPaint.accept(grphcs);
        }
    }
}