package com.jakduk.api.restcontroller.exceptionHandler;

import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.SuccessButNoContentException;
import com.jakduk.core.exception.UserFeelingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 4 오전 12:30
 */

@Slf4j
@ControllerAdvice(value = "com.jakduk.api.restcontroller")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Hibernate validator Exception
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ObjectError> globalErrors = result.getGlobalErrors();
        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        List<String> fields = globalErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        fields.addAll(fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()));

        RestError restError = new RestError(serviceError, fields);

        return new ResponseEntity<>(restError, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * RequestBody에서 request 값들을 객체화 실패했을때
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        RestError restError = new RestError(serviceError);

        return new ResponseEntity<>(restError, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * 쿼리 스트링 검증 실패
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        RestError restError = new RestError(serviceError.getCode(),
                String.format(ex.getMessage(), ex.getParameterType(), ex.getParameterName()));

        return new ResponseEntity<>(restError, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    /**
     * 파라미터 검증 실패.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ServiceError serviceError = ServiceError.FORM_VALIDATION_FAILED;

        RestError restError = new RestError(serviceError.getCode(),
                String.format(ex.getMessage(), ex.getRequestPartName()));

        return new ResponseEntity<>(restError, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    // 에러는 아니지만 데이터가 없음.
    @ExceptionHandler(SuccessButNoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public RestError successButNoContentException(SuccessButNoContentException e) {
        return new RestError(HttpStatus.NO_CONTENT.toString(), e.getMessage());
    }

    @ExceptionHandler(UserFeelingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError repositoryExistException(UserFeelingException e) {
        return new RestError(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<RestError> serviceException(ServiceException ex) {
        ServiceError serviceError = ex.getServiceError();
        RestError restError = new RestError(serviceError);

        return new ResponseEntity<>(restError, HttpStatus.valueOf(serviceError.getHttpStatus()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestError runtimeException(RuntimeException e) {
        return new RestError(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getLocalizedMessage());
    }
}


