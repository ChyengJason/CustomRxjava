package com.js.rxjavaapplication;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public interface CustomObservableOnSubscribe<T> {
    void subscribe(CustomEmitter<T> emitter);
}
