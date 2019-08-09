package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import androidx.fragment.app.Fragment;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Eliran Elbaz on 13-Jul-19.
 */
public class FragmentsTest {

    /**
     * Test to verify that the fragments are not returning null
     * @throws Exception
     */

    @Test
    public void Fragment1ShouldNotBeNull() throws Exception {
        Fragment Fragment1 = TabFragment1.newInstance();
        assertNotNull(Fragment1);
    }
    @Test
    public void Fragment2ShouldNotBeNull() throws Exception {
        Fragment Fragment2 = TabFragment2.newInstance();
        assertNotNull(Fragment2);
    }
    @Test
    public void Fragment3ShouldNotBeNull() throws Exception {
        Fragment Fragment3 = TabFragment3.newInstance();
        assertNotNull(Fragment3);
    }
    @Test
    public void Fragment4ShouldNotBeNull() throws Exception {
        Fragment Fragment4 = TabFragment4.newInstance();
        assertNotNull(Fragment4);
    }

}