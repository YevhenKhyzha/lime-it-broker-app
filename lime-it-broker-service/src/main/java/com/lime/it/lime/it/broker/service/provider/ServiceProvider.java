package com.lime.it.lime.it.broker.service.provider;

public interface ServiceProvider<T> {

    T provide(Class<T> tClass, String qualifier);
}
