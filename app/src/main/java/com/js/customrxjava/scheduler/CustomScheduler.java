package com.js.customrxjava.scheduler;

import java.util.concurrent.Executor;

/**
 * Created by chengjunsen on 2019-07-21
 **/
public class CustomScheduler {
    private final Executor executor;

    public CustomScheduler(Executor executor) {
        this.executor = executor;
    }

    public CustomWorker createWorker() {
        return new CustomWorker(executor);
    }

    public static class CustomWorker{
        private final Executor executor;
        public CustomWorker(Executor executor) {
            this.executor = executor;
        }

        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }
    }
}
