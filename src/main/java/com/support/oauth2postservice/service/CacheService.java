package com.support.oauth2postservice.service;

public interface CacheService {

  String getData(String key);

  int getDataAsInt(String key);

  void setData(String key, Object value);

  void setDataAndExpiration(String key, Object value, long duration);

  void deleteData(String key);

  void increment(String key);

  <T> T getObjectData(String key, Class<T> t);
}
