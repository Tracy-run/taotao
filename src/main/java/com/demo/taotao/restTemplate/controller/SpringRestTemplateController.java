package com.demo.taotao.restTemplate.controller;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

public class SpringRestTemplateController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/xxx")
    public String getJson(){
        String url = "http://localhost:8080/web/xxx";
        //String json =restTemplate.getForObject(url,Object.class);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET,null,String.class);
        String json = result.getBody();
        return json;
    }


    @PostMapping(value = "/xxx")
    public Object testPost(){
        String url = "";
        JSONObject postData = new JSONObject();
        postData.put("descp","request for post");
        JSONObject json = restTemplate.postForEntity(url,postData,JSONObject.class).getBody();
        return json;
    }






}
