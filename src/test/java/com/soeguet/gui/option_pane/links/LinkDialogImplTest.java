package com.soeguet.gui.option_pane.links;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LinkDialogImplTest {

    @Test
    void returnNullIfStatusCodeOfLinkIs400Or500() {

        LinkDialogHandler linkDialogHandler = Mockito.spy(LinkDialogHandler.class);

        Mockito.when(linkDialogHandler.checkStatusCodeOfLink(Mockito.anyString())).thenReturn(400);

        final MetadataStorageRecord metadataStorageRecord =
                linkDialogHandler.checkForMetaData("https://www.goasdogle.com");
        assertNull(metadataStorageRecord);
    }

    @Test
    void checkIfStatusCodeIs1000() {

        LinkDialogHandler linkDialogHandler = Mockito.spy(new LinkDialogHandler());
        final int statusCodeOfLink =
                linkDialogHandler.checkStatusCodeOfLink("https://www.googsssle.com");

        assertEquals(1000, statusCodeOfLink);
    }

    @Test
    void checkIfStatusCodeIs200() {

        LinkDialogHandler linkDialogHandler = Mockito.spy(new LinkDialogHandler());
        final int statusCodeOfLink =
                linkDialogHandler.checkStatusCodeOfLink("https://www.google.com");

        assertEquals(200, statusCodeOfLink);
    }
}
