package com.soeguet.gui.option_pane.links.interfaces;

import javax.swing.*;
import java.awt.*;

public interface LinkDialogInterface {

    void setVisible(boolean b);
    JPanel getContentPanel();
    void setSize(Dimension dimension);
    void pack();

    void setLocationRelativeTo(Component component);
}