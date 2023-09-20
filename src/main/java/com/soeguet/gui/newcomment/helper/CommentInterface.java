package com.soeguet.gui.newcomment.helper;

import javax.swing.*;

public interface CommentInterface {

    JLabel getNameLabel();
    JPanel getPanel1();
    void setupTextPanel();
    void setupPicturePanel();
    void addRightClickOptionToPanel();
}