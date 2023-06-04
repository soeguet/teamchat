package com.soeguet;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.soeguet.config.Settings;
import com.soeguet.gui.ChatImpl;

import javax.swing.*;
import java.util.prefs.Preferences;

public class Main {
  public static void main(String[] args) {

    Preferences preferences = Preferences.userNodeForPackage(Settings.class);

    switch (preferences.get("themeSetting", "LIGHT")) {
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
    }

    SwingUtilities.invokeLater(ChatImpl::new);
  }
}
