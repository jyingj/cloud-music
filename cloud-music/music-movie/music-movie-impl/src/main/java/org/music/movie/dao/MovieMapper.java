package org.music.movie.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.music.movie.entity.Movie;

@Mapper
public interface MovieMapper {
	
	/**查询列表*/
	List<Movie> selectMovieList();
	/**根据电影名称查询*/
	Movie selectMovieByName(String name);
	/**增加*/
	int insertMovie(Movie movie);
	/**修改*/
	int updateMovieById(Movie movie);
	/**删除*/
	int deleteMovieById(Integer id);
	
}
