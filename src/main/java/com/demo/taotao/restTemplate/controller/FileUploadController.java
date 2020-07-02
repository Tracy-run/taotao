package com.demo.taotao.restTemplate.controller;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class FileUploadController {


    @Test
    public void fileUploadTransferFile(){
        final String filePath = "F:";
        final String fileName = "test.txt";
        final String url = "http://localhost:8080/file/upload";

        RestTemplate restTemplate = new RestTemplate();

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        //设置请求体，注意是linkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(filePath+"/"+fileName);
        MultiValueMap<String,Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("filename",fileName);

        //用httpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String,Object>> files = new HttpEntity<>(form, headers);

        String str = restTemplate.postForObject(url,files,String.class);
        System.out.println(str);
    }


}
