package de.jazhead.placessuggestions.configuration;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Value("${google.api.key}")
    private String googleApiKey;

    @Bean(name = "geoApiContext")
    public GeoApiContext getGeoApiContext() {
        return new GeoApiContext().setApiKey(googleApiKey);
    }
}
