package com.elbaz.eliran.mynewsapp.Utils;

import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.NYTMostPopular;
import com.elbaz.eliran.mynewsapp.Models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.NYTNews;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

/**
 * Created by Eliran Elbaz on 17-Aug-19.
 */
@RunWith(JUnit4.class)
public class NYTStreamsTest {

    @Test
    public void isStream_fetchTopStoriesStream_correctSizeReturned() {
        Observable<NYTNews> observableTopStories = NYTStreams.streamFetchTopStories("home");

        TestObserver<NYTNews> topStoriesTestObserver = new TestObserver<>();

        observableTopStories.subscribeWith(topStoriesTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTNews mostPopular = topStoriesTestObserver.values().get(0);
        Assert.assertTrue(mostPopular.getResults().size() > 0);
    }

    @Test
    public void isStream_fetchMostPopularStream_correctSizeReturned() {
        Observable<NYTMostPopular> observableMostPopular = NYTStreams.streamFetchMostPopular("facebook");

        TestObserver<NYTMostPopular> mostPopularTestObserver = new TestObserver<>();

        observableMostPopular.subscribeWith(mostPopularTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTMostPopular mostPopular = mostPopularTestObserver.values().get(0);
        Assert.assertTrue(mostPopular.getResults().size() > 0);
    }

    @Test
    public void isStream_fetchSearchResultsStream_correctSizeReturned() {
        Observable<NYTSearch> observableSearchResults = NYTStreams.streamFetchSearchResults(null,null,"politics","","newest");

        TestObserver<NYTSearch> searchResultTestObserver = new TestObserver<>();

        observableSearchResults.subscribeWith(searchResultTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTSearch searchResults = searchResultTestObserver.values().get(0);
        Assert.assertTrue(searchResults.getResponse().getDocs().size() > 0);
    }
}