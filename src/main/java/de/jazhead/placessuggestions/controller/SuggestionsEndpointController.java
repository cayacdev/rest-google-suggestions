package de.jazhead.placessuggestions.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import de.jazhead.placessuggestions.model.Suggestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
public class SuggestionsEndpointController {

    @Value("${google.api.key}")
    private String googleApiKey;

    @RequestMapping("/test")
    public List<Suggestion> getSuggestions1(@RequestParam(value = "mood", defaultValue = "joy") final String mood) {

        final Random random = new Random();
        PlaceType placeType = PlaceType.CAFE;

        final int index;
        final List<PlaceType> placeTypes;

        switch (mood) {
            case "joy":
                System.out.println("joy");
                placeTypes = Arrays.asList(PlaceType.RESTAURANT, PlaceType.CASINO);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            case "fear":
                System.out.println("fear");
                placeTypes = Arrays.asList(PlaceType.AMUSEMENT_PARK, PlaceType.ZOO);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            case "disgust":
                System.out.println("disgust");
                placeTypes = Arrays.asList(PlaceType.NIGHT_CLUB, PlaceType.SPA);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            case "sadness":
                System.out.println("sadness");
                placeTypes = Arrays.asList(PlaceType.PARK, PlaceType.SHOE_STORE);
                index = random.nextInt(placeTypes.size());
                placeType = placeTypes.get(index);
                break;
            default:
                System.out.println("default");
        }

        final GeoApiContext context = new GeoApiContext().setApiKey(googleApiKey);


        PlacesSearchResponse result;
        List<Suggestion> suggestions;
        try {
            result = search(placeType, 5000, context);

            if (result.results.length > 0) {

                result = search(placeType, 50000, context);
            }

            if (result.results.length > 0) {

                suggestions = mapResponse(context, result);
            }
            else {
                suggestions = getDefaultList();
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            suggestions = getDefaultList();
        }

        return suggestions;
    }

    private PlacesSearchResponse search(final PlaceType placeType, final int radius, final GeoApiContext context) throws Exception {
        final NearbySearchRequest searchQuery = PlacesApi.nearbySearchQuery(context, new LatLng(48.1550547, 11.4017465));
        searchQuery.type(placeType);
        searchQuery.radius(radius);

        return searchQuery.await();
    }

    private List<Suggestion> mapResponse(final GeoApiContext context, final PlacesSearchResponse result) throws Exception {
        int counter = 0;
        final List<Suggestion> suggestions = new ArrayList<>();
        for (final PlacesSearchResult placesSearchResult : result.results) {

            if (placesSearchResult.openingHours != null && placesSearchResult.openingHours.openNow) {
                System.out.println(String.format("get suggestion %s", counter));
                final Suggestion suggestion = new Suggestion();
                suggestion.setName(placesSearchResult.name);
                suggestion.setTypes(Arrays.asList(placesSearchResult.types));
                suggestion.setRating(placesSearchResult.rating);

                final PlaceDetails await = PlacesApi.placeDetails(context, placesSearchResult.placeId).await();
                suggestion.setAddress(await.formattedAddress);
                suggestion.setWebsite(await.website.toString());
                suggestion.setUrl(await.url.toString());


                System.out.println(String.format("Add suggestion %s", counter));
                suggestions.add(suggestion);
                counter++;
            }

            if (counter >= 3) {
                break;
            }
        }
        return suggestions;
    }

    private List<Suggestion> getDefaultList() {
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
