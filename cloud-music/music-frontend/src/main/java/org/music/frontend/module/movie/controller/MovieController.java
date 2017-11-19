package org.music.frontend.module.movie.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.music.frontend.module.movie.service.MovieService;
import org.music.movie.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "视频 Controller", value = "/portal")
public class MovieController {

	Logger logger = LoggerFactory.getLogger(MovieController.class);
	@Autowired
	private MovieService movieService;

	@ApiOperation(value = "获取视频列表", notes = "")
	@RequestMapping(value = "/movie/list", method = { RequestMethod.GET, RequestMethod.POST })
	public List<Movie> getMovieList(HttpServletRequest request) {
		return movieService.getMovieList();
	}

	@ApiOperation(value = "根据视频名称获取视频信息", notes = "")
	@RequestMapping(value = "/movie/{name}", method = { RequestMethod.GET, RequestMethod.POST })
	public Movie getMovieById(HttpServletRequest request, @PathVariable("name") String name) {
		return movieService.getMovieByName(name);
	}

	@ApiOperation(value = "添加视频", notes = "")
	@RequestMapping(value = "/addMovie", method = { RequestMethod.GET, RequestMethod.POST })
	public String addMovie(@RequestBody Movie movie) {
		int count = movieService.addMovie(movie);
		return count > 0 ? "成功" : "失败";
	}

	@ApiOperation(value = "根据id修改视频信息", notes = "")
	@RequestMapping(value = "/updateMovie", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateMovieById(@RequestBody Movie movie) {
		int count = movieService.updateMovieById(movie);
		return count > 0 ? "成功" : "失败";
	}

	@ApiOperation(value = "根据视频名称获取视频信息", notes = "")
	@RequestMapping(value = "/delMovie/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String delMovieById(@PathVariable("id") Integer id) {
		int count = movieService.delMovieById(id);
		return count > 0 ? "成功" : "失败";
	}

}
