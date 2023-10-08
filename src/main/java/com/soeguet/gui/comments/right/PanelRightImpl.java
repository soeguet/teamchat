package com.soeguet.gui.comments.right;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.comments.interfaces.CommentInterface;
import com.soeguet.gui.comments.right.generated.PanelRight;
import com.soeguet.gui.comments.util.QuotePanelImpl;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import com.soeguet.model.jackson.PictureModel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 This class represents a right panel implementation that extends PanelRight and implements CommentInterface.
 */
public class PanelRightImpl extends PanelRight implements CommentInterface {

    private final Logger LOGGER = Logger.getLogger(PanelRightImpl.class.getName());
    private final BaseModel baseModel;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;
    private JTextPane actualTextPane;

    /**
     Creates a PanelRightImpl object with the given parameters.

     @param mainFrame the reference to the MainFrameInterface object
     @param baseModel the reference to the BaseModel object
     */
    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel) {

        super(mainFrame);

        this.baseModel = baseModel;
    }

    /**
     Sets up the text panel wrapper for the message.

     This method handles various tasks related to message processing and UI setup.
     It should only be called when the base model is an instance of MessageModel.
     The method uses SwingUtilities.invokeLater() to ensure that the UI-related tasks are executed on the Event Dispatch Thread (EDT).
     */
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

    @Override
    public JPanel getContainer() {

        return super.getContainer();
    }

    /**
     Handles the extraction of quoted sections from a text message.
     If the message contains any quotes, a QuotePanelImpl is created and added to the form_panel1.

     @param messageModel the message model containing the text message
     */
    private void handleTextMessageExtractionForQuotes(final MessageModel messageModel) {

        QuotePanelImpl quotedSectionPanel = checkForQuotesInMessage(messageModel);

        if (quotedSectionPanel != null) {

            form_container.add(quotedSectionPanel, "cell 0 0, wrap");
        }
    }

    /**
     Sets the text message on the chat panel.
     Creates a JTextPane with the user message from the given MessageModel and adds it to the form_panel1.
     The text color is set to black, and a right-click option is added to the panel.

     @param messageModel the message model containing the text message
     */
    private void setTextMessageOnChatPanel(final MessageModel messageModel) {

        actualTextPane = setUserMessage(messageModel);
        actualTextPane.setForeground(Color.BLACK);
        addRightClickOptionToPanel(actualTextPane);
        form_container.add(actualTextPane, "cell 0 1, wrap");
    }

    /**
     Sets up the comment essentials based on the given {@link MessageModel}.

     This method sets up the name field, time field, and popup menu for the comment based on the {@code messageModel} parameter.
     The name field is set up using the {@code form_nameLabel} parameter.
     The time field is set up using the {@code form_timeLabel} parameter.
     The popup menu is obtained from the {@code setupEditorPopupMenu} method.

     @param messageModel the message model containing the comment essentials
     */
    private void setupCommentEssentials(final MessageModel messageModel) {

        //setup time
        setupTimeField(messageModel, form_timeLabel);

        //setup name
        setupNameField(messageModel, form_nameLabel);

        //setup popup menu
        jPopupMenu = setupEditorPopupMenu(messageModel);
    }

    /**
     Sets up the picture panel wrapper for handling a {@link PictureModel} object.

     This method checks if the {@code baseModel} is an instance of the {@link PictureModel} class. If it is,
     it performs the following actions:
     - Handles image extraction and returns if the extracted image is null.
     - Sets the image on the chat panel and handles the maximum sizing of the image.
     - Handles the image caption and enables right-click ability on the text pane.
     - Sets up the remaining needed fields such as name, time, and popup menu using the {@code pictureModel} object.

     This method is executed asynchronously using {@link SwingUtilities#invokeLater(Runnable)} to ensure proper
     handling of Swing components.
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
     Handles the extraction of an image from a {@link PictureModel} object.

     @param pictureModel The {@code PictureModel} object from which to extract the image.

     @return {@code true} if the extracted image is {@code null}, indicating an error occurred. Otherwise, {@code false}.
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
     */
    private void setImageOnChatPanel() {

        JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
        form_container.add(imageLabel, "cell 0 0, wrap");
        addMaximizePictureOnClick(imageLabel, image);
    }

    /**
     Handles the image caption for the given PictureModel.

     @param pictureModel The PictureModel containing the image caption information.
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

     @param pictureModel The PictureModel containing the image caption information.
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
     Called when the reply button is clicked.

     @param e The MouseEvent that triggered the reply button click.
     */
    @Override
    protected void replyButtonClicked(MouseEvent e) {

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     Called when the mouse hovers over the action label.

     @param e The MouseEvent that triggered the mouse enter event.
     */
    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    /**
     Called when the mouse clicks the action label.

     @param e The MouseEvent that triggered the mouse click event.
     */
    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }

    /**
     Called when the mouse pointer exits the area of the action label.

     @param e The MouseEvent that triggered the mouse exit event.
     */
    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }

    /**
     Called when the component is resized.

     @param e The ComponentEvent that triggered the resize event.
     */
    @Override
    protected void thisComponentResized(ComponentEvent e) {

    }
}