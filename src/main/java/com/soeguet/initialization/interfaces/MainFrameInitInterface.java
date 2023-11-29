package com.soeguet.initialization.interfaces;

import com.soeguet.emoji.interfaces.EmojiInitializerInterface;
import com.soeguet.model.EnvVariables;

public interface MainFrameInitInterface {

    void repositionChatFrameForTestingPurposes();

    void loadUsernameFromEnvVariables(final EnvVariables envVariables);

    void initEmojiList(EmojiInitializerInterface emojiInitializer);

    void setScrollPaneMargins();

    void setMainFrameTitle();

    void setGuiIcon();

    void setButtonIcons();

    void setVisible(boolean b);

    void setEnvVariables(EnvVariables envVariables);

    void loadCustomProperties();

    void initGuiFunctionality();

    void initializeClientController();

    void initEmojiHandler();

    void setFixedScrollSpeed(int i);

    void setupSystemTrayIcon();
}
