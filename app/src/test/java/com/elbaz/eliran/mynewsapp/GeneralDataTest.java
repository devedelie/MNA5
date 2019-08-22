package com.elbaz.eliran.mynewsapp;

import com.elbaz.eliran.mynewsapp.models.Constants;
import com.elbaz.eliran.mynewsapp.views.NYTViewHolder;

import org.junit.Assert;
import org.junit.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eliran Elbaz on 21-Aug-19.
 */
public class GeneralDataTest {
    @Test
    public void MainActivity_verifyAPIKey_returnCorrectAPIKey(){
        Assert.assertEquals("eUdpsuImhyQRapDx4vkN0NMOJZEYSqYA", Constants.API_KEY);
    }

    @Test
    public void MainActivity_verifyBaseURL_returnCorrectBaseURL(){
        Assert.assertEquals("https://api.nytimes.com/", Constants.BASE_URL);
    }

    @Test
    public void MainActivity_fullDateFormatReceived_returnNewReadableFormat() {
        String originDate = "2019-08-08T10:32:10-10:00"; // Full date format
        assertEquals( NYTViewHolder.convertDate(originDate),"Aug 08, 2019");
    }
}