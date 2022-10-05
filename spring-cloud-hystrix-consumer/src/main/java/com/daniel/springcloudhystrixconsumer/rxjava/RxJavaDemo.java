package com.daniel.springcloudhystrixconsumer.rxjava;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class RxJavaDemo {

    public static void main(String[] args) throws InterruptedException {
        observerDemo();


        Single.just(new Action1().init())
                .subscribe(RxJavaDemo::println);

        Single.just(new Action1().init())
                .subscribeOn(Schedulers.io())    //放置到io线程
                .subscribe(RxJavaDemo::println);

        Single.just(new Action1().init())
                .subscribeOn(Schedulers.newThread())        //新建线程
                .subscribe(RxJavaDemo::println);

        Single.fromCallable(() -> "ok").subscribeOn(Schedulers.newThread()).subscribe(RxJavaDemo::isOk);

        Thread.sleep(1000L);
    }

    public static class Action1 {
        public String init() {
            return "事件1发布";
        }
    }

    public static void println(Object value) {
        System.out.println("[线程 " + Thread.currentThread().getName() + "] 数据: " + value);
    }

    public static void isOk(Object val) {
        if (val.equals("ok")) {
            System.out.println("执行成功");
        } else {
            System.out.println("执行失败");
        }
    }

    public static void observerDemo() throws InterruptedException {
        List<Integer> values = Arrays.asList(1, 2, 3, 4);
        Observable.from(values).subscribe(
                next -> {
                    if (next > 2) {
                        throw new IllegalArgumentException("非法参数");
                    }
                },
                error -> {
                    System.out.println("降级!!");
                },
                () -> {
                    System.out.println("执行完毕");
                }
        );
    }

}
