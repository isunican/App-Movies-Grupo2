package es.unican.movies.service;

import java.util.List;

/**
 * Interface for callbacks.
 * @param <T> the type of the object that will be returned by the callback
 */
public interface ICallback<T> {

    /**
     * Called when the request is successful.
     * @param body the object that was returned by the request
     */
    public void onSuccess(T body);

    /**
     * Called when the request fails.
     * @param e the exception that was thrown
     */
    public void onFailure(Throwable e);

}
