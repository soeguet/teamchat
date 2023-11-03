package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import com.soeguet.gui.comments.generic_comment.factories.QuotePanelFactory;
import com.soeguet.gui.comments.util.WrapEditorKit;
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

        [[          title         ][x]] // contains title and close button
        [[        quoted stuff       ]] // contains former, quoted stuff <- will contain CustomQuotePanel (?)
        [ [text pane] [ [1] [2] [3] ] ] //text pane and buttons

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

        // TODO: 02.11.23 this should be done in a factory class

        super.setBorder(new LineBorder(Color.DARK_GRAY, 2));

        //TITLE
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(189, 189, 189, 255));
        titlePanel.setLayout(new MigLayout("",
                                           //columns
                                           "[fill,grow][fill]",
                                           //rows
                                           "[fill, center]"));
        JLabel titleLabel = new JLabel("Reply to: " + baseModel.getSender());
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel, "cell 0 0");
        JButton closeButton = new JButton("x");
        titlePanel.add(closeButton, "cell 1 0");
        closeButton.addMouseListener(new MouseAdapter() {

            // overrides -- start
            @Override
            public void mouseClicked(final MouseEvent e) {

                CustomReplyPanel.super.setVisible(false);
            }
            // overrides -- end
        });
        add(titlePanel, "cell 0 0");

        //QUOTE
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new MigLayout("",
                                            //columns
                                            "[fill,grow]",
                                            //rows
                                            "[fill,grow]"));

        CustomQuotePanel customQuoteReplyPanel = new QuotePanelFactory(baseModel).createReplyQuotePanel();
        customQuoteReplyPanel.setLayoutManager();
        customQuoteReplyPanel.createQuotedTextPane();
        middlePanel.add(customQuoteReplyPanel, "cell 0 0");
        add(middlePanel, "cell 0 1, grow");

        //BOTTOM
        // -> TEXT PANE
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new MigLayout("",
                                            //columns
                                            "[fill,grow][fill]",
                                            //rows
                                            "[fill]"));
        JTextPane textPane = new JTextPane();
        textPane.setEditorKit(new WrapEditorKit());
        bottomPanel.add(textPane, "cell 0 0");

        // -> BUTTON PANEL
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new MigLayout("",
                                            //columns
                                            "[fill][fill][fill]",
                                            //rows
                                            "[fill]"));
        JButton emojiButton = new JButton("E");
        buttonPanel.add(emojiButton, "cell 0 0");

        JButton attachButton = new JButton("A");
        buttonPanel.add(attachButton, "cell 1 0");

        JButton sendButton = new JButton("S");
        buttonPanel.add(sendButton, "cell 2 0");

        bottomPanel.add(buttonPanel, "cell 1 0");

        this.add(bottomPanel, "cell 0 2");

    }

    private boolean isInCorner(Point p) {

        Dimension size = this.getSize();
        return p.x >= size.width - CORNER_SIZE && p.y >= size.height - CORNER_SIZE;
    }

    // overrides -- start
    // overrides -- start
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

        revalidate();
        repaint();

        //
        //        Point currentPoint = e.getPoint();
        //
        //        if (!isInCorner(currentPoint)) {
        //            int x = e.getX() - previousPoint.x + this.getX();
        //            int y = e.getY() - previousPoint.y + this.getY();
        //            this.setLocation(x, y);
        //
        //        } else {
        //
        //            System.out.println("is corner!");
        //            int x = e.getX() - previousPoint.x;
        //            int y = e.getY() - previousPoint.y;
        //            System.out.println("x: " + x + " y: " + y);
        //            this.setSize(this.getSize().width + x, this.getSize().height + y);
        //            System.out.println("width: " + this.getSize().width + " height: " + this.getSize().height);
        //            this.revalidate();
        //        }
        //            int dx = currentPoint.x - previousPoint.x;
        //            int dy = currentPoint.y - previousPoint.y;
        //            Dimension size = this.getSize();
        //            this.setSize(size.width + dx, size.height + dy);
        //            this.revalidate();
        //        }
        //
        //        else {
        //            System.out.println("currentPoint: " + currentPoint);
        //            int dx = currentPoint.x - previousPoint.x;
        //            int dy = currentPoint.y - previousPoint.y;
        //            this.setLocation(this.getLocation().x + dx, this.getLocation().y + dy);
        //        }
        //        previousPoint = currentPoint;

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
    // overrides -- end
    // overrides -- end
}