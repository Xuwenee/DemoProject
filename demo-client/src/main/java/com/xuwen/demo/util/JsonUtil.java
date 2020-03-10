package com.xuwen.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 工具类
 *
 * @author xuwen
 * @version 1.0
 * @date 2017-11-21 17:02
 **/
public class JsonUtil {

    public static Map objectToMap(Object object) {
        try {
            //转换器
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //对象-->json
            String json = mapper.writeValueAsString(object);
            //json-->map
            Map map = mapper.readValue(json, Map.class);
            return map;
        } catch (Exception e) {
            //异常捕捉返回null即转换失败
            e.printStackTrace();
            return null;
        }

    }

    public static Object mapToObject(Map map) {
        try {
            //转换器
            ObjectMapper mapper = new ObjectMapper();
            //map-->json
            String json = mapper.writeValueAsString(map);
            //json-->object
            Object object = mapper.readValue(json, Object.class);
            return object;
        } catch (Exception e) {
            //异常捕捉返回null即转换失败
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonStringToObject(String json, Class<T> tClass) {
        try {
            //转换器
            ObjectMapper mapper = new ObjectMapper();
            //忽略不明属性
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //json-->object
            T t = mapper.readValue(json, tClass);
            return t;
        } catch (Exception e) {
            //异常捕捉返回null即转换失败
            e.printStackTrace();
            return null;
        }
    }

    public static String objectToJsonString(Object object) {
        try {
            //转换器
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //对象-->json
            String json=mapper.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            //异常捕捉返回null即转换失败
            e.printStackTrace();
            return null;
        }
    }
}
