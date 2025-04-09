package com.example.chapter6.v3.config;

import com.example.chapter6.v2.userService.UserServiceImpl;
import com.example.chapter6.v3.factoryBean.MessageFactoryBean;
import com.example.chapter6.v3.factoryBean.TxProxyFactoryBean;
import com.example.chapter6.v3.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FactoryBeanConfig {
    private UserServiceImpl userService;
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public MessageFactoryBean message() {
        MessageFactoryBean factoryBean = new MessageFactoryBean();
        factoryBean.setText("simple factory");
        return factoryBean;
    }

    @Bean
    public TxProxyFactoryBean txService() {
        TxProxyFactoryBean factoryBean = new TxProxyFactoryBean(userService, platformTransactionManager, "upgradeLevels", UserService.class);
        return factoryBean;
    }
}
