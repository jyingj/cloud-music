package org.music.frontend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * restful Apis文档配置
 * @author wang
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	Docket createRestApi(){
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
//                .apis(RequestHandlerSelectors.any())//只扫描任何
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))//只扫描加了改注解的方法类
                .paths(PathSelectors.any())
                .build();
	}
	
	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
                .title("Swagger2 RESTful APIs")
                .description("前端controller接口文档")
                .termsOfServiceUrl("http://localhost:8080")
                .contact("music")
                .version("1.0")
                .build();
	}
}
