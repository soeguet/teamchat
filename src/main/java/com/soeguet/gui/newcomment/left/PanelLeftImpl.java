package com.soeguet.gui.newcomment.left;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.left.generated.PanelLeft;
import com.soeguet.gui.newcomment.util.QuotePanelImpl;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 This class represents an implementation of the PanelLeft interface and implements the CommentInterface.
 It provides methods for setting up a left panel in a graphical user interface.
 */
public class PanelLeftImpl extends PanelLeft implements CommentInterface {

    private final Logger LOGGER = Logger.getLogger(PanelLeftImpl.class.getName());
    private final BaseModel baseModel;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;

    /**
     Constructs a new PanelLeftImpl object.

     @param mainFrame the MainFrameInterface object to associate with the panel
     @param baseModel the BaseModel object to use for data access
     */
    public PanelLeftImpl(MainFrameInterface mainFrame, BaseModel baseModel) {

        super(mainFrame);

        this.baseModel = baseModel;
    }

    /**
     Set up the text panel wrapper for displaying messages.
     This method is called to initialize the text panel with the necessary components and settings.

     If the baseModel is an instance of MessageModel, the following actions are taken:
     1. Invoke the setupTextPanelWrapper method on the Swing event dispatch thread.
     2. Handle the extraction of messages containing quotes.
     3. Set the text message on the chat panel and add a right-click option to the text pane.
     4. Set up the remaining needed fields, such as name, time, and popup menu.
     */
    @Override
    public void setupTextPanelWrapper() {

        if (baseModel instanceof MessageModel messageModel) {

            SwingUtilities.invokeLater(() -> {

                //handle message containing quotes
                handleTextMessageExtraction(messageModel);

                //handle the actual message and add a right click option to text pane
                setTextMessageOnChatPanel(messageModel);

                //set up the remaining needed fields, e.g. name, time, popup menu
                setupCommentEssentials(messageModel);
            });
        }
    }

    /**
     Handle the extraction of messages containing quotes.
     If the given message model contains quotes, a quote panel is created and added to the form panel.

     @param messageModel the message model containing the message to handle
     */
    private void handleTextMessageExtraction(final MessageModel messageModel) {

        QuotePanelImpl quotedSectionPanel = checkForQuotesInMessage(messageModel);
        if (quotedSectionPanel != null) {
            form_panel1.add(quotedSectionPanel, "cell 1 0, wrap");
        }
    }

    /**
     Sets the text message on the chat panel.

     @param messageModel the message model containing the message to be set
     */
    private void setTextMessageOnChatPanel(final MessageModel messageModel) {

        JTextPane actualTextPane = setUserMessage(messageModel);
        addRightClickOptionToPanel(actualTextPane);
        form_panel1.add(actualTextPane, "cell 1 1, wrap");
    }

    /**
     Sets up the essentials for comments.

     @param messageModel the message model containing the comment information
     */
    private void setupCommentEssentials(final MessageModel messageModel) {

        //setup name
        setupNameField(messageModel, form_nameLabel);

        //setup time
        setupTimeField( messageModel, form_timeLabel);

        //setup popup menu
        jPopupMenu = setupEditorPopupMenu(messageModel);
    }

    /**
     Sets up the picture panel wrapper.
     This method is responsible for setting up the necessary components and behavior for displaying a picture in the chat panel.
     It handles image extraction, resizing, caption, and right-click functionality.
     After setting up the picture, it also calls the setupCommentEssentials method to set up the remaining needed fields.
     */
    @Override
    public void setupPicturePanelWrapper() {

        if (baseModel instanceof PictureModel pictureModel) {

            SwingUtilities.invokeLater(() -> {

                //handle image extraction and return, if null
                if (handleImageExtraction(pictureModel)) return;

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
     Handles image extraction from the given PictureModel.
     It extracts the image from the message and sets the extracted image to the instance variable 'image'.
     If the extracted image is null, it logs a severe level message and returns true.
     Otherwise, it returns false.

     @param pictureModel The PictureModel containing the image message.

     @return True if the extracted image is null, false otherwise.
     */
    private boolean handleImageExtraction(final PictureModel pictureModel) {

        this.image = extractImageFromMessage(pictureModel);
        if (image == null) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Buffered image is null");
            return true;
        }
        return false;
    }

    /**
     Sets the image on the chat panel.
     It creates a JLabel with the scaled image and adds it to the form_panel1.
     It also adds a click listener to the image label to support maximizing the image.

     @see #scaleImageIfTooBig(BufferedImage)
     @see #addMaximizePictureOnClick(JLabel, BufferedImage)
     */
    private void setImageOnChatPanel() {

        JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
        form_panel1.add(imageLabel, "cell 1 0, wrap");
        addMaximizePictureOnClick(imageLabel, image);
    }

    /**
     Handles the image caption for the given PictureModel.
     It creates a JTextPane with the caption text and adds it to the form_panel1 if the imageCaptionTextPane is not null.
     It also adds a right-click option to the image caption to support additional functionalities.

     @param pictureModel The PictureModel object containing the image caption.
     */
    private void handleImageCaption(final PictureModel pictureModel) {

        JTextPane imageCaptionTextPane = createImageCaptionTextPane(pictureModel);
        if (imageCaptionTextPane != null) {
            addRightClickOptionToPanel(imageCaptionTextPane);
            form_panel1.add(imageCaptionTextPane, "cell 1 1, wrap");
        }
    }

    /**
     Sets up the essential comment fields for the given PictureModel.
     It sets the name field, timestamp field, and the editor popup menu.

     @param pictureModel The PictureModel object for which to set up the comment essentials.
     */
    private void setupCommentEssentials(final PictureModel pictureModel) {

        //setup name
        setupNameField(pictureModel, form_nameLabel);

        //setup time
        setupTimeField( pictureModel, form_timeLabel);

        //setup popup menu
        jPopupMenu = setupEditorPopupMenu(pictureModel);
    }

    /**
     Handler for when the action label is mouse entered.

     @param e The MouseEvent object representing the mouse enter event.
     */
    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    /**
     Handler for when the mouse pointer exits the action label.

     @param e The MouseEvent object representing the mouse exit event.
     */
    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }

    /**
     Handler for when the reply button is clicked.
     Displays the editor popup menu at the position of the mouse click.

     @param e The MouseEvent object representing the mouse click event.
     */
    @Override
    protected void replyButtonClicked(MouseEvent e) {

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     Handler for when the action label is clicked.

     @param e The MouseEvent object representing the mouse click event.
     */
    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }
}