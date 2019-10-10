package com.lessons.config;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HttpClientConfig: sets up AsyncHttpClient for ElasticSearch
 */
@Configuration
public class HttpClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientConfig.class);

    @Bean
    public AsyncHttpClient asyncHttpClient()
    {
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        logger.debug("New asyncHttpClient created. Default read timeout: {}", asyncHttpClient.getConfig().getReadTimeout());
        logger.debug("New asyncHttpClient created. Default request timeout: {} ", asyncHttpClient.getConfig().getRequestTimeout());
        return asyncHttpClient;
    }

    public HttpClientConfig()
    {
        logger.debug("In HttpClientConfig constructor");
    }
}