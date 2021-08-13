package de.marvinstier.bob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.marvinstier.bob.requests.GetRequest;
import de.marvinstier.bob.requests.GiphyRequest;

public class TestGetRequest {
    @Test
    public void testCoffee() throws IOException, InterruptedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "1");

        HttpResponse<String> response = GetRequest.sendGet("https://api.sampleapis.com/coffee/hot", parameters);

        assertEquals("[{" + "\"title\":\"Black\","
                + "\"description\":\"Black coffee is as simple as it gets with ground coffee beans steeped in hot water, "
                + "served warm. And if you want to sound fancy, you can call black coffee by its proper name: cafe noir.\","
                + "\"ingredients\":[\"Coffee\"]," + "\"id\":1}]", response.body());
    }

    @Test
    public void testApexImageURINotNull() throws IOException, URISyntaxException, InterruptedException {
        assertNotNull(GiphyRequest.getApexImage(0));
    }
}
