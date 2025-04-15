package com.example.chapter6.v5;

import com.example.chapter6.v5.proxy.Hello;
import com.example.chapter6.v5.proxy.HelloTarget;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyFactoryBeanTest {

    @Test
    @DisplayName("ProxyFactoryBean 활용 테스트")
    void testProxyFactory() {
        // given
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        // when
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        // then
        assertThat(proxiedHello.sayHello("이현희")).isEqualTo("HELLO 이현희");
        assertThat(proxiedHello.sayHi("이현희")).isEqualTo("HI 이현희");
        assertThat(proxiedHello.sayThankYou("이현희")).isEqualTo("THANK YOU 이현희");
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    @DisplayName("ProxyFactoryBean + pointcut 적용 테스트")
    void test() {
        // given
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.addMethodName("sayH*");

        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        // when
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        // then
        assertThat(proxiedHello.sayHello("이현희")).isEqualTo("HELLO 이현희");
        assertThat(proxiedHello.sayHi("이현희")).isEqualTo("HI 이현희");
        assertThat(proxiedHello.sayThankYou("이현희")).isEqualTo("Thank you 이현희"); // 미적용
    }
}
