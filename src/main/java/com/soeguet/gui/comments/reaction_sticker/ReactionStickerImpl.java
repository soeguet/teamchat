package com.soeguet.gui.comments.reaction_sticker;

import com.soeguet.gui.main_frame.interfaces.MainFrameGuiInterface;
import com.soeguet.model.UserInteraction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReactionStickerImpl {

    private final MainFrameGuiInterface mainFrame;
    private final JLayeredPane container;
    private final List<UserInteraction> userInteractions;
    private JPanel stickerPanel;

    public ReactionStickerImpl(final MainFrameGuiInterface mainFrame, final JLayeredPane layeredPane, final List<UserInteraction> userInteractions) {

        this.mainFrame = mainFrame;
        this.container = layeredPane;
        this.userInteractions = userInteractions;
    }

    public synchronized void pasteStickerToContainer() {

        stickerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stickerPanel.setOpaque(false);

        userInteractions.forEach(userInteraction -> {

            final ImageIcon imageIcon = mainFrame.getEmojiList().get(userInteraction.emoji());
            imageIcon.setDescription(userInteraction.timeAndUsername());

            CustomStickerContainer customStickerContainer = new CustomStickerContainer(imageIcon);

            stickerPanel.add(customStickerContainer);
        });

        setStickerPanelBounds();
        container.add(stickerPanel, 0);
    }

    private void setStickerPanelBounds() {

        int panelHeight = 30;
        int panelHeightWithMargin = panelHeight + 5;
        stickerPanel.setBounds(0, container.getHeight() - panelHeightWithMargin - 1, stickerPanel.getPreferredSize().width,
                               stickerPanel.getPreferredSize().height);
    }

    public void repositionSticker() {

        if (stickerPanel != null) {

            setStickerPanelBounds();
        }
    }
}