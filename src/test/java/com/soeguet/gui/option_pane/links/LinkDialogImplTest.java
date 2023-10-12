package com.soeguet.gui.option_pane.links;

import com.soeguet.gui.main_frame.interfaces.MainFrameInterface;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertNull;

class LinkDialogImplTest {

    @Test
    void returnNullIfStatusCodeOfLinkIs400Or500() {

        LinkDialogImpl linkDialog = Mockito.spy(new LinkDialogImpl());

        final MetadataStorageRecord metadataStorageRecord = linkDialog.checkForMetaData("https://www.goasdogle.com");

        assertNull(metadataStorageRecord);
    }
}