package com.lessons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GenericExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Value("${development.mode}")
    private Boolean developmentMode;

    /**
     * @param exception See notes from 08-21-19
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception exception)
    {
        //logger.error("Something catastrophic occurred.", exception);

        //Gives the URL of the rest endpoint that threw the error
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (null != request) {
            logger.error("This request to " + request.getRequestURI() + " raised an exception.", exception);
        } else {
            logger.error("Exception raised.", exception);
        }

        if(developmentMode)
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(exception.getLocalizedMessage());
        }
        else
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Something went wrong. Please contact your sysadmin.");
        }
    }

    public GenericExceptionHandler()
    {
        logger.debug("Inside constructor");
    }

    @PostConstruct
    public void GenericExceptionHandlerPostConstruct()
    {
        logger.debug("Inside postconstruct");
    }
}