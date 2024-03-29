package com.soeguet.gui.option_pane.links.generated;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;

/*
 * Created by JFormDesigner on Wed Oct 11 21:14:43 CEST 2023
 */

/**
 * @author soeguet
 */
public abstract class LinkDialog extends JDialog {
    public LinkDialog(Window owner) {
        super(owner);
        initComponents();
    }

    public JPanel getDialogPane() {
        return form_dialogPane;
    }

    public JPanel getPanel1() {
        return form_panel1;
    }

    public JPanel getPanel2() {
        return form_panel2;
    }

    public JPanel getHSpacer1() {
        return form_hSpacer1;
    }

    public JPanel getContentPanel() {
        return form_contentPanel;
    }

    public JPanel getLinkTextPanel() {
        return form_linkTextPanel;
    }

    public JTextPane getLinkTextPane() {
        return form_linkTextPane;
    }

    public JTextPane getCommentTextPane() {
        return form_commentTextPane;
    }

    public JPanel getPanel3() {
        return form_panel3;
    }

    public JPanel getHSpacer2() {
        return form_hSpacer2;
    }

    public JPanel getButtonBar() {
        return form_buttonBar;
    }

    public JButton getOkButton() {
        return form_okButton;
    }

    public JButton getCancelButton() {
        return form_cancelButton;
    }

    protected abstract void okButtonActionPerformed(ActionEvent e);

    public JPanel getVSpacer1() {
        return form_vSpacer1;
    }

    public JPanel getVSpacer2() {
        return form_vSpacer2;
    }

    public JPanel getVSpacer3() {
        return form_vSpacer3;
    }

    protected abstract void cancelButtonActionPerformed(ActionEvent e);

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // @formatter:off
        // Generated using JFormDesigner non-commercial license
        form_dialogPane = new JPanel();
        form_vSpacer1 = new JPanel(null);
        form_panel1 = new JPanel();
        form_panel2 = new JPanel();
        form_hSpacer1 = new JPanel(null);
        form_contentPanel = new JPanel();
        form_linkTextPanel = new JPanel();
        form_vSpacer3 = new JPanel(null);
        form_linkTextPane = new JTextPane();
        form_commentTextPane = new JTextPane();
        form_panel3 = new JPanel();
        form_hSpacer2 = new JPanel(null);
        form_buttonBar = new JPanel();
        form_vSpacer2 = new JPanel(null);
        form_okButton = new JButton();
        form_cancelButton = new JButton();

        // ======== this ========
        setTitle("send link");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // ======== form_dialogPane ========
        {
            form_dialogPane.setLayout(new BorderLayout());

            // ---- form_vSpacer1 ----
            form_vSpacer1.setMinimumSize(new Dimension(12, 20));
            form_vSpacer1.setPreferredSize(new Dimension(10, 20));
            form_vSpacer1.setMaximumSize(new Dimension(12, 20));
            form_dialogPane.add(form_vSpacer1, BorderLayout.NORTH);

            // ======== form_panel1 ========
            {
                form_panel1.setLayout(new GridBagLayout());
                ((GridBagLayout) form_panel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout) form_panel1.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout) form_panel1.getLayout()).columnWeights =
                        new double[] {0.0, 1.0, 0.0, 1.0E-4};
                ((GridBagLayout) form_panel1.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                // ======== form_panel2 ========
                {
                    form_panel2.setLayout(new BorderLayout());

                    // ---- form_hSpacer1 ----
                    form_hSpacer1.setMinimumSize(new Dimension(20, 12));
                    form_hSpacer1.setPreferredSize(new Dimension(20, 10));
                    form_panel2.add(form_hSpacer1, BorderLayout.CENTER);
                }
                form_panel1.add(
                        form_panel2,
                        new GridBagConstraints(
                                0,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0),
                                0,
                                0));

