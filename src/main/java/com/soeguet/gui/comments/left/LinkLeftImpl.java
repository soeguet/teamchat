package com.soeguet.gui.comments.left;

import com.soeguet.gui.comments.interfaces.LinkPanelInterface;
import com.soeguet.gui.comments.left.generated.PanelLeft;
import com.soeguet.gui.comments.right.generated.PanelRight;
import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

public class LinkLeftImpl extends PanelLeft implements LinkPanelInterface {


    public LinkLeftImpl(final MainFrameInterface mainFrame) {

        super(mainFrame);
    }

    @Override
    public void implementComment(final Component component) {
        form_container.add(component, "cell 1 1, wrap");
    }

    @Override
    public void setBorderColor(final Color borderColor) {

    }

    @Override
    protected void replyButtonClicked(final MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseEntered(final MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseClicked(final MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseExited(final MouseEvent e) {

    }


    /**
     Adds a hyperlink listener to the given component.
     When the hyperlink is clicked, the URL is opened in the default external browser,
     and the link color is changed from blue to violet.

     @param component the component to add the hyperlink listener to
     */
    @Override
    public void addHyperlinkListener(final Component component) {

        if (component instanceof JEditorPane jEditorPane) {

            jEditorPane.addHyperlinkListener(e -> {

                prepareClickedLinkStyle(jEditorPane);

                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

                    try {

                        Desktop.getDesktop().browse(e.getURL().toURI());

                        //change link color from blue to violet after clicking on it
                        changeLinkColorToVioletAfterClickingOnIt(jEditorPane, e);

                    } catch (Exception ex) {

                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }

    public JEditorPane createEditorPaneForLinks(MessageModel messageModel) {

        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditorKit(new LinkWrapEditorKit());
        jEditorPane.setEditable(false);
        jEditorPane.setBackground(Color.WHITE);
        jEditorPane.setText("<a href=\"" + messageModel.getMessage() + "\" style=\"text-decoration:underline; color:blue; font-size:15;\">" + messageModel.getMessage() + "</a>");
        return jEditorPane;
    }

    /**
     Prepares the style for a clicked link in the JEditorPane.

     @param jEditorPane the JEditorPane to apply the clicked link style to
     */
    private void prepareClickedLinkStyle(final JEditorPane jEditorPane) {

        Document document = jEditorPane.getDocument();
        Style style = ((HTMLDocument) document).addStyle("visited", null);

        StyleConstants.setForeground(style, LinkPanelInterface.clickedLinkViolet);
    }

    /**
     Changes the color of the clicked link to violet in the given JEditorPane.

     @param jEditorPane    the JEditorPane in which the link is clicked
     @param hyperlinkEvent the HyperlinkEvent containing the information of the clicked link
     */
    private void changeLinkColorToVioletAfterClickingOnIt(final JEditorPane jEditorPane, final HyperlinkEvent hyperlinkEvent) {

        try {

            Document doc = jEditorPane.getDocument();
            ((HTMLDocument) doc).setCharacterAttributes(hyperlinkEvent.getSourceElement().getStartOffset(),
                    hyperlinkEvent.getSourceElement().getEndOffset() - hyperlinkEvent.getSourceElement().getStartOffset(),
                    ((HTMLDocument) doc).getStyle("visited"), false);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }

}