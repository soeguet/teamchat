package com.soeguet.gui.properties;

import static com.soeguet.gui.ChatImpl.mapOfIps;
import static com.soeguet.gui.ChatImpl.MAPPER;

import com.soeguet.Main;
import com.soeguet.config.Settings;
import com.soeguet.config.ThemeSettings;
import com.soeguet.model.Clients;
import com.soeguet.model.ClientsList;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import org.jetbrains.annotations.NotNull;

public class PropertiesImpl extends PropertiesFrame {

  private final Settings settings;

  public PropertiesImpl() {
    super();
    settings = Settings.getInstance();
    updateSettingValues();
    populateIpTable();
  }

  private void populateIpTable() {
    DefaultTableModel model = (DefaultTableModel) form_participantsTable.getModel();
    mapOfIps.forEach((a, b) -> model.addRow(new Object[] {a, b}));
  }

  private void saveIpTableInTextFile() {

    mapOfIps.clear();

    int rowCount = form_participantsTable.getRowCount();
    ClientsList clientsList = new ClientsList();
    for (int i = 0; i < rowCount; i++) {

      mapOfIps.put(
          String.valueOf(form_participantsTable.getModel().getValueAt(i, 0)),
          String.valueOf(form_participantsTable.getModel().getValueAt(i, 1)));
      clientsList
          .getClientsList()
          .add(
              new Clients(
                  String.valueOf(form_participantsTable.getModel().getValueAt(i, 0)),
                  String.valueOf(form_participantsTable.getModel().getValueAt(i, 1))));
    }

    try {
      String s = MAPPER.writerFor(ClientsList.class).writeValueAsString(clientsList);

      File file;

      if (String.valueOf(PropertiesImpl.class.getResource("PropertiesImpl.class"))
          .startsWith("jar:")) {

        URI location = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();

        Path path = Paths.get(Paths.get(location).getParent().getParent() + "\\bin\\conf\\ips.txt");

        file = new File(path.toUri());
      } else {
        file = new File("src/main/resources/conf/ips.txt");
      }
      try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

        fileOutputStream.write(s.getBytes());
      }

    } catch (IOException | URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

  // for initial displaying live values when opening window
  private void updateSettingValues() {
    form_ipTextField.setText(settings.getIp());
    form_portTextField.setText(String.valueOf(settings.getPort()));
    form_widthSpinner.setValue(settings.getMainFrameWidth());
    form_heightSpinner.setValue(settings.getMainFrameHeight());
    themeComboSetSelectedIndex();

    form_fontSizeSlider.setValue(settings.getFontSize());
    form_exampleTextLabel.setFont(
        new Font(
            form_exampleTextLabel.getFont().getFontName(),
            form_exampleTextLabel.getFont().getStyle(),
            form_fontSizeSlider.getValue()));
    form_exampleTextLabel.setText("example text " + form_fontSizeSlider.getValue());

    form_widthSpinner.setValue(settings.getMainJFrame().getWidth());
    form_heightSpinner.setValue(settings.getMainJFrame().getHeight());

    form_durationSpinner.setValue(settings.getMessageDuration());
  }

  private void themeComboSetSelectedIndex() {

    switch (settings.getThemeSetting()) {
      case "WHITE":
        form_themeOptionComboBox.setSelectedIndex(0);
        break;
      case "DARK":
        form_themeOptionComboBox.setSelectedIndex(1);
        break;
      case "INTELLIJ":
        form_themeOptionComboBox.setSelectedIndex(2);
        break;
      case "DARCULA":
        form_themeOptionComboBox.setSelectedIndex(3);
        break;
    }
  }

  private void resizeMainFrame() {

    SwingUtilities.invokeLater(
        () -> {
          settings
              .getMainJFrame()
              .setSize(
                  (Integer) form_widthSpinner.getModel().getValue(),
                  (Integer) form_heightSpinner.getModel().getValue());
          settings.getMainJFrame().setLocationRelativeTo(null);
          settings.getMainJFrame().revalidate();
          settings.getMainJFrame().repaint();
        });
  }

  private void getSelectionTheme() {

    switch (Objects.requireNonNull(form_themeOptionComboBox.getSelectedItem())
        .toString()
        .toUpperCase()) {
      case "WHITE":
        settings.setThemeSetting(ThemeSettings.LIGHT);
        break;
      case "DARK":
        settings.setThemeSetting(ThemeSettings.DARK);
        break;
      case "INTELLIJ":
        settings.setThemeSetting(ThemeSettings.INTELLIJ);
        break;
      case "DARCULA":
        settings.setThemeSetting(ThemeSettings.DARCULA);
        break;
    }
  }

  @Override
  protected void addRowButtonMouseClicked(MouseEvent e) {

    DefaultTableModel model = (DefaultTableModel) form_participantsTable.getModel();
    model.addRow(new Object[] {"ip", "name"});
  }

  @Override
  protected void deleteRowButtonMouseClicked(MouseEvent e) {

    int selectedRow = form_participantsTable.getSelectedRow();
    DefaultTableModel model = (DefaultTableModel) form_participantsTable.getModel();
    try {
      model.removeRow(selectedRow);
    } catch (ArrayIndexOutOfBoundsException ignored) {
    }
  }

  @Override
  protected void cancelButtonMouseClicked(MouseEvent e) {
    SwingUtilities.invokeLater(this::dispose);
  }

  @Override
  protected void thisWindowActivated(WindowEvent e) {
    if (!this.isVisible()) {
      updateSettingValues();
    }
  }

  @Override
  protected void fontSizeSliderStateChanged(ChangeEvent e) {

    form_exampleTextLabel.setFont(
        new Font(
            form_exampleTextLabel.getFont().getFontName(),
            form_exampleTextLabel.getFont().getStyle(),
            form_fontSizeSlider.getValue()));
    form_exampleTextLabel.setText("example text " + form_fontSizeSlider.getValue());

    if (form_fontSizeSlider.getValue() > 28) {

      pack();
      revalidate();
      repaint();
      setLocationRelativeTo(null);
    }
  }

  @Override
  protected void okButtonMouseClicked(@NotNull MouseEvent e) {

    saveIpTableInTextFile();

    getSelectionTheme();

    String newIP = form_ipTextField.getText();
    settings.setIp(newIP);

    //        address matching suspended
    //        boolean b = Pattern.matches("(^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})", newIP);
    //        if (b) {
    //            settings.setIp(newIP);
    //        }else {
    //            JOptionPane.showMessageDialog(this,"ip address wrong format!");
    //        }

    String newPort = form_portTextField.getText();
    settings.setPort(Integer.parseInt(newPort));

    //        port matching suspended
    //        boolean c = Pattern.matches("[0-9]+", newPort);
    //        if (c) {
    //            settings.setPort(Integer.parseInt(newPort));
    //        }else {
    //            JOptionPane.showMessageDialog(this,"port wrong format!");
    //        }

    settings.setMainFrameWidth((Integer) form_widthSpinner.getModel().getValue());
    settings.setMainFrameHeight((Integer) form_heightSpinner.getModel().getValue());
    resizeMainFrame();

    settings.setFontSize(form_fontSizeSlider.getValue());
    settings.setMessageDuration((int) form_durationSpinner.getValue());

    SwingUtilities.invokeLater(this::dispose);
    e.consume();
  }

  @Override
  protected void portTextFieldFocusGained(FocusEvent e) {
    form_portTextField.setSelectionStart(0);
    form_portTextField.setSelectionEnd(999);
  }

  @Override
  protected void ipTextFieldFocusGained(FocusEvent e) {
    form_ipTextField.setSelectionStart(0);
    form_ipTextField.setSelectionEnd(999);
  }
}
