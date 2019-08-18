package com.elbaz.eliran.mynewsapp.controllers.fragments;

import androidx.fragment.app.Fragment;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Eliran Elbaz on 13-Jul-19.
 */
public class FragmentsTest {

    //Test to verify that the fragments are not returning null
    @Test
    public void Fragment1ShouldNotBeNull() throws Exception {
        Fragment Fragment1 = new TabFragment1();
        assertNotNull(Fragment1);
    }
    @Test
    public void Fragment2ShouldNotBeNull() throws Exception {
        Fragment Fragment2 = new TabFragment2();
        assertNotNull(Fragment2);
    }
    @Test
    public void Fragment3ShouldNotBeNull() throws Exception {
        Fragment Fragment3 = new TabFragment3();
        assertNotNull(Fragment3);
    }
    @Test
    public void Fragment4ShouldNotBeNull() throws Exception {
        Fragment Fragment4 = new TabFragment4();
        assertNotNull(Fragment4);
    }

}