package org.georgie.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.georgie.service" })
public class ServiceConfig {
}
