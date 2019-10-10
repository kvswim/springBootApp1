package com.lessons.services;

import com.google.gson.Gson;
import com.lessons.App;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Class to check the status of ElasticSearch (and later Postgres) upon application startup
 */
@Service("com.lessons.services.HealthCheckService")
public class HealthCheckService {
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);
    private Gson gson = new Gson();

    @Resource
    private AsyncHttpClient asyncHttpClient;

    @Value("${es.url}")
    private String esUrl;



    public boolean doAllElasticSearchMappingsExist() throws Exception {
        logger.debug("Starting ElasticSearch health check");
        //Verify: ElasticSearch alias "reports" exists

        String jsonRequest = "";

        Response response = asyncHttpClient.prepareGet(esUrl)
                            .setHeader("accepts", "application/json")
                            .setHeader("contentType", "application/json")
                            .setBody(jsonRequest)
                            .setRequestTimeout(60000) //milliseconds
                            .setReadTimeout(90000)
                            .execute()
                            .get();
        String jsonResponse = response.getResponseBody();

        Map<String, Object> mapOfJsonResponse = gson.fromJson(jsonResponse, Map.class);
        if(mapOfJsonResponse.containsKey("error")) //if the json response map contains error that's bad
        {
            return false;
        } else {
            return true;
        }
    }
}
