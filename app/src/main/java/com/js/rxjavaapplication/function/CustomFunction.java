package com.js.rxjavaapplication.function;

/**
 * Created by chengjunsen on 2019-07-21
 **/
public interface CustomFunction<T, R> {
    R apply(T t);
}
