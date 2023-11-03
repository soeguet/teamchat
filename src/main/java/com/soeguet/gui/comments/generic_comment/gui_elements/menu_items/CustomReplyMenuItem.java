package com.soeguet.gui.comments.generic_comment.gui_elements.menu_items;

import com.soeguet.gui.comments.generic_comment.factories.ReplyPanelFactory;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.reply.ReplyPanelImpl;
import com.soeguet.gui.reply.interfaces.ReplyInterface;
import com.soeguet.model.jackson.BaseModel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomReplyMenuItem extends JMenuItem implements MouseListener {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    // variables -- end

    // constructors -- start
    public CustomReplyMenuItem(final MainFrameGuiInterface mainFrame, final BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;

        super.setText("reply");
        this.addMouseListener(this);
    }
    // constructors -- end

    // overrides -- start
    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

        new ReplyPanelFactory(mainFrame,baseModel).create();

        //CODE HERE
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
    // overrides -- end
}