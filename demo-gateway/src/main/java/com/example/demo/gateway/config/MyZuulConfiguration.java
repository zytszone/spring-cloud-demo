package com.example.demo.gateway.config;

import com.example.demo.gateway.rule.IpUserHashRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class MyZuulConfiguration {
	  @Autowired
	  IClientConfig ribbonClientConfig;
	  @Bean
	  public IRule ribbonRule(IClientConfig config) {
	    return new IpUserHashRule();
	  }
}
