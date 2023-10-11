package com.soeguet.gui.option_pane.links;

import com.soeguet.gui.option_pane.links.dtos.AbsoluteLinkRecord;
import com.soeguet.gui.option_pane.links.dtos.MetadataStorageRecord;
import com.soeguet.gui.option_pane.links.generated.LinkDialog;
import com.soeguet.gui.option_pane.links.interfaces.LinkDialogInterface;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LinkDialogImpl extends LinkDialog implements LinkDialogInterface {

    public LinkDialogImpl(final Window owner) {

        super(owner);
        setTitle("send link");
    }

    @Override
    public AbsoluteLinkRecord validateUri(final String link) {

        try {

            URI uri = new URI(link).normalize();

            if (!uri.isAbsolute()) {

                return new AbsoluteLinkRecord(false, uri);
            }

            return new AbsoluteLinkRecord(true, uri);

        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Document loadDocumentForLink(final URI uri) {

        try {

            return Jsoup.connect(uri.toURL().toString()).get();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public int checkStatusCodeOfLink(final String link) {

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request;

            try {
                request = HttpRequest.newBuilder()
                        .uri(new URI(link))
                        .build();

            } catch (URISyntaxException e) {

                throw new RuntimeException(e);
            }

            try {

                response = client.send(request, HttpResponse.BodyHandlers.ofString());

            } catch (IOException | InterruptedException e) {

                throw new RuntimeException(e);
            }

            int statusCode = response.statusCode();
            System.out.println(statusCode);

            return statusCode;
        }
    }

    @Override
    public MetadataStorageRecord fetchMetaDataFromLink(final Document doc) {

        String imageUrl = extractImageUrl(doc);
        BufferedImage image = null;

        if (imageUrl == null) {

            return new MetadataStorageRecord(doc.title(), null);
        }

        try {

            image = ImageIO.read(new URI(imageUrl).toURL());

            cropImageIfTooLarge(image);

        } catch (IOException | URISyntaxException e) {

            throw new RuntimeException(e);
        }

        return new MetadataStorageRecord(doc.title(), image);
    }

    @Override
    public void handleMetadata(final String link, final JTextPane jTextPane) {

    }

    private void cropImageIfTooLarge(final BufferedImage image) {

        if (image.getWidth() > 500) {

            image.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
        }
    }

    private String extractImageUrl(final Document doc) {
        Element imageElement = doc.select("meta[property=og:image]").first();
        if (imageElement != null) {
            return imageElement.attr("content");
        }
        return null;
    }
}