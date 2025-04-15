package com.example.chapter6.v5;

import com.example.chapter6.v5.proxy.Hello;
import com.example.chapter6.v5.proxy.HelloTarget;
import com.example.chapter6.v5.proxy.HelloUpperCase;
import com.example.chapter6.v5.proxy.UpperCaseHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyTest {
    @Test
    @DisplayName("간단한 프록시 예제 테스트")
    void testSimpleProxy() {
        // given
        Hello hello = new HelloTarget();
        Hello helloProxy = new HelloUpperCase(hello);

        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UpperCaseHandler(new HelloTarget())
        );

        // when

        // then
        assertThat(hello.sayHello("이현희")).isEqualTo("Hello 이현희");
        assertThat(hello.sayHi("이현희")).isEqualTo("Hi 이현희");
        assertThat(hello.sayThankYou("이현희")).isEqualTo("Thank you 이현희");


        assertThat(helloProxy.sayHello("이현희")).isEqualTo("HELLO 이현희");
        assertThat(helloProxy.sayHi("이현희")).isEqualTo("HI 이현희");
        assertThat(helloProxy.sayThankYou("이현희")).isEqualTo("THANK YOU 이현희");


        assertThat(proxiedHello.sayHello("이현희")).isEqualTo("HELLO 이현희");
        assertThat(proxiedHello.sayHi("이현희")).isEqualTo("HI 이현희");
        assertThat(proxiedHello.sayThankYou("이현희")).isEqualTo("THANK YOU 이현희");
    }

}
