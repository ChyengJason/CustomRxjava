package com.js.customrxjava;

/**
 * Created by chengjunsen on 2019-07-22
 * 对外的观察者发射器
 **/
public interface CustomEmitter<T> {
    void onNext(T value);

    void onError(Throwable e);

    void onComplete();
}
