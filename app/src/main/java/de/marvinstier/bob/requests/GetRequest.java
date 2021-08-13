package de.marvinstier.bob.requests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This is a utility class for sending get requests.
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class GetRequest {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(Version.HTTP_2).build();

    /**
     * Sends a GET request at given endpoint with given parameters, if any.
     *
     * @param endpoint URL pointing to the the API's endpoint
     * @param parameters Map containing key-value pairs of request parameters
     * @return the response of the GET request
     * @throws IOException when sending the request fails
     * @throws InterruptedException when the connection is interrupted
     */
    public static HttpResponse<String> sendGet(String endpoint, Map<String, String> parameters)
            throws IOException, InterruptedException {
        StringBuilder uriString = new StringBuilder();

        uriString.append(endpoint);

        if (parameters != null) {
            boolean firstElement = true;
            for (Entry<String, String> e : parameters.entrySet()) {
                if (firstElement) {
                    uriString.append("?");
                    firstElement = false;
                } else {
                    uriString.append("&");
                }

                uriString.append(e.getKey()).append("=").append(e.getValue());
            }
        }

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uriString.toString()))
                .setHeader("User-Agent", "Java 11 HttpClient Bot").build();

        return HTTP_CLIENT.send(request, BodyHandlers.ofString());
    }
}
