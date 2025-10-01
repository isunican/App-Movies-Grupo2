package es.unican.movies.service;

/**
 * Enumeration of image sizes used by the TMDB API.
 */
public enum EImageSize {

    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    ;

    public final String value;

    EImageSize(String value) {
        this.value = value;
    }
}