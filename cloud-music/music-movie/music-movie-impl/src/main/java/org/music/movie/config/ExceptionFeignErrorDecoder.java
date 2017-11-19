package org.music.movie.config;

import static feign.FeignException.errorStatus;

import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.exception.HystrixBadRequestException;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * 服务提供者所有异常都返回给服务调用者
 * 
 * @author jyj
 *
 * @date 2017年11月19日
 */
 
@Configuration
public class ExceptionFeignErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		System.out.println("==========ExceptionFeignErrorDecoder");
		FeignException exception = errorStatus(methodKey, response);
		String message = exception.getMessage();
		int index = message.indexOf("{");

		String messageJson = message.substring(index, message.length());

//		String returnMessage = JacksonUtils.getSubObj(messageJson, "message", String.class);
		return new HystrixBadRequestException(null);
	}
}
