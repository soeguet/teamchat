package com.soeguet.gui.option_pane.links.interfaces;

import com.soeguet.gui.option_pane.links.dtos.AbsoluteLinkRecord;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public interface LinkDialogInterface {

    void setVisible(boolean b);
    JPanel getContentPanel();
    void setSize(Dimension dimension);
    void pack();

    void setLocationRelativeTo(Component component);

    AbsoluteLinkRecord validateUri(String link);

    Document loadDocumentForLink(URI uri);

    int checkStatusCodeOfLink(String link);

    MetadataStorageRecord fetchMetaDataFromLink(Document doc);

    void handleMetadata(String link, JTextPane jTextPane);
}