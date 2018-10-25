package com.softvision.digital.pms.common.beans;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.softvision.digital.pms.common.model.Result;

//@RestController
//@RestControllerAdvice
public class ResultEntityWrappingHandler implements ResponseBodyAdvice<Object> {

	//extends ResponseEntityExceptionHandler
	// ResponseBodyAdvice
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Result> handleServiceException(Exception ex, WebRequest request) {
		Result result = new Result(Result.FAILURE, ex.getMessage(), ex);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	public Result beforeBodyWrite(Object content, MethodParameter arg1, MediaType arg2,
			Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse response) {
		Result result = new Result();
		result.setCode(Result.SUCCESS);
		result.setContent(content);
		return result;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> arg1) {
		return true;
	}

}
