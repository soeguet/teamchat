package com.soeguet.links;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soeguet.links.dtos.AbsoluteLinkRecord;
import com.soeguet.links.dtos.LinkTransferDTO;
import com.soeguet.links.dtos.MetadataStorageRecord;
import com.soeguet.model.MessageTypes;
import com.soeguet.model.jackson.LinkModel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;

import com.soeguet.properties.PropertiesRegister;
import com.soeguet.socket_client.ClientRegister;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LinkDialogHandler {

    public LinkDialogHandler() {}

    public MetadataStorageRecord checkForMetaData(final String link) {

        // if GET request is ok -> look for metadata
        int statusCode = checkStatusCodeOfLink(link);
        if (statusCode < 400) {

            // check if it is an absolute link -> if not, there are most likely no metadata
            AbsoluteLinkRecord absoluteLinkRecord = validateUri(link);

            if (absoluteLinkRecord.isAbsoluteLink()) {

                Document doc = loadDocumentForLink(absoluteLinkRecord.link());

                if (doc == null) {

                    return null;

                } else {

                    // fetch metadata -> title and preview image
                    return fetchMetaDataFromLink(doc);
                }
            }
        }
        return null;
    }

    /**
     * Sends an HTTP request to the given link and returns the status code of the response.
     *
     * @param link the URL of the link to check
     * @return the status code of the response, or 1000 if an error occurred
     */
    public int checkStatusCodeOfLink(final String link) {

        final AtomicReference<HttpResponse<String>> response = new AtomicReference<>();

        new Thread(
                        () -> {
                            try {

                                HttpClient client = HttpClient.newHttpClient();
                                HttpRequest request;

                                request = HttpRequest.newBuilder().uri(new URI(link)).build();

                                response.set(
                                        client.send(request, HttpResponse.BodyHandlers.ofString()));

                            } catch (IOException
                                    | InterruptedException
                                    | URISyntaxException
                                    | IllegalArgumentException e) {

                                System.err.println(
                                        "Failed to connect or send the HTTP request: "
                                                + e.getMessage());
                            }
                        })
                .start();

        return response.get() == null ? 1_000 : response.get().statusCode();
    }

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

    public Document loadDocumentForLink(final URI uri) {

        try {

            return Jsoup.connect(uri.toURL().toString()).get();

        } catch (IOException e) {

            return null;
        }
    }

    public MetadataStorageRecord fetchMetaDataFromLink(final Document doc) {

        String imageUrl = extractImageUrl(doc);
        BufferedImage image = null;

        if (imageUrl == null) {

            return new MetadataStorageRecord(doc.title(), null);
        }

        try {

            image = ImageIO.read(new URI(imageUrl).toURL());

        } catch (IOException | URISyntaxException e) {

            throw new RuntimeException(e);
        }

        cropImageIfTooLarge(image);

        return new MetadataStorageRecord(doc.title(), image);
    }

    private String extractImageUrl(final Document doc) {

        Element imageElement = doc.select("meta[property=og:image]").first();

        if (imageElement != null) {

            return imageElement.attr("content");
        }

        return null;
    }

    private void cropImageIfTooLarge(final BufferedImage image) {

        if (image.getWidth() > 500) {

            image.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
        }
    }

    /**
     * Sends a link message to the websocket server.
     *
     * @param linkTransferDTO the message to send
     */
    public void sendLinkToWebsocket(
            final LinkTransferDTO linkTransferDTO) {

        PropertiesRegister propertiesRegister = PropertiesRegister.getCustomUserPropertiesInstance();

        LinkModel linkModel = new LinkModel();
        linkModel.setMessageType(MessageTypes.LINK);
        linkModel.setTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        linkModel.setSender(propertiesRegister.getUsername());
        linkModel.setLink(linkTransferDTO.link());
        linkModel.setComment(linkTransferDTO.comment());
        // TODO: 02.11.23 add quote support

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String linkString = objectMapper.writeValueAsString(linkModel);

            ClientRegister clientRegister = ClientRegister.getWebSocketClientInstance();
//            clientRegister.send(linkString);
            // TODO 1
//            mainFrame.getWebsocketClient().send(linkString);

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(ex);
        }
    }
}