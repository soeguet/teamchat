package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.factories.ReferencePanelFactory;
import com.soeguet.gui.comments.generic_comment.gui_elements.buttons.CustomCloseButton;
import com.soeguet.gui.comments.generic_comment.gui_elements.textpanes.CustomSimpleTextPane;
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
    private final int CORNER_SIZE = 15;
    private Point previousPoint = new Point();
    private boolean isResizing = false;
    private boolean isMoving = false;
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

    public void moveReplyPanelToCenter() {

        final int width = this.getWidth();
        final int height = this.getHeight();
        final int mainFrameWidth = mainFrame.getMainTextPanelLayeredPane().getWidth();
        final int mainFrameHeight = mainFrame.getMainTextPanelLayeredPane().getHeight();

        this.setLocation((mainFrameWidth - width) / 2, (mainFrameHeight - height) / 2);
    }

    public void setCustomReplyPanelLayoutManger() {

        /*
        SCHEMA: ReplyPanel

        [[_________title__________][x]]     // contains title and close button
        [[______referenced_stuff_____]]     // contains refereced stuff <- will contain CustomQuotePanel (?)
        [ [text_pane] [ [1] [2] [3] ] ]     //text pane and buttons

         */

        super.setLayout(new MigLayout("insets 0",
                                      //columns
                                      "[fill,grow,center]",
                                      //rows
                                      "[fill][fill,grow][fill,bottom]"));
    }

    public void setMaximumSizeWithingMainFrame() {

        super.setBounds(0, 0, this.getPreferredSize().width + 50, this.getPreferredSize().height + 50);
    }

    public void populateCustomReplyPanel() {

        //TITLE
        populateReplyPanelTitle();

        //REFERENCE
        populateReplyPanelReferenceSection();

        //BOTTOM
        populateReplyPanelBottomSection();
    }

    private void populateReplyPanelBottomSection() {

        //create panel itself
        CustomSimpleJPanel bottomPanel = new CustomSimpleJPanel(new MigLayout("",
                                                                              //columns
                                                                              "[fill,grow][fill]",
                                                                              //rows
                                                                              "[fill]"));

        // -> TEXT PANE
        CustomSimpleTextPane textPane = new CustomSimpleTextPane();
        bottomPanel.add(textPane, "cell 0 0");

        // -> BUTTON PANEL
        CustomSimpleJPanel buttonPanel = new CustomSimpleJPanel(new MigLayout("",
                                                                              //columns
                                                                              "[fill][fill][fill]",
                                                                              //rows
                                                                              "[fill]"));
        populateButtonPanel(buttonPanel);
        bottomPanel.add(buttonPanel, "cell 1 0");

        //add panel to reply panel
        super.add(bottomPanel, "cell 0 2");
    }

    private void populateButtonPanel(final CustomSimpleJPanel buttonPanel) {

        JButton emojiButton = new JButton("E");
        buttonPanel.add(emojiButton, "cell 0 0");

        JButton attachButton = new JButton("A");
        buttonPanel.add(attachButton, "cell 1 0");

        JButton sendButton = new JButton("S");
        buttonPanel.add(sendButton, "cell 2 0");
    }

    private void populateReplyPanelReferenceSection() {

        CustomReferencePanel customReferencePanel = new ReferencePanelFactory(baseModel).createReferencePanel();
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

        CustomCloseButton closeButton = new CustomCloseButton(this, "x");
        titlePanel.add(closeButton, "cell 1 0");

        super.add(titlePanel, "cell 0 0");
    }

    private boolean isInCorner(Point p) {

        Dimension size = this.getSize();
        return p.x >= size.width - CORNER_SIZE && p.y >= size.height - CORNER_SIZE;
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
            System.out.println("is in corner!");
        } else {
            isMoving = true;
            System.out.println("is moving!");
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
}