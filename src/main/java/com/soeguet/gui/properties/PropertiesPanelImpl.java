package com.soeguet.gui.properties;

import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.properties.generated.PropertiesPanel;
import com.soeguet.properties.CustomUserProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

public class PropertiesPanelImpl extends PropertiesPanel {

    private final JFrame mainFrame;
    private final Point offset = new Point();
    private CustomUserProperties selectedClientInComboBox;

    public PropertiesPanelImpl(JFrame mainFrame) {

        this.mainFrame = mainFrame;

        setPosition();
        this.setVisible(true);

        addClientsToComboBox();
        setUpComponentsOnPropertiesPanel(selectedClientInComboBox);
    }

    private void setPosition() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        int textPaneWidth = gui.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = gui.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    private void addClientsToComboBox() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        gui.getChatClientPropertiesHashMap().forEach((key, value) -> {

            getClientSelectorComboBox().addItem(key);
        });

        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();
        selectedClientInComboBox = gui.getChatClientPropertiesHashMap().get(selectedItem);
    }

    private void setUpComponentsOnPropertiesPanel(CustomUserProperties client) {

        form_usernameTextField.setText(client.getUsername());
        form_nicknameTextField.setText(client.getNickname());
        form_colorPickerPanel.setBackground(new Color(client.getBorderColor()));
    }

    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }
        return (MainGuiElementsInterface) mainFrame;
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
    protected void thisMouseEntered(MouseEvent e) {

    }

    @Override
    protected void thisMouseExited(MouseEvent e) {

    }

    @Override
    protected void thisFocusLost(FocusEvent e) {

    }

    @Override
    protected void closeReplyPanelButtonMouseReleased(MouseEvent e) {

        this.removeAll();
        this.setVisible(false);
    }

    @Override
    protected void replyTextPaneFocusLost(FocusEvent e) {

    }

    @Override
    protected void quotePanelSenButtonMouseReleased(MouseEvent e) {

    }

    @Override
    protected void nicknameTextFieldFocusLost(FocusEvent e) {

    }

    @Override
    protected void clientSelectorComboBoxPropertyChange(PropertyChangeEvent e) {

        System.out.println("Property changed");
    }

    @Override
    protected void clientSelectorComboBoxItemStateChanged(ItemEvent e) {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        String selectedItem = (String) e.getItem();

        selectedClientInComboBox = gui.getChatClientPropertiesHashMap().get(selectedItem);

        setUpComponentsOnPropertiesPanel(selectedClientInComboBox);

        gui.getCustomProperties().save();
    }

    @Override
    protected void colorPickerPanelMouseClicked(MouseEvent e) {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return;
        }

        MainGuiElementsInterface gui = getMainFrame();

        //determin selected client from combobox
        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();

        Color borderColor = new Color(gui.getChatClientPropertiesHashMap().get(selectedItem).getBorderColor());

        Color color = JColorChooser.showDialog(this, "Choose a color", borderColor);
        getColorPickerPanel().setBackground(color);

        gui.getChatClientPropertiesHashMap().get(selectedItem).setBorderColor(color.getRGB());
    }
}
