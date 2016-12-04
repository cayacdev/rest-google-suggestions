package de.jazhead.placessuggestions.service.impl;

import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import de.jazhead.placessuggestions.model.Suggestion;
import de.jazhead.placessuggestions.service.GooglePlaceService;
import de.jazhead.placessuggestions.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DefaultSuggestionService implements SuggestionService {

    @Autowired
    private GooglePlaceService googlePlaceService;

    @Override
    public List<Suggestion> mapResponse(final PlacesSearchResponse result, final Integer limitInteger) {

        int counter = 0;
        final List<Suggestion> suggestions = new ArrayList<>();
        for (final PlacesSearchResult placesSearchResult : result.results) {

            if (placesSearchResult.openingHours != null && placesSearchResult.openingHours.openNow) {

                final Suggestion suggestion = new Suggestion();
                suggestion.setName(placesSearchResult.name);
                suggestion.setTypes(Arrays.asList(placesSearchResult.types));
                suggestion.setRating(placesSearchResult.rating);

                final PlaceDetails placeDetails = googlePlaceService.getPlaceDetails(placesSearchResult.placeId);

                suggestion.setAddress(placeDetails.formattedAddress);
                suggestion.setWebsite(placeDetails.website.toString());
                suggestion.setUrl(placeDetails.url.toString());


                suggestions.add(suggestion);
                counter++;
            }

            if (counter >= limitInteger) {
                break;
            }
        }
        return suggestions;
    }

    @Override
    public PlaceType getPlaceType(final String mood) {
        final Random random = new Random();
        PlaceType placeType = PlaceType.CAFE;

        final int index;
        final List<PlaceType> placeTypes;

        switch (mood) {
            case "joy":
                placeTypes = Arrays.asList(PlaceType.RESTAURANT, PlaceType.CASINO);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            case "fear":
                placeTypes = Arrays.asList(PlaceType.AMUSEMENT_PARK, PlaceType.ZOO);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            case "disgust":
                placeTypes = Arrays.asList(PlaceType.NIGHT_CLUB, PlaceType.SPA);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            case "sadness":
                placeTypes = Arrays.asList(PlaceType.PARK, PlaceType.SHOE_STORE);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            default:
        }
        return placeType;
    }
}
