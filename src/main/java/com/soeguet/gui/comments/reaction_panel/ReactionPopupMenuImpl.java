package com.soeguet.gui.comments.reaction_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soeguet.gui.comments.reaction_panel.dtos.ReactionPanelDTO;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.UserInteraction;
import com.soeguet.model.jackson.BaseModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ReactionPopupMenuImpl extends JPopupMenu {

    private final ReactionPanelDTO reactionPanelDTO;
    private final JPanel topPanel;
    Logger logger = Logger.getLogger(ReactionPopupMenuImpl.class.getName());
    private ReactionPopupHandler reactionPopupHandler;

    /**
     This class represents a reaction popup menu implementation. It provides functionality to handle reactions in a popup menu.
     */
    public ReactionPopupMenuImpl(final ReactionPanelDTO reactionPanelDTO, JPanel topPanel) {

        this.reactionPanelDTO = reactionPanelDTO;
        this.topPanel = topPanel;
    }

    /**
     Sets up the popup menu for handling reactions. It adds reaction items to the menu panel.
     */
    public void setPopupMenuUp() {

        JPanel jPanel = new JPanel(new MigLayout());
        addItemToMenu(jPanel, "/emojis/$+1f4af$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f92e$+.png");
        addItemToMenu(jPanel, "/emojis/$+1fae1$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f631$+.png");
        addItemToMenu(jPanel, "/emojis/$+2764$+.png");
        jPanel.add(new JLabel(" "), "wrap");
        addItemToMenu(jPanel, "/emojis/$+2714$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f923$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f44d-1f3fc$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f44c-1f3fc$+.png");
        addItemToMenu(jPanel, "/emojis/$+1f44e-1f3fc$+.png");
        add(jPanel);
    }

    /**
     Adds an item with the specified image to the given menu panel.

     @param jPanel
     the menu panel to add the item to
     @param item
     the path to the image for the item
     */
    private void addItemToMenu(final JPanel jPanel, final String item) {

        URL url = getClass().getResource(item);
        if (url != null) {
            ImageIcon imageIcon = new ImageIcon(url);

            int start = item.indexOf("$");
            int end = item.indexOf(".", start);
            String emojiName = item.substring(start, end);
            imageIcon.setDescription(emojiName);

            final JPanel panel = createReactionEmojiPanel();
            panel.add(new JLabel(imageIcon));
            jPanel.add(panel);
        }
    }

    private JPanel createReactionEmojiPanel() {

        JPanel panel = new JPanel();

        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {

                for (Component component : panel.getComponents()) {

                    if (component instanceof JLabel) {

                        ImageIcon icon = (ImageIcon) ((JLabel) component).getIcon();
                        String description = icon.getDescription();

                        BaseModel baseModel = reactionPanelDTO.baseModel();

                        baseModel.setMessageType(MessageTypes.INTERACTED);

                        final String timeAndUsername =
                                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + reactionPanelDTO.username();
                        UserInteraction userInteraction = new UserInteraction(timeAndUsername, description);

                        baseModel.getUserInteractions().add(userInteraction);

                        String serializedBaseModel;

                        try {

                            serializedBaseModel = reactionPanelDTO.objectMapper().writeValueAsString(baseModel);

                        } catch (JsonProcessingException jsonProcessingException) {

                            logger.log(java.util.logging.Level.SEVERE, "PLACE: ReactionPopupMenuImpl > createReactionEmojiPanel");
                            logger.log(java.util.logging.Level.SEVERE, jsonProcessingException.getMessage(), e);
                            throw new RuntimeException(jsonProcessingException);
                        }

                        reactionPanelDTO.websocketClient().send(serializedBaseModel);
                    }
                }

                panel.setBackground(null);
                setVisible(false);
            }

            @Override
            public void mouseEntered(final MouseEvent e) {

                panel.setBackground(new Color(0, 136, 191));
            }

            @Override
            public void mouseExited(final MouseEvent e) {

                panel.setBackground(null);
            }
        });

        return panel;
    }

    /**
     Starts the animation if the component is not currently visible.
     <p>
     If the component is not visible, the method initializes the popup timer of the reactionPopupHandler.
     */
    public void startAnimation() {

        if (!isVisible()) {

            reactionPopupHandler.initializePopupTimer();
        }
    }

    /**
     Initializes the reactionPopupHandler with the given layeredPane.
     <p>
     This method creates a new instance of ReactionPopupHandler and assigns it to the reactionPopupHandler variable.

     @param layeredPane
     the layeredPane to be used by the reactionPopupHandler
     */
    public void initializePopupHandler() {

        reactionPopupHandler = new ReactionPopupHandler(this,topPanel );
    }

    /**
     Stops the animation.
     <p>
     This method calls the stopPopupTimer() method of the reactionPopupHandler instance, effectively stopping the animation.
     <p>
     This method does not return any value.
     */
    public void stopAnimation() {

        reactionPopupHandler.stopPopupTimer();

    }

    /**
     Disposes of the component.
     <p>
     This method is called when a mouse event occurs and the component is visible. It sets the visibility of the component to false and recursively
     calls the dispose method with the MouseEvent e.

     @param e
     the MouseEvent that triggered the disposal
     */
    public void dispose(final MouseEvent e) {

        if (isVisible()) {

            setVisible(false);
            dispose(e);
        }
    }

    /**
     Checks if the mouse left the container completely.
     <p>
     This method is called when a mouse event occurs and checks if the mouse coordinates are outside the boundaries of the component. It returns true
     if the mouse coordinates are less than or equal to 0 on the x-axis, greater than or equal to the width of the component on the x-axis, less than
     or equal to 0 on the y-axis, and greater than or equal to the height of the component on the y-axis.

     @param e
     the MouseEvent to check

     @return true if the mouse left the container completely, false otherwise
     */
    public boolean mouseLeftContainerCompletely(final MouseEvent e) {

        return e.getX() <= 0 || e.getX() >= e.getComponent().getWidth() || e.getY() <= 0 || e.getY() >= e.getComponent().getHeight();
    }
}