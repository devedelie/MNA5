package com.elbaz.eliran.mynewsapp.models;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Eliran Elbaz on 21-Aug-19.
 */
public class ConstantsTest {
    @Test
    public void MainActivity_verifyAPIKey_returnCorrectAPIKey(){
        Assert.assertEquals("eUdpsuImhyQRapDx4vkN0NMOJZEYSqYA", Constants.API_KEY);
    }

    @Test
    public void MainActivity_verifyBaseURL_returnCorrectBaseURL(){
        Assert.assertEquals("https://api.nytimes.com/", Constants.BASE_URL);
    }

}