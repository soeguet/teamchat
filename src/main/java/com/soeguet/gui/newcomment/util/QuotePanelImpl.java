package com.soeguet.gui.newcomment.util;

import java.awt.*;

public class QuotePanelImpl extends QuotePanel {

    public QuotePanelImpl(String text, String sender, String time) {

        getQuoteText().setText(text);
        getQuoteSender().setText(sender);
        getQuoteTime().setText(time);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        //draw rectangle
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, 100);
        g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, 100);


        //draw line
//        g.drawLine(0, 0, 100, 100);
    }
}
