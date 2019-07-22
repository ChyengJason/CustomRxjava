package com.js.rxjavaapplication;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public interface CustomObservableSource<T> {

    void subscribe(CustomObserver<? super T> observer);

}