                // ======== form_contentPanel ========
                {
                    form_contentPanel.setLayout(new BorderLayout());

                    // ======== form_linkTextPanel ========
                    {
                        form_linkTextPanel.setLayout(new GridBagLayout());
                        ((GridBagLayout) form_linkTextPanel.getLayout()).columnWidths =
                                new int[] {0, 0};
                        ((GridBagLayout) form_linkTextPanel.getLayout()).rowHeights =
                                new int[] {0, 0, 0, 0};
                        ((GridBagLayout) form_linkTextPanel.getLayout()).columnWeights =
                                new double[] {1.0, 1.0E-4};
                        ((GridBagLayout) form_linkTextPanel.getLayout()).rowWeights =
                                new double[] {0.0, 1.0, 1.0, 1.0E-4};
                        form_linkTextPanel.add(
                                form_vSpacer3,
                                new GridBagConstraints(
                                        0,
                                        0,
                                        1,
                                        1,
                                        0.0,
                                        0.0,
                                        GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH,
                                        new Insets(0, 0, 5, 0),
                                        0,
                                        0));

                        // ---- form_linkTextPane ----
                        form_linkTextPane.setBorder(new TitledBorder("link"));
                        form_linkTextPane.setBackground(null);
                        form_linkTextPanel.add(
                                form_linkTextPane,
                                new GridBagConstraints(
                                        0,
                                        1,
                                        1,
                                        1,
                                        0.0,
                                        0.0,
                                        GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH,
                                        new Insets(0, 0, 5, 0),
                                        0,
                                        0));

                        // ---- form_commentTextPane ----
                        form_commentTextPane.setBorder(new TitledBorder("comment"));
                        form_commentTextPane.setBackground(null);
                        form_linkTextPanel.add(
                                form_commentTextPane,
                                new GridBagConstraints(
                                        0,
                                        2,
                                        1,
                                        1,
                                        0.0,
                                        0.0,
                                        GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH,
                                        new Insets(0, 0, 0, 0),
                                        0,
                                        0));
                    }
                    form_contentPanel.add(form_linkTextPanel, BorderLayout.SOUTH);
                }
                form_panel1.add(
                        form_contentPanel,
                        new GridBagConstraints(
                                1,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0),
                                0,
                                0));

                // ======== form_panel3 ========
                {
                    form_panel3.setLayout(new BorderLayout());

                    // ---- form_hSpacer2 ----
                    form_hSpacer2.setMinimumSize(new Dimension(20, 12));
                    form_hSpacer2.setPreferredSize(new Dimension(20, 10));
                    form_panel3.add(form_hSpacer2, BorderLayout.CENTER);
                }
                form_panel1.add(
                        form_panel3,
                        new GridBagConstraints(
                                2,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0),
                                0,
                                0));
            }
            form_dialogPane.add(form_panel1, BorderLayout.CENTER);

            // ======== form_buttonBar ========
            {
                form_buttonBar.setLayout(
                        new MigLayout(
                                "insets dialog,alignx right",
                                // columns
                                "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[fill]"
                                        + "[button,fill]"
                                        + "[button,fill]",
                                // rows
                                "[]" + "[]"));
                form_buttonBar.add(form_vSpacer2, "cell 0 0");

                // ---- form_okButton ----
                form_okButton.setText("OK");
                form_okButton.addActionListener(e -> okButtonActionPerformed(e));
                form_buttonBar.add(form_okButton, "cell 13 1");

                // ---- form_cancelButton ----
                form_cancelButton.setText("Cancel");
                form_cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                form_buttonBar.add(form_cancelButton, "cell 14 1");
            }
            form_dialogPane.add(form_buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(form_dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner non-commercial license
    protected JPanel form_dialogPane;
    protected JPanel form_vSpacer1;
    protected JPanel form_panel1;
    protected JPanel form_panel2;
    protected JPanel form_hSpacer1;
    protected JPanel form_contentPanel;
    protected JPanel form_linkTextPanel;
    protected JPanel form_vSpacer3;
    protected JTextPane form_linkTextPane;
    protected JTextPane form_commentTextPane;
    protected JPanel form_panel3;
    protected JPanel form_hSpacer2;
    protected JPanel form_buttonBar;
    protected JPanel form_vSpacer2;
    protected JButton form_okButton;
    protected JButton form_cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
