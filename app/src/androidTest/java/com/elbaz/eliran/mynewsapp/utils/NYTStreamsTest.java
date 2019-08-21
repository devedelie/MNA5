package com.elbaz.eliran.mynewsapp.utils;

import android.util.Log;

import com.elbaz.eliran.mynewsapp.models.MostPopularModels.NYTMostPopular;
import com.elbaz.eliran.mynewsapp.models.MostPopularModels.ResultMostPopular;
import com.elbaz.eliran.mynewsapp.models.SearchModels.Doc;
import com.elbaz.eliran.mynewsapp.models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.NYTNews;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.Result;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static android.content.ContentValues.TAG;

/**
 * Created by Eliran Elbaz on 17-Aug-19.
 */
@RunWith(JUnit4.class)
public class NYTStreamsTest {

    @Test
    public void isStream_fetchTopStoriesStream_returnedSizeBiggerThanZero() {
        Observable<NYTNews> observableTopStories = NYTStreams.streamFetchTopStories("home");

        TestObserver<NYTNews> topStoriesTestObserver = new TestObserver<>();

        observableTopStories.subscribeWith(topStoriesTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTNews topStories = topStoriesTestObserver.values().get(0);
        Assert.assertTrue(topStories.getResults().size() > 0);
        for(Result result: topStories.getResults()){
            Assert.assertTrue(result.getPublishedDate() != null && !result.getPublishedDate().isEmpty());
            Assert.assertTrue(result.getTitle() != null && !result.getTitle().isEmpty());
            Assert.assertTrue(result.getSection() != null && !result.getSection().isEmpty());
        }
    }

    @Test
    public void isStream_fetchMostPopularLast7Days_ReturnSizeBiggerThanZero() {
        Observable<NYTMostPopular> observableMostPopular = NYTStreams.streamFetchMostPopular("30"); // Only the following values are allowed: 1, 7, 30

        TestObserver<NYTMostPopular> mostPopularTestObserver = new TestObserver<>();

        observableMostPopular.subscribeWith(mostPopularTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTMostPopular mostPopular = mostPopularTestObserver.values().get(0);
        Assert.assertTrue(mostPopular.getResults().size() > 0);
        for (ResultMostPopular rms: mostPopular.getResults()){
            Assert.assertTrue(rms.getTitle() != null && !rms.getTitle().isEmpty());
            Assert.assertTrue(rms.getSection() != null && !rms.getSection().isEmpty());
            Assert.assertTrue(rms.getPublishedDate() != null && !rms.getPublishedDate().isEmpty());
        }
    }

    @Test
    public void isStream_fetchMostPopularLast7Days_ReturnResultsWithinTheLastWeek() throws Exception{
        Observable<NYTMostPopular> observableMostPopular = NYTStreams.streamFetchMostPopular("7"); // Only the following values are allowed: 1, 7, 30

        TestObserver<NYTMostPopular> mostPopularTestObserver = new TestObserver<>();

        observableMostPopular.subscribeWith(mostPopularTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTMostPopular mostPopular = mostPopularTestObserver.values().get(0);
        // Assert that all the titles are within the correct period (0-10 days-old max)
        String dateBefore7Days = subtractDays(10); // the period value '7' returns news within the last 10 days approximately
        for (ResultMostPopular result: mostPopular.getResults()){
            String date = convertDate(result.getPublishedDate());
            Log.d(TAG, "isStream_ReturnResultsWithinTheLastWeek: is bigger? " +dateBefore7Days + " < "+ date);
            // check if the date is
            Assert.assertTrue(Integer.parseInt(date) >= Integer.parseInt(dateBefore7Days));
        }
    }

    @Test
    public void isStream_fetchSearchResultsForCategory_ReturnCorrectCategoryWithCorrectSize() {
        Observable<NYTSearch> observableSearchResults = NYTStreams.streamFetchSearchResults(null,null,"news_desk:(\"Politics\")","","newest");

        TestObserver<NYTSearch> searchResultTestObserver = new TestObserver<>();

        observableSearchResults.subscribeWith(searchResultTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTSearch searchResults = searchResultTestObserver.values().get(0);
        // Assert: if bigger than 0 and if returns 10 results (only for un-precised search)
        Assert.assertTrue(searchResults.getResponse().getDocs().size() > 0);
        Assert.assertEquals(10,searchResults.getResponse().getDocs().size());

        for (Doc doc: searchResults.getResponse().getDocs()) {
            Assert.assertEquals( "Politics", doc.getNewsDesk());
        }
    }

    @Test
    public void isStream_fetchSearchResultsForFalseDate_ReturnNoResults() {
        Observable<NYTSearch> observableSearchResults = NYTStreams.streamFetchSearchResults("20801220",null,"news_desk:(\"Politics\")","","newest");

        TestObserver<NYTSearch> searchResultTestObserver = new TestObserver<>();

        observableSearchResults.subscribeWith(searchResultTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTSearch searchResults = searchResultTestObserver.values().get(0);

        Assert.assertEquals( true, searchResults.getResponse().getDocs().size() == 0);
    }

    @Test
    public void isStream_fetchSearchResultsForBeginDate_returnBeginDateEqualsToOldestTitle() {
        Observable<NYTSearch> observableSearchResults = NYTStreams.streamFetchSearchResults("20190801", null, "news_desk:(\"Politics\")", "", "oldest");

        TestObserver<NYTSearch> searchResultTestObserver = new TestObserver<>();

        observableSearchResults.subscribeWith(searchResultTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NYTSearch searchResults = searchResultTestObserver.values().get(0);
        Doc doc = searchResults.getResponse().getDocs().get(0); // Get first title in position [0] (sort = oldest)
        String date = convertDate(doc.getPubDate());
        Assert.assertEquals("20190801", date);
    }

    //----------------------------------------------------------------------------------------------
    //Helper methods
    //----------------------------------------------------------------------------------------------
    // Date format converter
    private String convertDate(String inputDate) {
        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid
            Log.e(TAG, "convertDate: parse error while converting the Date" );
        } catch(Exception exception) {
            // Generic catch.
            Log.e(TAG, "convertDate: generic error while converting the Date" );
        }
        theDateFormat = new SimpleDateFormat("yyyyMMdd");

        return theDateFormat.format(date);
    }

    // Date modifier to subtract days
    private static String subtractDays(int days){
        // get today's date
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        cal.add(Calendar.DATE, -days);
        return getTodayDate(cal);
    }
    // get today's date
    public static String getTodayDate(Calendar cal) {
        return "" + cal.get(Calendar.YEAR) + "0"+
                (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DATE);
    }

}