package com.soeguet;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.gui.main_frame.ChatMainFrameImpl;
import com.soeguet.model.EnvVariables;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

//        setTheme('');

        EnvVariables envVariables = checkForUsername();
        FlatIntelliJLaf.setup();
        SwingUtilities.invokeLater(() -> new ChatMainFrameImpl(envVariables));
    }

    private static EnvVariables checkForUsername() {

        final String chatUsername = System.getenv("chat.username");

        String username;

        if (chatUsername != null && !chatUsername.isEmpty()) {

            username = System.getenv("chat.username");

        } else {

            username = JOptionPane.showInputDialog(null, "Please enter your username", "");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (username == null || username.isEmpty()) {

            SwingUtilities.invokeLater(() -> {

                JOptionPane.showMessageDialog(null, "Username must not be empty", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            });

        }

        String chatIp = System.getenv("chat.ip");
        String chatPort = System.getenv("chat.port");

        return new EnvVariables(username, chatIp, chatPort);
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