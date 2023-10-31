package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.dto.CommentGuiDTO;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpane.CustomTextPane;
import com.soeguet.gui.comments.generic_comment.util.Side;
import com.soeguet.gui.comments.generic_comment.util.SideHandler;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private final TransparentTopPanel topContainer;
    /**
     The main container panel will be used for the side panel as well as the content container.
     */
    private final JPanel mainContainer;
    /**
     The content container will be used for the text and picture content as well as the quoted comments.
     */
    private final JPanel contentContainer;
    private int layeredPanelWidth = 0;
    private int layeredPanelHeight = 0;
    private JTextPane jTextPane;

    public CommentMainPanel(CommentGuiDTO commentGuiDTO) {

        this.mainFrame = commentGuiDTO.mainFrame();
        this.side = new SideHandler().determineSide(commentGuiDTO.commentType());
        this.baseModel = commentGuiDTO.baseModel();
        this.sidePanel = commentGuiDTO.sidePanel();
        this.topContainer = commentGuiDTO.topContainer();
        this.topContainer.setBackground(Color.RED);
        this.topContainer.setOpaque(true);

        this.contentContainer = commentGuiDTO.container();
        this.mainContainer = commentGuiDTO.mainContainer();

        //FIXME remove this
        this.getTopContainer().setBorder(new LineBorder(Color.BLACK, 5));

        this.getMainContainer().setBorder(new LineBorder(Color.GREEN, 1));
        this.getSidePanel().setBorder(new LineBorder(Color.BLUE, 1));
        this.getContentContainer().setBorder(new LineBorder(Color.RED, 1));

        // PINK -> GREEN + BLACK
        // BLACK -> BLUE + GREEN
        // GREEN -> RED + BLUE

    }

    public JPanel getTopContainer() {

        return topContainer;
    }

    public JPanel getMainContainer() {

        return mainContainer;
    }

    public CommentSidePanel getSidePanel() {

        return sidePanel;
    }

    public JPanel getContentContainer() {

        return contentContainer;
    }

    public BaseModel getBaseModel() {

        return baseModel;
    }

    public void setLayoutManager() {

        this.setThisComponentLayoutManager();

        this.setMainContainerLayoutManager();
        this.setMainContentContainerLayoutManager();
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

    private void setMainContentContainerLayoutManager() {

        final JPanel mainContentPanel = this.getContentContainer();
        mainContentPanel.setLayout(new MigLayout("",
                                                 // columns
                                                 "[fill]",
                                                 // rows
                                                 "[]" + "[fill]" + "[]"));
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
                mainPanel.add(mainContentPanel, "cell 0 0 1 2, dock east, grow");
                topPanel.setAlignmentX(1.0f);
                topPanel.setAlignmentY(0.5f);
            }
        }

        this.getContentContainer().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(final ComponentEvent e) {

                getTopContainer().setMaximumSize(new Dimension(getContentContainer().getSize().width + getSidePanel().getWidth(), getContentContainer().getSize().height));
                super.componentResized(e);
                revalidate();
                repaint();
            }
        });
    }

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

        jTextPane = new CustomTextPane(true);
        jTextPane.setEditable(false);
        jTextPane.setBackground(null);
        jTextPane.setText(baseModel.getMessage());

        this.getContentContainer().add(jTextPane);

        this.getTopContainer().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(final MouseEvent e) {

                System.out.println("mouse entered");
                super.mouseEntered(e);
            }
        });
    }

    public JPanel getLayeredContainer() {

        return topContainer;
    }
}