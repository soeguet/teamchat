package com.soeguet.main_frame.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

/**
 * Super class of ChatMainFrameImpl. Carries all gui components.
 *
 * @author soeguet
 */
public abstract class ChatPanel extends JFrame {
  public ChatPanel() {
    initComponents();
  }

  protected abstract void thisComponentResized(ComponentEvent e);

  protected abstract void propertiesMenuItemMousePressed(MouseEvent e);

  protected abstract void internalNotificationsMenuItemItemStateChanged(ItemEvent e);

  protected abstract void connectionDetailsButtonMousePressed(MouseEvent e);

  protected abstract void resetConnectionMenuItemMousePressed(MouseEvent e);

  protected abstract void exitMenuItemMousePressed(MouseEvent e);

  protected abstract void textEditorPaneKeyPressed(KeyEvent e);

  protected abstract void pictureButtonMouseClicked(MouseEvent e);

  protected abstract void emojiButton(ActionEvent e);

  protected abstract void sendButton(ActionEvent e);

  public JPanel getPanel() {
    return this.form_panel;
  }

  public JMenuBar getMenuBar1() {
    return this.form_menuBar1;
  }

  public JMenu getFileMenu() {
    return this.form_fileMenu;
  }

  public JMenuItem getPropertiesMenuItem() {
    return this.form_propertiesMenuItem;
  }

  public JMenu getMenu1() {
    return this.form_menu1;
  }

  public JCheckBoxMenuItem getAllNotificationMenuItem() {
    return this.form_allNotificationsMenuItem;
  }

  public JCheckBoxMenuItem getInternalNotificationsMenuItem() {
    return this.form_internalNotificationsMenuItem;
  }

  public JCheckBoxMenuItem getExternalNotificationsMenuItem() {
    return this.form_externalNotificationsMenuItem;
  }

  public JMenuItem getConnectionDetailsButton() {
    return this.form_connectionDetailsButton;
  }

  public JMenuItem getResetConnectionMenuItem() {
    return this.form_resetConnectionMenuItem;
  }

  public JMenuItem getExitMenuItem() {
    return this.form_exitMenuItem;
  }

  public JMenu getExtraMenu() {
    return this.form_extraMenu;
  }

  public JMenuItem getParticipantsMenuItem() {
    return this.form_participantsMenuItem;
  }

  public JPanel getPanel4() {
    return this.form_panel4;
  }

  public JPanel getWestMarginPanel() {
    return this.form_westMarginPanel;
  }

  public JPanel getHSpacer2() {
    return this.form_hSpacer2;
  }

  public JLayeredPane getMainTextPanelLayeredPane() {
    return this.form_mainTextPanelLayeredPane;
  }

  public JScrollPane getMainTextBackgroundScrollPane() {
    return this.form_mainTextBackgroundScrollPane;
  }

  public JPanel getMainTextPanel() {
    return this.form_mainTextPanel;
  }

  public JPanel getPanel6() {
    return this.form_panel6;
  }

  public JPanel getHSpacer3() {
    return this.form_hSpacer3;
  }

  public JPanel getInteractionAreaPanel() {
    return this.form_interactionAreaPanel;
  }

  public JPanel getPanel3() {
    return this.form_panel3;
  }

  public JLabel getTypingLabel() {
    return this.form_typingLabel;
  }

  public JScrollPane getMainTextFieldScrollPane() {
    return this.form_mainTextFieldScrollPane;
  }

  public JTextPane getTextEditorPane() {
    return this.form_textEditorPane;
  }

  public JPanel getPanel2() {
    return this.form_panel2;
  }

  public JButton getPictureButton() {
    return this.form_pictureButton;
  }

  public JButton getEmojiButton() {
    return this.form_emojiButton;
  }

  public JButton getSendButton() {
    return this.form_sendButton;
  }

  public JPanel getPanel5() {
    return this.form_panel5;
  }

  public JPanel getVSpacer1() {
    return this.form_vSpacer1;
  }

  public JPanel getPanel7() {
    return this.form_panel7;
  }

  public JPanel getHSpacer1() {
    return this.form_hSpacer1;
  }

  protected abstract void externalNotificationsMenuItemItemStateChanged(ItemEvent e);

  protected abstract void thisWindowClosing(WindowEvent e);

  protected abstract void interruptMenuItemMousePressed(MouseEvent e);

