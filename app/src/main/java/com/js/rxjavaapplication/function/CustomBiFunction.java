package com.js.rxjavaapplication.function;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public interface CustomBiFunction<T, U, R> {
    R apply(T t, U u);
}
