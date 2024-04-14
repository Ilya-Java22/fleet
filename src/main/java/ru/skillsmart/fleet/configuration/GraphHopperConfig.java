package ru.skillsmart.fleet.configuration;

import com.graphhopper.api.GraphHopperGeocoding;
import com.graphhopper.api.GraphHopperWeb;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class GraphHopperConfig {

    @Value("${graphhopper.api.key}")
    private String graphHopperApiKey;

    @Bean
    public GraphHopperGeocoding geocoding() {
        GraphHopperGeocoding geocoding = new GraphHopperGeocoding();
        geocoding.setKey(graphHopperApiKey);
        // change timeout, default is 5 seconds
        geocoding.setDownloader(new OkHttpClient.Builder().connectTimeout(5,
                TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build());
        return geocoding;
    }

    @Bean
    public GraphHopperWeb graphHopper() {
        GraphHopperWeb graphHopper = new GraphHopperWeb();
        graphHopper.setKey(graphHopperApiKey);
        // change timeout, default is 5 seconds
        graphHopper.setDownloader(new OkHttpClient.Builder().connectTimeout(5,
                TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build());
        return graphHopper;
    }
}