package com.soeguet.gui.newcomment.util;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Mon Sep 04 19:48:37 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class QuotePanel extends JPanel {
	public QuotePanel() {
		initComponents();
	}

	public JTextField getQuoteSender() {
		return form_quoteSender;
	}

	public JTextField getQuoteText() {
		return form_quoteText;
	}

	public JTextField getQuoteTime() {
		return form_quoteTime;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		form_quoteSender = new JTextField();
		form_quoteText = new JTextField();
		form_quoteTime = new JTextField();

		//======== this ========
		setLayout(new MigLayout(
			"fill,insets 5 15 5 15,hidemode 3,align center center",
			// columns
			"[fill]",
			// rows
			"[fill]" +
			"[fill]" +
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

		//---- form_quoteText ----
		form_quoteText.setHorizontalAlignment(SwingConstants.CENTER);
		form_quoteText.setText("test");
		form_quoteText.setEditable(false);
		form_quoteText.setEnabled(false);
		form_quoteText.setFont(form_quoteText.getFont().deriveFont(form_quoteText.getFont().getSize() + 3f));
		form_quoteText.setForeground(Color.black);
		form_quoteText.setBorder(null);
		add(form_quoteText, "cell 0 1");

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
	protected JTextField form_quoteText;
	protected JTextField form_quoteTime;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
