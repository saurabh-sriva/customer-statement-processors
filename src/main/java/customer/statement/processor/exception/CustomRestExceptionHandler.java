package customer.statement.processor.exception;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;

@ControllerAdvice
@RestController
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

		CustomerRecordValidationResponse response = new CustomerRecordValidationResponse(
				ValidationResultCode.INTERNAL_SERVER_ERROR, Collections.emptyList());

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		CustomerRecordValidationResponse response = new CustomerRecordValidationResponse(
				ValidationResultCode.BAD_REQUEST, Collections.emptyList());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}