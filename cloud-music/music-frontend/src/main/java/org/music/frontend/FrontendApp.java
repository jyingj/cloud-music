package org.music.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

// 启动服务注册、服务发现
@EnableDiscoveryClient
//开启客户端负载均衡(调用服务)(feign方式)
@EnableFeignClients
//Servlet组件扫描
@ServletComponentScan
@EnableCircuitBreaker
@SpringBootApplication
public class FrontendApp extends SpringBootServletInitializer{
	
	public static void main(String[] args) {
		SpringApplication.run(FrontendApp.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FrontendApp.class);
	}

}
