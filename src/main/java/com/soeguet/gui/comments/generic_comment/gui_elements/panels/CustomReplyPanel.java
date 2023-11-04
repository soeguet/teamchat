package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.factories.ReferencePanelFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.buttons.CustomCloseButton;
import com.soeguet.gui.comments.generic_comment.gui_elements.buttons.CustomReplySendButton;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.jackson.BaseModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class CustomReplyPanel extends JPanel implements MouseListener, MouseMotionListener {

    // variables -- start
    private final MainFrameGuiInterface mainFrame;
    private final BaseModel baseModel;
    private final int CORNER_SIZE = 15;
    private Point previousPoint = new Point();
    private boolean isResizing = false;
    private boolean isMoving = false;
    private CustomSimpleTextPane textPane;
    private Dimension maximumSize;
    // variables -- end

    // constructors -- start
    public CustomReplyPanel(MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;

        super.addMouseListener(this);
        super.addMouseMotionListener(this);

        super.setBorder(new LineBorder(Color.DARK_GRAY, 2));
    }
    // constructors -- end

    public void setCustomReplyPanelLayoutManger() {

        /*
        SCHEMA: CustomReplyPanel
                                                                                            __search for:
        [[_________title__________][x]]     // contains title and close button              #titlePanel
        [[______reference panel_____]]      // contains referenced stuff <- will contain    #CustomReferencePanel
        [ [text_pane] [ [1] [2] [3] ] ]     //text pane and buttons                         #textPane
                                                                                            #sendButton
         */

        super.setLayout(new MigLayout("insets 0",
                                      //columns
                                      "[fill,grow,center]",
                                      //rows
                                      "[fill][fill,grow][fill,bottom]"));
    }

    public void moveReplyPanelToCenter() {

        final int width = this.getWidth();
        final int height = this.getHeight();

        final int mainFrameWidth = mainFrame.getMainTextPanelLayeredPane().getWidth();
        final int mainFrameHeight = mainFrame.getMainTextPanelLayeredPane().getHeight();

        this.setLocation((mainFrameWidth - width) / 2, (mainFrameHeight - height) / 2);
    }

    public void setMaximumSizeWithingMainFrame() {

        int width = 600;
        int height = this.getPreferredSize().height + 50;

        super.setBounds(0, 0, width, height);

        this.setMaximumSize(new Dimension(width, height));
    }

    public void populateCustomReplyPanel() {

        //TITLE
        this.populateReplyPanelTitle();

        //REFERENCE
        this.populateReplyPanelReferenceSection();

        //BOTTOM
        this.populateReplyPanelBottomSection();
    }

    private void populateReplyPanelBottomSection() {

        //create panel itself
        CustomSimpleJPanel bottomPanel = new CustomSimpleJPanel(new MigLayout("",
                                                                              //columns
                                                                              "[fill,grow][fill]",
                                                                              //rows
                                                                              "[fill]"));

        // -> TEXT PANE
        this.textPane = new CustomSimpleTextPane(mainFrame);

        this.textPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        bottomPanel.add(this.textPane, "cell 0 0");

        // -> BUTTON PANEL
        CustomSimpleJPanel buttonPanel = new CustomSimpleJPanel(new MigLayout("",
                                                                              //columns
                                                                              "[fill][fill][fill]",
                                                                              //rows
                                                                              "[fill]"));
        this.populateButtonPanel(buttonPanel);
        bottomPanel.add(buttonPanel, "cell 1 0");

        //add panel to reply panel
        super.add(bottomPanel, "cell 0 2");
        this.textPane.requestFocus();
    }

    private void populateButtonPanel(final CustomSimpleJPanel buttonPanel) {

        // [1] EMOJI BUTTON
        JButton emojiButton = new JButton("E");
        emojiButton.setEnabled(false);
        buttonPanel.add(emojiButton, "cell 0 0");

        // [2] ATTACH BUTTON
        JButton attachButton = new JButton("A");
        attachButton.setEnabled(false);
        buttonPanel.add(attachButton, "cell 1 0");

        // [3] SEND BUTTON
        CustomReplySendButton sendButton = new CustomReplySendButton(this.mainFrame, this.baseModel, this);
        buttonPanel.add(sendButton, "cell 2 0");
    }

    private void populateReplyPanelReferenceSection() {

        CustomReferencePanel customReferencePanel =
                new ReferencePanelFactory(this.mainFrame, this.baseModel).createReferencePanel();
        customReferencePanel.setLayoutManager();

        super.add(customReferencePanel, "cell 0 1, grow, gapleft 10, gapright 10");
    }

    private void populateReplyPanelTitle() {

        JPanel titlePanel = new JPanel();

        titlePanel.setBackground(new Color(189, 189, 189, 255));
        titlePanel.setLayout(new MigLayout("",
                                           //columns
                                           "[fill,grow][fill]",
                                           //rows
                                           "[fill, center]"));

        JLabel titleLabel = new JLabel("Reply to: " + baseModel.getSender());
        titlePanel.add(titleLabel, "cell 0 0");

        CustomCloseButton closeButton = new CustomCloseButton(mainFrame, this, "x");
        titlePanel.add(closeButton, "cell 1 0");

        super.add(titlePanel, "cell 0 0");
    }

    private boolean isInCorner(Point p) {

        Dimension size = this.getSize();
        return p.x >= size.width - CORNER_SIZE && p.y >= size.height - CORNER_SIZE;
    }

    public void setFocusOnTextPane() {

        SwingUtilities.invokeLater(() -> {

            textPane.requestFocus(true);
            textPane.requestFocusInWindow();
        });
    }

    @Override
    protected void paintComponent(final Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        final int width = this.getWidth();
        final int height = this.getHeight();

        g2d.setColor(Color.LIGHT_GRAY);

        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(width - CORNER_SIZE, height, width, height - CORNER_SIZE);

        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(width - CORNER_SIZE + 5, height, width, height - CORNER_SIZE + 5);
        g2d.drawLine(width - CORNER_SIZE + 8, height, width, height - CORNER_SIZE + 8);
        g2d.drawLine(width - CORNER_SIZE + 11, height, width, height - CORNER_SIZE + 11);
    }

    @Override
    public void mouseDragged(final MouseEvent e) {

        if (isResizing) {

            Point currentPoint = e.getPoint();
            Dimension currentSize = this.getSize();

            int dx = (int) (currentPoint.getX() - previousPoint.getX());
            int dy = (int) (currentPoint.getY() - previousPoint.getY());

            int newX = (int) (currentSize.getWidth() + dx);
            int newY = (int) (currentSize.getHeight() + dy);

            if (newX < this.getMaximumSize().getWidth()) {

                newX = (int) this.getMaximumSize().getWidth();
            }

            if (newY < this.getMaximumSize().getHeight()) {

                newY = (int) this.getMaximumSize().getHeight();
            }

            this.setSize(newX, newY);

            previousPoint = currentPoint;
        }

        if (isMoving) {

            int x = e.getX() - previousPoint.x + this.getX();
            int y = e.getY() - previousPoint.y + this.getY();
            this.setLocation(x, y);
        }

        //this is needed, else the panel will not adjust to the new size
        revalidate();
        repaint();
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

    }

    @Override
    public void mouseClicked(final java.awt.event.MouseEvent e) {

    }

    @Override
    public void mousePressed(final java.awt.event.MouseEvent e) {

        if (isInCorner(e.getPoint())) {

            isResizing = true;

        } else {

            isMoving = true;
        }

        previousPoint.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(final java.awt.event.MouseEvent e) {

        isMoving = false;
        isResizing = false;
    }

    @Override
    public void mouseEntered(final java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseExited(final java.awt.event.MouseEvent e) {

    }

    // getter & setter -- start

    public CustomSimpleTextPane getTextPane() {

        return textPane;
    }

    // getter & setter -- end
}