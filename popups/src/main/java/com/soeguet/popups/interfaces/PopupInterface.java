package com.soeguet.popups.interfaces;

import javax.swing.*;

public interface PopupInterface {

  void configurePopupPanelPlacement();

  void initiatePopupTimer(int delayMilliseconds);

  JTextField getMessageTextField();
}
