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

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_quoteSender = new JTextField();
		form_scrollPane1 = new JScrollPane();
		form_panel2 = new JPanel();
		form_quoteText = new JTextPane();
		form_quoteTime = new JTextField();

		//======== this ========
		setLayout(new MigLayout(
			"fill,insets 5 15 5 15,hidemode 3,align center center",
			// columns
			"[fill]",
			// rows
			"[fill]rel" +
			"[grow,fill]rel" +
			"[fill]"));

		//---- form_quoteSender ----
		form_quoteSender.setHorizontalAlignment(SwingConstants.LEFT);
		form_quoteSender.setText("test");
		form_quoteSender.setEditable(false);
		form_quoteSender.setEnabled(false);
		form_quoteSender.setForeground(Color.black);
		form_quoteSender.setFont(new Font("sansserif", Font.BOLD, 10));
		form_quoteSender.setBorder(null);
		add(form_quoteSender, "cell 0 0,gapx 3 0,gapy 5 0");

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
		add(form_scrollPane1, "cell 0 1");

		//---- form_quoteTime ----
		form_quoteTime.setHorizontalAlignment(SwingConstants.RIGHT);
		form_quoteTime.setText("test");
		form_quoteTime.setEditable(false);
		form_quoteTime.setEnabled(false);
		form_quoteTime.setFont(new Font("sansserif", Font.ITALIC, 10));
		form_quoteTime.setForeground(Color.black);
		form_quoteTime.setBorder(null);
		add(form_quoteTime, "cell 0 2,gapx 0 3,gapy 5 5");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JTextField form_quoteSender;
	protected JScrollPane form_scrollPane1;
	protected JPanel form_panel2;
	protected JTextPane form_quoteText;
	protected JTextField form_quoteTime;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
