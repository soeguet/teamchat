package com.soeguet.gui.option_pane.links;

import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class LinkDialogImplTest {

    @Test
    void returnNullIfStatusCodeOfLinkIs400Or500() {

        final LinkDialogHandler linkDialogHandler = Mockito.spy(LinkDialogHandler.class);

        Mockito.when(linkDialogHandler.checkStatusCodeOfLink(Mockito.anyString())).thenReturn(400);

        final MetadataStorageRecord metadataStorageRecord =
                linkDialogHandler.checkForMetaData("https://www.goasdogle.com");
        Assertions.assertNull(metadataStorageRecord);
    }

    @Test
    void checkIfStatusCodeIs1000() {

        final LinkDialogHandler linkDialogHandler = Mockito.spy(new LinkDialogHandler());
        final int statusCodeOfLink =
                linkDialogHandler.checkStatusCodeOfLink("https://www.googsssle.com");

        Assertions.assertEquals(1000, statusCodeOfLink);
    }

    @Test
    void checkIfStatusCodeIs200() {

        final LinkDialogHandler linkDialogHandler = Mockito.spy(LinkDialogHandler.class);

        Mockito.when(linkDialogHandler.checkStatusCodeOfLink(Mockito.anyString())).thenReturn(200);
        final int statusCodeOfLink =
                linkDialogHandler.checkStatusCodeOfLink("https://www.google.com");

        Assertions.assertEquals(200, statusCodeOfLink);
    }
}
