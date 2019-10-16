package com.lessons;

import com.fasterxml.jackson.core.type.TypeReference;
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

        if(response.getStatusCode() == 200)
        {
            String jsonResponse = response.getResponseBody();
            logger.debug("jsonResponse is {} characters long", jsonResponse.length());

            long start = System.currentTimeMillis();
            String[] jsonResponseArray = jsonResponse.split("\"items\"");
            String onlyStuffWeCareAbout = jsonResponseArray[0];
            onlyStuffWeCareAbout = onlyStuffWeCareAbout.substring(0, onlyStuffWeCareAbout.length()-1);
            onlyStuffWeCareAbout = onlyStuffWeCareAbout + "}";
            TypeReference tr = new TypeReference<Map<String,Object>>(){};
            Map<String, Object> mapOfJsonResponse = objectMapper.readValue(onlyStuffWeCareAbout, tr);
            long end = System.currentTimeMillis();
            logger.debug("Conversion took: {} ms", end - start);
            int stop = 1;
//        boolean checkIfErrorDuringInsert = (boolean) mapOfJsonResponse.get("errors");
//        assertFalse(checkIfErrorDuringInsert);
//            TypeReference tr = new TypeReference<Map<String,Object>>(){};
//            long start = System.currentTimeMillis();
//            DTOTest mapOfJsonResponse = objectMapper.readValue(jsonResponse, DTOTest.class);
//            long end = System.currentTimeMillis();
//            logger.debug("Jackson map conversion took: {} ms", end-start);
//            start = System.currentTimeMillis();
//            Map<String, Object> mapOfJsonResponseGson = gson.fromJson(jsonResponse, Map.class);
//            end = System.currentTimeMillis();
//            logger.debug("Gson map conversion took: {} ms", end - start);
            //Boolean checkIfErrorDuringInsert = (Boolean) mapOfJsonResponse.get("errors");
//            if(checkIfErrorDuringInsert) {
//                logger.error("There were errors during the ElasticSearch insert.");
//                throw new RuntimeException("Deal with it.");
            }
//        } else {
//            throw new RuntimeException("Deal with it.");
//        }

    }
}
