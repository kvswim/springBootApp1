package com.lessons;

import com.lessons.services.HealthCheckService;
import com.lessons.utils.SpringAppContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * Main Application
 * http://localhost:8080/app1/api/dashboard/time
 * http://localhost:8080/app1
 **/
@SpringBootApplication
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Resource
    private DataSource dataSource;

    /**
     * Web Application Starts Here
     **/
    public static void main(String[] args) throws Exception {
        logger.debug("main() started.");

        // Start up Spring Boot
        SpringApplication.run(App.class, args);

        HealthCheckService hcs = (HealthCheckService) SpringAppContextUtils.getBean("com.lessons.services.HealthCheckService");
        if(!hcs.doAllElasticSearchMappingsExist())
        {
            logger.debug("Health check failed! Webapp is NOT up.");
            System.exit(666);
        }

        logger.debug("WebApp is Up.");
    }
}