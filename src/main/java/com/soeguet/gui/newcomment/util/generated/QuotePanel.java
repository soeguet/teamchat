package com.soeguet.gui.newcomment.util.generated;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;


public class QuotePanel extends JPanel {
	public QuotePanel() {
		initComponents();
	}

	public JTextField getQuoteSender() {
		return form_quoteSender;
	}

	public JScrollPane getScrollPane1() {
		return form_scrollPane1;
	}

	public JTextPane getQuoteText() {
		return form_quoteText;
	}

	public JTextField getQuoteTime() {
		return form_quoteTime;
	}

	public JPanel getPanel2() {
		return form_panel2;
	}

	public JPanel getPanel1() {
		return form_panel1;
	}

	public JTextField getTextField1() {
		return form_textField1;
	}

	public JPanel getPanel3() {
		return form_panel3;
	}

	public JPanel getPanel4() {
		return form_panel4;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_panel3 = new JPanel();
		form_panel1 = new JPanel();
		form_quoteSender = new JTextField();
		form_textField1 = new JTextField();
		form_quoteTime = new JTextField();
		form_panel4 = new JPanel();
		form_scrollPane1 = new JScrollPane();
		form_panel2 = new JPanel();
		form_quoteText = new JTextPane();

		//======== this ========
		setLayout(new BorderLayout());

		//======== form_panel3 ========
		{
			form_panel3.setLayout(new BorderLayout());

			//======== form_panel1 ========
			{
				form_panel1.setMinimumSize(null);
				form_panel1.setPreferredSize(null);
				form_panel1.setInheritsPopupMenu(true);
				form_panel1.setLayout(new BorderLayout());

				//---- form_quoteSender ----
				form_quoteSender.setText("test");
				form_quoteSender.setEditable(false);
				form_quoteSender.setEnabled(false);
				form_quoteSender.setForeground(Color.black);
				form_quoteSender.setFont(new Font("sansserif", Font.BOLD, 10));
				form_quoteSender.setBorder(null);
				form_quoteSender.setMinimumSize(null);
				form_quoteSender.setPreferredSize(null);
				form_quoteSender.setHorizontalAlignment(SwingConstants.TRAILING);
				form_panel1.add(form_quoteSender, BorderLayout.WEST);

				//---- form_textField1 ----
				form_textField1.setText(" - ");
				form_textField1.setEnabled(false);
				form_textField1.setEditable(false);
				form_textField1.setBorder(null);
				form_textField1.setMinimumSize(null);
				form_textField1.setPreferredSize(null);
				form_textField1.setHorizontalAlignment(SwingConstants.CENTER);
				form_panel1.add(form_textField1, BorderLayout.CENTER);

				//---- form_quoteTime ----
				form_quoteTime.setText("test");
				form_quoteTime.setEditable(false);
				form_quoteTime.setEnabled(false);
				form_quoteTime.setFont(new Font("sansserif", Font.ITALIC, 10));
				form_quoteTime.setForeground(Color.black);
				form_quoteTime.setBorder(null);
				form_quoteTime.setMinimumSize(null);
				form_quoteTime.setPreferredSize(null);
				form_panel1.add(form_quoteTime, BorderLayout.EAST);
			}
			form_panel3.add(form_panel1, BorderLayout.WEST);

			//======== form_panel4 ========
			{
				form_panel4.setLayout(new BorderLayout());
			}
			form_panel3.add(form_panel4, BorderLayout.CENTER);
		}
		add(form_panel3, BorderLayout.NORTH);

		//======== form_scrollPane1 ========
		{
			form_scrollPane1.setBorder(null);
			form_scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			form_scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

			//======== form_panel2 ========
			{
				form_panel2.setBorder(new EtchedBorder());
				form_panel2.setLayout(new MigLayout(
					"insets 3,hidemode 3",
					// columns
					"[fill]" +
					"[fill]" +
					"[fill]",
					// rows
					"[fill]" +
					"[fill]" +
					"[fill]"));

				//---- form_quoteText ----
				form_quoteText.setText("test");
				form_quoteText.setEditable(false);
				form_quoteText.setEnabled(false);
				form_quoteText.setForeground(Color.black);
				form_quoteText.setBorder(null);
				form_quoteText.setMargin(new Insets(10, 10, 10, 10));
				form_panel2.add(form_quoteText, "cell 1 1,grow");
			}
			form_scrollPane1.setViewportView(form_panel2);
		}
		add(form_scrollPane1, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JPanel form_panel3;
	protected JPanel form_panel1;
	protected JTextField form_quoteSender;
	protected JTextField form_textField1;
	protected JTextField form_quoteTime;
	protected JPanel form_panel4;
	protected JScrollPane form_scrollPane1;
	protected JPanel form_panel2;
	protected JTextPane form_quoteText;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
