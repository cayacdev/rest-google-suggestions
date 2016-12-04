package de.jazhead.placessuggestions.facade;

import de.jazhead.placessuggestions.model.Suggestion;

import java.util.List;

public interface SuggestionFacade {

    List<Suggestion> getSuggestions(String s, String radius, String lat, String mood, String limit);

    List<Suggestion> getMockData(String mood);
}
