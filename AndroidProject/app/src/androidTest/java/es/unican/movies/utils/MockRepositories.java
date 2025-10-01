package es.unican.movies.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.mockito.ArgumentMatchers;

import java.util.List;

import es.unican.movies.common.Utils;
import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;
import es.unican.movies.service.ICallback;

/**
 * Mock repositories for tests.
 */
public class MockRepositories {

    /**
     * Create a mock repository that uses the data in the given json resource
     *
     * @param context application context
     * @param jsonId  json raw file id
     * @return mock repository
     */
    public static IMoviesRepository getTestRepository(Context context, int jsonId) {
        IMoviesRepository mock = mock(IMoviesRepository.class);
        doAnswer(invocation -> {
            ICallback<List<Movie>> callback = invocation.getArgument(0);
            callback.onSuccess(Utils.parseMovies(context, jsonId));
            return null;
        }).when(mock).requestAggregateMovies(any(ICallback.class));
        return mock;
    }

}
