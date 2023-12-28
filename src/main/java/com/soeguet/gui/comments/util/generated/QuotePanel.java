package com.soeguet.gui.comments.util.generated;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;

public class QuotePanel extends JPanel {
    public QuotePanel() {
        initComponents();
    }

    public JLabel getQuoteSender() {
        return form_quoteSender;
    }

    public JTextPane getQuoteText() {
        return form_quoteText;
    }

    public JLabel getQuoteTime() {
        return form_quoteTime;
    }

    public JPanel getPanel2() {
        return form_panel2;
    }

    public JPanel getPanel1() {
        return form_panel1;
    }

    public JLabel getTextField1() {
        return form_textField1;
    }

    public JPanel getPanel3() {
        return form_panel3;
    }

    public JPanel getPanel4() {
        return form_panel4;
    }

    public JPanel getHSpacer1() {
        return form_hSpacer1;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // @formatter:off
        // Generated using JFormDesigner non-commercial license
        form_panel3 = new JPanel();
        form_panel1 = new JPanel();
        form_hSpacer1 = new JPanel(null);
        form_quoteTime = new JLabel();
        form_textField1 = new JLabel();
        form_quoteSender = new JLabel();
        form_panel4 = new JPanel();
        form_panel2 = new JPanel();
        form_quoteText = new JTextPane();

        // ======== this ========
        setLayout(new BorderLayout());

        // ======== form_panel3 ========
        {
            form_panel3.setLayout(new BorderLayout());

            // ======== form_panel1 ========
            {
                form_panel1.setMinimumSize(null);
                form_panel1.setPreferredSize(null);
                form_panel1.setInheritsPopupMenu(true);
                form_panel1.setLayout(new FlowLayout());

                // ---- form_hSpacer1 ----
                form_hSpacer1.setMinimumSize(new Dimension(20, 12));
                form_panel1.add(form_hSpacer1);

                // ---- form_quoteTime ----
                form_quoteTime.setText("test");
                form_quoteTime.setEnabled(false);
                form_quoteTime.setForeground(Color.black);
                form_quoteTime.setBorder(null);
                form_quoteTime.setMinimumSize(null);
                form_quoteTime.setPreferredSize(null);
                form_quoteTime.setMaximumSize(null);
                form_quoteTime.setHorizontalTextPosition(SwingConstants.LEADING);
                form_quoteTime.setFont(
                        form_quoteTime
                                .getFont()
                                .deriveFont(form_quoteTime.getFont().getSize() - 2f));
                form_panel1.add(form_quoteTime);

                // ---- form_textField1 ----
                form_textField1.setText(" - ");
                form_textField1.setEnabled(false);
                form_textField1.setBorder(null);
                form_textField1.setMinimumSize(null);
                form_textField1.setPreferredSize(null);
                form_textField1.setHorizontalAlignment(SwingConstants.CENTER);
                form_textField1.setFont(
                        form_textField1
                                .getFont()
                                .deriveFont(form_textField1.getFont().getSize() - 2f));
                form_panel1.add(form_textField1);

                // ---- form_quoteSender ----
                form_quoteSender.setText("test");
                form_quoteSender.setEnabled(false);
                form_quoteSender.setForeground(Color.black);
                form_quoteSender.setBorder(null);
                form_quoteSender.setMinimumSize(null);
                form_quoteSender.setPreferredSize(null);
                form_quoteSender.setMaximumSize(null);
                form_quoteSender.setHorizontalTextPosition(SwingConstants.LEADING);
                form_quoteSender.setFont(
                        form_quoteSender
                                .getFont()
                                .deriveFont(form_quoteSender.getFont().getSize() - 2f));
                form_panel1.add(form_quoteSender);
            }
            form_panel3.add(form_panel1, BorderLayout.WEST);

            // ======== form_panel4 ========
            {
                form_panel4.setLayout(new BorderLayout());
            }
            form_panel3.add(form_panel4, BorderLayout.CENTER);
        }
        add(form_panel3, BorderLayout.PAGE_START);

        // ======== form_panel2 ========
        {
            form_panel2.setBorder(new EtchedBorder());
            form_panel2.setLayout(
                    new MigLayout(
                            "insets 3,hidemode 3",
                            // columns
                            "[fill]" + "[fill]" + "[fill]",
                            // rows
                            "[fill]" + "[fill]" + "[fill]"));

            // ---- form_quoteText ----
            form_quoteText.setText("test");
            form_quoteText.setEditable(false);
            form_quoteText.setEnabled(false);
            form_quoteText.setForeground(Color.black);
            form_quoteText.setBorder(null);
            form_quoteText.setMargin(new Insets(10, 10, 10, 10));
            form_panel2.add(form_quoteText, "cell 1 1,grow");
        }
        add(form_panel2, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner non-commercial license
    protected JPanel form_panel3;
    protected JPanel form_panel1;
    protected JPanel form_hSpacer1;
    protected JLabel form_quoteTime;
    protected JLabel form_textField1;
    protected JLabel form_quoteSender;
    protected JPanel form_panel4;
    protected JPanel form_panel2;
    protected JTextPane form_quoteText;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
