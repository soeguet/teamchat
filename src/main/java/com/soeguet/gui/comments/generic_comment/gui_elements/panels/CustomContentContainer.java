package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;

public class CustomContentContainer extends JPanel {

// variables -- start
    private Consumer<Graphics> customPaint;
// variables -- end

// constructors -- start
    public CustomContentContainer() {

    }
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

        super.setBorder(new LineBorder(Color.GREEN, 1));
        super.setLayout(new MigLayout("",
                                      // columns
                                      "[grow,fill]",
                                      // rows
                                      "[][][]"));
    }

    public void overrideCustomPaint(final Consumer<Graphics> customPaint) {

        this.customPaint = customPaint;
        repaint();
    }

// overrides -- start
    @Override
    protected void paintComponent(Graphics graphics) {

        if (customPaint != null) {
            customPaint.accept(graphics);
        }
    }
// overrides -- end
}