package org.music.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 * 
 * @author jyj
 *
 * @date 2017年11月18日
 */
@SpringBootApplication
// 启动配置中心服务
@EnableConfigServer
// 启动服务注册、服务发现（集群）
@EnableDiscoveryClient
public class ConfigApp {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApp.class, args);
	}

}
