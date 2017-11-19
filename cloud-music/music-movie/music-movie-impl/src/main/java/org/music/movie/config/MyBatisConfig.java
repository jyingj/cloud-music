package org.music.movie.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.pagehelper.PageHelper;

/**
 * MyBatis 配置
 * 
 * @author jyj
 *
 * @date 2017年11月19日
 */
@Configuration
public class MyBatisConfig {
	/**
	 * 分页插件
	 * 
	 * https://github.com/pagehelper/Mybatis-PageHelper
	 * 
	 * @return
	 */
	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		// properties.setProperty("offsetAsPageNum", "true");
		// properties.setProperty("rowBoundsWithCount", "true");
		// true:pageNum<1则返回第一页,pageNum>pages则返回最后一页;false(默认):pageNum<1或pageNum>pages返回空数据
		// p.setProperty("reasonable", "true");
		// true:pageSize=0或RowBounds.limit=0返回所有;false(默认)
		// p.setProperty("pageSizeZero", "true");
		pageHelper.setProperties(properties);
		return pageHelper;
	}
}