  public JMenuItem getInterruptMenuItem() {
    return this.form_interruptMenuItem;
  }

  public JCheckBoxMenuItem getAllNotificationsMenuItem() {
    return this.form_allNotificationsMenuItem;
  }

  protected abstract void allNotificationsMenuItemItemStateChanged(ItemEvent e);

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // @formatter:off
    // Generated using JFormDesigner non-commercial license
    this.form_panel = new JPanel();
    this.form_menuBar1 = new JMenuBar();
    this.form_fileMenu = new JMenu();
    this.form_propertiesMenuItem = new JMenuItem();
    this.form_menu1 = new JMenu();
    this.form_allNotificationsMenuItem = new JCheckBoxMenuItem();
    this.form_internalNotificationsMenuItem = new JCheckBoxMenuItem();
    this.form_externalNotificationsMenuItem = new JCheckBoxMenuItem();
    this.form_connectionDetailsButton = new JMenuItem();
    this.form_resetConnectionMenuItem = new JMenuItem();
    this.form_exitMenuItem = new JMenuItem();
    this.form_extraMenu = new JMenu();
    this.form_interruptMenuItem = new JMenuItem();
    this.form_participantsMenuItem = new JMenuItem();
    this.form_panel4 = new JPanel();
    this.form_westMarginPanel = new JPanel();
    this.form_hSpacer2 = new JPanel(null);
    this.form_mainTextPanelLayeredPane = new JLayeredPane();
    this.form_mainTextBackgroundScrollPane = new JScrollPane();
    this.form_mainTextPanel = new JPanel();
    this.form_panel6 = new JPanel();
    this.form_hSpacer3 = new JPanel(null);
    this.form_interactionAreaPanel = new JPanel();
    this.form_panel3 = new JPanel();
    this.form_typingLabel = new JLabel();
    this.form_mainTextFieldScrollPane = new JScrollPane();
    this.form_textEditorPane = new JTextPane();
    this.form_panel2 = new JPanel();
    this.form_pictureButton = new JButton();
    this.form_emojiButton = new JButton();
    this.form_sendButton = new JButton();
    this.form_panel5 = new JPanel();
    this.form_vSpacer1 = new JPanel(null);
    this.form_panel7 = new JPanel();
    this.form_hSpacer1 = new JPanel(null);

