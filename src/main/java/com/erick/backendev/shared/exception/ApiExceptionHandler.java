package com.erick.backendev.shared.exception;

import com.erick.backendev.shared.client.tvmaze.TvMazeShowClient;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(TvMazeShowClient.TvMazeClientException.class)
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	public ProblemDetail handleTvMazeClientException(TvMazeShowClient.TvMazeClientException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_GATEWAY,
				ex.getMessage()
		);
		problemDetail.setTitle("Upstream API error");
		return problemDetail;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST,
				ex.getMessage()
		);
		problemDetail.setTitle("Validation error");
		return problemDetail;
	}
}
