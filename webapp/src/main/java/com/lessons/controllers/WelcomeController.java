package com.lessons.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller("com.lessons.controllers.WelcomeController")
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);


    /**********************************************************************
     * showDefaultPage()
     ***********************************************************************/
    @RequestMapping("/")
    public String showDefaultPage()
    {
        logger.debug("showDefaultPage() started");

        // Forward the user to the main page
        return "forward:/app.html";
    }

    @RequestMapping(value="/api/bad", method= RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> throwException() {
        logger.debug("throwException() started.");

        //exception is caught by GenericExceptionHandler
        int i=1;
        if(i==1){
            throw new RuntimeException("critical error, please throw server out window");
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }
}