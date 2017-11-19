package org.music.movie.impl;

import java.util.List;

import org.music.movie.api.MovieApi;
import org.music.movie.dao.MovieMapper;
import org.music.movie.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author jyj
 *
 * @date 2017年11月19日
 */
@RestController
@RequestMapping("/movieApi")
@Transactional(rollbackFor = Exception.class)
public class MovieImpl implements MovieApi {

	@Autowired
	private MovieMapper movieMapper;

	@Override
	public List<Movie> getMovieList() {
		return movieMapper.selectMovieList();
	}

	@Override
	public Movie getMovieByName(String name) {
		return movieMapper.selectMovieByName(name);
	}

	@Override
	public int addMovie(Movie movie) {
		int count = movieMapper.insertMovie(movie);
		return count > 0 ? 1 : 0;
	}

	@Override
	public int updateMovieById(Movie movie) {
		int count = movieMapper.updateMovieById(movie);
		return count > 0 ? 1 : 0;
	}

	@Override
	public int delMovieById(Integer id) {
		int count = movieMapper.deleteMovieById(id);
		return count > 0 ? 1 : 0;
	}

}
