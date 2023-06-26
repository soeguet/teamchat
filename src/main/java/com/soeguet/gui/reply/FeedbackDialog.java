/*
 * Created by JFormDesigner on Sun Mar 05 19:57:07 CET 2023
 */

package com.soeguet.gui.reply;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 * @author osmansogut
 */
public abstract class FeedbackDialog extends JDialog {

  public FeedbackDialog() {
    super();
    setModal(true);
    initComponents();
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  protected JPanel form_dialogPane;
  protected JPanel form_contentPanel;
  protected JScrollPane form_panel1;
  protected JTextPane form_replyTextPane;
  protected JButton form_emojiButton;
  protected JButton form_sendButton;

  protected abstract void sendMessageReply();

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // @formatter:off
    this.form_dialogPane = new JPanel();
    this.form_contentPanel = new JPanel();
    this.form_panel1 = new JScrollPane();
    this.form_replyTextPane = new JTextPane();
    this.form_emojiButton = new JButton();
    this.form_sendButton = new JButton();

    // ======== this ========
    setMaximumSize(new Dimension(450, 2147483647));
    setAlwaysOnTop(true);
    setType(Type.POPUP);
    setName("this"); // NON-NLS
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentHidden(ComponentEvent e) {
            thisComponentHidden(e);
          }
        });
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // ======== form_dialogPane ========
    {
      this.form_dialogPane.setRequestFocusEnabled(false);
      this.form_dialogPane.setFocusable(false);
      this.form_dialogPane.setVerifyInputWhenFocusTarget(false);
      this.form_dialogPane.setName("dialogPane"); // NON-NLS
      this.form_dialogPane.addKeyListener(
          new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
              dialogPaneKeyPressed(e);
            }
          });
      this.form_dialogPane.setLayout(new BorderLayout());

      // ======== form_contentPanel ========
      {
        this.form_contentPanel.setMaximumSize(new Dimension(450, 2147483647));
        this.form_contentPanel.setFocusable(false);
        this.form_contentPanel.setRequestFocusEnabled(false);
        this.form_contentPanel.setVerifyInputWhenFocusTarget(false);
        this.form_contentPanel.setName("contentPanel"); // NON-NLS
        this.form_contentPanel.setLayout(
            new MigLayout(
                "fill,insets dialog,align center center", // NON-NLS
                // columns
                "[grow,fill]"
                    + // NON-NLS
                    "[right]"
                    + // NON-NLS
                    "[right]", // NON-NLS
                // rows
                "[]"
                    + // NON-NLS
                    "[]")); // NON-NLS

        // ======== form_panel1 ========
        {
          this.form_panel1.setName("panel1"); // NON-NLS

          // ---- form_replyTextPane ----
          this.form_replyTextPane.setPreferredSize(new Dimension(250, 50));
          this.form_replyTextPane.setMinimumSize(new Dimension(250, 50));
          this.form_replyTextPane.setFocusTraversalPolicyProvider(true);
          this.form_replyTextPane.setName("replyTextPane"); // NON-NLS
          this.form_replyTextPane.addKeyListener(
              new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                  replyTextPaneKeyPressed(e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                  replyTextPaneKeyReleased(e);
                }
              });
          this.form_panel1.setViewportView(this.form_replyTextPane);
        }
        this.form_contentPanel.add(this.form_panel1, "cell 0 1,growy"); // NON-NLS

        // ---- form_emojiButton ----
        this.form_emojiButton.setText("\u263a"); // NON-NLS
        this.form_emojiButton.setDoubleBuffered(true);
        this.form_emojiButton.setMinimumSize(new Dimension(50, 50));
        this.form_emojiButton.setPreferredSize(new Dimension(50, 50));
        this.form_emojiButton.setMaximumSize(new Dimension(32767, 50));
        this.form_emojiButton.setName("emojiButton"); // NON-NLS
        this.form_emojiButton.addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                emojiButtonMouseClicked(e);
              }
            });
        this.form_contentPanel.add(this.form_emojiButton, "cell 1 1"); // NON-NLS

        // ---- form_sendButton ----
        this.form_sendButton.setText("\u2192"); // NON-NLS
        this.form_sendButton.setMinimumSize(new Dimension(50, 50));
        this.form_sendButton.setMaximumSize(new Dimension(32767, 50));
        this.form_sendButton.setPreferredSize(new Dimension(50, 50));
        this.form_sendButton.setDoubleBuffered(true);
        this.form_sendButton.setName("sendButton"); // NON-NLS
        this.form_sendButton.addActionListener(this::sendButton);
        this.form_contentPanel.add(this.form_sendButton, "cell 2 1"); // NON-NLS
      }
      this.form_dialogPane.add(this.form_contentPanel, BorderLayout.CENTER);
    }
    contentPane.add(this.form_dialogPane, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
  }

  protected abstract void dialogPaneKeyPressed(KeyEvent e);

  protected abstract void sendButton(ActionEvent e);

  protected abstract void thisComponentHidden(ComponentEvent e);

  protected abstract void replyTextPaneKeyReleased(KeyEvent e);

  protected abstract void replyTextPaneKeyPressed(KeyEvent e);

  protected abstract void emojiButtonMouseClicked(MouseEvent e);
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
