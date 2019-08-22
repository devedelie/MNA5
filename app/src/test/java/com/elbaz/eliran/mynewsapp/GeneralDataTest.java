package com.elbaz.eliran.mynewsapp;

import com.elbaz.eliran.mynewsapp.models.Constants;
import com.elbaz.eliran.mynewsapp.views.NYTViewHolder;

import org.junit.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eliran Elbaz on 21-Aug-19.
 */
public class GeneralDataTest {
    @Test
    public void MainActivity_verifyAPIKey_returnCorrectAPIKey(){
        assertEquals(Constants.API_KEY, "eUdpsuImhyQRapDx4vkN0NMOJZEYSqYA");
    }

    @Test
    public void MainActivity_verifyBaseURL_returnCorrectBaseURL(){
        assertEquals(Constants.BASE_URL, "https://api.nytimes.com/");
    }

    @Test
    public void MainActivity_fullDateFormatReceived_returnNewReadableFormat() {
        String originDate = "2019-08-08T10:32:10-10:00"; // Full date format
        assertEquals( NYTViewHolder.convertDate(originDate),"Aug 08, 2019");
    }
}