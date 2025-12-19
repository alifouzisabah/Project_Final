package com.example.Project_Finall.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T,ID>{
    T save(T entity);
    Optional<T> findById(ID id);
    void deleteById(ID id);
    List<T> findAll();
    T update (T entity ,ID id);
}
