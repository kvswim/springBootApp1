package com.lessons.services;

import com.lessons.models.SearchDTO;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class ElasticSearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    @Resource
    private AsyncHttpClient asyncHttpClient;

    @Value("${es.url}")
    private String esUrl;

    public void testHttpClientConfig() {
        logger.debug("Inside testHttpClientConfig");
    }

//    /**
//     * Outgoing call to ElasticSearch
//     * @param rawQuery
//     * @return
//     * @throws Exception
//     */
//    public String runStringSearch(String rawQuery) throws Exception
//    {
//        String result = "";
//        Response response = asyncHttpClient.preparePost(esUrl + "/reports/_search?pretty=true?q=" + rawQuery)
//                .setHeader("accepts", "application/json")
//                .setHeader("contentType", "application/json")
//                .execute()
//                .get();
//        result = response.getResponseBody();
//
//        if(response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
//            throw new RuntimeException("The response from ElasticSearchService.runStringSearch was not in the 200 series.");
//        }
//        return result;
//    }

    public String runElasticQueryWithDTO(SearchDTO searchDTO) throws Exception {
        String result = "";
        String url = esUrl + "/reports/_search?pretty=true";
        String jsonRequest = constructJsonRequest(searchDTO);

        Response response = asyncHttpClient.preparePost(url)
                                            .setHeader("accepts", "application/json")
                                            .setHeader("contentType", "application/json")
                                            .setBody(jsonRequest)
                                            .execute()
                                            .get();
        result = response.getResponseBody();

        return result;
    }

    public String constructJsonRequest(SearchDTO searchDTO)
    {
        String filters = searchDTO.getFilters().get(0); //single case only
        filters = filters.replace(":", "\":\"");
        filters = "\"" + filters + "\"";

        String jsonRequest = "{\n" +
                "   \"query\":{\n" +
                "       \"bool\":{\n" +
                "           \"must\": [\n" +
                "               {\n" +
                "                 \"query_string\": {\n" +
                "                  \"query\":\"" + searchDTO.getRawQuery() +
                "               \"}\n" +
                "               }\n" +
                "               ],\n" +
                "                               \"filter\":[\n" +
                "                                {\"term\":{\n" +
                                                    filters +
                "                                }}\n" +
                "                               ]\n" +
                "       }\n" +
                "   }\n" +
                "}";
        return jsonRequest;
    }
}
