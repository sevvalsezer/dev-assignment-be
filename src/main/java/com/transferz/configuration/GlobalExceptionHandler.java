package com.transferz.configuration;

import com.transferz.dto.response.ApiErrorResponse;
import com.transferz.exception.BadRequestException;
import com.transferz.exception.ConflictException;
import com.transferz.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private static final String DEFAULT_MESSAGE_CODE = "error.default";

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String messageCode = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String errorMessage = getErrorMessage(messageCode);

        return buildApiError(errorMessage);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleDataIntegrityViolationException(ConstraintViolationException ex) {
        String messageCode = "error.".concat(ex.getConstraintName()).concat(".Constraint");
        String errorMessage = getErrorMessage(messageCode);

        return buildApiError(errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
        String messageCode = "error.".concat(ex.resource).concat(".NotFound");
        String errorMessage = getErrorMessage(messageCode);

        return buildApiError(errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ApiErrorResponse handleBadRequestException(BadRequestException ex) {
        String messageCode = "error.".concat(ex.reason);
        String errorMessage = getErrorMessage(messageCode);

        return buildApiError(errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(ConflictException.class)
    public ApiErrorResponse handleConflictException(ConflictException ex) {
        String messageCode = "error.".concat(ex.reason);
        String errorMessage = getErrorMessage(messageCode);

        return buildApiError(errorMessage);
    }

    private ApiErrorResponse buildApiError(String message) {
        return ApiErrorResponse.builder()
                .message(message)
                .build();
    }

    private String getErrorMessage(String errorCode) {
        if (errorCode == null) {
            errorCode = DEFAULT_MESSAGE_CODE;
        }

        String message = messageSource.getMessage(errorCode, null, null, LocaleContextHolder.getLocale());

        if (message == null) {
            message = messageSource.getMessage(DEFAULT_MESSAGE_CODE, null, LocaleContextHolder.getLocale());
        }

        return message;
    }
}

