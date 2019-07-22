package com.js.rxjavaapplication.observable;

import com.js.rxjavaapplication.CustomObservableSource;
import com.js.rxjavaapplication.CustomObserver;
import com.js.rxjavaapplication.scheduler.CustomScheduler;

import java.util.Random;

/**
 * Created by chengjunsen on 2019-07-22
 **/
class CustomObservableSubscribeOn<T> extends CustomObservable<T> {
    private CustomObservableSource<T> source;
    private CustomScheduler scheduler;

    public CustomObservableSubscribeOn(CustomObservableSource<T> source, CustomScheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(final CustomObserver observer) {
        final CustomSubscribeOnObserver subscribeOnObserver = new CustomSubscribeOnObserver(observer);
        CustomScheduler.CustomWorker worker = scheduler.createWorker();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                source.subscribe(subscribeOnObserver);
            }
        });
    }

    private static final class CustomSubscribeOnObserver<T>  implements CustomObserver<T> {
        final CustomObserver<? super T> actual;

        CustomSubscribeOnObserver(CustomObserver<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void onStart() {
            actual.onStart();
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable error) {
            actual.onError(error);

        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }

    }
}
