package com.soeguet.gui.comments.interfaces;

import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;
import java.awt.*;

public interface LinkPanelInterface {

    void implementComment(Component accessible);

    void setBorderColor(Color borderColor);

    void addHyperlinkListener(Component component);

    JEditorPane createEditorPaneForLinks(MessageModel messageModel);
}