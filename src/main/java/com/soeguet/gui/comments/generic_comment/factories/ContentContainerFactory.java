package com.soeguet.gui.comments.generic_comment.factories;

import com.soeguet.gui.comments.generic_comment.gui_elements.panels.CustomContentContainer;

public class ContentContainerFactory {

    public ContentContainerFactory() {}

    public CustomContentContainer create() {

        CustomContentContainer customContentContainer = new CustomContentContainer();
        customContentContainer.setContentContainerLayoutManager();

        return customContentContainer;
    }
}
