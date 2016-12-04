package de.jazhead.placessuggestions.service;

import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;

public interface GooglePlaceService {

    PlaceDetails getPlaceDetails(String placeId);

    PlacesSearchResponse search(PlaceType placeType, int radius, double lat, double lng);
}
