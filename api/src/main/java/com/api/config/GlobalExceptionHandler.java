package com.api.config;

import com.core.dto.BaseResponse;
import com.core.exception.BusinessException;
import com.core.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.api")
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity
			.status(errorCode.getStatus())
			.body(BaseResponse.error(errorCode, e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return ResponseEntity
			.status(errorCode.getStatus())
			.body(BaseResponse.error(errorCode));
	}
}
