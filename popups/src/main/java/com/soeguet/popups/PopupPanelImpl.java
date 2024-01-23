package com.soeguet.popups;

import com.soeguet.cache.factory.CacheManagerFactory;
import com.soeguet.cache.implementations.MessageQueue;
import com.soeguet.cache.manager.CacheManager;
import com.soeguet.popups.generated.PopupPanel;
import com.soeguet.popups.interfaces.PopupInterface;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class PopupPanelImpl extends PopupPanel implements PopupInterface {

  private final CacheManager cacheManager = CacheManagerFactory.getCacheManager();

  public PopupPanelImpl() {}

  @Override
  protected void messageTextFieldMouseClicked(final MouseEvent e) {
    // TODO is this needed?
    removeThisPopup();
  }

  private void removeThisPopup() {

    // TODO 1
    //        this.mainFrame.getMainTextPanelLayeredPane().remove(PopupPanelImpl.this);
    //        setVisible(false);
    //
    //        ((JFrame) mainFrame).revalidate();
    //        ((JFrame) mainFrame).repaint();
  }

  /**
   * Implements a popup with the given delay in milliseconds.
   *
   * @param delayMilliseconds The delay in milliseconds before the popup is displayed.
   */
  public void implementPopup(int delayMilliseconds) {

    initiatePopupTimer(delayMilliseconds);
  }

  /**
   * Initiates a timer to trigger a popup with the given delay in milliseconds.
   *
   * @param delayMilliseconds The delay in milliseconds before the popup is displayed.
   */
  @Override
  public void initiatePopupTimer(int delayMilliseconds) {

    Timer swingTimer =
        new Timer(
            delayMilliseconds,
            event -> {
              removeThisPopup();
              checkForMorePopupsInQueue();
            });

    swingTimer.setRepeats(false);
    swingTimer.start();
  }

  private void checkForMorePopupsInQueue() {

    MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");

    if (!messageQueue.isEmpty()) {

      try {

        recursivelyProcessStringsInQueue();

      } catch (InterruptedException e) {

        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Recursively processes strings in the message queue until it is empty. Throws
   * InterruptedException if the thread is interrupted while waiting for a message to be available
   * in the queue.
   *
   * @throws InterruptedException If the thread is interrupted while waiting for a message to be
   *     available in the queue.
   */
  private void recursivelyProcessStringsInQueue() throws InterruptedException {

    MessageQueue messageQueue = (MessageQueue) cacheManager.getCache("messageQueue");
    String message = messageQueue.pollFirst();

    generatePopup(message);
  }

  /**
   * Generates a popup with the given message.
   *
   * @param message The message to be displayed in the popup.
   */
  private void generatePopup(final String message) {

    PopupInterface popup = new PopupPanelImpl();
    popup.getMessageTextField().setText(message);
    popup.configurePopupPanelPlacement();
    popup.initiatePopupTimer(2_000);
  }

  /** Configures the placement of the popup panel within the main GUI. */
  @Override
  public void configurePopupPanelPlacement() {

    // TODO 1
    //        this.setBounds(
    //                (this.mainFrame.getMainTextPanelLayeredPane().getWidth() - 250) / 2, 100, 250,
    // 100);
    //        this.mainFrame.getMainTextPanelLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
  }
}
