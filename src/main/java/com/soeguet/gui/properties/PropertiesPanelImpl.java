package com.soeguet.gui.properties;

import com.soeguet.gui.main_frame.MainFrameInterface;
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

    private final MainFrameInterface mainFrame;
    private final Point offset = new Point();
    private CustomUserProperties selectedClientInComboBox;

    public PropertiesPanelImpl(MainFrameInterface mainFrame) {

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

        int textPaneWidth = mainFrame.getMainTextPanelLayeredPane().getWidth();
        int textPaneHeight = mainFrame.getMainTextPanelLayeredPane().getHeight();

        this.setBounds(20, 20, textPaneWidth - 40, textPaneHeight - 40);

        mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
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

        CustomUserProperties ownClient = mainFrame.getChatClientPropertiesHashMap().get("own");

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

        mainFrame.getChatClientPropertiesHashMap().forEach((key, value) -> {

            if (key.equals("own")) {
                return;
            }

            getClientSelectorComboBox().addItem(key);
        });

        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();
        selectedClientInComboBox = mainFrame.getChatClientPropertiesHashMap().get(selectedItem);
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

    }

    @Override
    protected void clientSelectorComboBoxPropertyChange(PropertyChangeEvent e) {

    }

    @Override
    protected void clientSelectorComboBoxItemStateChanged(ItemEvent e) {

    }

    @Override
    protected void ownUserNameTextFieldFocusLost(FocusEvent e) {

    }

    @Override
    protected void propertiesOkButtonMousePressed(final MouseEvent e) {

        String ownName = getOwnUserNameTextField().getText();

        mainFrame.getChatClientPropertiesHashMap().get("own").setUsername(ownName);
        mainFrame.setUsername(ownName);

///
        final String selectedUsername = selectedClientInComboBox.getUsername();
        selectedClientInComboBox = mainFrame.getChatClientPropertiesHashMap().get(selectedUsername);

///

        if (form_clientSelectorComboBox.getItemCount() == 0) {

            return;
        }

        String nickname = getNicknameTextField().getText();
        String username = getUsernameTextField().getText();

        mainFrame.getChatClientPropertiesHashMap().get(username).setNickname(nickname);



        ///

        mainFrame.getCustomProperties().save();

        new PopupPanelImpl(mainFrame,"nickname saved").implementPopup(1000);
        setUpComponentsOnPropertiesPanel(selectedClientInComboBox);
    }

    @Override
    protected void ownBorderColorPanelMouseClicked(MouseEvent e) {

        CustomUserProperties ownProperties = mainFrame.getChatClientPropertiesHashMap().get("own");
        Color borderColor = null;

        if (ownProperties != null) {

            borderColor = new Color(ownProperties.getBorderColor());

        } else {

            final String username = mainFrame.getUsername() != null ? mainFrame.getUsername() : "own";
            mainFrame.setUsername(username);
            ownProperties = new CustomUserProperties(username);
            mainFrame.getChatClientPropertiesHashMap().put("own", ownProperties);
        }

        Color color = JColorChooser.showDialog(this, "Choose a color", (borderColor != null ? borderColor : Color.BLACK));

        if (color != null) {

            getOwnBorderColorPanel().setBackground(color);
            ownProperties.setBorderColor(color.getRGB());
        }
    }

    @Override
    protected void colorPickerPanelMouseClicked(MouseEvent e) {


        if (form_clientSelectorComboBox.getItemCount() == 0) {

            return;
        }

        //determin selected client from combobox
        String selectedItem = (String) getClientSelectorComboBox().getSelectedItem();

        Color borderColor = new Color(mainFrame.getChatClientPropertiesHashMap().get(selectedItem).getBorderColor());

        Color color = JColorChooser.showDialog(this, "Choose a color", borderColor);

        if (color != null) {

            getColorPickerPanel().setBackground(color);
            mainFrame.getChatClientPropertiesHashMap().get(selectedItem).setBorderColor(color.getRGB());
        }
    }
}