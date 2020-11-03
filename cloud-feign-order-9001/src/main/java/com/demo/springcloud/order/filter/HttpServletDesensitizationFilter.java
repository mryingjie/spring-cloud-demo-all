package com.demo.springcloud.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.demo.springcloud.commons.entities.Payment;
import com.demo.springcloud.commons.entities.TestField;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * created by Yingjie Zheng at 2020/5/7 20:40
 */

@Slf4j
public class HttpServletDesensitizationFilter implements Filter {

    private Set<String> fields;



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String fields = filterConfig.getInitParameter("field");
        if(fields != null){
            String[] split = fields.split(",");
            this.fields = new HashSet<>(Arrays.asList(split));
        }

        // WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        // if (webApplicationContext != null) {
        //     cookieService = webApplicationContext.getBean(CookieService.class);
        // }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            //包装request和response 拦截数据
            JsonTypeHttpServletRequestWrapper requestWrapper = new JsonTypeHttpServletRequestWrapper((HttpServletRequest) request, fields);
            JsonTypeHttpServletResponseWrapper responseWrapper = new JsonTypeHttpServletResponseWrapper((HttpServletResponse) response);
            chain.doFilter(requestWrapper, responseWrapper);
            //对返回数据进行脱敏处理
            byte[] result = replaceResponse( responseWrapper);
            //把返回值输出到客户端
            if (result != null) {
                ServletOutputStream out = response.getOutputStream();
                out.write(result);
                out.flush();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private byte[] replaceResponse(JsonTypeHttpServletResponseWrapper responseWrapper) throws IOException {
        //获取返回值
        byte[] content = responseWrapper.getContent();
        byte[] resultContent = null;
        if (content != null && content.length > 0) {
            String str = new String(content, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(str);
            Map<String, Object> param = genParam(jsonObject, fields);
            //对response的敏感数据进行加密脱敏
            if (MapUtils.isNotEmpty(param)) {
                log.info("检测到需要脱敏的数据:{} ", param);
                Map<String, Object> result = mask(param);
                replace(jsonObject, result);
                String resultStr = jsonObject.toJSONString();
                log.info("原返回值:{} 脱敏后的返回值:{}", str, resultStr);
                resultContent = resultStr.getBytes(StandardCharsets.UTF_8);
            }else {
                resultContent = content;
            }
        }
        return resultContent;
    }



    /**
     * 解密
     */
    private Map<String, Object> restore(@NotNull Map<String, Object> param) {

        //TODO 调用解密api
        Map<String, Object> desensitization = DesensitizationUtil.desensitization(param);
        return desensitization;
    }

    /**
     * 加密脱敏
     */
    private Map<String, Object> mask(@NotNull Map<String, Object> param) {
        //TODO 调用加密api
        Map<String, Object> desensitization = DesensitizationUtil.desensitization(param);

        return desensitization;
    }


    private class JsonTypeHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] buffer;

        public JsonTypeHttpServletRequestWrapper(HttpServletRequest request, Set<String> fields) {
            super(request);
            String sessionStream = getBodyString(request);
            JSONObject jsonObject = JSON.parseObject(sessionStream);
            Map<String, Object> param = genParam(jsonObject, fields);
            if(MapUtils.isNotEmpty(param)){
                log.info("检测到需要解密的参数:{}", param);
                // 对参数的脱敏数据进行解密
                Map<String, Object> result = restore(param);
                // 请求参数替换明文
                replace(jsonObject, result);
                String jsonString = jsonObject.toJSONString();
                log.info(" 原请求参数:{}  解密后的参数：{}", sessionStream, jsonObject.toJSONString());
                buffer = jsonString.getBytes(StandardCharsets.UTF_8);
            }else {
                buffer = sessionStream.getBytes(StandardCharsets.UTF_8);
            }

        }

        /**
         * 获取请求Body
         */
        public String getBodyString( ServletRequest request) {
            StringBuilder sb = new StringBuilder();
            try(InputStream inputStream  =  cloneInputStream(request.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
            ) {
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                log.error("获取请求体流数据异常",e);
            }
            return sb.toString();

        }


        /**
         * 复制输入流
         *
         * @param inputStream
         * @return</br>
         */
        public InputStream cloneInputStream(ServletInputStream inputStream) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(buffer)) > -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byteArrayOutputStream.flush();
            } catch (IOException e) {
                log.error("复制请求体流数据异常",e);
            }
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {

            final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

            return new ServletInputStream() {

                @Override
                public int read() throws IOException {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }
    }


    private class JsonTypeHttpServletResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayOutputStream buffer;
        private ServletOutputStream out;


        public JsonTypeHttpServletResponseWrapper(HttpServletResponse response) {
            super(response);
            buffer = new ByteArrayOutputStream();
            out = new WrapperOutputStream(buffer);
        }


        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return out;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (out != null) {
                out.flush();
            }
        }

        public byte[] getContent() throws IOException {
            flushBuffer();
            return buffer.toByteArray();
        }

        private class WrapperOutputStream extends ServletOutputStream {

            private ByteArrayOutputStream bos;

            public WrapperOutputStream(ByteArrayOutputStream bos) {
                this.bos = bos;
            }

            @Override
            public void write(int b) {
                bos.write(b);
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }
        }

    }

    public static void main(String[] args) {
        List<Payment> objects = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            Payment payment = new Payment();
            payment.setId((long) i);
            payment.setSerial(i+"");
            payment.setIdList(Arrays.asList((long)i+10,(long)i+11,(long)i+12));
            payment.setSerialList(Arrays.asList(i+10+"",i+11+"",i+12+""));
            payment.setName("sasaa");
            TestField testField = new TestField();
            testField.setId((long) (i+100));
            testField.setSerial(i+100+"");
            testField.setIdList(Arrays.asList((long)i+100,(long)i+101,(long)i+102));
            testField.setSerialList(Arrays.asList(i+100+"",i+101+"",i+102+""));
            testField.setName("abjac");


            payment.setTestField(testField);


            objects.add(payment);

        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("data", objects);
        System.out.println(JSON.toJSONString(result));
    }


    public static Map<String, Object> genParam(JSONObject jsonObject, Set<String> fields) {
        if (jsonObject == null || CollectionUtils.isEmpty(fields)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        genParam0(jsonObject, map, fields, null, false);
        return map;
    }

    public static void genParam0(JSONObject jsonObject, Map<String, Object> map, Set<String> fields, String fieldName, boolean nextLevel) {
        if (MapUtils.isEmpty(jsonObject)){
            return;
        }
        Map<String, Object> newMap = null;
        if (nextLevel && fieldName != null) {
            newMap = new HashMap<>();
            map.put(fieldName, newMap);
        } else {
            newMap = map;
        }
        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof JSONObject) {
                genParam0((JSONObject) value, newMap, fields, key, true);
            } else {
                if (fields.contains(key)) {
                    newMap.put(key, value);
                }
            }

        }
        if(nextLevel  && newMap.size() == 0){
            map.remove(fieldName);
        }
    }


    public static void replace(JSONObject originalData, Map<String, Object> replaceMap) {
        Set<Map.Entry<String, Object>> entrySet = replaceMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (originalData.get(key) != null && originalData.get(key) instanceof JSONPObject) {
                replace((JSONObject) originalData.get(key), (Map<String, Object>) value);
            } else {
                originalData.put(key, value);
            }
        }
    }
}
