package com.soeguet.links.interfaces;

import com.soeguet.links.dtos.MetadataStorageRecord;
import java.awt.*;
import javax.swing.*;

public interface LinkDialogInterface {

    MetadataStorageRecord checkForMetaData(final String link);

    JPanel createMetadataPanel(MetadataStorageRecord metadataStorageRecord);

    void add(Component comp, Object constraints);

    void generate();

    JPanel getContentPanel();

    JTextPane getLinkTextPane();

    JTextPane getCommentTextPane();
}