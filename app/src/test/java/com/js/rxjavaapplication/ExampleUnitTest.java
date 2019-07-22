package com.js.rxjavaapplication;

import com.js.rxjavaapplication.function.CustomBiFunction;
import com.js.rxjavaapplication.function.CustomFunction;
import com.js.rxjavaapplication.observable.CustomObservable;
import com.js.rxjavaapplication.scheduler.CustomScheduler;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() {
        testCreate();
        testFromIterable();
        testMap();
        testFlatMap();
        testSubscribeOn();
        testObserveOn();
        testSubscribeOnAndObserveOn();
        testFix();
    }

    @Test
    public void test2 () {
        testCustomRxJava();
        testRxjava();
    }

    @Test
    public void testZip() {
        CustomObservable<Integer> custom1 = CustomObservable.create(new CustomObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(CustomEmitter<Integer> emitter) {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });
        CustomObservable<Integer> custom2 = CustomObservable.create(new CustomObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(CustomEmitter<Integer> emitter) {
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        });
        CustomObservable.zip(custom1, custom2, new CustomBiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer o1, Integer o2) {
                return o1 + o2;
            }
        }).subscribe(new CustomObserver<Integer>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onNext(Integer o) {
                System.out.println(o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("custom complete");
            }
        });

        Observable<Integer> o1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });
        Observable<Integer> o2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });
        Observable.zip(o1, o2, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("rxjava complete");
            }
        });
    }

    @Test
    public void testRxjava() {
        final AtomicBoolean isFinish = new AtomicBoolean(false);
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " sub");
                emitter.onNext(1);
                emitter.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(final Integer integer) {
                return new ObservableSource<String>() {
                    @Override
                    public void subscribe(Observer<? super String> observer) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread() + " flat1");
                        observer.onNext("flat1 "  + integer);
                        observer.onComplete();
                    }
                };
            }
        })
                .observeOn(Schedulers.from(Executors.newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-main-1");
                        return thread;
                    }
                })))
                .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(5, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-1");
                        return thread;
                    }
                })))
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final String s) {
                        return new ObservableSource<String>() {
                            @Override
                            public void subscribe(Observer<? super String> observer) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(Thread.currentThread() + " flat2");
                                observer.onNext("flat2 " + s);
                                observer.onComplete();
                            }
                        };
                    }
                })
                .observeOn(Schedulers.from(Executors.newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-main-2");
                        return thread;
                    }
                })))
                .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(5, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-2");
                        return thread;
                    }
                })))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("==== start rxjava " + Thread.currentThread() + " ====");
                    }

                    @Override
                    public void onNext(String o) {
                        System.out.println(Thread.currentThread() + " next: " + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(Thread.currentThread() + " error: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("==== complete rxjava" + Thread.currentThread() + " ====");
                        isFinish.set(true);
                    }
                });

        while (!isFinish.get()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testCustomRxJava() {
        final AtomicBoolean isFinish = new AtomicBoolean(false);
        CustomObservable.create(new CustomObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(CustomEmitter<Integer> emitter) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " sub");
                emitter.onNext(1);
                emitter.onComplete();
            }
        }).flatMap(new CustomFunction<Integer, CustomObservableSource<String>>() {
            @Override
            public CustomObservableSource<String> apply(final Integer integer) {
                return new CustomObservableSource<String>() {
                    @Override
                    public void subscribe(CustomObserver<? super String> observer) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread() + " flat1");
                        observer.onNext("flat1 "  + integer);
                        observer.onComplete();
                    }
                };
            }
        })
                .observeOn(new CustomScheduler(Executors.newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-main-1");
                        return thread;
                    }
                })))
                .subscribeOn(new CustomScheduler(Executors.newFixedThreadPool(5, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-1");
                        return thread;
                    }
                })))
                .flatMap(new CustomFunction<String, CustomObservableSource<String>>() {
                    @Override
                    public CustomObservableSource<String> apply(final String value) {
                        return new CustomObservableSource<String>() {
                            @Override
                            public void subscribe(CustomObserver<? super String> observer) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(Thread.currentThread() + " flat2");
                                observer.onNext("flat2 " + value);
                                observer.onComplete();
                            }
                        };
                    }
                })
                .observeOn(new CustomScheduler(Executors.newSingleThreadExecutor(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-main-2");
                        return thread;
                    }
                })))
                .subscribeOn(new CustomScheduler(Executors.newFixedThreadPool(5, new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread-2");
                        return thread;
                    }
                })))
                .subscribe(new CustomObserver<String>() {
                    @Override
                    public void onStart() {
                        System.out.println("==== custom start " + Thread.currentThread() + " ====");
                    }

                    @Override
                    public void onNext(String o) {
                        System.out.println(Thread.currentThread() + " next: " + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(Thread.currentThread() + " error: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("==== custom complete " + Thread.currentThread() + " ====");
                        isFinish.set(true);
                    }
                });

        while (!isFinish.get()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCreate() {
        CustomObservable.create(new CustomObservableOnSubscribe<String>() {
            @Override
            public void subscribe(CustomEmitter<String> emitter) {
                emitter.onNext("test create");
                emitter.onComplete();
            }
        }).subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testFromIterable() {
        CustomObservable.from(Arrays.asList(
                "test from 0",
                "test from 1",
                "test from 2"
        )).subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testMap() {
        CustomObservable.create(new CustomObservableOnSubscribe<String>() {
            @Override
            public void subscribe(CustomEmitter<String> emitter) {
                emitter.onNext("test create");
                emitter.onComplete();
            }
        }).map(new CustomFunction<String, String>() {
            @Override
            public String apply(String s) {
                return "test map " + s;
            }
        }).subscribe(ExampleUnitTest.<String>getObserver());

        CustomObservable.from(Arrays.asList(
                "test from 0",
                "test from 1",
                "test from 2"
        )).map(new CustomFunction<String, String>() {
            @Override
            public String apply(String s) {
                return "test map " + s;
            }
        }).subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testFlatMap() {
        CustomObservable.create(new CustomObservableOnSubscribe<String>() {
            @Override
            public void subscribe(CustomEmitter<String> emitter) {
                emitter.onNext("test create");
                emitter.onComplete();
            }
        }).flatMap(new CustomFunction<String, CustomObservableSource<String>>() {
            @Override
            public CustomObservableSource<String> apply(final String s) {
                return new CustomObservableSource<String>() {
                    @Override
                    public void subscribe(CustomObserver<? super String> observer) {
                        observer.onNext("flatmap " + s);
                        observer.onComplete();
                    }
                };
            }
        }).subscribe(ExampleUnitTest.<String>getObserver());

        CustomObservable.from(Arrays.asList(
                "test from 0",
                "test from 1",
                "test from 2"
        )).flatMap(new CustomFunction<String, CustomObservableSource<String>>() {
            @Override
            public CustomObservableSource<String> apply(final String s) {
                return new CustomObservableSource<String>() {
                    @Override
                    public void subscribe(CustomObserver<? super String> observer) {
                        observer.onNext("flatmap " + s);
                        observer.onComplete();
                    }
                };
            }
        }).subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testSubscribeOn() {
        CustomObservable.create(new CustomObservableOnSubscribe<String>() {
            @Override
            public void subscribe(CustomEmitter<String> emitter) {
                emitter.onNext("test SubscribeOn");
                emitter.onComplete();
            }
        }).subscribeOn(new CustomScheduler(Executors.newSingleThreadExecutor()))
                .subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testObserveOn() {
        CustomObservable.create(new CustomObservableOnSubscribe<String>() {
            @Override
            public void subscribe(CustomEmitter<String> emitter) {
                emitter.onNext("test ObserveOn :" + Thread.currentThread());
                emitter.onComplete();
            }
        }).observeOn(new CustomScheduler(Executors.newSingleThreadExecutor()))
                .subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testSubscribeOnAndObserveOn() {
        CustomObservable.create(new CustomObservableOnSubscribe<String>() {
            @Override
            public void subscribe(CustomEmitter<String> emitter) {
                emitter.onNext("test SubscribeOn ObserveOn :" + Thread.currentThread());
                emitter.onComplete();
            }
        }).subscribeOn(new CustomScheduler(Executors.newSingleThreadExecutor()))
                .observeOn(new CustomScheduler(Executors.newSingleThreadExecutor()))
                .subscribe(ExampleUnitTest.<String>getObserver());
    }

    @Test
    public void testFix() {
        final AtomicBoolean isFinish = new AtomicBoolean(false);
        CustomObservable.from(Arrays.asList(
                "test from 0",
                "test from 1",
                "test from 2",
                "test from 3",
                "test from 4",
                "test from 5"))
                .flatMap(new CustomFunction<String, CustomObservableSource<String>>() {
                    @Override
                    public CustomObservableSource<String> apply(final String s) {
                        return new CustomObservableSource<String>() {
                            @Override
                            public void subscribe(CustomObserver<? super String> observer) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(Thread.currentThread());
                                observer.onNext("flat " + s);
                                observer.onComplete();
                            }
                        };
                    }
                })
                .subscribeOn(new CustomScheduler(Executors.newFixedThreadPool(5)))
                .flatMap(new CustomFunction<String, CustomObservableSource<String>>() {
                    @Override
                    public CustomObservableSource<String> apply(final String s) {
                        return new CustomObservableSource<String>() {
                            @Override
                            public void subscribe(CustomObserver<? super String> observer) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(Thread.currentThread());
                                observer.onNext("flat " + s);
                                observer.onComplete();
                            }
                        };
                    }
                })
                .subscribeOn(new CustomScheduler(Executors.newFixedThreadPool(5)))
                .subscribe(new CustomObserver<String>() {
                    @Override
                    public void onStart() {

                        System.out.println("==== start " + Thread.currentThread() + " ====");
                    }

                    @Override
                    public void onNext(String t) {
                        System.out.println(Thread.currentThread() + " next: " + t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(Thread.currentThread() + " error: " + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("==== " + Thread.currentThread() + " complete ==== \n");
                        isFinish.set(true);
                    }
                });

        while (!isFinish.get()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> CustomObserver getObserver() {
        CustomObserver<T> observer = new CustomObserver<T>() {
            @Override
            public void onStart() {
                System.out.println("==== start " + Thread.currentThread() + " ====");
            }

            @Override
            public void onNext(T t) {
                System.out.println(Thread.currentThread() + " next: " + t);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(Thread.currentThread() + " error: " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("==== " + Thread.currentThread() + " complete ==== \n");
            }
        };
        return observer;
    }
}