package com.soeguet.gui.comments.interfaces;

import javax.swing.*;
import java.awt.*;

public interface CommentInterface {

    JLabel getNameLabel();

    JPanel getContainer();
    void setupTextPanelWrapper();
    void setupPicturePanelWrapper();
    void setBorderColor(Color borderColor);
}