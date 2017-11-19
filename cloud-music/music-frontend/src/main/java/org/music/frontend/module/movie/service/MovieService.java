package org.music.frontend.module.movie.service;

import org.music.frontend.module.movie.service.fallback.MovieServiceFallBack;
import org.music.movie.api.MovieApi;
import org.springframework.cloud.netflix.feign.FeignClient;

//指定调用哪个服务
@FeignClient(name = MovieApi.APPLICATION_NAME + "/movieApi", fallback = MovieServiceFallBack.class)
public interface MovieService extends MovieApi{

}
