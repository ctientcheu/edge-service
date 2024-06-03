package com.polarbookshop.edgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;

/**
 * @author clement.tientcheu@cerebrau.com
 * @project edge-service
 * @org Cerebrau
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestEdgeServiceApplication {

  public static void main(String[] args) {
    SpringApplication.from(EdgeServiceApplication::main)
        .with(TestEdgeServiceApplication.class)
        .run(args);
  }

  @Bean
  @ServiceConnection(name = "redis")
  GenericContainer<?> redisContainer() {
    return new GenericContainer<>("redis:7.2.4").withExposedPorts(6379);
  }
}
