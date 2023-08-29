package com.soeguet.gui.reply;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Tue Aug 29 07:23:30 CEST 2023
 */



/**
 * @author soeguet
 */
public abstract class CustomReply extends JDialog {
	public CustomReply(Window owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		JPanel quotePanel = new JPanel();
		JPanel panel2 = new JPanel();
		JScrollPane scrollPane1 = new JScrollPane();
		JTextArea userInputTextArea = new JTextArea();
		JButton pictureButton = new JButton();
		JButton emojiButton = new JButton();
		JButton sendButton = new JButton();

		//======== this ========
		setMinimumSize(new Dimension(600, 300));
		setPreferredSize(new Dimension(600, 300));
		setName("this");
		Container contentPane = getContentPane();
		contentPane.setLayout(new MigLayout(
			"fill,hidemode 2,align center center",
			// columns
			"[fill]",
			// rows
			"[]" +
			"[]"));

		//======== quotePanel ========
		{
			quotePanel.setName("quotePanel");
			quotePanel.setLayout(new FlowLayout());
		}
		contentPane.add(quotePanel, "cell 0 0,grow");

		//======== panel2 ========
		{
			panel2.setName("panel2");
			panel2.setLayout(new GridBagLayout());
			((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
			((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
			((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 1.0E-4};
			((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

			//======== scrollPane1 ========
			{
				scrollPane1.setName("scrollPane1");

				//---- userInputTextArea ----
				userInputTextArea.setWrapStyleWord(true);
				userInputTextArea.setLineWrap(true);
				userInputTextArea.setName("userInputTextArea");
				scrollPane1.setViewportView(userInputTextArea);
			}
			panel2.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- pictureButton ----
			pictureButton.setText("photo");
			pictureButton.setName("pictureButton");
			panel2.add(pictureButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- emojiButton ----
			emojiButton.setText(":D");
			emojiButton.setName("emojiButton");
			panel2.add(emojiButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

			//---- sendButton ----
			sendButton.setText("->");
			sendButton.setName("sendButton");
			panel2.add(sendButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(panel2, "cell 0 1,grow");
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
