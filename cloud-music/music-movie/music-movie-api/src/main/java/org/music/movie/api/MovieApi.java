package org.music.movie.api;

import java.util.List;

import org.music.movie.entity.Movie;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 视频Api
 * 
 * @author jyj
 *
 * @date 2017年11月19日
 */
public interface MovieApi {

	/**
	 * 服务应用名称(spring.application.name = APPLICATION_NAME)
	 */
	public String APPLICATION_NAME = "MOVIE";

	/**
	 * 查询视频列表
	 * 
	 * @return
	 */
	@RequestMapping("/getMovieList")
	public List<Movie> getMovieList();

	/**
	 * 根据电影名称查询
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/getMovieByName")
	public Movie getMovieByName(@RequestParam("name") String name);

	/**
	 * 增加视频
	 * 
	 * @param movie
	 * @return
	 */
	@RequestMapping("/addMovie")
	public int addMovie(@RequestBody Movie movie);

	/**
	 * 修改视频
	 * 
	 * @param movie
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateMovieById")
	public int updateMovieById(@RequestBody Movie movie);

	/**
	 * 删除视频
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delMovieById")
	public int delMovieById(@RequestParam("id") Integer id);

}
