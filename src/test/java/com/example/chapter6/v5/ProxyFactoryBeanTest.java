package com.example.chapter6.v5;

import com.example.chapter6.v5.proxy.Hello;
import com.example.chapter6.v5.proxy.HelloTarget;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyFactoryBeanTest {

    @Autowired
    private ApplicationContext context;

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

    @Test
    @DisplayName("class 이름 필터링 pointcut 적용 테스트")
    void testClassNameFilteringPointcut() {
        // given
        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT"); // 클래스 이름
            }
        };
        nameMatchMethodPointcut.setMappedName("sayH*"); // 메서드 이름

        class HelloWorld extends HelloTarget {}
        class HelloTobi extends HelloTarget {};

        // when

        // then
        HelloTarget helloTarget = new HelloTarget();
        check(helloTarget, nameMatchMethodPointcut, true);

        HelloWorld helloWorld = new HelloWorld();
        check(helloWorld, nameMatchMethodPointcut, false);

        HelloTobi helloTobi = new HelloTobi();
        check(helloTobi, nameMatchMethodPointcut, true);
    }

    private static void check(HelloTarget target, NameMatchMethodPointcut nameMatchMethodPointcut, Boolean isAdviced) {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(nameMatchMethodPointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        // advice 적용
        if(isAdviced) {
            assertThat(proxiedHello.sayHello("이현희")).isEqualTo("HELLO 이현희");
            assertThat(proxiedHello.sayHi("이현희")).isEqualTo("HI 이현희");
            assertThat(proxiedHello.sayThankYou("이현희")).isEqualTo("Thank you 이현희"); // 미적용
        } else { // advice 미적용
            assertThat(proxiedHello.sayHello("이현희")).isEqualTo("Hello 이현희");
            assertThat(proxiedHello.sayHi("이현희")).isEqualTo("Hi 이현희");
            assertThat(proxiedHello.sayThankYou("이현희")).isEqualTo("Thank you 이현희");
        }
    }
}
