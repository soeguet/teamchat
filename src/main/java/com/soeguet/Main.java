package com.soeguet;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.gui.main_frame.ChatMainGuiElementsImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        System. out. println("Your OS name -> " + System. getProperty("os.name"));
//        setTheme('');
        FlatIntelliJLaf.setup();
        SwingUtilities.invokeLater(ChatMainGuiElementsImpl::new);
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
