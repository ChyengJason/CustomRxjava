package com.js.rxjavaapplication.observable;

import com.js.rxjavaapplication.CustomObservableSource;
import com.js.rxjavaapplication.function.CustomFunction;
import com.js.rxjavaapplication.CustomObserver;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public class CustomObservableMap<R, T> extends CustomObservable {
    private CustomObservableSource<T> source;
    private CustomFunction<T, R> mapper;

    public CustomObservableMap(CustomObservableSource<T> source, CustomFunction<T, R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(CustomObserver observer) {
        CustomMapObserver<T, R> mapObserver = new CustomMapObserver(observer, mapper);
        source.subscribe(mapObserver);
    }

    private static class CustomMapObserver<T, R> implements CustomObserver<T> {
        private CustomObserver<R> observer;
        private CustomFunction<T, R> function;

        public CustomMapObserver(CustomObserver<R> observer, CustomFunction<T, R> function) {
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onStart() {
            observer.onStart();
        }

        @Override
        public void onNext(T result) {
            observer.onNext(function.apply(result));
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
