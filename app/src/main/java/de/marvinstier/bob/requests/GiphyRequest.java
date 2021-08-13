package de.marvinstier.bob.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class GiphyRequest extends GetRequest {
    public static class GiphyImage {
        private String url;
        private String title;
        private String media;

        public GiphyImage(String url, String title, String media) {
            this.url = url;
            this.title = title;
            this.media = media;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getMedia() {
            return media;
        }
    }

    private static String getToken() throws IOException, URISyntaxException {
        byte[] data;
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("giphy_token.txt")) {
            data = in.readAllBytes();
        }
        return new String(data);
    }

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
