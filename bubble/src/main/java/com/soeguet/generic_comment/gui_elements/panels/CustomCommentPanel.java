package com.soeguet.generic_comment.gui_elements.panels;

import com.soeguet.enums.CommentTypeEnum;
import com.soeguet.generic_comment.factories.LinkPanelFactory;
import com.soeguet.generic_comment.factories.PicturePanelFactory;
import com.soeguet.generic_comment.factories.TextMessageFactory;
import com.soeguet.generic_comment.gui_elements.util.ChatBubblePaintHandler;
import com.soeguet.generic_comment.util.Side;
import com.soeguet.model.jackson.BaseModel;
import com.soeguet.model.jackson.MessageModel;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This panel is placed on the GUI as the main comment panel.<br>
 * It covers one row on the scrollpane and will be pushed up the more of these are added.<br>
 * One is added per row and it covers the whole width.<br>
 *
 * <p>It contains everything else related to a chat message:<br>
 * - the side panel {@link CommentSidePanel}<br>
 * - the top panel {@link TransparentTopPanel}<br>
 * - the main content panel<br>
 * - the text panel<br>
 */
public class CustomCommentPanel extends JPanel {

  // variables -- start

  /** Represents a side of the comment - LEFT or RIGHT. */
  private Side side;

  /**
   * Represents the main information Model of this app.
   *
   * @see BaseModel
   */
  private BaseModel baseModel;

  private CommentTypeEnum commentType;
  private CommentSidePanel sidePanel;

  /**
   * The top level container will be used for MouseEvents as well as reaction emojis.
   *
   * @see JPanel
   */
  private TransparentTopPanel topContainer;

  /** The main container panel will be used for the side panel as well as the content container. */
  private CustomMainWrapperContainer customMainWrapperContainer;

  /**
   * The content container will be used for the text and picture content as well as the quoted
   * comments.
   */
  private CustomContentContainer customContentContainer;

  private JTextPane jTextPane;
  private Color borderColor;
  private ChatBubblePaintHandler chatBubblePaintHandler;

  // variables -- end

  // constructors -- start
  public CustomCommentPanel() {}

  // constructors -- end

  public void setLayoutManager() {

    this.setThisComponentLayoutManager();
  }

  private void setThisComponentLayoutManager() {

    this.setLayout(new OverlayLayout(this));
  }

  public void addComponents() {

    /*
     * CONSTRAINS EXPLAINED
     * cell 1 1 1 2
     * colum 1, row 1, span col 1, span row 2
     */

    // MainPanel - MigLayout - wraps SIDE and CONTENT - wrapped by
    // CustomCommentPanel (OverlayLayout)
    final CustomMainWrapperContainer mainPanel = this.getCustomMainWrapperContainer();

    // wrapped by CustomCommentPanel (OverlayLayout)
    final TransparentTopPanel topPanel = this.getTopContainer();

    // wrapped by CustomMainWrapperContainer -- SIDE
    final CommentSidePanel commentSidePanel = this.getSidePanel();

    // TODO: 06.11.23 - this works but it needs to be cleaned up ->
    // contentStackPanel is new
    // wrapped by CustomMainWrapperContainer -- CONTENT -> again wrapped by
    // contentStackPanel -> topPanel and mainContentPanel
    // everything else is a hassle to adjust the right alignment of the top panel

    final JPanel contentStackPanel = new JPanel();
    contentStackPanel.setLayout(new OverlayLayout(contentStackPanel));
    final CustomContentContainer mainContentPanel = this.getCustomContentContainer();
    topPanel.setReferenceToMainContentContainer(mainContentPanel);

    // hella hacky approach, but I need that margin for the emoticons. a margin right on the
    // maincontentpanel
    // does not work
    JPanel marginPanel = new JPanel();
    marginPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
    marginPanel.add(mainContentPanel);

    contentStackPanel.add(topPanel);
    contentStackPanel.add(marginPanel);

    switch (this.getSide()) {
      case LEFT -> {

        // SIDE
        mainPanel.add(commentSidePanel, "cell 0 0 1 2, dock west");

        // CONTENT
        mainPanel.add(contentStackPanel, "cell 1 0 1 2, dock west, gapleft 5, gapright 50");
      }

      case RIGHT -> {

        // SIDE
        mainPanel.add(commentSidePanel, "cell 1 0 1 2, dock east");

        // CONTENT
        mainPanel.add(contentStackPanel, "cell 0 0 1 2, dock east, gapright 5, gapleft 50, grow");
      }
    }

    this.add(mainPanel);
  }

  public void addContext() {

    // setup
    final CustomContentContainer contentContainer = this.getCustomContentContainer();
    final Side commentSide = this.getSide();

    switch (this.commentType) {
      case LEFT_TEXT, RIGHT_TEXT -> this.processIncomingTextMessage(commentSide, contentContainer);

      case LEFT_PICTURE, RIGHT_PICTURE -> this.processIncomingPictureMessage(
          commentSide, contentContainer);

      case LEFT_LINK, RIGHT_LINK -> this.processIncomingLinkMessage(commentSide, contentContainer);
    }
  }

