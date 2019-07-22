package com.js.customrxjava.scheduler;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by chengjunsen on 2019-07-21
 **/
public class MainThreadExecutor implements Executor {

    private Handler handler;

    public MainThreadExecutor() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void execute(Runnable command) {
        handler.post(command);
    }
}
