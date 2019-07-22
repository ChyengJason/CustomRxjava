package com.js.rxjavaapplication;

import com.js.rxjavaapplication.CustomObserver;

/**
 * Created by chengjunsen on 2019-07-21
 **/
public interface CustomSubscriber<T> {
    void subscribe(CustomObserver<T> emitter);
}
