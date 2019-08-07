package com.elbaz.eliran.mynewsapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Eliran Elbaz on 06-Aug-19.
 */
public class NotificationWorker extends Worker {

    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Do the work here

        //The Result returned from doWork() informs WorkManager whether the task:
//        finished successfully via Result.success()
//        failed via Result.failure()
//        needs to be retried at a later time via Result.retry()

        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }

}
