package com.js.customrxjava.function;

/**
 * Created by chengjunsen on 2019-07-22
 **/
public class CustomFunctions {
    public static final class Array2Func<T1, T2, R> implements CustomFunction<Object[], R> {
        final CustomBiFunction<? super T1, ? super T2, ? extends R> f;

        public Array2Func(CustomBiFunction<? super T1, ? super T2, ? extends R> f) {
            this.f = f;
        }

        @Override
        public R apply(Object[] a) {
            if (a.length != 2) {
                throw new IllegalArgumentException("Array of size 2 expected but got " + a.length);
            }
            return f.apply((T1)a[0], (T2)a[1]);
        }
    }
}
