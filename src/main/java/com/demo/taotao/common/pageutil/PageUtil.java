package com.demo.taotao.common.pageutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageUtil {

    //结束值
    private int maxIndex;

    //开始值
    private int startIndex;

    //第几页
    private int page;

    //每页的个数
    private int size;

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     *  分页对象
     * @param request
     * @return
     */
    public static PageUtil getPageBean(HttpServletRequest request){
        int page = 1;
        int size = 10;
        String pageIndex = request.getParameter("page");
        if(pageIndex != null && !"".equals(pageIndex )){
            page = Integer.parseInt(pageIndex);
        }
        String pageSize = request.getParameter("size");
        if(pageSize != null && !"".equals(pageSize)){
            size = Integer.parseInt(pageSize);
        }

        PageUtil util = new PageUtil();
        util.setStartIndex((page -1)* size);
        util.setMaxIndex(size);
        util.setPage(page);
        util.setSize(size);
        return util;
    }

    /**
     *  查询结果封装为json对象
     * @param list
     * @param count
     * @param page
     * @param params
     * @return
     */
    public static String getResultJsonArray(Object list,int count, int page, int ...params){
        Map<String,Object> json = new HashMap<>();
        json.put("size",list);
        int total =0;
        int size = 10;
        if(params.length > 0){
            size = params[0];
        }
        if(count % size == 0){
            total = count / size;
        }else{
            total = count / size + 1;
        }
        json.put("page",page);
        json.put("total",total);
        json.put("records",count);
        String result = JSON.toJSONString(json, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullBooleanAsFalse);
        return result;
    }

    /**
     *
     * @param list
     * @param code 返回成功:1  失败：0
     * @param msg
     * @return
     */
    public static String getResultJsonArray(Object list,int code ,String msg){
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("body",list);
        jsonMap.put("code",code);
        jsonMap.put("msg",msg);
         return JSON.toJSONString(jsonMap);
    }

    public static String getResultJsonArray(Object list){
        String jsonArray = JSON.toJSONString(list,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse);// 格式化result 
        return jsonArray;
    }

    /**
     *  将查询结果封装成json对象数组
     * @param result
     * @param count
     * @param pages
     * @return
     */
    public static String getStringJsonArray(String result,int count,int pages){
        StringBuffer sb = new StringBuffer();

        int total = 0;
        if (count % 10 == 0) {
            total = count / 10;
        } else {
            total = count / 10 + 1;
        }
        sb.append(JSON.toJSONString("page")+":"+pages+",");
        sb.append(JSON.toJSONString("total")+":"+total+",");
        sb.append(JSON.toJSONString("records")+":"+count+",");
        sb.append(JSON.toJSONString("rows")+":"+result);
        return "{"+sb.toString()+"}";
    }

    /**
     *  对象的分割 将对象以","分割后返回list对象
     * @param ids
     * @return
     */
    public static List<String> getIdsForList(String ids){
        List<String> list = new ArrayList<>();
        String [] idsString = ids.split(",");
        for(int i=0;i < idsString.length; i++){
            list.add(idsString[i]);
        }
        return list;
    }



}
