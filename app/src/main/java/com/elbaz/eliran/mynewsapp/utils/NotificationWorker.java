package com.elbaz.eliran.mynewsapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.elbaz.eliran.mynewsapp.models.SearchModels.NYTSearch;
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
    String searchStartDate, filters , query;
    Context mContext;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Do work
        // Get Today's date (to set as EndDate limit for search + default value for startDate)
        String todaysDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        // Get search data from sharedPreferences
        mSharedPreferences = getApplicationContext().getSharedPreferences("save_switch_state", MODE_PRIVATE);
        searchStartDate = mSharedPreferences.getString("search_start_date", todaysDate);
        filters = mSharedPreferences.getString("checkboxes_filter_string", "");
        query = mSharedPreferences.getString("query_string", "");

        // Execute Http request with the current data
        executeHttpRequestWithRetrofit(searchStartDate, todaysDate, filters, query, "newest");
        Log.d(TAG, "doWork received data: " + searchStartDate + todaysDate+ " "+ filters + " "+ query );

        // Next periodic task preparation: set the next day "startDate" as today's date, and save in sharedPreferences
        SharedPreferences.Editor editor = mContext.getSharedPreferences("save_switch_state", MODE_PRIVATE).edit();
        editor.putString("search_start_date", todaysDate);
        editor.commit();

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

    //-----------------
    // HTTP (RxJAVA)
    //-----------------
    // 1 - Execute the stream
    private void executeHttpRequestWithRetrofit(String userBeginDate, String userEndDate, String filterString, String userQueryString, String sort){
        Log.d(TAG, "Http received data: " + userBeginDate +" "+ userEndDate+ " "+ filterString + " "+ userQueryString + " "+ sort);
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
                            // If there are no matches
                            displayNotification("No articles matching your criteria today!", "You can try expanding your search criteria to improve results");
                        }else {
                            displayNotification("We have found new daily articles",  sizeOfList + " new articles matching your search criteria");
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

}
