package org.music.frontend.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * SpringMVC配置(相当于applicationContext-mvc.xml)
 * 
 * @author qq
 *
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	/**
	 * 跨域访问配置
	 * 
	 * 相当于<mvc:cors>
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).maxAge(3600)
		.allowedMethods("GET", "POST", "OPTIONS").exposedHeaders("WWW-Authenticate");
	}

	/*
	 * 视图解析器
	 */
	/**
	 * JSP
	 */
	@Bean
	public ViewResolver jspViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		// viewClass
		viewResolver.setViewClass(JstlView.class);
		// prefix
		viewResolver.setPrefix("/frontend/pages/");
		// suffix
		viewResolver.setSuffix(".jsp");
		// contentType
		viewResolver.setContentType("text/html;charset=UTF-8");
		// order
		viewResolver.setOrder(0);
		return viewResolver;
	}




	/**
	 * 拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludePathPatterns = new ArrayList<String>();
		// 视频访问
		excludePathPatterns.add("/movie/**");
		String[] excludePathPatternsStr = new String[excludePathPatterns.size()];
		excludePathPatterns.toArray(excludePathPatternsStr);
		super.addInterceptors(registry);
	}
}
