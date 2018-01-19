package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import static ru.javawebinar.topjava.util.ValidationUtil.getErrorResponse;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

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
            String msg = "User with this email already exists";
            log.warn("{} at request  {}: {}", ErrorType.DATA_ERROR, req.getRequestURL(), msg);
            return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, msg);
        }
        if (e.toString().toLowerCase().contains("meals_unique_user_datetime_idx")) {
            String msg = "Meal with this dateTime already exists";
            log.warn("{} at request  {}: {}", ErrorType.DATA_ERROR, req.getRequestURL(), msg);
            return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, msg);
        }
           return logAndGetErrorInfo(req, e, true, ErrorType.DATA_ERROR);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo validationError(HttpServletRequest req, BindException e) {
        String msg = getErrorResponse(e.getFieldErrors());
        log.warn("{} at request  {}: {}", ErrorType.DATA_ERROR, req.getRequestURL(), msg);
        return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, msg);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo validationError(HttpServletRequest req, MethodArgumentNotValidException e) {
        String msg = getErrorResponse(e.getBindingResult().getFieldErrors());
        log.warn("{} at request  {}: {}", ErrorType.DATA_ERROR, req.getRequestURL(), msg);
        return new ErrorInfo(req.getRequestURL(), ErrorType.DATA_ERROR, msg);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), errorType, rootCause.toString());
    }
}