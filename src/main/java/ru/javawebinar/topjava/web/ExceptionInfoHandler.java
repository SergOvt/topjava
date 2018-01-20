package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.LocaleResolver;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Supplier;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Autowired
    public ExceptionInfoHandler(LocaleResolver localeResolver, MessageSource messageSource) {
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        if (e.toString().toLowerCase().contains("users_unique_email_idx")) {
            return logAndGetDuplicateErrorInfo(req, "user.duplicate");
        }
        if (e.toString().toLowerCase().contains("meals_unique_user_datetime_idx")) {
            return logAndGetDuplicateErrorInfo(req, "meal.duplicate");
        }
        return logAndGetErrorInfo(req, e, true, ErrorType.DATA_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo validationError(HttpServletRequest req, BindException e) {
        return logAndGetValidationErrorInfo(req, e::getFieldErrors);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo validationError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetValidationErrorInfo(req, () -> e.getBindingResult().getFieldErrors());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), errorType, rootCause.toString());
    }

    private ErrorInfo logAndGetValidationErrorInfo(HttpServletRequest req, Supplier<List<FieldError>> supplier) {
        StringJoiner joiner = new StringJoiner("<br>");
        supplier.get().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (!msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    joiner.add(msg);
                });
        log.warn("{} at request  {}: {}", ErrorType.DATA_ERROR, req.getRequestURL(), joiner.toString());
        return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, joiner.toString());
    }

    private ErrorInfo logAndGetDuplicateErrorInfo(HttpServletRequest req, String code) {
        String msg = messageSource.getMessage(code, new Object[]{}, localeResolver.resolveLocale(req));
        log.warn("{} at request  {}: {}", ErrorType.DATA_ERROR, req.getRequestURL(), msg);
        return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, msg);
    }

}