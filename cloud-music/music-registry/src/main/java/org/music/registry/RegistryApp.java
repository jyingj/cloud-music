package org.music.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心
 * 
 * @author jyj
 *
 * @date 2017年11月18日
 */
@SpringBootApplication
@EnableEurekaServer
public class RegistryApp {

	public static void main(String[] args) {
		SpringApplication.run(RegistryApp.class, args);
	}

}
