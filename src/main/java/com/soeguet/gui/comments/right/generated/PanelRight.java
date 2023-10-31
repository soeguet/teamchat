/*
 * Created by JFormDesigner on Sat Mar 04 00:03:24 CET 2023
 */

package com.soeguet.gui.comments.right.generated;

import java.awt.event.*;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomFormContainer;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.comments.origin.CustomOriginPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class PanelRight extends CustomOriginPanel {

	protected Color borderColor;

	public Color getBorderColor() {

		return borderColor;
	}

	public PanelRight(MainFrameGuiInterface mainFrame) {
		super(mainFrame);
		initComponents();
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		repaint();
	}

	protected abstract void replyButtonClicked(MouseEvent e);
	protected abstract void actionLabelMouseEntered(MouseEvent e);
	protected abstract void actionLabelMouseClicked(MouseEvent e);
	protected abstract void actionLabelMouseExited(MouseEvent e);

	private void createUIComponents() {

		form_container = new CustomFormContainer() {


//			@Override
//			protected void paintComponent(Graphics grphcs) {
//
//				int rounding = 20;
//				Graphics2D g2d = (Graphics2D) grphcs;
//
//				final Color backgroundColor = Color.WHITE;
//
//				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g2d.setColor(backgroundColor);
//				g2d.fillRoundRect(0, 0, getWidth() - 13, getHeight() - 1, rounding, rounding);
//
//				g2d.setColor(borderColor);
//				g2d.drawRoundRect(0, 0, getWidth() - 13, getHeight() - 1, rounding, rounding);
//
//				g2d.setColor(backgroundColor);
//				g2d.fillPolygon(new int[]{getWidth() - 1, getWidth() - 28, getWidth() - 13},
//						new int[]{getHeight() - 1, getHeight() - 1, getHeight() - 13}, 3);
//
//				g2d.setColor(borderColor);
//				g2d.drawLine(getWidth() - 30, getHeight() - 1, getWidth(), getHeight() - 1);
//				g2d.drawLine(getWidth() - 13, getHeight() - 13, getWidth(), getHeight());
//			}

		};
	}

	public JLayeredPane getLayeredContainer() {
		return this.form_layeredContainer;
	}

	public JLabel getNameLabel() {
		return this.form_nameLabel;
	}

	public JPanel getContainer() {
		return this.form_container;
	}

	public JLabel getTimeLabel() {
		return this.form_timeLabel;
	}

	public JButton getButton1() {
		return this.form_button1;
	}

	public JPanel getPanel2() {
		return this.form_panel2;
	}

	public JPanel getHSpacer1() {
		return this.form_hSpacer1;
	}

	protected abstract void layeredContainerMouseEntered(MouseEvent e);

	protected abstract void layeredContainerMouseExited(MouseEvent e);

	protected abstract void layeredContainerMousePressed(MouseEvent e);

	public JPanel getVSpacer1() {
		return this.form_vSpacer1;
	}

	protected abstract void layeredContainerComponentResized(ComponentEvent e);

	protected abstract void layeredContainerMouseDragged(MouseEvent e);

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner non-commercial license
		createUIComponents();

		this.form_layeredContainer = new JLayeredPane();
		this.form_hSpacer1 = new JPanel(null);
		this.form_panel2 = new JPanel();
		this.form_timeLabel = new JLabel();
		this.form_button1 = new JButton();
		this.form_nameLabel = new JLabel();
		this.form_vSpacer1 = new JPanel(null);

		//======== this ========
		setPreferredSize(null);
		setMinimumSize(null);
		setMaximumSize(null);
		setOpaque(false);
		setLayout(new MigLayout(
			"insets 0,align trailing bottom",
			// columns
			"[right]" +
			"[fill]" +
			"[right]" +
			"[fill]" +
			"[center]",
			// rows
			"[]" +
			"[]" +
			"[]"));

		//======== form_layeredContainer ========
		{
			this.form_layeredContainer.setForeground(null);
			this.form_layeredContainer.setMinimumSize(null);
			this.form_layeredContainer.setPreferredSize(null);
			this.form_layeredContainer.setBackground(null);
			this.form_layeredContainer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					layeredContainerMouseEntered(e);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					layeredContainerMouseExited(e);
				}
				@Override
				public void mousePressed(MouseEvent e) {
					layeredContainerMousePressed(e);
				}
			});
			this.form_layeredContainer.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					layeredContainerMouseDragged(e);
				}
			});
			this.form_layeredContainer.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					layeredContainerComponentResized(e);
				}
			});
		}
		add(this.form_layeredContainer, "cell 1 1 2 2,grow");

		//======== form_container ========
		{
			this.form_container.setBackground(null);
			this.form_container.setMinimumSize(null);
			this.form_container.setPreferredSize(null);
			this.form_container.setMaximumSize(null);
			this.form_container.setForeground(null);
			this.form_container.setLayout(new MigLayout(
				"",
				// columns
				"[grow,fill]" +
				"[7!]",
				// rows
				"[]" +
				"[]"));
		}
		add(this.form_container, "pad 0,cell 2 0 1 2,aligny bottom,growy 0");

		//---- form_hSpacer1 ----
		this.form_hSpacer1.setMaximumSize(new Dimension(10, 10));
		this.form_hSpacer1.setMinimumSize(new Dimension(10, 10));
		this.form_hSpacer1.setForeground(null);
		this.form_hSpacer1.setBackground(null);
		add(this.form_hSpacer1, "cell 3 1");

		//======== form_panel2 ========
		{
			this.form_panel2.setLayout(new MigLayout(
				"insets 0,gap 0 0",
				// columns
				"[grow,fill]" +
				"[fill]",
				// rows
				"[fill]" +
				"[grow,fill]"));

			//---- form_timeLabel ----
			this.form_timeLabel.setEnabled(false);
			this.form_timeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			this.form_panel2.add(this.form_timeLabel, "cell 0 1");

			//---- form_button1 ----
			this.form_button1.setText("...");
			this.form_button1.setMaximumSize(null);
			this.form_button1.setMinimumSize(null);
			this.form_button1.setPreferredSize(null);
			this.form_button1.setBorder(null);
			this.form_button1.setBorderPainted(false);
			this.form_button1.setRequestFocusEnabled(false);
			this.form_button1.setOpaque(false);
			this.form_button1.setForeground(null);
			this.form_button1.setBackground(null);
			this.form_button1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					replyButtonClicked(e);
				}
			});
			this.form_panel2.add(this.form_button1, "cell 1 1");

			//---- form_nameLabel ----
			this.form_nameLabel.setFont(this.form_nameLabel.getFont().deriveFont(this.form_nameLabel.getFont().getStyle() | Font.BOLD, this.form_nameLabel.getFont().getSize() + 1f));
			this.form_nameLabel.setEnabled(false);
			this.form_nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			this.form_panel2.add(this.form_nameLabel, "cell 0 0 2 1");
		}
		add(this.form_panel2, "cell 4 1,aligny bottom,growy 0");

		//---- form_vSpacer1 ----
		this.form_vSpacer1.setMinimumSize(new Dimension(12, 15));
		this.form_vSpacer1.setPreferredSize(new Dimension(10, 15));
		this.form_vSpacer1.setMaximumSize(new Dimension(32767, 15));
		add(this.form_vSpacer1, "cell 4 2");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner non-commercial license
	protected JLayeredPane form_layeredContainer;
	protected JPanel form_container;
	protected JPanel form_hSpacer1;
	protected JPanel form_panel2;
	protected JLabel form_timeLabel;
	protected JButton form_button1;
	protected JLabel form_nameLabel;
	protected JPanel form_vSpacer1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}