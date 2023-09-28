package com.soeguet.gui.newcomment.right;

import com.soeguet.gui.main_frame.MainFrameInterface;
import com.soeguet.gui.newcomment.helper.CommentInterface;
import com.soeguet.gui.newcomment.right.generated.PanelRight;
import com.soeguet.model.PanelTypes;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 This class represents a right panel implementation that extends PanelRight and implements CommentInterface.
 It provides methods to set up the text panel wrapper, the picture panel, and add right-click options to the panel.
 */
public class PanelRightImpl extends PanelRight implements CommentInterface {

    private final Logger LOGGER = Logger.getLogger(PanelRightImpl.class.getName());
    private final MainFrameInterface mainFrame;
    private final BaseModel baseModel;
    private PanelTypes panelTyp;
    private JPopupMenu jPopupMenu;
    private BufferedImage image;

    /**
     Constructs a new PanelRightImpl object.

     @param mainFrame the MainFrameInterface object representing the main frame
     @param baseModel the BaseModel object representing the base model
     @param panelTyp  the PanelTypes object representing the panel type
     */
    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel, PanelTypes panelTyp) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
        this.panelTyp = panelTyp;
    }

    /**
     Creates a new instance of PanelRightImpl.

     @param mainFrame the main frame object
     @param baseModel the base model object
     */
    public PanelRightImpl(MainFrameInterface mainFrame, BaseModel baseModel) {

        this.mainFrame = mainFrame;
        this.baseModel = baseModel;
    }

    /**
     Sets up the text panel wrapper.

     This method is used to set up the text panel wrapper for a message. It performs
     several operations to configure the text panel wrapper, such as checking for
     quotes in the message, adding the actual message, setting up the editor popup
     menu, adding a right-click option to the panel, and setting up reply panels.

     This method should be called when the base model is an instance of the MessageModel
     class. It should be invoked using SwingUtilities.invokeLater() to ensure that the
     operation is executed on the Event Dispatch Thread.
     */
    @Override
    public void setupTextPanelWrapper() {

        if (!(baseModel instanceof MessageModel)) {
            return;
        }

        SwingUtilities.invokeLater(() -> {

            checkForQuotesInMessage(mainFrame, baseModel, form_panel1);

            JTextPane actualTextPane = setUserMessage(mainFrame, baseModel, form_panel1);
            setNameField(mainFrame, baseModel, form_nameLabel, panelTyp);
            setTimestampField(mainFrame, baseModel, form_nameLabel, panelTyp);
            jPopupMenu = setupEditorPopupMenu(mainFrame,baseModel);
            addRightClickOptionToPanel( this,actualTextPane);

            setupReplyPanels(panelTyp, form_panel1);
        });
    }

    /**
     Sets up the picture panel in the user interface.
     This method is called when a picture message is received and needs to be displayed.
     It performs multiple tasks as a wrapper method.
     */
    @Override
    public void setupPicturePanelWrapper() {

        SwingUtilities.invokeLater(() -> {

            jPopupMenu = setupEditorPopupMenu(mainFrame, baseModel);

            this.image = extractImageFromMessage(baseModel);

            if (image == null) {

                LOGGER.log(java.util.logging.Level.SEVERE, "Buffered image is null");
                return;
            }

            JLabel imageLabel = new JLabel(scaleImageIfTooBig(image));
            form_panel1.add(imageLabel, "cell 0 0, wrap");

            addMaximizePictureOnClick(imageLabel, image);

            JTextPane imageCaptionTextPane = createImageCaptionTextPane( baseModel);
            //if null -> there is no message
            if (imageCaptionTextPane != null) {
                form_panel1.add(imageCaptionTextPane, "cell 0 1, wrap");
            }

            addRightClickOptionToPanel(null, imageCaptionTextPane);

            setNameField(mainFrame, baseModel, form_nameLabel, panelTyp);
            setTimestampField(mainFrame, baseModel, form_timeLabel, panelTyp);
        });
    }



    /**
     Overrides the replyButtonClicked method from the parent class.

     @param e The MouseEvent object representing the mouse event that triggered the method.

     The method shows a JPopupMenu at the specified location obtained from the MouseEvent object's
     getX() and getY() methods. The component on which the method is called is obtained from the
     getComponent() method of the MouseEvent object.
     */
    @Override
    protected void replyButtonClicked(MouseEvent e) {

        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    protected void actionLabelMouseEntered(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseClicked(MouseEvent e) {

    }

    @Override
    protected void actionLabelMouseExited(MouseEvent e) {

    }

    @Override
    protected void thisComponentResized(ComponentEvent e) {

    }
}