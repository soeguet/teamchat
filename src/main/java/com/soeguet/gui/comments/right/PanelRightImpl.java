package com.soeguet.gui.comments.right;

import com.soeguet.gui.comments.border.BorderHandlerImpl;
import com.soeguet.gui.comments.border.interfaces.BorderHandlerInterface;
import com.soeguet.gui.comments.border.interfaces.BorderInterface;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.comments.interfaces.LinkPanelInterface;
import com.soeguet.gui.comments.reaction_panel.ReactionPopupMenuImpl;
import com.soeguet.gui.comments.reaction_panel.dtos.ReactionPanelDTO;
import com.soeguet.gui.comments.reaction_sticker.CustomStickerContainer;
import com.soeguet.gui.comments.reaction_sticker.ReactionStickerImpl;
import com.soeguet.gui.comments.right.generated.PanelRight;
import com.soeguet.gui.comments.util.LinkWrapEditorKit;
import com.soeguet.gui.comments.util.QuotePanelImpl;
import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.UserInteraction;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;

/**
 This class represents a right panel implementation that extends PanelRight and implements CommentInterface.
 */
public class PanelRightImpl extends PanelRight implements CommentInterface, BorderInterface {

    private final Logger LOGGER = Logger.getLogger(PanelRightImpl.class.getName());
    private final BaseModel baseModel;
    private final MainFrameGuiInterface mainFrame;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;
    private BorderHandlerInterface borderHandler;
    private ReactionPopupMenuImpl popupMenu;
    private ReactionStickerImpl reactionSticker;

    public PanelRightImpl(MainFrameGuiInterface mainFrame, BaseModel baseModel) {

        super(mainFrame);
        this.mainFrame = mainFrame;

        this.baseModel = baseModel;
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
    public void setupTextPanelWrapper() {

        if (baseModel instanceof MessageModel messageModel) {

            SwingUtilities.invokeLater(() -> {

                //handle message containing quotes
                handleTextMessageExtractionForQuotes(messageModel);

                //handle the actual message and add a right click option to text pane
                setTextMessageOnChatPanel(messageModel);

                //set up the remaining needed fields, e.g. name, time, popup menu
                setupCommentEssentials(messageModel);
            });
        }
    }

    private void handleTextMessageExtractionForQuotes(final MessageModel messageModel) {

        QuotePanelImpl quotedSectionPanel = checkForQuotesInMessage(messageModel);

        if (quotedSectionPanel != null) {

            form_container.add(quotedSectionPanel, "cell 0 0, wrap");
        }
    }

    private void setTextMessageOnChatPanel(final MessageModel messageModel) {

        JTextPane actualTextPane = setUserMessage(messageModel);
        actualTextPane.setForeground(Color.BLACK);
        addRightClickOptionToPanel(actualTextPane);
        form_container.add(actualTextPane, "cell 0 1, wrap");
    }

    private void setupCommentEssentials(final MessageModel messageModel) {

        //setup time
        setupTimeField(messageModel, form_timeLabel);

        //setup name
        setupNameField(messageModel, form_nameLabel);

        //setup popup menu
        jPopupMenu = setupEditorPopupMenu(messageModel);
    }

    @Override
    public void setupPicturePanelWrapper() {

        if (baseModel instanceof PictureModel pictureModel) {

            SwingUtilities.invokeLater(() -> {

                //handle image extraction and return, if null
                if (handleImageExtraction(pictureModel)) {return;}

                //handle image and max sizing of image on the main panel
                setImageOnChatPanel();

                //handle image caption and right click ability on the text pane
                handleImageCaption(pictureModel);

                //set up the remaining needed fields, e.g. name, time, popup menu
                setupCommentEssentials(pictureModel);
            });
        }
    }

    /**
     Handles the extraction of an image from a {@link PictureModel} object.

     @param pictureModel
     The {@code PictureModel} object from which to extract the image.

     @return {@code true} if the extracted image is {@code null}, indicating an error occurred. Otherwise, {@code false}.
     */
    private boolean handleImageExtraction(final PictureModel pictureModel) {

        this.image = extractImageFromMessage(pictureModel);

        if (image == null) {

            LOGGER.log(java.util.logging.Level.SEVERE, "Buffered image is null");
            LOGGER.log(java.util.logging.Level.SEVERE, "PanelRightImpl > handleImageExtraction()");
            return true;
        }

        return false;
    }

    /**
     Sets the image on the chat panel.
     */
    private void setImageOnChatPanel() {

        JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
        form_container.add(imageLabel, "cell 0 0, wrap");
        addMaximizePictureOnClick(imageLabel, image);
    }

    /**
     Handles the image caption for the given PictureModel.

     @param pictureModel
     The PictureModel containing the image caption information.
     */
    private void handleImageCaption(final PictureModel pictureModel) {

        JTextPane imageCaptionTextPane = createImageCaptionTextPane(pictureModel);
        if (imageCaptionTextPane != null) {
            addRightClickOptionToPanel(imageCaptionTextPane);
            form_container.add(imageCaptionTextPane, "cell 0 1, wrap");
        }
    }

    /**
     Handles the image caption for the given PictureModel.

     @param pictureModel
     The PictureModel containing the image caption information.
     */
    private void setupCommentEssentials(final PictureModel pictureModel) {

        //setup time
        setupTimeField(pictureModel, form_timeLabel);

        //setup name
        setupNameField(pictureModel, form_nameLabel);

        //setup popup menu
        jPopupMenu = setupEditorPopupMenu(pictureModel);
    }

    /**
     Initializes the border handler for the component.

     @param borderColor
     The border color to be saved and used by the border handler.
     */
    @Override
    public void initializeBorderHandler(final Color borderColor) {

        borderHandler = new BorderHandlerImpl(mainFrame, this);
        borderHandler.saveBorderColor(borderColor);
    }

    @Override
    public void initializeReactionStickerHandler(final List<UserInteraction> userInteractions) {

        reactionSticker = new ReactionStickerImpl(this.mainFrame, this.form_layeredContainer, userInteractions);
        reactionSticker.pasteStickerToContainer();
    }

    /**
     Called when the reply button is clicked.

     @param e
     The MouseEvent that triggered the reply button click.
     */
    @Override
    protected void replyButtonClicked(MouseEvent e) {

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     Called when the mouse hovers over the action label.

     @param e
     The MouseEvent that triggered the mouse enter event.
     */
    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    /**
     Called when the mouse clicks the action label.

     @param e
     The MouseEvent that triggered the mouse click event.
     */
    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }

    /**
     Called when the mouse pointer exits the area of the action label.

     @param e
     The MouseEvent that triggered the mouse exit event.
     */
    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }

