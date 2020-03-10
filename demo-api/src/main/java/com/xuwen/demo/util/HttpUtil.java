//package com.autohome.datacentersso.util;
//
//import net.sf.json.JSONObject;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.commons.net.util.Base64;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @Description:
// * @author: XuWen
// * @Date: 2018/4/10 10:36
// */
//public class HttpUtil {
//
//    private static final Log logger = LogFactory.getLog(HttpUtil.class);
//
//
//    private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
//    // 连接超时时间
//    private static int connectionTimeOut = 15000;
//    private static int socketTimeOut = 15000;
//    private static int readTimeOut = 20000;
//    private static int maxConnectionPerHost = 5;
//    private static int maxTotalConnections = 40;
//
//    // 标志初始化是否完成的flag
//    private static boolean initialed = false;
//
//    // 初始化ConnectionManger的方法
//    public static void SetPara() {
//        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
//        connectionManager.getParams().setSoTimeout(socketTimeOut);
//        connectionManager.getParams().setDefaultMaxConnectionsPerHost( maxConnectionPerHost);
//        connectionManager.getParams().setMaxTotalConnections( maxTotalConnections);
//        initialed = true;
//    }
//
//
//    /**
//     * 发送post请求
//     *
//     * @param url     请求的url
//     * @param param   请求的参数
//     * @param timeout 超时时间，毫秒
//     * @return
//     */
//    public static String sendPost(String url, Map<String, String> param, Integer timeout) {
//
//        //将Map类型的参数转换成NameValuePair数组
//        Set<String> keys = param.keySet();
//        NameValuePair[] data = new NameValuePair[keys.size()];
//        int idx = 0;
//        for (String key : keys) {
//            String value = param.get(key);
//            NameValuePair item = new NameValuePair(key, value);
//            data[idx] = item;
//            idx++;
//        }
//
//        StringBuilder contentBuffer = new StringBuilder();
//        PostMethod postMethod = null;
//        try {
//            HttpClient httpClient = new HttpClient();
//
//            //设置超时时间
//            if (null != timeout) {
//                httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
//            }
//
//            postMethod = new PostMethod(url);
//            //设置编码
//            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
//
//            //将值放入postMethod中
//            postMethod.setRequestBody(data);
//            //执行postMethod
//            int statusCode = httpClient.executeMethod(postMethod);
//            if (statusCode == HttpStatus.SC_OK) {
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(postMethod.getResponseBodyAsStream(),"UTF-8"));
//                String  line;
//                while ((line = br.readLine()) != null) {
//                    contentBuffer.append(line);
//                }
//                br.close();
//            } else{
//                logger.error("HTTP请求失败！返回状态为：" + statusCode);
//            }
//        } catch (Exception e) {
//            logger.error("HTTP POST请求失败！", e);
//        } finally {
//            if (postMethod != null) {
//                postMethod.releaseConnection();
//            }
//        }
//
//        return contentBuffer.toString();
//    }
//
//
//    /**
//     * 发送post请求
//     *
//     * @param url   请求的url
//     * @param param 请求的参数
//     * @return
//     */
//    public static String sendPost(String url, Map<String, String> param) {
//        return sendPost(url, param, 150000);
//    }
//
//    /**
//     * 发送post请求
//     *
//     * @param url   请求的url
//     * @param jsonobject 请求的参数
//     * @return
//     */
//    public static String sendPostForJson(String url, JSONObject jsonobject) {
//        return sendPostForJson(url, jsonobject, 150000);
//    }
//
//
//    /**
//     * 发送post请求
//     *
//     * @param url     请求的url
//     * @param jsonobject   请求的参数
//     * @param timeout 超时时间，毫秒
//     * @return
//     */
//    public static String sendPostForJson(String url,  JSONObject jsonobject, Integer timeout) {
//
//        StringBuilder contentBuffer = new StringBuilder();
//        PostMethod postMethod = null;
//        try {
//            HttpClient httpClient = new HttpClient();
//
//            //设置超时时间
//            if (null != timeout) {
//                httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
//            }
//
//            postMethod = new PostMethod(url);
//            //设置编码
//            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
//
//            //将值放入postMethod中
//            postMethod.setRequestBody(jsonobject.toString());
//            postMethod.setRequestHeader("Accept", "application/json");
//            if(jsonobject.containsKey("Authorization") ){
//                postMethod.setRequestHeader("content-type","application/json");
//                byte[] authEncBytes = Base64.encodeBase64(jsonobject.get("Authorization").toString().getBytes());
//                String authStringEnc = "Basic " + new String(authEncBytes);
//                postMethod.setRequestHeader("Authorization", authStringEnc);
//            }
//
//            //执行postMethod
//            int statusCode = httpClient.executeMethod(postMethod);
//            if (statusCode == HttpStatus.SC_OK) {
//                BufferedReader br = new BufferedReader(
//                        new InputStreamReader(postMethod.getResponseBodyAsStream(),"UTF-8"));
//                String  line;
//                while ((line = br.readLine()) != null) {
//                    contentBuffer.append(line);
//                }
//                br.close();
//            } else{
//                logger.error("HTTP请求失败！返回状态为：" + statusCode);
//            }
//        } catch (Exception e) {
//            logger.error("HTTP POST请求失败！", e);
//        } finally {
//            if (postMethod != null) {
//                postMethod.releaseConnection();
//            }
//        }
//
//        return contentBuffer.toString();
//    }
//
//
//    /**
//     * 提交GET方法
//     * @param url  目的地址
//     * @return String 返回结果
//     */
//    public static String sendGet(String url) {
//        GetMethod getMethod = new GetMethod(url);
//        StringBuffer contentBuffer = commonGet(getMethod);
//        return contentBuffer.toString();
//    }
//
//
//    /**
//     * 提交GET方法
//     * @param url  目的地址
//     * @param auths  验证权限
//     * @return String 返回结果
//     */
//    public static String sendGetAndAuth(String url, String auths) {
//        GetMethod getMethod = new GetMethod(url);
//        byte[] authEncBytes = Base64.encodeBase64(auths.getBytes());
//        String authStringEnc = "Basic " + new String(authEncBytes);
//        getMethod.setRequestHeader("Authorization", authStringEnc);
//        getMethod.setRequestHeader("content-type","application/json");
//        StringBuffer contentBuffer = commonGet(getMethod);
//        return contentBuffer.toString();
//    }
//
//    private static StringBuffer commonGet(GetMethod getMethod){
//        HttpClient client = new HttpClient(connectionManager);
//        if (!initialed) {
//            SetPara();
//        }
//        getMethod.getParams().setSoTimeout(readTimeOut);
//
//        StringBuffer contentBuffer = new StringBuffer();
//        try {
//            int statusCode = client.executeMethod(getMethod);
//            if (statusCode == HttpStatus.SC_OK) {
//                InputStream in = getMethod.getResponseBodyAsStream();
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(in,
//                                getMethod.getResponseCharSet()));
//                String inputLine = null;
//                while ((inputLine = reader.readLine()) != null) {
//                    contentBuffer.append(inputLine);
//                }
//                in.close();
//
//            } else {
//                logger.error("get method erro and result :  " + getMethod.getStatusLine());
//            }
//
//        } catch (IOException e) {
//            logger.error("get method erro and the error message:" + e.getMessage());
//        } finally {
//            getMethod.releaseConnection();
//        }
//        return contentBuffer;
//    }
//
//    public static String postForJsonString(String url, String paramsJsonString) {
//        PrintWriter printWriter = null;
//        HttpURLConnection con = null;
//
//        BufferedReader in = null;
//        try {
//                URL obj = new URL(url);
//                con = (HttpURLConnection) obj.openConnection();
//                con.setRequestMethod("POST");
//
//                con.setRequestProperty("Content-Type", "application/json");
//                con.setRequestProperty("connection", "Keep-Alive");
//                con.setRequestProperty("Content-Length", String.valueOf(paramsJsonString.length()));
//
//                // 设置连接主机超时
//                con.setConnectTimeout(connectionTimeOut);
//                // 设置从主机读取数据超时
//                con.setReadTimeout(readTimeOut);
//
//                // 发送 post 必须
//                con.setDoOutput(true);
//                con.setDoInput(true);
//
//
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "utf-8"));
//                bw.write(paramsJsonString);
//                bw.flush();
//                bw.close();
//
//                // 根据ResponseCode判断连接是否成功
//                int responseCode = con.getResponseCode();
//
//                in = new BufferedReader(new InputStreamReader(responseCode == 200 ? con.getInputStream() : con.getErrorStream(), "utf-8"));
//
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//                response.append("\r\n");
//            }
//            in.close();
//
//            return response.toString();
//
//
//            } catch (Exception e) {
//                return e.getMessage();
//            } finally {
//                if (con != null) {
//                    con.disconnect();
//            }
//
//            if (printWriter != null) {
//                printWriter.close();
//            }
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//}
//
