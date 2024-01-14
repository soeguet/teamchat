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
            final MainFrameGuiInterface mainFrame,
            final CustomCommentPanel customCommentPanel,
            final ReactionPanelDTO reactionPanelDTO) {

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
    private void dispatchEvent(final MouseEvent e) {

        final Component deepComponent =
                SwingUtilities.getDeepestComponentAt(this.mainContentPanel, e.getX(), e.getY());

        if (deepComponent == null) {
            return;
        }

        final Point pt = SwingUtilities.convertPoint(this, e.getPoint(), deepComponent);

        final Component actualComponent =
                SwingUtilities.getDeepestComponentAt(deepComponent, pt.x, pt.y);

        if (actualComponent == null) {
            return;
        }

        if (!(actualComponent instanceof TransparentTopPanel)) {
            final MouseEvent e2 =
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

        this.dispatchEvent(e);
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        this.dispatchEvent(e);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

        this.dispatchEvent(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

        this.dispatchEvent(e);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {

        this.dispatchEvent(e);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {

        if (this.reactionPopupMenu == null) {

            this.reactionPopupMenu = new ReactionPopupMenuImpl(this.reactionPanelDTO, this);
            this.reactionPopupMenu.setPopupMenuUp();
            this.reactionPopupMenu.initializePopupHandler();
        }

        if (!this.reactionPopupMenu.isVisible()) {

            this.reactionPopupMenu.startAnimation();
        }

        SwingUtilities.invokeLater(
                () -> {
                    this.mainFrame.repaint();
                });

        this.dispatchEvent(e);
    }

    @Override
    public void mouseExited(final MouseEvent e) {

        this.getChatBubblePaintHandler().setBorderColor(this.customCommentPanel.getBorderColor());

        if (this.reactionPopupMenu != null) {

            this.reactionPopupMenu.stopAnimation();
        }

        SwingUtilities.invokeLater(
                () -> {
                    this.mainFrame.repaint();
                });

        this.dispatchEvent(e);
    }

    // getter & setter -- start
    public ChatBubblePaintHandler getChatBubblePaintHandler() {

        if (this.chatBubblePaintHandler == null) {
            this.chatBubblePaintHandler = this.customCommentPanel.getChatBubblePaintHandler();
        }
        return this.chatBubblePaintHandler;
    }

    public void setReferenceToMainContentContainer(final CustomContentContainer mainContentPanel) {

        this.mainContentPanel = mainContentPanel;
    }
    // getter & setter -- end
}
