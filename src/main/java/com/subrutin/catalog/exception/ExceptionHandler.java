package com.subrutin.catalog.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.subrutin.catalog.dto.ErrorResponseDTO;
import com.subrutin.catalog.enums.ErrorCode;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> details = new ArrayList<String>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ErrorResponseDTO errorResponse = ErrorResponseDTO.of("invalid data", details, ErrorCode.INVALID_DATA,
				HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(errorResponse);

	}

}
