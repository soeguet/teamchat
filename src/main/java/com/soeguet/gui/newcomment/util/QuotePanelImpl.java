package com.soeguet.gui.newcomment.util;

import com.soeguet.gui.newcomment.util.generated.QuotePanel;

public class QuotePanelImpl extends QuotePanel {

    public QuotePanelImpl(String text, String sender, String time) {

        this.getQuoteText().setEditorKit(new WrapEditorKit());

        this.getQuoteText().setText(text);
        this.getQuoteSender().setText(sender);
        this.getQuoteTime().setText(time);
    }
}
