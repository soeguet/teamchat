package com.pre.gui.newcomment.pane;

import com.pre.config.Settings;

import javax.swing.*;
import java.awt.*;

public class TextPaneImpl extends TextPaneComment {

    public TextPaneImpl() {

        super();

        Settings settings = Settings.getInstance();

        this.setFont(new Font(getFont().getFontName(), getFont().getStyle(), settings.getFontSize()));
        this.repaint();
    }

    @Override
    public void setText(String t) {

        super.setText(t);
    }

    @Override
    public void insertIcon(Icon g) {

        super.insertIcon(g);
    }
}
