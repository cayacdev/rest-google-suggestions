package de.jazhead.placessuggestions.service;

import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import de.jazhead.placessuggestions.model.Suggestion;

import java.util.List;

public interface SuggestionService {

    List<Suggestion> mapResponse(final PlacesSearchResponse result, Integer limitInteger);

    PlaceType getPlaceType(String mood);
}
