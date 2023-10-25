package com.soeguet.gui.comments.interfaces;

import javax.swing.*;
import java.awt.*;

public interface CommentInterface {

    JLabel getNameLabel();

    void setupTextPanelWrapper();

    void setupPicturePanelWrapper();

    void setBorderColor(Color borderColor);

    void initializeBorderHandler(final Color borderColor);
}