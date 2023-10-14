package com.soeguet.gui.interrupt_dialog;

import com.soeguet.gui.interrupt_dialog.generated.InterruptDialog;
import com.soeguet.gui.interrupt_dialog.interfaces.InterruptDialogInterface;
import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.properties.CustomUserProperties;
import com.soeguet.util.ByteArrayHandler;
import com.soeguet.util.interfaces.ByteArrayHandlerInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterruptDialogImpl extends InterruptDialog implements InterruptDialogInterface {

    private final MainFrameInterface mainFrame;
    private final List<JCheckBox> clientCheckBoxList;

    public InterruptDialogImpl(final Window owner) {

        super(owner);
        this.mainFrame = (MainFrameInterface) owner;
        clientCheckBoxList = new ArrayList<>();
    }

    @Override
    public void populateDialogWithAllRegisteredClients(HashMap<String, CustomUserProperties> clientPropertiesMap) {


        clientPropertiesMap.forEach((id, client) -> {

            //we don't need this client in the list
            if (!client.getUsername().equals("own")) {

                JCheckBox clientCheckBox = new JCheckBox();

                clientCheckBox.setText(client.getUsername());

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