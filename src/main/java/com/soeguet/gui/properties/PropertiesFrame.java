/*
 * Created by JFormDesigner on Sun Mar 12 21:29:06 CET 2023
 */

package com.soeguet.gui.properties;

import java.awt.event.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Workstation
 */
public abstract class PropertiesFrame extends JFrame {
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_buttonBar;
	protected JButton form_okButton;
	protected JButton form_cancelButton;
	protected JTabbedPane form_panel1;
	protected JPanel form_dialogPane;
	protected JPanel form_contentPanel;
	protected JLabel form_label6;
	protected JTextField form_ipTextField;
	protected JLabel form_label5;
	protected JTextField form_portTextField;
	protected JSeparator form_separator1;
	protected JSeparator form_separator7;
	protected JSeparator form_separator6;
	protected JLabel form_label3;
	protected JSpinner form_widthSpinner;
	protected JLabel form_label2;
	protected JSpinner form_heightSpinner;
	protected JSeparator form_separator3;
	protected JLabel form_label4;
	protected JComboBox<String> form_themeOptionComboBox;
	protected JSeparator form_separator4;
	protected JLabel form_label8;
	protected JSlider form_fontSizeSlider;
	protected JLabel form_exampleTextLabel;
	protected JSeparator form_separator5;
	protected JLabel form_label9;
	protected JSpinner form_durationSpinner;
	protected JPanel form_panel2;
	protected JPanel form_panel3;
	protected JButton form_addRowButton;
	protected JButton form_deleteRowButton;
	protected JPanel form_participantsPane;
	protected JScrollPane form_scrollPane1;
	protected JTable form_participantsTable;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

	protected abstract void portTextFieldFocusGained(FocusEvent e);

	protected abstract void ipTextFieldFocusGained(FocusEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_buttonBar = new JPanel();
		form_okButton = new JButton();
		form_cancelButton = new JButton();
		form_panel1 = new JTabbedPane();
		form_dialogPane = new JPanel();
		form_contentPanel = new JPanel();
		form_label6 = new JLabel();
		form_ipTextField = new JTextField();
		form_label5 = new JLabel();
		form_portTextField = new JTextField();
		form_separator1 = new JSeparator();
		form_separator7 = new JSeparator();
		form_separator6 = new JSeparator();
		form_label3 = new JLabel();
		form_widthSpinner = new JSpinner();
		form_label2 = new JLabel();
		form_heightSpinner = new JSpinner();
		form_separator3 = new JSeparator();
		form_label4 = new JLabel();
		form_themeOptionComboBox = new JComboBox<>();
		form_themeOptionComboBox.addItem("white");
		form_themeOptionComboBox.addItem("dark");
		form_themeOptionComboBox.addItem("intellij");
		form_themeOptionComboBox.addItem("darcula");
		form_separator4 = new JSeparator();
		form_label8 = new JLabel();
		form_fontSizeSlider = new JSlider();
		form_exampleTextLabel = new JLabel();
		form_separator5 = new JSeparator();
		form_label9 = new JLabel();
		form_durationSpinner = new JSpinner();
		form_panel2 = new JPanel();
		form_panel3 = new JPanel();
		form_addRowButton = new JButton();
		form_deleteRowButton = new JButton();
		form_participantsPane = new JPanel();
		form_scrollPane1 = new JScrollPane();
		form_participantsTable = new JTable();

		//======== this ========
		setTitle("settings");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				thisWindowActivated(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== form_buttonBar ========
		{
			form_buttonBar.setLayout(new MigLayout(
				"insets dialog,alignx right",
				// columns
				"[button,fill]" +
				"[button,fill]",
				// rows
				null));

			//---- form_okButton ----
			form_okButton.setText("OK");
			form_okButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					okButtonMouseClicked(e);
				}
			});
			form_buttonBar.add(form_okButton, "cell 0 0");

