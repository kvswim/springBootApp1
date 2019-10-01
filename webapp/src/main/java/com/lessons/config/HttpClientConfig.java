package com.lessons.config;

import com.ning.http.client.AsyncHttpClient;
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
        return new AsyncHttpClient();
    }

    public HttpClientConfig()
    {
        logger.debug("In HttpClientConfig constructor");
    }
}