package com.soeguet;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.config.Settings;
import com.soeguet.gui.ChatImpl;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        /*
         * load preferences and set theme while startup
         */
        Settings settings = Settings.getInstance();

        switch (settings.getThemeSetting()) {
            case "LIGHT":
                FlatLightLaf.setup();
                break;
            case "DARK":
                FlatDarkLaf.setup();
                break;
            case "INTELLIJ":
                FlatIntelliJLaf.setup();
                break;
            case "DARCULA":
                FlatDarculaLaf.setup();
                break;
            default:
                break;
        }

        SwingUtilities.invokeLater(ChatImpl::new);
    }
}
