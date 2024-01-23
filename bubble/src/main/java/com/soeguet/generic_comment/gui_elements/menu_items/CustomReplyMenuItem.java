package com.soeguet.generic_comment.gui_elements.menu_items;

import com.soeguet.generic_comment.factories.ReplyPanelFactory;
import com.soeguet.model.jackson.BaseModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class CustomReplyMenuItem extends JMenuItem implements MouseListener {

  // variables -- start
  private final BaseModel baseModel;

  // variables -- end

  // constructors -- start
  public CustomReplyMenuItem(final BaseModel baseModel) {

    this.baseModel = baseModel;

    super.setText("reply");
    this.addMouseListener(this);
  }

  // constructors -- end

  @Override
  public void mouseClicked(final MouseEvent e) {}

  @Override
  public void mousePressed(final MouseEvent e) {

    ReplyPanelFactory replyPanelFactory = new ReplyPanelFactory(baseModel);
    replyPanelFactory.create();

    // I don't know how to fix this otherwise. the damn text pane keeps stealing the focus
    // TODO 1
    //        mainFrame.getTextEditorPane().setFocusable(false);
    replyPanelFactory.setFocusOnTextPane();
  }

  @Override
  public void mouseReleased(final MouseEvent e) {}

  @Override
  public void mouseEntered(final MouseEvent e) {}

  @Override
  public void mouseExited(final MouseEvent e) {}
}
