package com.app.inclusiweb.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public class Review {
    /*
    * "1. The accessibility score of the page. The score is a number between 0 and 1 please thanks. The higher the score, the better the accessibility. "+
                "2. The basic recommendations. These are given based on how the page looks and feels "+
                "3. The layout recommendations. These are given based on the layout of the page. "+
                "4. The visibility recommendations. These are given based on the visibility of the page. "+
                "5. The language recommendations. These are given based on the language and text of the page. "+
                "6. The disabilities affected. These are given based on the disabilities that are affected by the page. "*/
    @JsonPropertyDescription("The rating is MANDATORY on a scale from 0 to 100. The higher the rating, the better the accessibility.")
    private String rating;
    @JsonPropertyDescription("The basic recommendations. These are MANDATORY and given based on how the page looks and feels")
    private String basicRecommendations;
    @JsonPropertyDescription("The layout recommendations. These are MANDATORY and given based on the layout of the page.")
    private String layoutRecommendations;
    @JsonPropertyDescription("The visibility recommendations. These are MANDATORY and given based on the visibility of the page.")
    private String visibilityRecommendations;
    @JsonPropertyDescription("The language recommendations. These are MANDATORY and given based on the language and text of the page.")
    private String languageRecommendations;
    @JsonPropertyDescription("The multimedia recommendations. These are MANDATORY and given based on the multimedia of the page.")
    private String multimediaRecommendations;
    @JsonPropertyDescription("The disabilities of people that cannot use the page. These are MANDATORY and given based on the disabilities that people who cannot use the page have.")
    private String disabilitiesAffected;

    public Review(String rating, String basicRecommendations, String layoutRecommendations, String visibilityRecommendations, String languageRecommendations, String multimediaRecommendations, String disabilitiesAffected) {
        this.rating = rating;
        this.basicRecommendations = basicRecommendations;
        this.layoutRecommendations = layoutRecommendations;
        this.visibilityRecommendations = visibilityRecommendations;
        this.languageRecommendations = languageRecommendations;
        this.multimediaRecommendations = multimediaRecommendations;
        this.disabilitiesAffected = disabilitiesAffected;
    }

    public Review() {
    }

    public Review(String rating, String basicRecommendations) {
        this.rating = rating;
        this.basicRecommendations = basicRecommendations;
    }

    public String getRating() {
        return rating;
    }

    public String getBasicRecommendations() {
        return basicRecommendations;
    }
    public Review(String rating){
        this.rating = rating;
    }

    public String getLayoutRecommendations() {
        return layoutRecommendations;
    }

    public String getVisibilityRecommendations() {
        return visibilityRecommendations;
    }

    public String getLanguageRecommendations() {
        return languageRecommendations;
    }

    public String getMultimediaRecommendations() {
        return multimediaRecommendations;
    }

    public String getDisabilitiesAffected() {
        return disabilitiesAffected;
    }
}
