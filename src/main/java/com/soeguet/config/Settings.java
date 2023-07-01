package com.soeguet.config;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.swing.*;

import com.soeguet.gui.ChatImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Settings {
  private static Settings instance;
  private final File settingsFile;
//  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final Preferences PREFERENCES = Preferences.userNodeForPackage(Settings.class);
  private JFrame mainJFrame;

  private Settings() {
    File settingsFolder = new File(System.getProperty("user.home") + "/.teamchat");
    if (!settingsFolder.exists()) {
      if (settingsFolder.mkdir()) {
        Logger LOG = getLogger(Settings.class);
        LOG.atInfo().log("Created settings folder");
      }
    }
    settingsFile = new File(System.getProperty("user.home") + "/.teamchat/settings.json");
    if (!settingsFile.exists()) {
      createDefaultSettingsFile();
    }
  }

  private void createDefaultSettingsFile() {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode root = objectMapper.createObjectNode();

    ObjectNode userPreferencesNode = userPreferencesNode(objectMapper);
    root.set("userPreferences", userPreferencesNode);

    ObjectNode chatParticipantsNode = objectMapper.createObjectNode();
    ObjectNode participantsNode = objectMapper.createObjectNode();
    participantsNode.put("name", "me");
    participantsNode.put("fontColor", 0);
    participantsNode.put("borderColor", 0);
    participantsNode.put("quoteColor", 0);
    chatParticipantsNode.set("127.0.0.1", participantsNode);


    root.set("chatParticipants", chatParticipantsNode);

    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile, root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  private ObjectNode userPreferencesNode(ObjectMapper objectMapper) {
    ObjectNode userPreferencesNode = objectMapper.createObjectNode();
    userPreferencesNode.put("ip", "127.0.0.1");
    userPreferencesNode.put("port", "8100");
    userPreferencesNode.put("width", "550");
    userPreferencesNode.put("height", "750");
    userPreferencesNode.put("themeSetting", "INTELLIJ");
    userPreferencesNode.put("fontSize", 15);
    userPreferencesNode.put("textColor", 0);
    userPreferencesNode.put("notificationDuration", 5);
    return userPreferencesNode;
  }

  public static Settings getInstance() {

    if (instance == null) {
      instance = new Settings();
    }
    return instance;
  }

  public String getThemeSetting() {

    try {
      JsonNode root = ChatImpl.MAPPER.readTree(settingsFile);
      return root.get("userPreferences").get("themeSetting").asText();
    } catch (IOException e) {
      showErrorMessage("Error while reading settings - themeSetting");
    }
    return "INTELLIJ";
  }

  private void showErrorMessage(String errorMessage) {
    JOptionPane.showInternalMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public void setThemeSetting(ThemeSettings themeSetting) {

    updateUserPreferences("themeSetting", themeSetting.toString());
  }

  public JFrame getMainJFrame() {

    return mainJFrame;
  }

  public void setMainJFrame(JFrame mainJFrame) {

    this.mainJFrame = mainJFrame;
  }

  public int getMainFrameWidth() {

    return PREFERENCES.getInt("mainFrameWidth", (mainJFrame != null ? mainJFrame.getWidth() : 400));
  }

  public void setMainFrameWidth(int mainFrameWidth) {

    PREFERENCES.putInt("mainFrameWidth", mainFrameWidth);
  }

  public int getMainFrameHeight() {

    return PREFERENCES.getInt(
        "mainFrameHeight", (mainJFrame != null ? mainJFrame.getHeight() : 600));
  }

  public void setMainFrameHeight(int mainFrameHeight) {

    PREFERENCES.putInt("mainFrameHeight", mainFrameHeight);
  }

  public Color getTextColor() {

    try {
      JsonNode root = ChatImpl.MAPPER.readTree(settingsFile);
      return new Color(root.get("userPreferences").get("textColor").asInt());
    } catch (IOException e) {
      showErrorMessage("Error while reading settings - textColor");
    }
    return new Color(0);
  }

  public int getFontSize() {
    try {
      JsonNode root = ChatImpl.MAPPER.readTree(settingsFile);
      return root.get("userPreferences").get("fontSize").asInt();
    } catch (IOException e) {
      showErrorMessage("Error while reading settings - fontSize");
    }
    return 12;
  }

  public void setFontSize(int fontSize) {

    updateUserPreferences("fontSize", fontSize);
  }

  public int getMessageDuration() {
    try {
      JsonNode root = ChatImpl.MAPPER.readTree(settingsFile);
      return root.get("userPreferences").get("notificationDuration").asInt();
    } catch (IOException e) {
      showErrorMessage("Error while reading settings - notificationDuration");
    }
    return 5;
  }

  public void setMessageDuration(int seconds) {

    updateUserPreferences("notificationDuration", seconds);
  }

  public int getPort() {
    try {
      JsonNode root = ChatImpl.MAPPER.readTree(settingsFile);
      return root.get("userPreferences").get("port").asInt();
    } catch (IOException e) {
      showErrorMessage("Error while reading settings - port");
    }
    return 8100;
  }

  public void setPort(int port) {

    updateUserPreferences("port", port);
  }

  public String getIp() {
    try {
      JsonNode root = ChatImpl.MAPPER.readTree(settingsFile);
      return root.get("userPreferences").get("ip").asText();
    } catch (IOException e) {
      showErrorMessage("Error while reading settings - ip");
    }
    return "192.168.178.69";
  }

  public void setIp(String ip) {

    updateUserPreferences("ip", ip);
  }

  private synchronized <T> void updateUserPreferences(String key, T value) {

    try {
      ObjectNode root = (ObjectNode) ChatImpl.MAPPER.readTree(settingsFile);
      ObjectNode userPreferencesNode = root.get("userPreferences").deepCopy();

      if (value instanceof String) {
        userPreferencesNode.put(key, (String) value);

      } else if (value instanceof Integer) {
        userPreferencesNode.put(key, (Integer) value);
      } else {
        throw new IllegalArgumentException("Unsupported value type");
      }
      root.set("userPreferences", userPreferencesNode);
      ChatImpl.MAPPER.writerWithDefaultPrettyPrinter().writeValue(settingsFile, root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
