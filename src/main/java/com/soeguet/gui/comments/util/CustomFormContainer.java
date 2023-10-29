package com.soeguet.gui.comments.util;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CustomFormContainer extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        if (customPaint != null) {
            customPaint.accept(g);
        }
    }
    private Consumer<Graphics> customPaint;

    public Consumer<Graphics> getCustomPaint() {

        return customPaint;
    }

    public void setCustomPaint(final Consumer<Graphics> customPaint) {

        this.customPaint = customPaint;
        repaint();
    }
}