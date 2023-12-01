package com.pinc.android.MB360.database.workmanagers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackGroundSaveWorker extends Worker {

    public BackGroundSaveWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        //workers work

        return Result.success();
    }

}
