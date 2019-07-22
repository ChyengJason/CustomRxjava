package com.js.rxjavaapplication.observable;

import com.js.rxjavaapplication.CustomObservableSource;
import com.js.rxjavaapplication.CustomObserver;
import com.js.rxjavaapplication.function.CustomFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public class CustomObservableFlatMap<T, R> extends CustomObservable {
    private CustomObservableSource<T> source;
    private CustomFunction<T, CustomObservableSource<R>> mapper;

    public CustomObservableFlatMap(CustomObservableSource<T> source, CustomFunction<T, CustomObservableSource<R>> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(CustomObserver observer) {
        CustomFlatMapObserver<T, R> flatMapObserver = new CustomFlatMapObserver(observer, mapper);
        source.subscribe(flatMapObserver);
    }

    private static class CustomFlatMapObserver<T, R> implements CustomObserver<T> {
        private CustomObserver<R> observer;
        private CustomFunction<T, CustomObservableSource<R>> mapper;
        private List<CustomObservableSource<R>> sources;
        private List<InnerObserver<R>> observers;

        public CustomFlatMapObserver(CustomObserver<R> observer, CustomFunction<T, CustomObservableSource<R>> mapper) {
            this.observer = observer;
            this.mapper = mapper;
            this.sources = new ArrayList<>();
        }

        @Override
        public void onStart() {
            observer.onStart();
        }

        @Override
        public void onNext(T t) {
            CustomObservableSource<R> source = mapper.apply(t);
            InnerObserver<R> innerObserver = new InnerObserver<>(observer);
            source.subscribe(innerObserver);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }

        private static class InnerObserver<R> implements CustomObserver<R> {
            private CustomObserver<R> observer;

            InnerObserver(CustomObserver<R> observer) {
                this.observer = observer;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onNext(R result) {
                observer.onNext(result);
            }

            @Override
            public void onError(Throwable e) {
                observer.onError(e);
            }

            @Override
            public void onComplete() {

            }
        }
    }
}
