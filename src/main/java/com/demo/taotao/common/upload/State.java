package com.demo.taotao.common.upload;

import java.util.HashMap;
import java.util.Map;

public enum  State {

    OK(200,"长传成功"),
    ERROR(500,"上传失败"),
    OVER_FILE_LIMIT(501,"超过上传大小限制"),
    NO_SUPPORT_EXTENSION(502,"不支持的扩展名");

    private int code;
    private String message;
    private State(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("code",this.code);
        map.put("message",this.message);
        return map;
    }
}
