package org.music.frontend.module.movie.service.fallback;

import java.util.List;

import org.music.frontend.module.movie.service.MovieService;
import org.music.movie.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MovieServiceFallBack implements MovieService{

	private static final Logger log = LoggerFactory.getLogger(MovieServiceFallBack.class);

	@Override
	public List<Movie> getMovieList() {
		System.out.println("请求超时...");
		log.debug("请求超时...");
		return null;
	}

	@Override
	public Movie getMovieByName(String name) {
		System.out.println("请求超时...");
		log.debug("请求超时...");
		return null;
	}

	@Override
	public int addMovie(Movie movie) {
		System.out.println("请求超时...");
		log.debug("请求超时...");
		return 0;
	}

	@Override
	public int updateMovieById(Movie movie) {
		System.out.println("请求超时...");
		log.debug("请求超时...");
		return 0;
	}

	@Override
	public int delMovieById(Integer id) {
		System.out.println("请求超时...");
		log.debug("请求超时...");
		return 0;
	}

}
