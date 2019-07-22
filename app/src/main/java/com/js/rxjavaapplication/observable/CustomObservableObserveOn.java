package com.js.rxjavaapplication.observable;

import com.js.rxjavaapplication.CustomObservableSource;
import com.js.rxjavaapplication.CustomObserver;
import com.js.rxjavaapplication.scheduler.CustomScheduler;

/**
 * Created by chengjunsen on 2019-07-22
 **/
class CustomObservableObserveOn<T> extends CustomObservable<T> {
    private CustomObservableSource<T> source;
    private CustomScheduler scheduler;

    public CustomObservableObserveOn(CustomObservableSource source, CustomScheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(CustomObserver observer) {
        CustomScheduler.CustomWorker worker = scheduler.createWorker();
        CustomObserverObserveOn observerObserveOn = new CustomObserverObserveOn<T>(observer, worker);
        source.subscribe(observerObserveOn);
    }

    private static class CustomObserverObserveOn<T> implements CustomObserver<T> {
        private CustomObserver<T> observer;
        private CustomScheduler.CustomWorker worker;

        public CustomObserverObserveOn(CustomObserver<T> observer, CustomScheduler.CustomWorker worker) {
            this.observer = observer;
            this.worker = worker;
        }

        @Override
        public void onStart() {
            this.worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onStart();
                }
            });
        }

        @Override
        public void onNext(final T t) {
            this.worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onNext(t);
                }
            });
        }

        @Override
        public void onError(final Throwable e) {
            this.worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onError(e);
                }
            });
        }

        @Override
        public void onComplete() {
            this.worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onComplete();
                }
            });
        }
    }
}
