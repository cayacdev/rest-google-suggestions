package de.jazhead.placessuggestions.facade.impl;

import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import de.jazhead.placessuggestions.facade.SuggestionFacade;
import de.jazhead.placessuggestions.model.Suggestion;
import de.jazhead.placessuggestions.service.GooglePlaceService;
import de.jazhead.placessuggestions.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultSuggestionFacade implements SuggestionFacade {

    @Autowired
    private SuggestionService suggestionService;

    @Autowired
    private GooglePlaceService googlePlaceService;

    @Override
    public List<Suggestion> getSuggestions(final String mood, final String radius, final String lat, final String lng, final String limit) {

        final PlaceType placeType = suggestionService.getPlaceType(mood);
        final Integer radiusInteger = Integer.valueOf(radius);
        final Double latDouble = Double.valueOf(lat);
        final Double lngDouble = Double.valueOf(lng);
        final Integer limitInteger = Integer.valueOf(limit);

        final PlacesSearchResponse response = googlePlaceService.search(placeType, radiusInteger, latDouble, lngDouble);
        return suggestionService.mapResponse(response, limitInteger);
    }

    @Override
    public List<Suggestion> getMockData(final String mood) {
        final List<Suggestion> suggestions = new ArrayList<>();

        suggestions.add(createSuggestion("Landgasthof Deutsche Eiche", Arrays.asList("lodging", "restaurant", "food", "point_of_interest", "establishment"),
                3.59, "Ranertstraße 1, 81249 München, Germany", "http://www.deutsche-eiche-mendel.de/", "https://maps.google.com/?cid=11765102940797365029"));

        suggestions.add(createSuggestion("Hotel ibis Muenchen City West", Arrays.asList("lodging", "restaurant", "food", "point_of_interest", "establishment"),
                3.70, "Westendstraße 181, 80686 München, Germany", "http://www.accorhotels.com/lien_externe.svlt?goto=fiche_hotel&code_hotel=6903&merchantid=seo-maps-DE-6903&sourceid=aw-cen", "https://maps.google.com/?cid=14972984563578409450"));

        suggestions.add(createSuggestion("Sheraton Munich Westpark Hotel", Arrays.asList("lodging", "restaurant", "food", "point_of_interest", "establishment"),
                4.19, "Garmischer Str. 2, 80339 München, Germany", "http://sheratonwestpark.com/", "https://maps.google.com/?cid=5556748020999555647"));

        return suggestions;
    }

    private Suggestion createSuggestion(final String name, final List<String> types, final double rating, final String address, final String website, final String url) {

        final Suggestion suggestion = new Suggestion();
        suggestion.setName(name);
        suggestion.setTypes(types);
        suggestion.setRating(rating);
        suggestion.setAddress(address);
        suggestion.setWebsite(website);
        suggestion.setUrl(url);

        return suggestion;
    }
}
