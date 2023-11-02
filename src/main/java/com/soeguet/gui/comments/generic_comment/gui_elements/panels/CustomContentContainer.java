package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.function.Consumer;

public class CustomContentContainer extends JPanel implements ComponentListener {

    // variables -- start
    private Consumer<Graphics> customPaint;
    private TransparentTopPanel topPanel;
    // variables -- end
    // constructors -- start
    public CustomContentContainer() {

        addComponentListener(this);
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
    public void componentResized(final ComponentEvent e) {

        // FIXME: 02.11.23 -> this is not working
        final int combinedWidthForTopPanel = super.getWidth() + super.getWidth();
        final int heightForTopPanel = super.getHeight();

        this.topPanel.setSize(new Dimension(combinedWidthForTopPanel, heightForTopPanel));
        this.topPanel.setMaximumSize(new Dimension(combinedWidthForTopPanel, heightForTopPanel));
//        topPanel.setBorder(new LineBorder(Color.RED,5));
//        topPanel.setOpaque(true);
//        topPanel.setBackground(Color.GREEN);
//        revalidate();
//        repaint();
    }

    @Override
    public void componentMoved(final ComponentEvent e) {

    }

    @Override
    public void componentShown(final ComponentEvent e) {

    }

    @Override
    public void componentHidden(final ComponentEvent e) {

    }

    @Override
    protected void paintComponent(Graphics graphics) {

        if (customPaint != null) {
            customPaint.accept(graphics);
        }
    }
    // overrides -- end

// getter & setter -- start
    public void setTopPanelReference(final TransparentTopPanel topPanel) {

        this.topPanel = topPanel;
    }
// getter & setter -- end
}