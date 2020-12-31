package ru.viterg.restavote.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.viterg.restavote.util.ValidationUtil;
import ru.viterg.restavote.util.exception.ApplicationException;
import ru.viterg.restavote.util.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetExceptionView(req, e, false, ErrorType.WRONG_REQUEST, null);
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView updateRestrictionException(HttpServletRequest req, ApplicationException appEx) {
        return logAndGetExceptionView(req, appEx, false, appEx.getType(), appEx.getMsgCode());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("Exception at request " + req.getRequestURL(), e);
        return logAndGetExceptionView(req, e, true, ErrorType.APP_ERROR, null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String code) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logException, errorType);

        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause, "message", code != null ? code : ValidationUtil.getMessage(rootCause),
                        "typeMessage", errorType.getErrorCode(),
                        "status", errorType.getStatus()));
        mav.setStatus(errorType.getStatus());
        return mav;
    }
}
