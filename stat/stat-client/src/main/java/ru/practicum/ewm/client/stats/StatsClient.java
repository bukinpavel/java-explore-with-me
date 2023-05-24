package ru.practicum.ewm.client.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;


@Service
public class StatsClient {
    @Value("${stats-service.url}")
    private String statsServiceUrl;
    private final HttpClient statsClient = HttpClient.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void postHit(ViewsStatsRequest statsRequest) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%/hit", statsServiceUrl)))
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(statsRequest)))
                .build();
        HttpResponse<?> response = statsClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public void getStats(String start, String end, String[] uris, String unique) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URIBuilder getStatsUri = new URIBuilder(String.format("%stats", statsServiceUrl));
        getStatsUri.addParameter("start", start);
        getStatsUri.addParameter("end", end);
        getStatsUri.addParameter("uris", Arrays.toString(uris));
        getStatsUri.addParameter("unique", unique);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getStatsUri.build())
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
