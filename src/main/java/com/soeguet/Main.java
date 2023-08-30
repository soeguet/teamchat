package com.soeguet;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.config.Settings;
import com.soeguet.gui.ChatGuiImpl;
import com.soeguet.gui.ChatImpl;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Settings settings = Settings.getInstance();

        setTheme(settings.getThemeSetting());

        SwingUtilities.invokeLater(ChatGuiImpl::new);
    }

    private static void setTheme(String themeSetting) {

        if (themeSetting == null) {
            throw new IllegalArgumentException("Theme setting must not be null");
        }

        switch (themeSetting.toUpperCase()) {
            case "LIGHT":
                FlatLightLaf.setup();
                break;
            case "DARK":
                FlatDarkLaf.setup();
                break;
            case "DARCULA":
                FlatDarculaLaf.setup();
                break;
            default:
                FlatIntelliJLaf.setup();
                break;
        }
    }
}
