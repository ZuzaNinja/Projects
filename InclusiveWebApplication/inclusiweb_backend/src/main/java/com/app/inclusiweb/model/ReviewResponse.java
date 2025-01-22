package com.app.inclusiweb.model;

public class ReviewResponse {
    private String rating;
    private String basicRecommendations;
    private String layoutRecommendations;
    private String visibilityRecommendations;
    private String languageRecommendations;
    private String multimediaRecommendations;
    private String disabilitiesAffected;

    public ReviewResponse(String rating, String basicRecommendations, String layoutRecommendations, String visibilityRecommendations, String languageRecommendations, String disabilitiesAffected) {
        this.rating = rating;
        this.basicRecommendations = basicRecommendations;
        this.layoutRecommendations = layoutRecommendations;
        this.visibilityRecommendations = visibilityRecommendations;
        this.languageRecommendations = languageRecommendations;
//        this.multimediaRecommendations = multimediaRecommendations;
        this.disabilitiesAffected = disabilitiesAffected;
    }

    public ReviewResponse(String rating, String basicRecommendations) {
        this.rating = rating;
        this.basicRecommendations = basicRecommendations;
    }

    public String getRating() {
        return rating;
    }

    public String getBasicRecommendations() {
        return basicRecommendations;
    }
    public ReviewResponse(String rating){
        this.rating = rating;
    }
}
