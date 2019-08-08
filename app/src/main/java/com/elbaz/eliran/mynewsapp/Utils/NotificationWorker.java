package com.elbaz.eliran.mynewsapp.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.elbaz.eliran.mynewsapp.Models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Eliran Elbaz on 06-Aug-19.
 */
public class NotificationWorker extends Worker {
    private Disposable mDisposable;
    SharedPreferences mSharedPreferences;
    String searchStartDate, searchEndDate, query;

    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Do work
        // Get Today's date
        String todaysDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        // Get start date from sharedPreferences
        mSharedPreferences = getApplicationContext().getSharedPreferences("save_switch_state", MODE_PRIVATE);
        searchStartDate = mSharedPreferences.getString("switch_start_date", todaysDate);

        executeHttpRequestWithRetrofit(searchStartDate, todaysDate, "news_desk:(\"politics\")", query, "newest" );

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

    // A method to display the news result with a daily notification
    private void displayNotification(String task, String description){

        // 1 - Create a notificationManager in order to create notification channel
        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // 2 - If the version is >= from version O, then we create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("dailyNotification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "dailyNotification")
                .setContentTitle(task)
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());
    }

    //-----------------
    // HTTP (RxJAVA)
    //-----------------
    // 1 - Execute the stream
    private void executeHttpRequestWithRetrofit(String userBeginDate, String userEndDate, String filterString, String userQueryString, String sort){
        // 1.2 - Execute the stream subscribing to Observable defined inside NYTStream
        this.mDisposable = NYTStreams.streamFetchSearchResults(userBeginDate, userEndDate, filterString,userQueryString, sort)
                .subscribeWith(new DisposableObserver<NYTSearch>(){

                    @Override
                    public void onNext(NYTSearch nytSearch) {
                        // 1.3 - Update UI with list of titles
                        Log.e(TAG, "onNext" );
//                        updateNotificationWithData(nytSearch.getResponse().getDocs());
                        // Check if there are new results between the new dates
                        int sizeOfList = nytSearch.getResponse().getDocs().size();
                        if (sizeOfList == 0){
                            // If no matches - *******
                            displayNotification("New daily articles", "We have found " + sizeOfList + " new articles matching your search criteria");

                        }else {
                            // ********
                            displayNotification("New daily articles", "We have found " + sizeOfList + " new articles matching your search criteria");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+ e );
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }

}
