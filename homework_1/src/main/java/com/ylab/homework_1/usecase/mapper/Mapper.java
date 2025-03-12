package com.ylab.homework_1.usecase.mapper;

@FunctionalInterface
public interface Mapper <T,V>{
    T apply(V value);
}
