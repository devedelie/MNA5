package com.elbaz.eliran.mynewsapp.utils;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by Eliran Elbaz on 20-Aug-19.
 */
public class SnackbarMessagesAndVibrations {

    public static void showSnakbarMessage(View rootView, String mMessage) {
        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    // Vibration method
    public static void Vibration (Context context){
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);
    }
}
