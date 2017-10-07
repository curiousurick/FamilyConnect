package org.georgie.spring;

import org.georgie.security.user.ActiveUserStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig
{
    // beans

    @Bean
    public ActiveUserStore activeUserStore()
    {
        return new ActiveUserStore();
    }

}