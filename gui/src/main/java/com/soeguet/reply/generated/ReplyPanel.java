package com.soeguet.reply.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/*
 * Created by JFormDesigner on Sun Sep 10 12:09:03 CEST 2023
 */

/**
 * @author soeguet
 */
public abstract class ReplyPanel extends JPanel {
  public ReplyPanel() {
    initComponents();
  }

  public JPanel getFormerMessagePanel() {
    return form_formerMessagePanel;
  }

  public JPanel getPanel2() {
    return form_panel2;
  }

  public JScrollPane getScrollPane1() {
    return form_scrollPane1;
  }

  public JTextPane getReplyTextPane() {
    return form_replyTextPane;
  }

  public JPanel getPanel3() {
    return form_panel3;
  }

  public JButton getQuotePanelPictureButton() {
    return form_quotePanelPictureButton;
  }

  public JButton getQuotePanelEmojiButton() {
    return form_quotePanelEmojiButton;
  }

  public JButton getQuotePanelSendButton() {
    return form_quotePanelSendButton;
  }

  protected abstract void thisMousePressed(MouseEvent e);

  protected abstract void thisMouseDragged(MouseEvent e);

  public JLabel getLabel1() {
    return form_label1;
  }

  public JPanel getPanel4() {
    return form_panel4;
  }

  public JButton getCloseReplyPanelButton() {
    return form_closeReplyPanelButton;
  }

  protected abstract void closeReplyPanelButtonMouseReleased(MouseEvent e);

  protected abstract void thisMouseEntered(MouseEvent e);

  protected abstract void thisMouseExited(MouseEvent e);

  protected abstract void thisFocusLost(FocusEvent e);

  protected abstract void replyTextPaneFocusLost(FocusEvent e);

  public JPanel getHSpacer1() {
    return form_hSpacer1;
  }

  public JPanel getHSpacer2() {
    return form_hSpacer2;
  }

  public JTextPane getMainQuoteTextField() {
    return form_mainQuoteTextField;
  }

  public JScrollPane getPanel5() {
    return form_panel5;
  }

  public JPanel getPanel6() {
    return form_panel6;
  }

  public JPanel getPanel7() {
    return form_panel7;
  }

  public JLabel getQuotedTime() {
    return form_quotedTime;
  }

  public JLabel getLabel3() {
    return form_label3;
  }

  public JLabel getQuotedSender() {
    return form_quotedSender;
  }

  public JPanel getVSpacer1() {
    return form_vSpacer1;
  }

  public JPanel getVSpacer2() {
    return form_vSpacer2;
  }

  public JPanel getHSpacer3() {
    return form_hSpacer3;
  }

  public JPanel getHSpacer4() {
    return form_hSpacer4;
  }

  public JPanel getVSpacer3() {
    return form_vSpacer3;
  }

  public JPanel getPanel8() {
    return form_panel8;
  }

  protected abstract void replyTextPaneKeyPressed(KeyEvent e);

  protected abstract void quotePanelPictureButtonMouseClicked(MouseEvent e);

  protected abstract void quotePanelEmojiButtonMouseClicked(MouseEvent e);

  protected abstract void quotePanelSendButtonMouseClicked(MouseEvent e);

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // @formatter:off
    // Generated using JFormDesigner non-commercial license
    form_panel4 = new JPanel();
    form_label1 = new JLabel();
    form_closeReplyPanelButton = new JButton();
    form_formerMessagePanel = new JPanel();
    form_hSpacer1 = new JPanel(null);
    form_hSpacer2 = new JPanel(null);
    form_vSpacer2 = new JPanel(null);
    form_panel6 = new JPanel();
    form_panel5 = new JScrollPane();
    form_mainQuoteTextField = new JTextPane();
    form_panel7 = new JPanel();
    form_quotedTime = new JLabel();
    form_label3 = new JLabel();
    form_quotedSender = new JLabel();
    form_vSpacer1 = new JPanel(null);
    form_panel2 = new JPanel();
    form_hSpacer3 = new JPanel(null);
    form_hSpacer4 = new JPanel(null);
    form_vSpacer3 = new JPanel(null);
    form_panel8 = new JPanel();
    form_scrollPane1 = new JScrollPane();
    form_replyTextPane = new JTextPane();
    form_panel3 = new JPanel();
    form_quotePanelPictureButton = new JButton();
    form_quotePanelEmojiButton = new JButton();
    form_quotePanelSendButton = new JButton();

