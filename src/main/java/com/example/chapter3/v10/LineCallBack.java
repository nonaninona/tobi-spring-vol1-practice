package com.example.chapter3.v10;

public interface LineCallBack<T> {
    T doSomethingWithLine(String line, T value); // 각 라인, 현재까지 계산 값
}
