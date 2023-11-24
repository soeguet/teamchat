package com.soeguet.gui.comments.generic_comment.gui_elements.panels;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class CustomPictureWrapperPanel extends JPanel {

    public CustomPictureWrapperPanel() {

    }

    public void setPictureWrapperPanelLayoutManager() {

        /*
        SCHEMA: Picture Panel consists of:

        [QUOTE]
        [PICTURE]
        [DESCRIPTION/TEXT]
         */

        super.setLayout(new MigLayout("",
                                      //columns
                                      "[fill,grow]",
                                      //rows
                                      "[fill,grow][fill,grow][fill,grow]")
                       );
    }
}