package com.demo.taotao.common.objectParse;

import net.sf.json.JSONArray;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 解析类方法
 */
public class ObjectParse {

    private final static Logger logger = LoggerFactory.getLogger(ObjectParse.class);


    /**
     * xml 文件解析为json
     * @param xmlText
     * @return
     */
    private JSONArray parseXML2JsonObject(String xmlText){
        Document doc = null;
        try{
            if("".equals(xmlText) || null == xmlText){
                return null;
            }
            doc = DocumentHelper.parseText(xmlText);

            Element rootElt = doc.getRootElement();

            String result = rootElt.getText();

            JSONArray array = convertString2JsonArray(result);

            return array;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("xml解析失败！");
        }

        return null;
    }


    /**
     * 将字符串转化为json对象
     * @param jsonStr
     * @return
     */
    private static JSONArray convertString2JsonArray(String jsonStr){
        if(jsonStr == null){
            return null;
        }
        // JSONArray array = JSONArray.parseArray(jsonStr);//jsonfast的解析
        JSONArray array = net.sf.json.JSONArray.fromObject(jsonStr);
        return array;
    }

}
