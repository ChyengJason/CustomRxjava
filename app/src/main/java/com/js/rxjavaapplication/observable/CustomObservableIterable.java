package com.js.rxjavaapplication.observable;

import com.js.rxjavaapplication.CustomObservableSource;
import com.js.rxjavaapplication.CustomObserver;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public class CustomObservableIterable<T> extends CustomObservable {
    private Iterable<T> valueIter;

    public CustomObservableIterable(Iterable<T> valueIter) {
        this.valueIter = valueIter;
    }

    @Override
    protected void subscribeActual(CustomObserver observer) {
        CustomIterableObserver<T> iterableObserver = new CustomIterableObserver<>(valueIter, observer);
        CustomInterableSource source = new CustomInterableSource();
        source.subscribe(iterableObserver);
    }

    private class CustomInterableSource implements CustomObservableSource {
        @Override
        public void subscribe(CustomObserver observer) {
            observer.onStart();
            observer.onNext(null);
            observer.onComplete();
        }
    }

    private static class CustomIterableObserver<T> implements CustomObserver<T> {
        private Iterable<T> valueIter;
        private CustomObserver<T> observer;

        CustomIterableObserver(Iterable<T> valueIter, CustomObserver<T> observer) {
            this.valueIter = valueIter;
            this.observer = observer;
        }

        @Override
        public void onStart() {
            this.observer.onStart();
        }

        @Override
        public void onNext(T t) {
            for (T value : valueIter) {
                this.observer.onNext(value);
            }
        }

        @Override
        public void onError(Throwable e) {
            this.observer.onError(e);
        }

        @Override
        public void onComplete() {
            this.observer.onComplete();
        }
    }
}