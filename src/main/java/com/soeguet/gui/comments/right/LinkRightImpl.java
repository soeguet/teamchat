package com.soeguet.gui.comments.right;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.gui.comments.interfaces.LinkPanelInterface;
import com.soeguet.gui.comments.right.generated.PanelRight;
import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.gui.option_pane.links.dtos.LinkTransferDTO;
import com.soeguet.model.jackson.BaseModel;
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

public class LinkRightImpl extends PanelRight implements LinkPanelInterface {

    @Override
    protected void layeredContainerMouseDragged(final MouseEvent e) {

    }

    private final MainFrameGuiInterface mainFrame;

    public LinkRightImpl(final MainFrameGuiInterface mainFrame) {

        super(mainFrame);
        this.mainFrame = mainFrame;
    }

    @Override
    protected void layeredContainerMouseEntered(final MouseEvent e) {

    }

    @Override
    protected void layeredContainerMousePressed(final MouseEvent e) {

    }

    @Override
    protected void layeredContainerMouseExited(final MouseEvent e) {

    }

    @Override
    public void setCursorOnLink(final Cursor cursor) {

        form_container.setCursor(cursor);
        form_layeredContainer.setCursor(cursor);
    }

    /**
     Adds the given component to the form container.

     @param component
     the component to be added to the form container
     */
    @Override
    public void implementComment(Component component) {

        form_container.add(component, "cell 0 1");
    }

    /**
     Adds a hyperlink listener to the given component. When the hyperlink is clicked, the URL is opened in the default external browser, and the link
     color is changed from blue to violet.

     @param component
     the component to add the hyperlink listener to
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

        //TODO implement an abstract class for this method and the one in LinkLeftImpl

        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditorKit(new LinkWrapEditorKit());
        jEditorPane.setEditable(false);
        jEditorPane.setBackground(Color.WHITE);
        LinkTransferDTO linkCommentRecord = extractLinkFromMessageModel(messageModel);
        final String hyperlinkHtml =
                "<a href=\"" + linkCommentRecord.link() + "\" style=\"text-decoration:underline; color:blue; font-size:15;\">" + linkCommentRecord.link() + "</a><p style=\"font-size:15;\">" + linkCommentRecord.comment() + "</p>";
        jEditorPane.setText(hyperlinkHtml);
        return jEditorPane;
    }

    private LinkTransferDTO extractLinkFromMessageModel(final MessageModel messageModel) {

        try {

            return this.mainFrame.getObjectMapper().readValue(messageModel.getMessage(), LinkTransferDTO.class);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     Sets up a time field for the given base model.

     @param baseModel
     the base model to set up the time field for
     */
    @Override
    public void setupTimeField(final BaseModel baseModel) {

        super.setupTimeField(baseModel, getTimeLabel());
    }

    /**
     Sets up the name field for the given base model.

     @param baseModel
     the base model to set up the name field for
     */
    @Override
    public void setupNameField(final BaseModel baseModel) {

        super.setupNameField(baseModel, getNameLabel());
    }

    /**
     Prepares the style for a clicked link in the JEditorPane.

     @param jEditorPane
     the JEditorPane to apply the clicked link style to
     */
    private void prepareClickedLinkStyle(final JEditorPane jEditorPane) {

        Document document = jEditorPane.getDocument();
        Style style = ((HTMLDocument) document).addStyle("visited", null);

        StyleConstants.setForeground(style, LinkPanelInterface.clickedLinkViolet);
    }

    /**
     Changes the color of the clicked link to violet in the given JEditorPane.

     @param jEditorPane
     the JEditorPane in which the link is clicked
     @param hyperlinkEvent
     the HyperlinkEvent containing the information of the clicked link
     */
    private void changeLinkColorToVioletAfterClickingOnIt(final JEditorPane jEditorPane, final HyperlinkEvent hyperlinkEvent) {

        try {

            Document doc = jEditorPane.getDocument();
            ((HTMLDocument) doc).setCharacterAttributes(hyperlinkEvent.getSourceElement().getStartOffset(),
                                                        hyperlinkEvent.getSourceElement().getEndOffset() - hyperlinkEvent.getSourceElement().getStartOffset(), ((HTMLDocument) doc).getStyle("visited"), false);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
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
}