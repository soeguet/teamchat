package com.soeguet.gui.comments.generic_comment.gui_elements;

import com.soeguet.gui.comments.generic_comment.dto.CommentGuiDTO;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.comments.util.WrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CommentMainPanel extends JPanel {

    private final MainFrameGuiInterface mainFrame;
    /**
     Represents a side of the comment - LEFT or RIGHT.
     */
    private final Side side;
    /**
     Represents the main information Model of this app.

     @see BaseModel
     */
    private final BaseModel baseModel;
    private final CommentSidePanel sidePanel;
    /**
     The top level container will be used for MouseEvents as well as reaction emojis.

     @see JPanel
     */
    private final JPanel topContainer;
    /**
     The main container panel will be used for the side panel as well as the content container.
     */
    private final JPanel mainContainer;
    /**
     The content container will be used for the text and picture content as well as the quoted comments.
     */
    private final JPanel contentContainer;

    public CommentMainPanel(CommentGuiDTO commentGuiDTO) {

        this.mainFrame = commentGuiDTO.mainFrame();
        this.side = new SideHandler().determineSide(commentGuiDTO.commentType());
        this.baseModel = commentGuiDTO.baseModel();
        this.sidePanel = commentGuiDTO.sidePanel();
        this.topContainer = commentGuiDTO.topContainer();
        this.contentContainer = commentGuiDTO.container();
        this.mainContainer = commentGuiDTO.mainContainer();

        //FIXME remove this
        this.getSidePanel().setBorder(new LineBorder(Color.BLUE, 1));
        this.getContainer().setBorder(new LineBorder(Color.RED, 1));
        this.getMainContainer().setBorder(new LineBorder(Color.GREEN, 1));
        this.getTopContainer().setBorder(new LineBorder(Color.BLACK, 3));
    }

    public BaseModel getBaseModel() {

        return baseModel;
    }

    public JPanel getTopContainer() {

        return topContainer;
    }

    public JPanel getMainContainer() {

        return mainContainer;
    }

    public JPanel getContentContainer() {

        return contentContainer;
    }

    public void setLayoutManager() {

        this.setThisComponentLayoutManager();

        this.setMainContainerLayoutManager();
    }

    private void setThisComponentLayoutManager() {

        this.setLayout(new OverlayLayout(this));
    }

    private void setMainContainerLayoutManager() {

        final JPanel mainPanel = this.getMainContainer();
        switch (this.getSide()) {
            case LEFT -> mainPanel.setLayout(new MigLayout("insets 0,align left bottom",
                                                           // columns
                                                           "[right]" + "[fill]" + "[fill]" + "[fill]" + "[fill]",
                                                           // rows
                                                           "[]" + "[fill]" + "[]"));
            case RIGHT -> mainPanel.setLayout(new MigLayout("",
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

        final CommentSidePanel commentSidePanel = this.getSidePanel();

        commentSidePanel.getNameLabel().setText(baseModel.getSender());
        commentSidePanel.getTimeLabel().setText("10:00");
        commentSidePanel.setLayoutManager();
        commentSidePanel.addComponents();
    }

    public CommentSidePanel getSidePanel() {

        return sidePanel;
    }

    public void addComponents() {

        /*
        cell 1 1 1 2
        colum 1, row 1, span col 1, span row 2
         */
        // init setup
        final JPanel mainPanel = this.getMainContainer();
        final JPanel topPanel = this.getTopContainer();
        final CommentSidePanel commentSidePanel = this.getSidePanel();
        final JPanel mainContentPanel = this.getContentContainer();

        //container components
        this.add(mainPanel);
        mainPanel.setBackground(Color.red);
        this.add(topPanel);

        switch (this.getSide()) {
            case LEFT -> {
                mainPanel.setAlignmentX(0.0f);
                mainPanel.setAlignmentY(0.5f);
                mainPanel.add(commentSidePanel, "cell 0 0 1 2, dock west");
                mainPanel.add(mainContentPanel, "cell 1 0 1 2, dock west");
                topPanel.setAlignmentX(0.0f);
                topPanel.setAlignmentY(0.5f);
            }
            case RIGHT -> {
                mainPanel.setAlignmentX(1.0f);
                mainPanel.setAlignmentY(0.5f);
                mainPanel.add(commentSidePanel, "cell 1 0 1 2, dock east");
                mainPanel.add(mainContentPanel, "cell 0 0 1 2, dock east");

                topPanel.setAlignmentX(1.0f);
                topPanel.setAlignmentY(0.5f);
            }
        }

        //addition -> main panel
//        switch (this.getSide()) {
//            case LEFT -> {
//                mainPanel.add(commentSidePanel, "cell 0 0 1 2, dock west");
//                mainPanel.add(mainContentPanel, "cell 1 0 1 2, dock west");
//            }
//            case RIGHT -> {
//                mainPanel.add(commentSidePanel, "cell 1 0 1 2, dock east");
//                mainPanel.add(mainContentPanel, "cell 0 0 1 2, dock east");
//            }
//        }

    }
    private int layeredPanelWidth = 0;
    private int layeredPanelHeight = 0;

    public MainFrameGuiInterface getMainFrame() {

        return mainFrame;
    }

    public int getLayeredPanelWidth() {

        return layeredPanelWidth;
    }

    public void setLayeredPanelWidth(final int layeredPanelWidth) {

        this.layeredPanelWidth = layeredPanelWidth;
    }

    public int getLayeredPanelHeight() {

        return layeredPanelHeight;
    }

    public void setLayeredPanelHeight(final int layeredPanelHeight) {

        this.layeredPanelHeight = layeredPanelHeight;
    }

    public void addContext() {

        JTextPane jTextPane = new JTextPane();
        jTextPane.setEditorKit(new WrapEditorKit());
        jTextPane.setText(baseModel.getMessage());
        jTextPane.setEditable(false);

        switch (this.getSide()) {
            case LEFT -> {
                this.getMainContainer().add(jTextPane, "cell 1 0, wrap");
            }
            case RIGHT -> {
                this.getContainer().add(jTextPane, "cell 0 0, wrap");
            }
        }
    }

    public JPanel getContainer() {

        return contentContainer;
    }

    public JPanel getLayeredContainer() {

        return topContainer;
    }
}