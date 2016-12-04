package de.jazhead.placessuggestions.controller;

import de.jazhead.placessuggestions.facade.SuggestionFacade;
import de.jazhead.placessuggestions.model.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SuggestionsEndpointController {

    @Autowired
    private SuggestionFacade suggestionFacade;

    @RequestMapping("/getSuggestions")
    public List<Suggestion> getSuggestions(@RequestParam(value = "mood", defaultValue = "joy") final String mood,
                                           @RequestParam(value = "radius", defaultValue = "1000") final String radius,
                                           @RequestParam(value = "lat", defaultValue = "48.1550547") final String lat,
                                           @RequestParam(value = "lng", defaultValue = "11.4017465") final String lng,
                                           @RequestParam(value = "limit", defaultValue = "3") final String limit) {

        List<Suggestion> suggestions = suggestionFacade.getSuggestions(mood, radius, lat, lng, limit);

        if (CollectionUtils.isEmpty(suggestions)) {
            suggestions = suggestionFacade.getSuggestions(mood, "50000", lat, lng, limit);
        }

        if (CollectionUtils.isEmpty(suggestions)) {
            suggestions = suggestionFacade.getMockData(mood);
        }

        return suggestions;
    }
}
