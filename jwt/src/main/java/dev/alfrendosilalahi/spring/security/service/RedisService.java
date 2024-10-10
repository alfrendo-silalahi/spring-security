package dev.alfrendosilalahi.spring.security.service;

public interface RedisService {

    <T> void putValue(String key, T value);

    <T> T findObject(String key, Class<T> returnType);

}
