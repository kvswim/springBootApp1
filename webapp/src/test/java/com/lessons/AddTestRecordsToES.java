package com.lessons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.Gson;
import com.lessons.models.ShortReportDTO;
import com.lessons.services.ReportService;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddTestRecordsToES {
    private static final Logger logger = LoggerFactory.getLogger(AddTestRecordsToES.class);
    private Gson gson = new Gson();

    @Resource
    private AsyncHttpClient asyncHttpClient;

    @Resource
    private ReportService reportService;

    @Value("${es.url}")
    private String esUrl;

    @Test
    public void insertTestRecordsToES() throws Exception {
        logger.debug("inside test method");
        String jsonToInsert = "{\"index\" : { \"_index\" : \"reports\", \"_type\" : \"record\"}}\n" +
                "{\"priority\" : \"low\", \"actors\" : \"Some Guy\", \"description\" : \"Free HK\", \"id\": 6, \"ip\" : \"192.168.80.11\", \"created_date\" : \"1995-03-22\", \"is_alive\" : false, \"country\" : \"UNITED states\"}\n";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE); //hiss i'm a snek
//        String jsonToInsert2 = "";
//        for(int i=0; i<1000; i++)
//        {
//            jsonToInsert2 = jsonToInsert2 + jsonToInsert;
//        }

//        StringBuilder sbsAreBs = new StringBuilder();
//        for(int i=0; i<1000; i++)
//        {
//            sbsAreBs.append(jsonToInsert);
//        }
//        jsonToInsert = sbsAreBs.toString();
        List<ShortReportDTO> listOfAllReportsShort = reportService.getAllShortReports2();

        StringBuilder sb = new StringBuilder();
        for (ShortReportDTO currentReport : listOfAllReportsShort) {
            String jsonLine1 = "{\"index\" : { \"_index\" : \"reports\", \"_type\" : \"record\", \"_id\" : "+ currentReport.getId() + "}}\n";
            String jsonLine2 = objectMapper.writeValueAsString(currentReport) + "\n";
            sb.append(jsonLine1);
            sb.append(jsonLine2);
        }
        String bigJsonToInsert = sb.toString();

        Response response = asyncHttpClient.preparePost(esUrl+"/_bulk")
                .setHeader("accepts", "application/json")
                .setHeader("contentType", "application/json")
                .setBody(bigJsonToInsert)
                .setRequestTimeout(60000) //milliseconds
                .setReadTimeout(90000)
                .execute()
                .get();


        String jsonResponse = response.getResponseBody();
//        Map<String, Object> mapOfJsonResponse = gson.fromJson(jsonResponse, Map.class);
//        boolean checkIfErrorDuringInsert = (boolean) mapOfJsonResponse.get("errors");
//        assertFalse(checkIfErrorDuringInsert);
    }
}
