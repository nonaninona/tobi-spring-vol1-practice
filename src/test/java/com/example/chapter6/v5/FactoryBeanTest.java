package com.example.chapter6.v5;

import com.example.chapter6.v5.config.DataSourceConfig;
import com.example.chapter6.v5.config.FactoryBeanConfig;
import com.example.chapter6.v5.config.MailSenderConfig;
import com.example.chapter6.v5.config.UserServiceConfig;
import com.example.chapter6.v5.factoryBean.Message;
import com.example.chapter6.v5.userService.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { FactoryBeanConfig.class, UserServiceConfig.class, DataSourceConfig.class, MailSenderConfig.class })
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("팩토리 빈 테스트")
    void testSimpleFactoryBean() {
        // given
        Object message = context.getBean("message");
        Object messageFactory = context.getBean("&message");

        // when

        // then
        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo("simple factory");

        assertThat(messageFactory).isInstanceOf(FactoryBean.class);
    }

    @Test
    @DisplayName("유저 서비스 팩토리 빈 테스트")
    void testUserServiceFactoryBean() throws Exception {
        // given
        Object bean = context.getBean("txService");
        Object beanFactory = context.getBean("&txService");

        // when

        // then
        assertThat(bean).isInstanceOf(UserService.class);
        assertThat(beanFactory).isInstanceOf(FactoryBean.class);

        Object object = ((FactoryBean<Object>) beanFactory).getObject();
        assertThat(object).isInstanceOf(UserService.class);
    }
}