    // ======== this ========
    setFocusable(false);
    setMinimumSize(new Dimension(750, 700));
    setPreferredSize(new Dimension(750, 700));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            thisComponentResized(e);
          }
        });
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            thisWindowClosing(e);
          }
        });
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    // ======== form_panel ========
    {
      this.form_panel.setMinimumSize(null);
      this.form_panel.setMaximumSize(null);
      this.form_panel.setPreferredSize(null);
      this.form_panel.setLayout(new BorderLayout());

      // ======== form_menuBar1 ========
      {

        // ======== form_fileMenu ========
        {
          this.form_fileMenu.setText("file");

          // ---- form_propertiesMenuItem ----
          this.form_propertiesMenuItem.setText("properties");
          this.form_propertiesMenuItem.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                  propertiesMenuItemMousePressed(e);
                }
              });
          this.form_fileMenu.add(this.form_propertiesMenuItem);
          this.form_fileMenu.addSeparator();

          // ======== form_menu1 ========
          {
            this.form_menu1.setText("notifications");

            // ---- form_allNotificationsMenuItem ----
            this.form_allNotificationsMenuItem.setText("ALL NOTIFICATIONS OFF");
            this.form_allNotificationsMenuItem.addItemListener(
                e -> allNotificationsMenuItemItemStateChanged(e));
            this.form_menu1.add(this.form_allNotificationsMenuItem);
            this.form_menu1.addSeparator();

            // ---- form_internalNotificationsMenuItem ----
            this.form_internalNotificationsMenuItem.setText("internal - on");
            this.form_internalNotificationsMenuItem.setSelected(true);
            this.form_internalNotificationsMenuItem.addItemListener(
                e -> internalNotificationsMenuItemItemStateChanged(e));
            this.form_menu1.add(this.form_internalNotificationsMenuItem);

            // ---- form_externalNotificationsMenuItem ----
            this.form_externalNotificationsMenuItem.setText("external - on");
            this.form_externalNotificationsMenuItem.setSelected(true);
            this.form_externalNotificationsMenuItem.addItemListener(
                e -> externalNotificationsMenuItemItemStateChanged(e));
            this.form_menu1.add(this.form_externalNotificationsMenuItem);
          }
          this.form_fileMenu.add(this.form_menu1);
          this.form_fileMenu.addSeparator();

          // ---- form_connectionDetailsButton ----
          this.form_connectionDetailsButton.setText("connection details");
          this.form_connectionDetailsButton.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                  connectionDetailsButtonMousePressed(e);
                }
              });
          this.form_fileMenu.add(this.form_connectionDetailsButton);

          // ---- form_resetConnectionMenuItem ----
          this.form_resetConnectionMenuItem.setText("reset connection");
          this.form_resetConnectionMenuItem.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                  resetConnectionMenuItemMousePressed(e);
                }
              });
          this.form_fileMenu.add(this.form_resetConnectionMenuItem);
          this.form_fileMenu.addSeparator();

          // ---- form_exitMenuItem ----
          this.form_exitMenuItem.setText("exit");
          this.form_exitMenuItem.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                  exitMenuItemMousePressed(e);
                }
              });
          this.form_fileMenu.add(this.form_exitMenuItem);
        }
        this.form_menuBar1.add(this.form_fileMenu);

        // ======== form_extraMenu ========
        {
          this.form_extraMenu.setText("extra");

          // ---- form_interruptMenuItem ----
          this.form_interruptMenuItem.setText("interrupt");
          this.form_interruptMenuItem.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                  interruptMenuItemMousePressed(e);
                }
              });
          this.form_extraMenu.add(this.form_interruptMenuItem);
          this.form_extraMenu.addSeparator();

          // ---- form_participantsMenuItem ----
          this.form_participantsMenuItem.setText("participants");
          this.form_participantsMenuItem.setEnabled(false);
          this.form_extraMenu.add(this.form_participantsMenuItem);
        }
        this.form_menuBar1.add(this.form_extraMenu);
      }
      this.form_panel.add(this.form_menuBar1, BorderLayout.PAGE_START);

      // ======== form_panel4 ========
      {
        this.form_panel4.setBorder(null);
        this.form_panel4.setLayout(new BorderLayout());

        // ======== form_westMarginPanel ========
        {
          this.form_westMarginPanel.setMinimumSize(new Dimension(30, 0));
          this.form_westMarginPanel.setLayout(new BorderLayout());

          // ---- form_hSpacer2 ----
          this.form_hSpacer2.setMinimumSize(new Dimension(30, 12));
          this.form_westMarginPanel.add(this.form_hSpacer2, BorderLayout.CENTER);
        }
        this.form_panel4.add(this.form_westMarginPanel, BorderLayout.WEST);

        // ======== form_mainTextPanelLayeredPane ========
        {
          this.form_mainTextPanelLayeredPane.setOpaque(true);
          this.form_mainTextPanelLayeredPane.setBackground(null);
          this.form_mainTextPanelLayeredPane.setForeground(null);
          this.form_mainTextPanelLayeredPane.setBorder(null);

          // ======== form_mainTextBackgroundScrollPane ========
          {
            this.form_mainTextBackgroundScrollPane.setFocusable(false);
            this.form_mainTextBackgroundScrollPane.setRequestFocusEnabled(false);
            this.form_mainTextBackgroundScrollPane.setBackground(null);
            this.form_mainTextBackgroundScrollPane.setDoubleBuffered(true);
            this.form_mainTextBackgroundScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            this.form_mainTextBackgroundScrollPane.setAutoscrolls(true);
            this.form_mainTextBackgroundScrollPane.setMaximumSize(null);
            this.form_mainTextBackgroundScrollPane.setAlignmentX(0.0F);
            this.form_mainTextBackgroundScrollPane.setAlignmentY(0.0F);
            this.form_mainTextBackgroundScrollPane.setPreferredSize(null);
            this.form_mainTextBackgroundScrollPane.setMinimumSize(null);
            this.form_mainTextBackgroundScrollPane.setOpaque(false);
            this.form_mainTextBackgroundScrollPane.setBorder(new LineBorder(Color.black, 1, true));

            // ======== form_mainTextPanel ========
            {
              this.form_mainTextPanel.setAutoscrolls(true);
              this.form_mainTextPanel.setVerifyInputWhenFocusTarget(false);
              this.form_mainTextPanel.setRequestFocusEnabled(false);
              this.form_mainTextPanel.setMaximumSize(null);
              this.form_mainTextPanel.setFont(
                  new Font("Calibri Light", this.form_mainTextPanel.getFont().getStyle(), 18));
              this.form_mainTextPanel.setBackground(null);
              this.form_mainTextPanel.setMinimumSize(null);
              this.form_mainTextPanel.setPreferredSize(null);
              this.form_mainTextPanel.setOpaque(false);
              this.form_mainTextPanel.setBorder(null);
              this.form_mainTextPanel.setLayout(
                  new MigLayout(
                      "fill,novisualpadding,hidemode 3",
                      // columns
                      "[fill]",
                      // rows
                      "[fill]"));
            }
            this.form_mainTextBackgroundScrollPane.setViewportView(this.form_mainTextPanel);
          }
          this.form_mainTextPanelLayeredPane.add(
              this.form_mainTextBackgroundScrollPane, JLayeredPane.DEFAULT_LAYER);
          this.form_mainTextBackgroundScrollPane.setBounds(0, 0, 875, 480);
        }
        this.form_panel4.add(this.form_mainTextPanelLayeredPane, BorderLayout.CENTER);

        // ======== form_panel6 ========
        {
          this.form_panel6.setMinimumSize(new Dimension(30, 0));
          this.form_panel6.setLayout(new BorderLayout());

          // ---- form_hSpacer3 ----
          this.form_hSpacer3.setMinimumSize(new Dimension(30, 12));
          this.form_panel6.add(this.form_hSpacer3, BorderLayout.CENTER);
        }
        this.form_panel4.add(this.form_panel6, BorderLayout.EAST);
      }
      this.form_panel.add(this.form_panel4, BorderLayout.CENTER);

      // ======== form_interactionAreaPanel ========
      {
        this.form_interactionAreaPanel.setBorder(null);
        this.form_interactionAreaPanel.setMinimumSize(new Dimension(222, 89));
        this.form_interactionAreaPanel.setLayout(new BorderLayout());

        // ======== form_panel3 ========
        {
          this.form_panel3.setLayout(new BorderLayout());

          // ---- form_typingLabel ----
          this.form_typingLabel.setBackground(new Color(0x00ffffff, true));
          this.form_typingLabel.setEnabled(false);
          this.form_typingLabel.setFocusable(false);
          this.form_typingLabel.setInheritsPopupMenu(false);
          this.form_typingLabel.setRequestFocusEnabled(false);
          this.form_typingLabel.setVerifyInputWhenFocusTarget(false);
          this.form_typingLabel.setText(" ");
          this.form_panel3.add(this.form_typingLabel, BorderLayout.CENTER);
        }
        this.form_interactionAreaPanel.add(this.form_panel3, BorderLayout.PAGE_START);

        // ======== form_mainTextFieldScrollPane ========
        {
          this.form_mainTextFieldScrollPane.setMinimumSize(new Dimension(22, 50));
          this.form_mainTextFieldScrollPane.setPreferredSize(new Dimension(53, 50));
          this.form_mainTextFieldScrollPane.setMaximumSize(new Dimension(32767, 50));

          // ---- form_textEditorPane ----
          this.form_textEditorPane.setMinimumSize(new Dimension(22, 50));
          this.form_textEditorPane.setMaximumSize(new Dimension(32767, 50));
          this.form_textEditorPane.setPreferredSize(new Dimension(53, 50));
          this.form_textEditorPane.setOpaque(false);
          this.form_textEditorPane.setDoubleBuffered(true);
          this.form_textEditorPane.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
          this.form_textEditorPane.addKeyListener(
              new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                  textEditorPaneKeyPressed(e);
                }
              });
          this.form_mainTextFieldScrollPane.setViewportView(this.form_textEditorPane);
        }
        this.form_interactionAreaPanel.add(this.form_mainTextFieldScrollPane, BorderLayout.CENTER);

        // ======== form_panel2 ========
        {
          this.form_panel2.setLayout(new FlowLayout());

          // ---- form_pictureButton ----
          this.form_pictureButton.setDoubleBuffered(true);
          this.form_pictureButton.setMinimumSize(new Dimension(50, 50));
          this.form_pictureButton.setPreferredSize(new Dimension(50, 50));
          this.form_pictureButton.setMaximumSize(new Dimension(32767, 50));
          this.form_pictureButton.setToolTipText("picture");
          this.form_pictureButton.setIcon(null);
          this.form_pictureButton.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  pictureButtonMouseClicked(e);
                }
              });
          this.form_panel2.add(this.form_pictureButton);

          // ---- form_emojiButton ----
          this.form_emojiButton.setDoubleBuffered(true);
          this.form_emojiButton.setMinimumSize(new Dimension(50, 50));
          this.form_emojiButton.setPreferredSize(new Dimension(50, 50));
          this.form_emojiButton.setMaximumSize(new Dimension(32767, 50));
          this.form_emojiButton.setSelectedIcon(null);
          this.form_emojiButton.setToolTipText("emoji");
          this.form_emojiButton.addActionListener(e -> emojiButton(e));
          this.form_panel2.add(this.form_emojiButton);

          // ---- form_sendButton ----
          this.form_sendButton.setMinimumSize(new Dimension(50, 50));
          this.form_sendButton.setMaximumSize(new Dimension(32767, 50));
          this.form_sendButton.setPreferredSize(new Dimension(50, 50));
          this.form_sendButton.setDoubleBuffered(true);
          this.form_sendButton.setToolTipText("send");
          this.form_sendButton.addActionListener(e -> sendButton(e));
          this.form_panel2.add(this.form_sendButton);
        }
        this.form_interactionAreaPanel.add(this.form_panel2, BorderLayout.LINE_END);

        // ======== form_panel5 ========
        {
          this.form_panel5.setMinimumSize(new Dimension(0, 30));
          this.form_panel5.setLayout(new BorderLayout());

          // ---- form_vSpacer1 ----
          this.form_vSpacer1.setMinimumSize(new Dimension(12, 30));
          this.form_panel5.add(this.form_vSpacer1, BorderLayout.CENTER);
        }
        this.form_interactionAreaPanel.add(this.form_panel5, BorderLayout.PAGE_END);

        // ======== form_panel7 ========
        {
          this.form_panel7.setMinimumSize(new Dimension(30, 0));
          this.form_panel7.setLayout(new BorderLayout());

          // ---- form_hSpacer1 ----
          this.form_hSpacer1.setMinimumSize(new Dimension(30, 12));
          this.form_panel7.add(this.form_hSpacer1, BorderLayout.CENTER);
        }
        this.form_interactionAreaPanel.add(this.form_panel7, BorderLayout.LINE_START);
      }
      this.form_panel.add(this.form_interactionAreaPanel, BorderLayout.PAGE_END);
    }
    contentPane.add(this.form_panel, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
  // Generated using JFormDesigner non-commercial license
  protected JPanel form_panel;
  protected JMenuBar form_menuBar1;
  protected JMenu form_fileMenu;
  protected JMenuItem form_propertiesMenuItem;
  protected JMenu form_menu1;
  protected JCheckBoxMenuItem form_allNotificationsMenuItem;
  protected JCheckBoxMenuItem form_internalNotificationsMenuItem;
  protected JCheckBoxMenuItem form_externalNotificationsMenuItem;
  protected JMenuItem form_connectionDetailsButton;
  protected JMenuItem form_resetConnectionMenuItem;
  protected JMenuItem form_exitMenuItem;
  protected JMenu form_extraMenu;
  protected JMenuItem form_interruptMenuItem;
  protected JMenuItem form_participantsMenuItem;
  protected JPanel form_panel4;
  protected JPanel form_westMarginPanel;
  protected JPanel form_hSpacer2;
  protected JLayeredPane form_mainTextPanelLayeredPane;
  protected JScrollPane form_mainTextBackgroundScrollPane;
  protected JPanel form_mainTextPanel;
  protected JPanel form_panel6;
  protected JPanel form_hSpacer3;
  protected JPanel form_interactionAreaPanel;
  protected JPanel form_panel3;
  protected JLabel form_typingLabel;
  protected JScrollPane form_mainTextFieldScrollPane;
  protected JTextPane form_textEditorPane;
  protected JPanel form_panel2;
  protected JButton form_pictureButton;
  protected JButton form_emojiButton;
  protected JButton form_sendButton;
  protected JPanel form_panel5;
  protected JPanel form_vSpacer1;
  protected JPanel form_panel7;
  protected JPanel form_hSpacer1;
  // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
