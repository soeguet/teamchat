package com.soeguet.gui.interaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.gui.interaction.generated.ReplyPanel;
import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.newcomment.left.PanelLeftImpl;
import com.soeguet.gui.newcomment.right.PanelRightImpl;
import com.soeguet.model.MessageModel;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.PanelTypes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

public class ReplyPanelImpl extends ReplyPanel {

    private final JFrame mainFrame;
    private final MessageModel messageModel;
    private Border border = this.getBorder();

    final Point offset = new Point();

    public ReplyPanelImpl(JFrame mainFrame, MessageModel messageModel) {
        this.mainFrame = mainFrame;
        this.messageModel = messageModel;

        populatePanel();
        setPosition();

        requestAllFocus();
    }

    private void requestAllFocus() {

        SwingUtilities.invokeLater(() -> {

            this.setBorder(new LineBorder(Color.BLUE));

            this.getReplyTextPane().requestFocus();
            this.getReplyTextPane().grabFocus();
        });
    }

    private void populatePanel() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        if (gui.getUsername().equals(messageModel.getSender())) {

            PanelRightImpl panelRight = new PanelRightImpl(mainFrame, messageModel, PanelTypes.REPLY);
            this.getFormerMessagePanel().add(panelRight);

        } else {

            PanelLeftImpl panelLeft = new PanelLeftImpl(mainFrame, messageModel, PanelTypes.REPLY);
            this.getFormerMessagePanel().add(panelLeft);
        }
    }

    private void setPosition() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        int textPaneWidth = gui.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = gui.getMainTextPanelLayeredPane().getHeight();


        int height = (int) this.getPreferredSize().getHeight();

        //make it look a little less stuffed
        if (this.getPreferredSize().getWidth() > 500) {
            height += 100;
        } else {
            height += 30;
        }

        this.setBounds((textPaneWidth - 500) / 2, (textPaneHeight - height) / 2, 500, height);
    }


    @Override
    protected void thisMousePressed(MouseEvent e) {

        offset.setLocation(e.getX(), e.getY());
    }

    @Override
    protected void thisMouseDragged(MouseEvent e) {
        int x = e.getX() - offset.x + this.getX();
        int y = e.getY() - offset.y + this.getY();
        this.setLocation(x, y);
    }

    @Override
    protected void replySendButtonMouseReleased(MouseEvent e) {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = (MainGuiElementsInterface) mainFrame;

        MessageModel sendModel = new MessageModel((byte) MessageTypes.NORMAL, "osman", this.getReplyTextPane().getText(), messageModel.getSender(), messageModel.getTime(), messageModel.getMessage());


        try {

            gui.getWebsocketClient().send(gui.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sendModel));

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(ex);
        }

        this.removeAll();
        this.setVisible(false);
    }

    @Override
    protected void closeReplyPanelButtonMouseReleased(MouseEvent e) {

        this.removeAll();
        this.setVisible(false);
    }

    @Override
    protected void thisMouseEntered(MouseEvent e) {

        this.setBorder(new LineBorder(Color.BLUE));
    }

    @Override
    protected void thisMouseExited(MouseEvent e) {


        if (!getReplyTextPane().hasFocus()) {

            if (e.getX() < 0 || e.getY() < 0 || e.getX() > this.getWidth() - 1 || e.getY() > this.getHeight() - 1) {

                this.setBorder(border);
            }
        }
    }

    @Override
    protected void thisFocusLost(FocusEvent e) {
        System.out.println("lost");
    }

    @Override
    protected void replyTextPaneFocusLost(FocusEvent e) {


    }
}
