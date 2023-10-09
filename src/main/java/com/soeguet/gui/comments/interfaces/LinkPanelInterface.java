package com.soeguet.gui.comments.interfaces;

import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;
import java.awt.*;

public interface LinkPanelInterface {

    Color clickedLinkViolet = Color.decode("#551A8B");

void setCursorOnLink(Cursor cursor);
    void implementComment(Component accessible);

    void setBorderColor(Color borderColor);

    void addHyperlinkListener(Component component);

    JEditorPane createEditorPaneForLinks(MessageModel messageModel);

    void setupTimeField(BaseModel baseModel);

    void setupNameField(BaseModel baseModel);
}