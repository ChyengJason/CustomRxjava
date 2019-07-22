package com.js.customrxjava.observable;

import com.js.customrxjava.CustomObservableSource;
import com.js.customrxjava.CustomObserver;
import com.js.customrxjava.function.CustomFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Created by chengjunsen on 2019-07-22
 *
 **/
public class CustomObservableZip<T, U, R> extends CustomObservable<T> {
    List<CustomObservableSource<T>> sources;
    CustomFunction<Object[], R> mapper;

    public CustomObservableZip(List<CustomObservableSource<T>> sources, CustomFunction<Object[], R> mapper) {
        this.sources = sources;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(CustomObserver observer) {
        ZipCoordinator zipCoordinator = new ZipCoordinator(observer, sources, mapper);
        zipCoordinator.subscribe();
    }

    static final class ZipCoordinator<T, R> {
        CustomObserver<R> actual;
        List<CustomObservableSource<T>> sources;
        List<ZipObserver<T, R>> observers;
        CustomFunction<Object[], R> mapper;
        int size;
        boolean isFinish;

        ZipCoordinator(CustomObserver<R> observer,
                       List<CustomObservableSource<T>> sources,
                       CustomFunction<Object[], R> mapper) {
            this.actual = observer;
            this.sources = sources;
            this.mapper = mapper;
            this.size = sources.size();
            this.observers = new ArrayList<>(size);
            this.isFinish = false;
        }

        public void subscribe() {
            actual.onStart();
            for (int i = 0; i<size; i++) {
                ZipObserver observer = new ZipObserver<T, R>(this);
                observers.add(observer);
            }
            for (int i = 0; i<size; i++) {
                sources.get(i).subscribe(observers.get(i));
            }
        }

        void drain() {
            if (isFinish) {
                return;
            }
            boolean canMerge = true;
            boolean isDone = true;
            for (ZipObserver<T, R> observer: observers) {
                if (!observer.isDone) {
                    isDone = false;
                }
                if (observer.queue.isEmpty()) {
                    canMerge = false;
                }
            }
            if (canMerge) {
                List<T> mergeList = new ArrayList<>(size);
                for (ZipObserver<T, R> observer: observers) {
                    T t = observer.queue.poll();
                    mergeList.add(t);
                }
                actual.onNext(mapper.apply(mergeList.toArray()));
            }
            if (isDone) {
                actual.onComplete();
            }
        }
    }

    static class ZipObserver<T, R> implements CustomObserver<T> {
        boolean isDone;
        ZipCoordinator<T, R> parent;
        Queue<T> queue;
        Throwable error;

        public ZipObserver(ZipCoordinator parent) {
            this.parent = parent;
            this.queue = new LinkedList<>();
            this.isDone = false;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onNext(T o) {
            queue.add(o);
            parent.drain();
        }

        @Override
        public void onError(Throwable e) {
            isDone = true;
            error = e;
            parent.drain();
        }

        @Override
        public void onComplete() {
            isDone = true;
            parent.drain();
        }
    }
}
