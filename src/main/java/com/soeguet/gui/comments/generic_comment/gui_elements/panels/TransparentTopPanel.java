package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.gui_elements.util.ChatBubblePaintHandler;
import com.soeguet.gui.comments.reaction_panel.ReactionPopupMenuImpl;
import com.soeguet.gui.comments.reaction_panel.dtos.ReactionPanelDTO;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TransparentTopPanel extends JPanel implements MouseListener, MouseMotionListener {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final CustomCommentPanel customCommentPanel;
    private final ReactionPanelDTO reactionPanelDTO;
    private ReactionPopupMenuImpl reactionPopupMenu;
    private ChatBubblePaintHandler chatBubblePaintHandler;
    private CustomContentContainer mainContentPanel;

    // variables -- end

    // constructors -- start
    public TransparentTopPanel(
            MainFrameGuiInterface mainFrame,
            final CustomCommentPanel customCommentPanel,
            ReactionPanelDTO reactionPanelDTO) {

        this.mainFrame = mainFrame;
        this.customCommentPanel = customCommentPanel;
        this.reactionPanelDTO = reactionPanelDTO;

        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    // constructors -- end

    /**
     * Dispatches a MouseEvent to the appropriate component. Else top panel grabs all events!
     *
     * @param e the MouseEvent to dispatch.
     */
    private void dispatchEvent(MouseEvent e) {

        final Component deepComponent =
                SwingUtilities.getDeepestComponentAt(mainContentPanel, e.getX(), e.getY());

        if (deepComponent == null) {
            return;
        }

        Point pt = SwingUtilities.convertPoint(this, e.getPoint(), deepComponent);

        final Component actualComponent =
                SwingUtilities.getDeepestComponentAt(deepComponent, pt.x, pt.y);

        if (actualComponent == null) {
            return;
        }

        if (!(actualComponent instanceof TransparentTopPanel)) {
            MouseEvent e2 =
                    new MouseEvent(
                            actualComponent,
                            e.getID(),
                            e.getWhen(),
                            e.getModifiersEx(),
                            pt.x,
                            pt.y,
                            e.getClickCount(),
                            e.isPopupTrigger(),
                            e.getButton());

            actualComponent.dispatchEvent(e2);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {

        dispatchEvent(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        dispatchEvent(e);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        dispatchEvent(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

        dispatchEvent(e);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

        dispatchEvent(e);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {

        if (reactionPopupMenu == null) {

            reactionPopupMenu = new ReactionPopupMenuImpl(reactionPanelDTO, this);
            reactionPopupMenu.setPopupMenuUp();
            reactionPopupMenu.initializePopupHandler();
        }

        if (!reactionPopupMenu.isVisible()) {

            reactionPopupMenu.startAnimation();
        }

        SwingUtilities.invokeLater(
                () -> {
                    this.mainFrame.repaint();
                });

        dispatchEvent(e);
    }

    @Override
    public void mouseExited(final MouseEvent e) {

        getChatBubblePaintHandler().setBorderColor(customCommentPanel.getBorderColor());

        if (reactionPopupMenu != null) {

            reactionPopupMenu.stopAnimation();
        }

        SwingUtilities.invokeLater(
                () -> {
                    this.mainFrame.repaint();
                });

        dispatchEvent(e);
    }

    // getter & setter -- start
    public ChatBubblePaintHandler getChatBubblePaintHandler() {

        if (chatBubblePaintHandler == null) {
            chatBubblePaintHandler = customCommentPanel.getChatBubblePaintHandler();
        }
        return chatBubblePaintHandler;
    }

    public void setReferenceToMainContentContainer(final CustomContentContainer mainContentPanel) {

        this.mainContentPanel = mainContentPanel;
    }
    // getter & setter -- end
}
