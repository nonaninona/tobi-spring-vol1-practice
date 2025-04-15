package com.example.chapter6.v4.proxy;

public class HelloUpperCase implements Hello {

    private Hello target;

    public HelloUpperCase(Hello target) {
        this.target = target;
    }

    @Override
    public String sayHello(String name) {
        return target.sayHello(name).toUpperCase();
    }

    @Override
    public String sayHi(String name) {
        return target.sayHi(name).toUpperCase();
    }

    @Override
    public String sayThankYou(String name) {
        return target.sayThankYou(name).toUpperCase();
    }
}
