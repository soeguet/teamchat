package com.soeguet.gui.properties;

import com.soeguet.gui.main_frame.MainGuiElementsInterface;
import com.soeguet.gui.popups.PopupPanelImpl;
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

        setupOwnTabbedPane();
        setupClientsTabbedPane();
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
     This method sets up the own tabbed pane with the custom user properties.
     It sets the username text field with the username from the own client properties.
     It sets the background color of the border color panel with the border color from the own client properties.

     Preconditions:
     - The main frame must be set in the GUI.
     - The "own" chat client properties must exist in the chat client properties hash map.

     Postconditions:
     - The own tabbed pane is set up with the custom user properties.
     - The username text field is set with the username from the own client properties.
     - The background color of the border color panel is set with the border color from the own client properties.
     */
    private void setupOwnTabbedPane() {

        MainGuiElementsInterface gui = getMainFrame();

        if (gui == null) {

            return;
        }

        CustomUserProperties ownClient = gui.getChatClientPropertiesHashMap().get("own");

        if (ownClient == null) {

            ownClient = new CustomUserProperties();
        }

        if (ownClient.getUsername() == null) {

            getOwnUserNameTextField().setText("me");

        } else {

            getOwnUserNameTextField().setText(ownClient.getUsername());
        }

        getOwnBorderColorPanel().setBackground(new Color(ownClient.getBorderColor()));
    }

    /**
     This method sets up the clients tabbed pane.
     It adds clients to the combo box and sets up components on the properties panel.

     Preconditions:
     - None.

     Postconditions:
     - Clients are added to the combo box.
     - Components on the properties panel are set up.
     */
    private void setupClientsTabbedPane() {

        addClientsToComboBox();
        setUpComponentsOnPropertiesPanel(selectedClientInComboBox);
    }

    /**
     This method returns the main frame of the GUI, cast as a MainGuiElementsInterface object.
     The main frame must implement the MainGuiElementsInterface for the method to return a valid instance.
     If the main frame does not implement the MainGuiElementsInterface, null is returned.

     @return the main frame of the GUI as a MainGuiElementsInterface object, or null if the main frame does not implement the MainGuiElementsInterface.
     */
    private MainGuiElementsInterface getMainFrame() {

        if (!(mainFrame instanceof MainGuiElementsInterface)) {
            return null;
        }
        return (MainGuiElementsInterface) mainFrame;
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

            if (key.equals("own")) {
                return;
            }

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

        if (client == null) {

            return;
        }

        form_usernameTextField.setText(client.getUsername());
        form_nicknameTextField.setText(client.getNickname());
        form_colorPickerPanel.setBackground(new Color(client.getBorderColor()));
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
    protected void closePropertiesPanelButtonMouseReleased(MouseEvent e) {

        //TODO save properties

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

        if (gui == null || form_clientSelectorComboBox.getItemCount() == 0) {

            return;
        }

        String nickname = getNicknameTextField().getText();
        String username = getUsernameTextField().getText();

        gui.getChatClientPropertiesHashMap().get(username).setNickname(nickname);

        new PopupPanelImpl(gui.getMainFrame(), "nickname saved").implementPopup(1000);
    }

    @Override
    protected void clientSelectorComboBoxPropertyChange(PropertyChangeEvent e) {

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

        MainGuiElementsInterface gui = getMainFrame();
        assert gui != null;

        String username = getOwnUserNameTextField().getText();

        gui.getChatClientPropertiesHashMap().get("own").setUsername(username);
        gui.setUsername(username);

        gui.getCustomProperties().save();
    }

    @Override
    protected void ownBorderColorPanelMouseClicked(MouseEvent e) {

        MainGuiElementsInterface gui = getMainFrame();

        if (gui == null) {

            return;
        }

        CustomUserProperties ownProperties = gui.getChatClientPropertiesHashMap().get("own");
        Color borderColor = null;

        if (ownProperties != null) {

            borderColor = new Color(ownProperties.getBorderColor());

        } else {

            final String username = gui.getUsername() != null ? gui.getUsername() : "own";
            gui.setUsername(username);
            ownProperties = new CustomUserProperties(username);
            gui.getChatClientPropertiesHashMap().put("own", ownProperties);
        }

        Color color = JColorChooser.showDialog(this, "Choose a color", (borderColor != null ? borderColor : Color.BLACK));

        if (color != null) {

            getOwnBorderColorPanel().setBackground(color);
            ownProperties.setBorderColor(color.getRGB());
        }
    }

    @Override
    protected void colorPickerPanelMouseClicked(MouseEvent e) {

        MainGuiElementsInterface gui = getMainFrame();

        if (gui == null || form_clientSelectorComboBox.getItemCount() == 0) {

            return;
        }

        //determin selected client from combobox
        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();

        Color borderColor = new Color(gui.getChatClientPropertiesHashMap().get(selectedItem).getBorderColor());

        Color color = JColorChooser.showDialog(this, "Choose a color", borderColor);

        if (color != null) {

            getColorPickerPanel().setBackground(color);
            gui.getChatClientPropertiesHashMap().get(selectedItem).setBorderColor(color.getRGB());
        }
    }
}