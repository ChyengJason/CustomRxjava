package com.js.customrxjava;

/**
 * Created by chengjunsen on 2019-07-21
 **/
public interface CustomSubscriber<T> {
    void subscribe(CustomObserver<T> emitter);
}
