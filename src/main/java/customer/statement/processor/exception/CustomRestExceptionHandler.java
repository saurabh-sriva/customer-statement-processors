package customer.statement.processor.exception;

import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
@RestController
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomRestExceptionHandler.class);
    private static final String EXCEPTION_MESSAGE = "Exception has occurred while processing of this request";
    private static final String INVALID_RECORD_EXCEPTION_MESSAGE = "Invalid record exception";

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CustomerRecordValidationResponse> handleAllExceptions(Exception ex, WebRequest request) {

        CustomerRecordValidationResponse response = new CustomerRecordValidationResponse(
                ValidationResultCode.INTERNAL_SERVER_ERROR, Collections.emptyList());

        LOG.error(EXCEPTION_MESSAGE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomerRecordValidationException.class)
    public ResponseEntity<CustomerRecordValidationResponse> handleCustomException(final CustomerRecordValidationException exception) {
        CustomerRecordValidationResponse response = new CustomerRecordValidationResponse(
                ValidationResultCode.BAD_REQUEST, Collections.emptyList());
        response.setErrorRecords(exception.getErrorRecords());

        LOG.error(INVALID_RECORD_EXCEPTION_MESSAGE, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {

        CustomerRecordValidationResponse response = new CustomerRecordValidationResponse(
                ValidationResultCode.BAD_REQUEST, Collections.emptyList());

        LOG.error(INVALID_RECORD_EXCEPTION_MESSAGE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}