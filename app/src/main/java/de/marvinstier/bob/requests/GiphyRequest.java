package de.marvinstier.bob.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This utility class provides means of requesting images from giphy.com
 *
 * @author Marvin Stier
 * @version 1.0
 */
public class GiphyRequest extends GetRequest {
    /**
     * This class stores information about an image fetched from giphy.com
     *
     * @author Marvin Stier
     * @version 1.0
     */
    public static class GiphyImage {
        private String url;
        private String title;
        private String media;

        /**
         * This constructor takes a URL, an image title and a URL to the media and
         * stores it.
         *
         * @param url
         * @param title
         * @param media
         */
        public GiphyImage(String url, String title, String media) {
            this.url = url;
            this.title = title;
            this.media = media;
        }

        /**
         * @return the URL to the giphy page
         */
        public String getUrl() {
            return url;
        }

        /**
         * @return the images's title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the media URL
         */
        public String getMedia() {
            return media;
        }
    }

    /**
     * Loads the Giphy API token from a file
     *
     * @return Giphy API token
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String getToken() throws IOException, URISyntaxException {
        byte[] data;
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("giphy_token.txt")) {
            data = in.readAllBytes();
        }
        return new String(data);
    }

    /**
     * This method provides means to fetch a random Apex Legends image from
     * giphy.com
     *
     * @param offset index of the image to be fetched
     * @return instance containing information about the image
     * @throws IOException when the request fails
     * @throws URISyntaxException when the URI is illegally formatted
     * @throws InterruptedException when connection is interrupted
     */
    public static GiphyImage getApexImage(int offset) throws IOException, URISyntaxException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("api_key", getToken());
        parameters.put("q", "apex-legends");
        parameters.put("limit", "1");
        parameters.put("offset", String.valueOf(offset));
        parameters.put("rating", "g");
        parameters.put("lang", "en");

        HttpResponse<String> response = GetRequest.sendGet("https://api.giphy.com/v1/gifs/search", parameters);

        GiphyImage giphyImage = null;

        try {
            JSONObject image = new JSONObject(response.body()).getJSONArray("data").getJSONObject(0);
            JSONObject original = image.getJSONObject("images").getJSONObject("original");
            giphyImage = new GiphyImage(image.getString("url"), image.getString("title"), original.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return giphyImage;
    }
}
