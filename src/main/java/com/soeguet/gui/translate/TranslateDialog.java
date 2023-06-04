package com.soeguet.gui.translate;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;

/*
 * Created by JFormDesigner on Sat Apr 15 08:51:32 CEST 2023
 */


/**
 * @author Workstation
 */
public abstract class TranslateDialog extends JDialog {
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner non-commercial license
    protected JPanel form_dialogPane;
    protected JPanel form_contentPanel;
    protected JPanel form_panel1;
    protected JLabel form_label1;
    protected JLabel form_label2;
    protected JScrollPane form_scrollPane1;
    protected JTextArea form_selectedText;
    protected JLabel form_label3;
    protected JScrollPane form_scrollPane2;
    protected JTextArea form_translatedText;
    protected JComboBox<String> form_fromComboBox;
    protected JComboBox<String> form_toComboBox;
    protected JPanel form_buttonBar;
    protected JButton form_translateButton;
	public TranslateDialog(Window owner) {
		super(owner);
		initComponents();
	}

	protected abstract void translateButtonMouseClicked(MouseEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner non-commercial license
        this.form_dialogPane = new JPanel();
        this.form_contentPanel = new JPanel();
        this.form_panel1 = new JPanel();
        this.form_label1 = new JLabel();
        this.form_label2 = new JLabel();
        this.form_scrollPane1 = new JScrollPane();
        this.form_selectedText = new JTextArea();
        this.form_label3 = new JLabel();
        this.form_scrollPane2 = new JScrollPane();
        this.form_translatedText = new JTextArea();
        this.form_fromComboBox = new JComboBox<>();
        this.form_toComboBox = new JComboBox<>();
        this.form_buttonBar = new JPanel();
        this.form_translateButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== form_dialogPane ========
        {
            this.form_dialogPane.setLayout(new BorderLayout());

            //======== form_contentPanel ========
            {
                this.form_contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,align center center",
                    // columns
                    "[fill]",
                    // rows
                    "[fill]"));

                //======== form_panel1 ========
                {
                    this.form_panel1.setLayout(new MigLayout(
                        "fill,align center center",
                        // columns
                        "[300,fill]" +
                        "[50,center]" +
                        "[300,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- form_label1 ----
                    this.form_label1.setText("selected text");
                    this.form_label1.setHorizontalAlignment(SwingConstants.CENTER);
                    this.form_label1.setEnabled(false);
                    this.form_panel1.add(this.form_label1, "cell 0 0");

                    //---- form_label2 ----
                    this.form_label2.setText("translated text");
                    this.form_label2.setHorizontalAlignment(SwingConstants.CENTER);
                    this.form_label2.setEnabled(false);
                    this.form_panel1.add(this.form_label2, "cell 2 0");

                    //======== form_scrollPane1 ========
                    {
                        this.form_scrollPane1.setPreferredSize(new Dimension(400, 150));

                        //---- form_selectedText ----
                        this.form_selectedText.setLineWrap(true);
                        this.form_selectedText.setMinimumSize(null);
                        this.form_selectedText.setPreferredSize(null);
                        this.form_selectedText.setMaximumSize(null);
                        this.form_selectedText.setWrapStyleWord(true);
                        this.form_scrollPane1.setViewportView(this.form_selectedText);
                    }
                    this.form_panel1.add(this.form_scrollPane1, "cell 0 1,grow");

                    //---- form_label3 ----
                    this.form_label3.setText("->");
                    this.form_label3.setEnabled(false);
                    this.form_panel1.add(this.form_label3, "cell 1 1");

                    //======== form_scrollPane2 ========
                    {
                        this.form_scrollPane2.setPreferredSize(new Dimension(400, 150));

                        //---- form_translatedText ----
                        this.form_translatedText.setLineWrap(true);
                        this.form_translatedText.setMinimumSize(null);
                        this.form_translatedText.setPreferredSize(null);
                        this.form_translatedText.setMaximumSize(null);
                        this.form_translatedText.setWrapStyleWord(true);
                        this.form_scrollPane2.setViewportView(this.form_translatedText);
                    }
                    this.form_panel1.add(this.form_scrollPane2, "cell 2 1,grow");

                    //---- form_fromComboBox ----
                    this.form_fromComboBox.addItem("de");
                    this.form_fromComboBox.addItem("en");
                    this.form_fromComboBox.addItem("tr");
                    this.form_panel1.add(this.form_fromComboBox, "cell 0 2,alignx center,growx 0");

                    //---- form_toComboBox ----
                    this.form_toComboBox.addItem("de");
                    this.form_toComboBox.addItem("en");
                    this.form_toComboBox.addItem("tr");
                    this.form_panel1.add(this.form_toComboBox, "cell 2 2,alignx center,growx 0");
                }
                this.form_contentPanel.add(this.form_panel1, "cell 0 0");
            }
            this.form_dialogPane.add(this.form_contentPanel, BorderLayout.CENTER);

            //======== form_buttonBar ========
            {
                this.form_buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx center",
                    // columns
                    "[button,fill]",
                    // rows
                    null));

                //---- form_translateButton ----
                this.form_translateButton.setText("translate");
                this.form_translateButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        translateButtonMouseClicked(e);
                    }
                });
                this.form_buttonBar.add(this.form_translateButton, "cell 0 0");
            }
            this.form_dialogPane.add(this.form_buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(this.form_dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
