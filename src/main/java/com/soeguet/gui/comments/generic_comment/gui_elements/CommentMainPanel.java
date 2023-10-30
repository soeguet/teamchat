package com.soeguet.gui.comments.generic_comment.gui_elements;

import com.soeguet.gui.comments.generic_comment.dto.CommentGuiDTO;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.model.jackson.BaseModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CommentMainPanel extends JPanel {

    private final Side side;
    private final CommentSidePanel sidePanel;
    private final JLayeredPane layeredContainer;
    private final JPanel container;

    public CommentMainPanel(CommentGuiDTO commentGuiDTO) {

        this.side = new SideHandler().determineSide(commentGuiDTO.commentType());
        this.sidePanel = commentGuiDTO.sidePanel();
        this.layeredContainer = commentGuiDTO.layeredContainer();
        this.container = commentGuiDTO.container();
    }

    public void setLayoutManager() {

        switch (this.getSide()) {
            case LEFT -> this.setLayout(new MigLayout("insets 0,align left bottom",
                                                      // columns
                                                      "[right]" + "[fill]" + "[fill]" + "[fill]" + "[fill]",
                                                      // rows
                                                      "[]" + "[fill]" + "[]"));
            case RIGHT -> this.setLayout(new MigLayout("",
                                                       // columns
                                                       "[grow,fill]" + "[7!]",
                                                       // rows
                                                       "[]" + "[]"));
        }
    }

    public Side getSide() {

        return side;
    }

    public void setupSidePanel(final BaseModel baseModel) {

        this.getSidePanel().getNameLabel().setText(baseModel.getSender());
        this.getSidePanel().getTimeLabel().setText(baseModel.getTime());
        this.getSidePanel().setLayoutManager();
        this.getSidePanel().addComponents();
    }

    public CommentSidePanel getSidePanel() {

        return sidePanel;
    }

    public void addComponents() {

        switch (this.getSide()) {
            case LEFT -> {
                this.add(this.getSidePanel(), "cell 1 0 1 2, aligny bottom, growy 0");
                this.add(this.getContainer(), "cell 2 0 1 2");
                this.add(this.getLayeredContainer(), "cell 2 1 2 2, grow");
            }
            case RIGHT -> {
                this.add(this.getSidePanel(), "cell 0 0 4 1, aligny bottom, growy 0");
                this.add(this.getContainer(), "cell 2 0 1 2, aligny bottom, growy 0");
                this.add(this.getLayeredContainer(), "cell 1 1 2 2, grow");
            }
        }
    }

    public JPanel getContainer() {

        return container;
    }

    public JLayeredPane getLayeredContainer() {

        return layeredContainer;
    }
}