    // ======== this ========
    setBorder(new EtchedBorder());
    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            thisMouseEntered(e);
          }

          @Override
          public void mouseExited(MouseEvent e) {
            thisMouseExited(e);
          }

          @Override
          public void mousePressed(MouseEvent e) {
            thisMousePressed(e);
          }
        });
    addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent e) {
            thisMouseDragged(e);
          }
        });
    addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            thisFocusLost(e);
          }
        });
    setLayout(new BorderLayout());

    // ======== form_panel4 ========
    {
      form_panel4.setLayout(new BorderLayout());

      // ---- form_label1 ----
      form_label1.setText("reply");
      form_label1.setHorizontalTextPosition(SwingConstants.CENTER);
      form_label1.setHorizontalAlignment(SwingConstants.CENTER);
      form_label1.setFocusable(false);
      form_label1.setEnabled(false);
      form_panel4.add(form_label1, BorderLayout.CENTER);

      // ---- form_closeReplyPanelButton ----
      form_closeReplyPanelButton.setText("x");
      form_closeReplyPanelButton.setBorder(UIManager.getBorder("TitledBorder.border"));
      form_closeReplyPanelButton.addMouseListener(
          new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
              closeReplyPanelButtonMouseReleased(e);
            }
          });
      form_panel4.add(form_closeReplyPanelButton, BorderLayout.EAST);
    }
    add(form_panel4, BorderLayout.PAGE_START);

    // ======== form_formerMessagePanel ========
    {
      form_formerMessagePanel.setLayout(new BorderLayout());

      // ---- form_hSpacer1 ----
      form_hSpacer1.setMinimumSize(new Dimension(15, 12));
      form_hSpacer1.setPreferredSize(new Dimension(15, 10));
      form_formerMessagePanel.add(form_hSpacer1, BorderLayout.WEST);

      // ---- form_hSpacer2 ----
      form_hSpacer2.setMinimumSize(new Dimension(15, 12));
      form_hSpacer2.setPreferredSize(new Dimension(15, 10));
      form_formerMessagePanel.add(form_hSpacer2, BorderLayout.EAST);

      // ---- form_vSpacer2 ----
      form_vSpacer2.setPreferredSize(new Dimension(10, 15));
      form_vSpacer2.setMinimumSize(new Dimension(12, 15));
      form_formerMessagePanel.add(form_vSpacer2, BorderLayout.NORTH);

      // ======== form_panel6 ========
      {
        form_panel6.setLayout(new BorderLayout());

        // ======== form_panel5 ========
        {
          form_panel5.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

          // ---- form_mainQuoteTextField ----
          form_mainQuoteTextField.setEditable(false);
          form_mainQuoteTextField.setEnabled(false);
          form_panel5.setViewportView(form_mainQuoteTextField);
        }
        form_panel6.add(form_panel5, BorderLayout.CENTER);

        // ======== form_panel7 ========
        {
          form_panel7.setLayout(new FlowLayout(FlowLayout.LEADING));

          // ---- form_quotedTime ----
          form_quotedTime.setText("text");
          form_quotedTime.setEnabled(false);
          form_quotedTime.setFocusable(false);
          form_panel7.add(form_quotedTime);

          // ---- form_label3 ----
          form_label3.setText("  -  ");
          form_label3.setEnabled(false);
          form_label3.setFocusable(false);
          form_panel7.add(form_label3);

          // ---- form_quotedSender ----
          form_quotedSender.setText("text");
          form_quotedSender.setFocusable(false);
          form_quotedSender.setEnabled(false);
          form_panel7.add(form_quotedSender);
        }
        form_panel6.add(form_panel7, BorderLayout.NORTH);

        // ---- form_vSpacer1 ----
        form_vSpacer1.setMinimumSize(new Dimension(12, 15));
        form_vSpacer1.setPreferredSize(new Dimension(10, 15));
        form_panel6.add(form_vSpacer1, BorderLayout.SOUTH);
      }
      form_formerMessagePanel.add(form_panel6, BorderLayout.CENTER);
    }
    add(form_formerMessagePanel, BorderLayout.CENTER);

    // ======== form_panel2 ========
    {
      form_panel2.setLayout(new BorderLayout());
      form_panel2.add(form_hSpacer3, BorderLayout.WEST);
      form_panel2.add(form_hSpacer4, BorderLayout.EAST);
      form_panel2.add(form_vSpacer3, BorderLayout.SOUTH);

      // ======== form_panel8 ========
      {
        form_panel8.setLayout(new BorderLayout(5, 0));

        // ======== form_scrollPane1 ========
        {

          // ---- form_replyTextPane ----
          form_replyTextPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
          form_replyTextPane.addFocusListener(
              new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                  replyTextPaneFocusLost(e);
                }
              });
          form_replyTextPane.addKeyListener(
              new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                  replyTextPaneKeyPressed(e);
                }
              });
          form_scrollPane1.setViewportView(form_replyTextPane);
        }
        form_panel8.add(form_scrollPane1, BorderLayout.CENTER);

        // ======== form_panel3 ========
        {
          form_panel3.setLayout(new GridLayout(1, 3, 4, 0));

          // ---- form_quotePanelPictureButton ----
          form_quotePanelPictureButton.setText("@");
          form_quotePanelPictureButton.setPreferredSize(new Dimension(35, 35));
          form_quotePanelPictureButton.setMaximumSize(new Dimension(35, 35));
          form_quotePanelPictureButton.setMinimumSize(new Dimension(35, 35));
          form_quotePanelPictureButton.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  quotePanelPictureButtonMouseClicked(e);
                }
              });
          form_panel3.add(form_quotePanelPictureButton);

          // ---- form_quotePanelEmojiButton ----
          form_quotePanelEmojiButton.setText("#");
          form_quotePanelEmojiButton.setPreferredSize(new Dimension(35, 35));
          form_quotePanelEmojiButton.setMaximumSize(new Dimension(35, 35));
          form_quotePanelEmojiButton.setMinimumSize(new Dimension(35, 35));
          form_quotePanelEmojiButton.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  quotePanelEmojiButtonMouseClicked(e);
                }
              });
          form_panel3.add(form_quotePanelEmojiButton);

          // ---- form_quotePanelSendButton ----
          form_quotePanelSendButton.setText("\u2192");
          form_quotePanelSendButton.setMinimumSize(new Dimension(35, 35));
          form_quotePanelSendButton.setPreferredSize(new Dimension(35, 35));
          form_quotePanelSendButton.setMaximumSize(new Dimension(35, 35));
          form_quotePanelSendButton.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  quotePanelSendButtonMouseClicked(e);
                }
              });
          form_panel3.add(form_quotePanelSendButton);
        }
        form_panel8.add(form_panel3, BorderLayout.LINE_END);
      }
      form_panel2.add(form_panel8, BorderLayout.CENTER);
    }
    add(form_panel2, BorderLayout.PAGE_END);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  // Generated using JFormDesigner non-commercial license
  protected JPanel form_panel4;
  protected JLabel form_label1;
  protected JButton form_closeReplyPanelButton;
  protected JPanel form_formerMessagePanel;
  protected JPanel form_hSpacer1;
  protected JPanel form_hSpacer2;
  protected JPanel form_vSpacer2;
  protected JPanel form_panel6;
  protected JScrollPane form_panel5;
  protected JTextPane form_mainQuoteTextField;
  protected JPanel form_panel7;
  protected JLabel form_quotedTime;
  protected JLabel form_label3;
  protected JLabel form_quotedSender;
  protected JPanel form_vSpacer1;
  protected JPanel form_panel2;
  protected JPanel form_hSpacer3;
  protected JPanel form_hSpacer4;
  protected JPanel form_vSpacer3;
  protected JPanel form_panel8;
  protected JScrollPane form_scrollPane1;
  protected JTextPane form_replyTextPane;
  protected JPanel form_panel3;
  protected JButton form_quotePanelPictureButton;
  protected JButton form_quotePanelEmojiButton;
  protected JButton form_quotePanelSendButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
