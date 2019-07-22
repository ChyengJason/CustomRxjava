package com.js.rxjavaapplication;

/**
 * Created by chengjunsen on 2019-07-21
 * 对内的观察者
 **/
public interface CustomObserver<T> {
    void onStart();

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
