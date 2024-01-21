package com.soeguet.comments.reaction_sticker;

import com.soeguet.emoji.EmojiRegister;
import com.soeguet.model.UserInteraction;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ReactionStickerImpl {

    private final JLayeredPane container;
    private final List<UserInteraction> userInteractions;
    private JPanel stickerPanel;

    public ReactionStickerImpl(
            final JLayeredPane layeredPane,
            final List<UserInteraction> userInteractions) {

        this.container = layeredPane;
        this.userInteractions = userInteractions;
    }

    public synchronized void pasteStickerToContainer() {

        stickerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stickerPanel.setOpaque(false);

        EmojiRegister emojiRegister = EmojiRegister.getInstance();

        userInteractions.forEach(
                userInteraction -> {
                    final ImageIcon imageIcon =
                           emojiRegister.getEmojiList().get(userInteraction.emoji());
                    imageIcon.setDescription(userInteraction.timeAndUsername());

                    CustomStickerContainer customStickerContainer =
                            new CustomStickerContainer(imageIcon);

                    stickerPanel.add(customStickerContainer);
                });

        setStickerPanelBounds();
        container.add(stickerPanel, 0);
    }

    private void setStickerPanelBounds() {

        int panelHeight = 30;
        int panelHeightWithMargin = panelHeight + 5;
        stickerPanel.setBounds(
                0,
                container.getHeight() - panelHeightWithMargin - 1,
                stickerPanel.getPreferredSize().width,
                stickerPanel.getPreferredSize().height);
    }

    public void repositionSticker() {

        if (stickerPanel != null) {

            setStickerPanelBounds();
        }
    }

    /**
     * Revalidates the sticker count based on the maximum number of slots for emotes and the number
     * of available slots in the sticker panel container. This is determined by the width of the
     * container divided by the width of the emote slot. This method hides or shows the components
     * in the sticker panel based on the maximum number of slots for emotes.
     */
    public void revalidateStickerCount() {

        final Component[] components = stickerPanel.getComponents();

        int maxSlotsForEmotes = container.getWidth() / 40;

        for (int i = 0; i < components.length; i++) {

            final boolean b = i < maxSlotsForEmotes;
            components[i].setVisible(b);
        }
    }
}