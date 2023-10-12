package com.soeguet.gui.option_pane.links.interfaces;

import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;

import javax.swing.*;
import java.awt.*;

public interface LinkDialogInterface {


    MetadataStorageRecord checkForMetaData(final String link);

    JPanel createMetadataPanel(MetadataStorageRecord metadataStorageRecord);

    void add(Component comp, Object constraints);

    void generate();
    JPanel getContentPanel();

    JTextPane getLinkTextPane();
    JTextPane getCommentTextPane();
}