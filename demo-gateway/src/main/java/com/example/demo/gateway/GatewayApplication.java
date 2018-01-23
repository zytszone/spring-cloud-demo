package com.example.demo.gateway;

import com.example.demo.gateway.config.MyZuulConfiguration;
import com.example.demo.gateway.filters.pre.PreRequestLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhangyutao
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@RibbonClient(name = "microservice-provider-user", configuration = MyZuulConfiguration.class)
public class GatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

  @Bean
  public PreRequestLogFilter preRequestLogFilter() {
    return new PreRequestLogFilter();
  }
}
