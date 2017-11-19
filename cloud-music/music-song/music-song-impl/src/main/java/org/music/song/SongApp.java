package org.music.song;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 歌曲服务
 * 
 * @author jyj
 *
 * @date 2017年11月19日
 */
// 启动服务注册、服务发现
@EnableDiscoveryClient
// 开启客户端负载均衡(调用服务)(feign方式)
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class SongApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SongApp.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SongApp.class);
	}
}
