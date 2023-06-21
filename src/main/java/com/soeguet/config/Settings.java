package com.soeguet.config;

import java.awt.Color;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

public class Settings {

  private static Settings instance;

  private final Preferences preferences;

  private JFrame mainJFrame;

  private Settings() {

    preferences = Preferences.userNodeForPackage(Settings.class);
  }

  public static Settings getInstance() {

    if (instance == null) {
      instance = new Settings();
    }
    return instance;
  }

  public int getNotificationSetting() {

    return preferences.getInt("notificationSetting", 0);
  }

  public void setNotificationSetting(NotificationSettings notificationSetting) {

    preferences.putInt("notificationSetting", notificationSetting.ordinal());
  }

  public String getThemeSetting() {

    return preferences.get("themeSetting", "INTELLIJ");
  }

  public void setThemeSetting(ThemeSettings themeSetting) {

    preferences.put("themeSetting", themeSetting.toString());
  }

  public JFrame getMainJFrame() {

    return mainJFrame;
  }

  public void setMainJFrame(JFrame mainJFrame) {

    this.mainJFrame = mainJFrame;
  }

  public int getMainFrameWidth() {

    return preferences.getInt("mainFrameWidth", (mainJFrame != null ? mainJFrame.getWidth() : 400));
  }

  public void setMainFrameWidth(int mainFrameWidth) {

    preferences.putInt("mainFrameWidth", mainFrameWidth);
  }

  public int getMainFrameHeight() {

    return preferences.getInt(
        "mainFrameHeight", (mainJFrame != null ? mainJFrame.getHeight() : 600));
  }

  public void setMainFrameHeight(int mainFrameHeight) {

    preferences.putInt("mainFrameHeight", mainFrameHeight);
  }

  public Color getTextColor() {

    return new Color(preferences.getInt("textColor", 0));
  }

  public void setTextColor(Color color) {

    preferences.putInt("textColor", color.getRGB());
  }

  public Color getChatColor() {

    return new Color(preferences.getInt("chatColor", -2710887));
  }

  public void setChatColor(Color color) {

    preferences.putInt("chatColor", color.getRGB());
  }

  public Color getBorderColor() {

    return new Color(preferences.getInt("borderColor", -2710887));
  }

  public void setBorderColor(Color color) {

    preferences.putInt("borderColor", color.getRGB());
  }

  public int getFontSize() {

    return preferences.getInt("fontSize", 12);
  }

  public void setFontSize(int fontSize) {

    preferences.putInt("fontSize", fontSize);
  }

  public int getMessageDuration() {

    return preferences.getInt("messageSeconds", 5);
  }

  public void setMessageDuration(int seconds) {

    preferences.putInt("messageSeconds", seconds);
  }

  public int getPort() {

    return preferences.getInt("port", 8100);
  }

  public void setPort(int port) {

    preferences.putInt("port", port);
  }

  public String getIp() {

    return preferences.get("ip", "192.168.178.69");
  }

  public void setIp(String ip) {

    preferences.put("ip", ip);
  }
}
