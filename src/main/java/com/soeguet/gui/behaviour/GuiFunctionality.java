package com.soeguet.gui.behaviour;

import com.soeguet.gui.main_frame.MainGuiInterface;
import com.soeguet.socket_client.CustomWebsocketClient;

import javax.swing.*;
import java.awt.*;

public class GuiFunctionality {

    private final JFrame mainFrame;

    public GuiFunctionality(JFrame mainFrame) {

        this.mainFrame = mainFrame;
    }

    public void clearTextPaneAndSendMessageToSocket() {

        if (mainFrame instanceof MainGuiInterface) {

            ((MainGuiInterface) mainFrame).getMainTextPanel().setAutoscrolls(true);

            JTextPane textPane = ((MainGuiInterface) mainFrame).getTextEditorPane();
            String textPaneContent = textPane.getText();
            textPane.setText("");

            JPanel mainTextPanel = ((MainGuiInterface) mainFrame).getMainTextPanel();
            JLabel myLabel = new JLabel(textPaneContent);
            mainTextPanel.add(myLabel, "wrap");

            mainTextPanel.repaint();
            mainTextPanel.revalidate();

            JScrollPane scrollPane = ((MainGuiInterface) mainFrame).getMainTextBackgroundScrollPane();
            scrollMainPanelDownToLastMessage(scrollPane);
        }
    }

    private void scrollMainPanelDownToLastMessage(JScrollPane scrollPane) {

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getMaximumSize().height));
    }
}
