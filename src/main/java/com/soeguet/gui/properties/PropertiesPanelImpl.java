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

    /**
     This method sets the position of the current element.
     It sets the bounds of the element based on the size of the main text panel in the GUI.
     The element is added to the main text panel layered pane with a modal layer.

     Preconditions:
     - The main frame must be set in the GUI.

     Postconditions:
     - The position of the element is set.
     - The element is added to the main text panel layered pane.
     */
    private void setPosition() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        int textPaneWidth = gui.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = gui.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        gui.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
    }

    /**
     This method adds clients to the combo box.
     It retrieves the main frame from the GUI and asserts that it is not null.
     For each client in the chat client properties hash map, the client is added to the combo box.
     The selected item in the combo box is retrieved as a string.
     The selected client in the combo box is set based on the chat client properties hash map.

     Preconditions:
     - The main frame must be set in the GUI.
     - The chat client properties hash map must not be null.

     Postconditions:
     - The clients are added to the combo box.
     - The selected client in the combo box is set.
     */
    private void addClientsToComboBox() {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        gui.getChatClientPropertiesHashMap().forEach((key, value) -> {

            getClientSelectorComboBox().addItem(key);
        });

        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();
        selectedClientInComboBox = gui.getChatClientPropertiesHashMap().get(selectedItem);
    }

    /**
     This method sets up the components on the properties panel using the given CustomUserProperties object.
     It sets the username text field with the username from the CustomUserProperties object.
     It sets the nickname text field with the nickname from the CustomUserProperties object.
     It sets the background color of the color picker panel with the border color from the CustomUserProperties object.

     Preconditions:
     - The CustomUserProperties object must not be null.

     Postconditions:
     - The components on the properties panel are set up with the values from the CustomUserProperties object.
     */
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

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        String nickname = getNicknameTextField().getText();
        String username = getUsernameTextField().getText();

        gui.getChatClientPropertiesHashMap().get(username).setNickname(nickname);

        gui.getCustomProperties().save();
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

        gui.getCustomProperties().save();

        setUpComponentsOnPropertiesPanel(selectedClientInComboBox);
    }

    @Override
    protected void ownUserNameTextFieldFocusLost(FocusEvent e) {

    }

    @Override
    protected void ownBorderColorPanelMouseClicked(MouseEvent e) {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        Color borderColor = new Color(gui.getChatClientPropertiesHashMap().get("own").getBorderColor());

        Color color = JColorChooser.showDialog(this, "Choose a color", borderColor);
        getColorPickerPanel().setBackground(color);

        gui.getChatClientPropertiesHashMap().get("own").setBorderColor(color.getRGB());
    }

    @Override
    protected void colorPickerPanelMouseClicked(MouseEvent e) {

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        //determin selected client from combobox
        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();

        Color borderColor = new Color(gui.getChatClientPropertiesHashMap().get(selectedItem).getBorderColor());

        Color color = JColorChooser.showDialog(this, "Choose a color", borderColor);
        getColorPickerPanel().setBackground(color);

        gui.getChatClientPropertiesHashMap().get(selectedItem).setBorderColor(color.getRGB());
    }
}