  private void processIncomingLinkMessage(
      final Side commentSide, final CustomContentContainer contentContainer) {

    final CustomLinkPanel customLinkPanel = new LinkPanelFactory(this.baseModel).create();

    switch (commentSide) {
      case LEFT -> {
        final String leftConstraints = "cell 0 0, gapleft 15, gapright 5";
        contentContainer.add(customLinkPanel, leftConstraints);
      }

      case RIGHT -> {
        final String rightConstraints = "cell 0 0, gapleft 3, gapright 15";
        contentContainer.add(customLinkPanel, rightConstraints);
      }
    }
  }

  private void processIncomingPictureMessage(
      final Side commentSide, final CustomContentContainer contentContainer) {

    final CustomPictureWrapperPanel pictureLabel = new PicturePanelFactory(this.baseModel).create();

    switch (commentSide) {
      case LEFT -> {
        final String leftConstraints = "cell 0 0, wrap, gapleft 15, gapright 5, grow 1.1";
        contentContainer.add(pictureLabel, leftConstraints);
      }

      case RIGHT -> {
        final String rightConstraints = "cell 0 0, wrap, gapleft 3, gapright 15, grow 1.1";
        contentContainer.add(pictureLabel, rightConstraints);
      }
    }
  }

  private void processIncomingTextMessage(
      final Side commentSide, final CustomContentContainer contentContainer) {

    // TODO factory method maybe?
    if (this.baseModel instanceof final MessageModel messageModel) {

      final CustomTextAndQuoteForBubblePanel customTextAndQuoteForBubblePanel =
          new TextMessageFactory(messageModel).create();

      switch (commentSide) {
        case LEFT -> {
          final String leftConstraints = "gapleft 15, grow";
          contentContainer.add(customTextAndQuoteForBubblePanel, leftConstraints);
        }

        case RIGHT -> {
          final String rightConstraints = "gapright 15, grow";
          contentContainer.add(customTextAndQuoteForBubblePanel, rightConstraints);
        }
      }
    }
  }

  public void paintChatBubble() {

    final Side commentSide = this.getSide();
    // TODO: 02.11.23 border color implementation
    this.setBorderColor(new Color(23, 0, 146));
    this.chatBubblePaintHandler =
        new ChatBubblePaintHandler(
            this.getCustomContentContainer(), commentSide, this.getBorderColor());

    this.chatBubblePaintHandler.setupChatBubble();
  }

  public ChatBubblePaintHandler getChatBubblePaintHandler() {

    return this.chatBubblePaintHandler;
  }

  public void setSide() {

    switch (this.getCommentType()) {
      case LEFT_TEXT, LEFT_PICTURE, LEFT_LINK -> this.side = Side.LEFT;

      case RIGHT_TEXT, RIGHT_PICTURE, RIGHT_LINK -> this.side = Side.RIGHT;
    }
  }

  public void prepareInteractionButtons() {}

  // getter & setter -- start
  public BaseModel getBaseModel() {

    return this.baseModel;
  }

  public void setBaseModel(final BaseModel baseModel) {

    this.baseModel = baseModel;
  }

  public Color getBorderColor() {

    return this.borderColor;
  }

  public void setBorderColor(final Color borderColor) {

    this.borderColor = borderColor;
  }

  public CommentTypeEnum getCommentType() {

    return this.commentType;
  }

  public void setCommentType(final CommentTypeEnum commentType) {

    this.commentType = commentType;
  }

  public CustomContentContainer getCustomContentContainer() {

    return this.customContentContainer;
  }

  public void setCustomContentContainer(final CustomContentContainer customContentContainer) {

    this.customContentContainer = customContentContainer;
  }

  public CustomMainWrapperContainer getCustomMainWrapperContainer() {

    return this.customMainWrapperContainer;
  }

  public void setCustomMainWrapperContainer(
      final CustomMainWrapperContainer customMainWrapperContainer) {

    this.customMainWrapperContainer = customMainWrapperContainer;
  }

  public Side getSide() {

    return this.side;
  }

  public CommentSidePanel getSidePanel() {

    return this.sidePanel;
  }

  public void setSidePanel(final CommentSidePanel sidePanel) {

    this.sidePanel = sidePanel;
  }

  public TransparentTopPanel getTopContainer() {

    return this.topContainer;
  }

  public void setTopContainer(final TransparentTopPanel topContainer) {

    this.topContainer = topContainer;
  }

  public JTextPane getjTextPane() {

    return this.jTextPane;
  }

  public void setTextPane(final JTextPane jTextPane) {

    this.jTextPane = jTextPane;
  }
  // getter & setter -- end
}
