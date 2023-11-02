package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class CustomReplyPanel extends JPanel implements MouseListener, MouseMotionListener {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    private final Point offset = new Point();
    // variables -- end

    // constructors -- start
    public CustomReplyPanel(MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;

        super.addMouseListener(this);
        super.addMouseMotionListener(this);
    }
    // constructors -- end

    public void setCustomReplyPanelLayoutManger() {

        /*
        SCHEMA: ReplyPanel

        [            title         [x]] // contains title and close button
        [           quoted stuff      ] // contains former, quoted stuff
        [ [text pane] [ [1] [2] [3] ] ] //text pane and buttons

         */

        super.setLayout(new MigLayout("debug",
                                      //columns
                                      "[fill,grow,center]",
                                      //rows
                                      "[fill][fill,grow][fill]"));
    }

    public void setMaximumSizeWithingMainFrame() {

        super.setBounds(0, 0, 500, 500);
    }

    public void populateCustomReplyPanel() {

        // TODO: 02.11.23 implement this

        super.setBorder(new LineBorder(Color.BLUE, 1));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new MigLayout("debug",
                                           //columns
                                           "[fill,grow][fill]",
                                           //rows
                                           "[fill]"));
        titlePanel.setBorder(new LineBorder(Color.RED, 1));
        JLabel titleLabel = new JLabel("Reply to: " + baseModel.getSender());
        titlePanel.add(titleLabel, "cell 0 0");
        JButton closeButton = new JButton("x");
        titlePanel.add(closeButton, "cell 1 0");
        closeButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                CustomReplyPanel.super.setVisible(false);
            }
        });
        add(titlePanel, "cell 0 0");


        JLabel quotedStuff = new JLabel(baseModel.getSender());
        add(quotedStuff, "cell 0 1");

        JTextPane textPane = new JTextPane();
        textPane.setEditorKit(new WrapEditorKit());
        add(textPane, "cell 0 2");
    }

    // overrides -- start

    @Override
    public void mouseDragged(final MouseEvent e) {

        int x = e.getX() - offset.x + this.getX();
        int y = e.getY() - offset.y + this.getY();
        this.setLocation(x, y);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

    }

    @Override
    public void mouseClicked(final java.awt.event.MouseEvent e) {


    }

    @Override
    public void mousePressed(final java.awt.event.MouseEvent e) {

        offset.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(final java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseEntered(final java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseExited(final java.awt.event.MouseEvent e) {

    }
    // overrides -- end
}