package com.soeguet.gui.comments.generic_comment.gui_elements.menu_items;

import com.soeguet.gui.comments.generic_comment.factories.ReplyPanelFactory;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;

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

    @Override
    public void mouseClicked(final MouseEvent e) {}

    @Override
    public void mousePressed(final MouseEvent e) {

        ReplyPanelFactory replyPanelFactory = new ReplyPanelFactory(mainFrame, baseModel);
        replyPanelFactory.create();

        // I don't know how to fix this otherwise. the damn text pane keeps stealing the focus
        mainFrame.getTextEditorPane().setFocusable(false);
        replyPanelFactory.setFocusOnTextPane();
    }

    @Override
    public void mouseReleased(final MouseEvent e) {}

    @Override
    public void mouseEntered(final MouseEvent e) {}

    @Override
    public void mouseExited(final MouseEvent e) {}
}
