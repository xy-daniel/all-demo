package com.daniel.springcloudhystrixconsumer.define;

public interface Command<T> {

    T run();

    T fallback();
}
