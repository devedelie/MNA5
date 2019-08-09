package com.elbaz.eliran.mynewsapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Eliran Elbaz on 07-Aug-19.
 */
public class CheckInternetConnection {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d(TAG, "isNetworkAvailable: " + activeNetworkInfo);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
