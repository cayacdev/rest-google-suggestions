package de.jazhead.placessuggestions.service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import de.jazhead.placessuggestions.service.GooglePlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultGooglePlaceService implements GooglePlaceService {

    @Autowired
    private GeoApiContext geoApiContext;


    @Override
    public PlaceDetails getPlaceDetails(final String placeId) {
        try {
            return PlacesApi.placeDetails(geoApiContext, placeId).await();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PlacesSearchResponse search(final PlaceType placeType, final int radius, final double lat, final double lng) {

        final NearbySearchRequest searchQuery = PlacesApi.nearbySearchQuery(geoApiContext, new LatLng(lat, lng));
        searchQuery.type(placeType);
        searchQuery.radius(radius);

        try {
            return searchQuery.await();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
