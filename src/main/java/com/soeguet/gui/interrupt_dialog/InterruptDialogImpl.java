package com.soeguet.gui.interrupt_dialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;

import com.soeguet.gui.interrupt_dialog.generated.InterruptDialog;
import com.soeguet.gui.interrupt_dialog.interfaces.InterruptDialogInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.properties.dto.CustomUserPropertiesDTO;
import com.soeguet.util.ByteArrayHandler;
import com.soeguet.util.interfaces.ByteArrayHandlerInterface;

public class InterruptDialogImpl extends InterruptDialog implements InterruptDialogInterface {

    private final MainFrameGuiInterface mainFrame;
    private final List<JCheckBox> clientCheckBoxList;

    public InterruptDialogImpl(final Window owner) {

        super(owner);
        this.mainFrame = (MainFrameGuiInterface) owner;
        clientCheckBoxList = new ArrayList<>();
    }

    @Override
    public void populateDialogWithAllRegisteredClients(HashMap<String, CustomUserPropertiesDTO> clientPropertiesMap) {

        clientPropertiesMap.forEach((id, client) -> {

            //we don't need this client in the list
            if (!client.username().equals("own")) {

                JCheckBox clientCheckBox = new JCheckBox();

                clientCheckBox.setText(client.username());

                clientCheckBoxList.add(clientCheckBox);
                form_checkBoxPanel.add(clientCheckBox,-1);
            }
        });
    }

    @Override
    protected void okButtonActionPerformed(final ActionEvent e) {

        List<String> selectedClients = new ArrayList<>();

        clientCheckBoxList.forEach(box -> {

            if (box.isSelected()) {

                selectedClients.add(box.getText());
            }
        });

        ByteArrayHandlerInterface byteArrayHandler = new ByteArrayHandler();
        byte[] clientByteArray = byteArrayHandler.convertListToByteArray(selectedClients);

        mainFrame.getWebsocketClient().send(clientByteArray);

        cancelButtonActionPerformed(e);
    }

    @Override
    protected void cancelButtonActionPerformed(final ActionEvent e) {

        this.dispose();
        this.setVisible(false);
    }
}