			//---- form_cancelButton ----
			form_cancelButton.setText("Cancel");
			form_cancelButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					cancelButtonMouseClicked(e);
				}
			});
			form_buttonBar.add(form_cancelButton, "cell 1 0");
		}
		contentPane.add(form_buttonBar, BorderLayout.SOUTH);

		//======== form_panel1 ========
		{

			//======== form_dialogPane ========
			{
				form_dialogPane.setLayout(new BorderLayout());

				//======== form_contentPanel ========
				{
					form_contentPanel.setLayout(new MigLayout(
						"fill,insets dialog,align center center",
						// columns
						"[fill]" +
						"[fill]" +
						"[fill]" +
						"[shrink 0,center]" +
						"[shrink 0,center]" +
						"[shrink 0,center]" +
						"[fill]",
						// rows
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]" +
						"[]"));

					//---- form_label6 ----
					form_label6.setText("ip");
					form_contentPanel.add(form_label6, "cell 1 1,align center center,grow 0 0");

					//---- form_ipTextField ----
					form_ipTextField.setSelectionEnd(20);
					form_ipTextField.addFocusListener(new FocusAdapter() {
						@Override
						public void focusGained(FocusEvent e) {
							ipTextFieldFocusGained(e);
						}
					});
					form_contentPanel.add(form_ipTextField, "cell 3 1 3 1,grow");

					//---- form_label5 ----
					form_label5.setText("port");
					form_contentPanel.add(form_label5, "cell 1 3,align center center,grow 0 0");

					//---- form_portTextField ----
					form_portTextField.setSelectionEnd(20);
					form_portTextField.addFocusListener(new FocusAdapter() {
						@Override
						public void focusGained(FocusEvent e) {
							portTextFieldFocusGained(e);
						}
					});
					form_contentPanel.add(form_portTextField, "cell 3 3 3 1,grow");

					//---- form_separator1 ----
					form_separator1.setOrientation(SwingConstants.VERTICAL);
					form_contentPanel.add(form_separator1, "cell 2 1 1 12,grow");
					form_contentPanel.add(form_separator7, "cell 1 2 5 1");
					form_contentPanel.add(form_separator6, "cell 1 4 5 1");

					//---- form_label3 ----
					form_label3.setText("resolution");
					form_contentPanel.add(form_label3, "cell 1 5,align center center,grow 0 0");

					//---- form_widthSpinner ----
					form_widthSpinner.setModel(new SpinnerNumberModel(250, 250, null, 50));
					form_contentPanel.add(form_widthSpinner, "cell 3 5,aligny center,grow 100 0");

					//---- form_label2 ----
					form_label2.setText("x");
					form_contentPanel.add(form_label2, "cell 4 5,align center center,grow 0 0");

					//---- form_heightSpinner ----
					form_heightSpinner.setModel(new SpinnerNumberModel(250, 250, null, 50));
					form_contentPanel.add(form_heightSpinner, "cell 5 5,aligny center,grow 100 0");
					form_contentPanel.add(form_separator3, "cell 1 6 5 1");

					//---- form_label4 ----
					form_label4.setText("theme");
					form_contentPanel.add(form_label4, "cell 1 7,align center center,grow 0 0");
					form_contentPanel.add(form_themeOptionComboBox, "cell 3 7 3 1,aligny center,grow 100 0");

					//---- form_separator4 ----
					form_separator4.setMinimumSize(new Dimension(1, 2));
					form_contentPanel.add(form_separator4, "cell 1 8 5 1");

					//---- form_label8 ----
					form_label8.setText("text font size");
					form_contentPanel.add(form_label8, "cell 1 9,align center center,grow 0 0");

					//---- form_fontSizeSlider ----
					form_fontSizeSlider.setMaximum(30);
					form_fontSizeSlider.setMinimum(6);
					form_fontSizeSlider.setMinorTickSpacing(1);
					form_fontSizeSlider.setMajorTickSpacing(1);
					form_fontSizeSlider.setValue(11);
					form_fontSizeSlider.setPreferredSize(new Dimension(100, 20));
					form_fontSizeSlider.addChangeListener(e -> fontSizeSliderStateChanged(e));
					form_contentPanel.add(form_fontSizeSlider, "cell 3 9 3 1,growx");

					//---- form_exampleTextLabel ----
					form_exampleTextLabel.setText("example text");
					form_exampleTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
					form_contentPanel.add(form_exampleTextLabel, "cell 3 10 3 1,grow");

					//---- form_separator5 ----
					form_separator5.setMinimumSize(new Dimension(1, 2));
					form_contentPanel.add(form_separator5, "cell 1 11 5 1");

					//---- form_label9 ----
					form_label9.setText("notification duration");
					form_contentPanel.add(form_label9, "cell 1 12,align center center,grow 0 0");

					//---- form_durationSpinner ----
					form_durationSpinner.setModel(new SpinnerNumberModel(5, 1, 10, 1));
					form_contentPanel.add(form_durationSpinner, "cell 4 12,aligny center,grow 100 0");
				}
				form_dialogPane.add(form_contentPanel, BorderLayout.CENTER);
			}
			form_panel1.addTab("look & feel", form_dialogPane);

			//======== form_panel2 ========
			{
				form_panel2.setLayout(new BorderLayout());

				//======== form_panel3 ========
				{
					form_panel3.setLayout(new FlowLayout(FlowLayout.RIGHT));

					//---- form_addRowButton ----
					form_addRowButton.setText("+");
					form_addRowButton.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							addRowButtonMouseClicked(e);
						}
					});
					form_panel3.add(form_addRowButton);

					//---- form_deleteRowButton ----
					form_deleteRowButton.setText("-");
					form_deleteRowButton.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							deleteRowButtonMouseClicked(e);
						}
					});
					form_panel3.add(form_deleteRowButton);
				}
				form_panel2.add(form_panel3, BorderLayout.NORTH);

				//======== form_participantsPane ========
				{
					form_participantsPane.setLayout(new BorderLayout());

					//======== form_scrollPane1 ========
					{

						//---- form_participantsTable ----
						form_participantsTable.setModel(new DefaultTableModel(
							new Object[][] {
							},
							new String[] {
								"IP", "NAME"
							}
						));
						form_participantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						form_participantsTable.setSurrendersFocusOnKeystroke(true);
						form_participantsTable.setAutoCreateRowSorter(true);
						form_scrollPane1.setViewportView(form_participantsTable);
					}
					form_participantsPane.add(form_scrollPane1, BorderLayout.CENTER);
				}
				form_panel2.add(form_participantsPane, BorderLayout.CENTER);
			}
			form_panel1.addTab("participants", form_panel2);
		}
		contentPane.add(form_panel1, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

    public PropertiesFrame() {
        super();
        initComponents();
    }

    protected abstract void addRowButtonMouseClicked(MouseEvent e);

    protected abstract void deleteRowButtonMouseClicked(MouseEvent e);

    protected abstract void cancelButtonMouseClicked(MouseEvent e);

    protected abstract void thisWindowActivated(WindowEvent e);

    protected abstract void fontSizeSliderStateChanged(ChangeEvent e);

    protected abstract void okButtonMouseClicked(MouseEvent e);
}
