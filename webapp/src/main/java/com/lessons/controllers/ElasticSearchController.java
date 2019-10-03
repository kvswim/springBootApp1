package com.lessons.controllers;

import com.google.gson.Gson;
import com.lessons.models.SearchDTO;
import com.lessons.services.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import javax.annotation.Resource;
import java.util.Map;

@Controller("com.lessons.controllers.ElasticSearchController")
public class ElasticSearchController {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchController.class);
    private Gson gson = new Gson();

    @Resource
    private ElasticSearchService elasticSearchService;



//    @RequestMapping(value = "/api/elasticsearch/search", method = RequestMethod.POST, produces = "application/json")
//    public ResponseEntity<?> runESQuery(@RequestParam(name="elasticQuery", required = false) String elasticQuery) throws Exception
//    {
//        String jsonResponse = elasticSearchService.runStringSearch(elasticQuery);
//        return ResponseEntity
//                .status(HttpStatus.I_AM_A_TEAPOT)
//                .body(jsonResponse);
//    }

    @RequestMapping(value = "/api/search", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runSearch(@RequestBody SearchDTO searchDTO) throws Exception
    {
        //null check
        if(searchDTO.getRawQuery() == null) {
            throw new RuntimeException("rawQuery was empty in the DTO. Don't do that.");
        }

        String jsonResponse = elasticSearchService.runElasticQueryWithDTO(searchDTO);

        if(checkForESErrors(jsonResponse))
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(jsonResponse);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jsonResponse);
    }

    private boolean checkForESErrors(String json) {
        Map<String, Object> esResponseMap = gson.fromJson(json, Map.class);
        return esResponseMap.containsKey("error");
    }
}
