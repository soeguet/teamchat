package com.soeguet.generic_comment.gui_elements.panels;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class CustomPictureWrapperPanel extends JPanel {

    public CustomPictureWrapperPanel() {}

    public void setPictureWrapperPanelLayoutManager() {

        /*
        SCHEMA: Picture Panel consists of:

        [QUOTE]
        [PICTURE]
        [DESCRIPTION/TEXT]
         */

        super.setLayout(
                new MigLayout(
                        "",
                        // columns
                        "[fill,grow]",
                        // rows
                        "[fill,grow][fill,grow][fill,grow]"));
    }
}