    @Override
    public JPanel getContainer() {

        return super.getContainer();
    }

    @Override
    protected void layeredContainerMouseEntered(final MouseEvent e) {

        //border
        borderHandler.highlightBorder();

        //popup menu
        if (popupMenu == null) {

            ReactionPanelDTO reactionPanelDTO = new ReactionPanelDTO(baseModel, mainFrame.getUsername(), mainFrame.getWebsocketClient(),
                                                                     mainFrame.getObjectMapper());

            popupMenu = new ReactionPopupMenuImpl(reactionPanelDTO);
            popupMenu.setPopupMenuUp();
            popupMenu.initializePopupHandler(form_layeredContainer);
        }

        //start timer and show popup menu
        popupMenu.startAnimation();
    }

    @Override
    protected void layeredContainerMouseExited(final MouseEvent e) {

        //border
        borderHandler.revertBorderColor();

        //popup menu
        if (!popupMenu.isVisible() || popupMenu.mouseLeftContainerCompletely(e)) {

            popupMenu.dispose(e);
        }

        popupMenu.stopAnimation();
    }

    @Override
    protected void layeredContainerMousePressed(final MouseEvent e) {

        //make text beneath the popup menu selectable
        super.passMouseEventToJTextPane(e, form_layeredContainer, form_container);
    }

    @Override
    protected void layeredContainerComponentResized(final ComponentEvent e) {

        if (reactionSticker != null) {

            reactionSticker.repositionSticker();
        }
    }

    @Override
    protected void layeredContainerMouseDragged(final MouseEvent e) {

        //make text beneath the popup menu selectable
        super.passMouseEventToJTextPane(e, form_layeredContainer, form_container);
    }
}