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

            JTextPane textPane = ((MainGuiInterface) mainFrame).getTextEditorPane();
            String textPaneContent = textPane.getText();
            textPane.setText("");

            CustomWebsocketClient websocketClient = ((MainGuiInterface) mainFrame).getWebsocketClient();
            System.out.println("websocketClient.getReadyState() = " + websocketClient.getReadyState());

            JPanel mainTextPanel = ((MainGuiInterface) mainFrame).getMainTextPanel();
            JLabel myLabel = new JLabel(textPaneContent);
            mainTextPanel.add(myLabel, "wrap");

            mainTextPanel.repaint();
            mainTextPanel.revalidate();

            Rectangle rectangle = myLabel.getBounds();
            rectangle.y = myLabel.getY();
            rectangle.height = myLabel.getHeight();


            //scroll mainTextPanel to last message
            JScrollPane scrollPane = ((MainGuiInterface) mainFrame).getScrollPane1();

            scrollPane.scrollRectToVisible(rectangle);

        }
    }
}
