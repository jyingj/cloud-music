package org.music.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
/**
 * 网关服务
 * 
 * @author jyj
 *
 * @date 2017年11月18日
 */
//开启Zuul的API网关服务
@EnableZuulProxy
@SpringBootApplication
public class GateWayApp {

	public static void main(String[] args) {
		SpringApplication.run(GateWayApp.class, args);
	}

}
