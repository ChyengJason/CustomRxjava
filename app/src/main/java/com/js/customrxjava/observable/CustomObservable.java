package com.js.customrxjava.observable;

import com.js.customrxjava.CustomObservableOnSubscribe;
import com.js.customrxjava.CustomObservableSource;
import com.js.customrxjava.CustomObserver;
import com.js.customrxjava.function.CustomBiFunction;
import com.js.customrxjava.function.CustomFunction;
import com.js.customrxjava.function.CustomFunctions;
import com.js.customrxjava.scheduler.CustomScheduler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chengjunsen on 2019-07-21
 **/
public abstract class CustomObservable<T> implements CustomObservableSource {

    public static <T> CustomObservable<T> create(CustomObservableOnSubscribe<T> source) {
        return new CustomObservableCreate(source);
    }

    public static <T> CustomObservable<T> from(Iterable<T> values) {
        return new CustomObservableIterable<>(values);
    }

    public static <T, U, R> CustomObservable<R> zip(final CustomObservableSource<T> o1,
                                                    final CustomObservableSource<U> o2,
                                                    CustomBiFunction<T, U, R> mapper) {
        List<CustomObservableSource<?>> list = Arrays.asList(o1, o2);
        CustomFunction<Object[], R> arrayFunc = new CustomFunctions.Array2Func(mapper);
        return new CustomObservableZip(list, arrayFunc);
    }

    public <R> CustomObservable<R> map(CustomFunction<T, R> function) {
        return new CustomObservableMap(this, function);
    }

    public <R> CustomObservable<R> flatMap(CustomFunction<T, CustomObservableSource<R>> function) {
        return new CustomObservableFlatMap(this, function);
    }

    public CustomObservable<T> subscribeOn(CustomScheduler scheduler) {
        return new CustomObservableSubscribeOn(this, scheduler);
    }

    public CustomObservable<T> observeOn(CustomScheduler scheduler) {
        return new CustomObservableObserveOn(this, scheduler);
    }

    @Override
    public void subscribe(CustomObserver observer) {
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(CustomObserver observer);
}
