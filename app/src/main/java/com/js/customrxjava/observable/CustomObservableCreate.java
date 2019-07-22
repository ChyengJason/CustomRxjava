package com.js.customrxjava.observable;

import com.js.customrxjava.CustomEmitter;
import com.js.customrxjava.CustomObservableOnSubscribe;
import com.js.customrxjava.CustomObserver;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public class CustomObservableCreate<T> extends CustomObservable {
    private CustomObservableOnSubscribe<T> subscriber;

    public CustomObservableCreate(CustomObservableOnSubscribe<T> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    protected void subscribeActual(CustomObserver observer) {
        CustomCreateEmitter emitter = new CustomCreateEmitter<T>(observer);
        observer.onStart();
        subscriber.subscribe(emitter);
    }

    private static class CustomCreateEmitter<T> implements CustomEmitter<T> {
        private CustomObserver<? super T> observer;

        CustomCreateEmitter(CustomObserver<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T o) {
            observer.onNext(o);
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
