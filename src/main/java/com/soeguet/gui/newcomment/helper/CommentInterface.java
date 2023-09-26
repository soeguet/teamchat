package com.soeguet.gui.newcomment.helper;

import javax.swing.*;
import java.awt.*;

public interface CommentInterface {

    JLabel getNameLabel();
    JPanel getPanel1();
    void setupTextPanelWrapper();
    void setupPicturePanelWrapper();
    void addRightClickOptionToPanel();
    void setBorderColor(Color borderColor);
}