package com.demo.springcloud.order.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by Yingjie Zheng at 2020/5/8 16:46
 */
public class DesensitizationUtil {


    public static Map<String, Object> desensitization(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();
        if (param.get("id") != null) {
            result.put("id", Integer.parseInt(param.get("id").toString()) + 9999);
        }
        if (param.get("serial") != null) {
            result.put("serial", param.get("serial").toString() + "--");
        }

        if (param.get("idList") != null) {
            List<Integer> idList = (List<Integer>) param.get("idList");
            idList = idList.stream().map(integer -> integer + 9999).collect(Collectors.toList());
            result.put("idList", idList);
        }
        if (param.get("serialList") != null) {
            List<String> serialList = (List<String>) param.get("serialList");
            serialList = serialList.stream().map(integer -> integer + "--").collect(Collectors.toList());
            result.put("serialList", serialList);
        }

        if (param.get("testField") != null) {
            Map<String, Object> testField = (Map<String, Object>) param.get("testField");
            if (testField.get("id") != null) {
                testField.put("id", Integer.parseInt(testField.get("id").toString()) + 9999);
            }
            if (testField.get("serial") != null) {
                testField.put("serial", testField.get("serial").toString() + "--");
            }

            if (testField.get("idList") != null) {
                List<Integer> idList = (List<Integer>) testField.get("idList");
                idList = idList.stream().map(integer -> integer + 9999).collect(Collectors.toList());
                testField.put("idList", idList);
            }
            if (testField.get("serialList") != null) {
                List<String> serialList = (List<String>) testField.get("serialList");
                serialList = serialList.stream().map(integer -> integer + "--").collect(Collectors.toList());
                testField.put("serialList", serialList);
            }
            result.put("testField", testField);

        }
        return result;
    }


}
