package com.xuwen.demo.util;

import com.xuwen.demo.util.file.FileUtil;
import com.xuwen.demo.util.file.IoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发送http请求工具类
 *
 * @author xuwen
 * @version 1.0
 * @date 2018-01-17 11:52
 **/

public class HttpSendUtils {
    private static int SOCKET_TIMEOUT;
    private static int CONNECT_TIMEOUT;
    private static int connectionRequestTimeout;
    public void setSocketTimeout(int socketTimeout) {
        HttpSendUtils.SOCKET_TIMEOUT = socketTimeout;
    }
    public void setConnectTimeout(int connectTimeout) {
        HttpSendUtils.CONNECT_TIMEOUT = connectTimeout;
    }
    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        HttpSendUtils.connectionRequestTimeout = connectionRequestTimeout;
    }
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
            .setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(connectionRequestTimeout).build();
    private static StringEntity getStringEntity(String contentStr, String contentType) {
        if (StringUtils.isBlank(contentStr)) {
            contentStr = "";
        }
        StringEntity stringEntity = new StringEntity(contentStr, "UTF-8");
        if (contentType == null) {
            stringEntity.setContentType("application/x-www-form-urlencoded");
        } else {
            stringEntity.setContentType(contentType);
        }
        return stringEntity;
    }
    private static UrlEncodedFormEntity getUrlEncodedFormEntity(Map<String, String> params)
            throws UnsupportedEncodingException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null) {
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
            }
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
        urlEncodedFormEntity.setContentType("application/x-www-form-urlencoded");
        return urlEncodedFormEntity;
    }
    private static void setHeader(HttpRequestBase requestBase, Map<String, String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBase.addHeader(key, headers.get(key));
            }
        }
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月29日 下午9:25:09
     * @version 1.0
     * @description 发送HTTP/HTTPS请求
     *
     * @param sslConnectionSocketFactory
     * @param httpRequestBase
     * @return
     * @throws FileNotFoundException
     */
    public static HttpResponse sendHttp(SSLConnectionSocketFactory sslConnectionSocketFactory,
                                        HttpRequestBase httpRequestBase) {
        httpRequestBase.setConfig(requestConfig);
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        HttpEntity httpEntity = null;
        String responseContent = null;
        int status = 0;
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        if (sslConnectionSocketFactory != null) {
            httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        }
        closeableHttpClient = httpClientBuilder.build();
        try {
            closeableHttpResponse = closeableHttpClient.execute(httpRequestBase);
            httpEntity = closeableHttpResponse.getEntity();
            responseContent = EntityUtils.toString(httpEntity, "UTF-8");
            status = closeableHttpResponse.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatus(status);
        httpResponse.setContent(responseContent);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年1月16日 上午10:55:34
     * @version 1.0
     * @description 发送HTTP请求的核心方法
     *
     * @param httpPost
     * @return
     */
    public static HttpResponse sendHttp(HttpRequestBase httpRequestBase) {
        HttpResponse httpResponse = sendHttp(null, httpRequestBase);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月21日 下午6:36:47
     * @version 1.0
     * @description 下载资源，下载失败返回null
     *
     * @param requestBase
     * @param fileFullName
     *            文件全路径
     * @param overwrite
     *            是否覆盖
     * @return
     */
    public static File download(HttpRequestBase requestBase, String fileFullName, boolean overwrite) {
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        HttpEntity httpEntity = null;
        int status = 0;
        File file = null;
        closeableHttpClient = HttpClients.createDefault();
        requestBase.setConfig(requestConfig);
        try {
            closeableHttpResponse = closeableHttpClient.execute(requestBase);
            httpEntity = closeableHttpResponse.getEntity();
            status = closeableHttpResponse.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                InputStream inputStream = httpEntity.getContent();
                if (inputStream != null) {
                    try {
                        file = FileUtil.getFileIsHisAndCreate(fileFullName, overwrite, overwrite);
                        boolean result = IoUtil.inputStreamToFile(inputStream, file, true);
                        if (result) {
                            return file;
                        }
                        if (file != null) {
                            file.deleteOnExit();
                        }
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (file != null) {
                            file.deleteOnExit();
                        }
                    }
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月21日 下午6:35:49
     * @version 1.0
     * @description 下载文件
     *
     * @param httpUrl
     *            文件的url
     * @param fileFullName
     *            保存的文件
     * @param overwrite
     *            是否覆盖
     * @return
     */
    public static File download(String httpUrl, String fileFullName, boolean overwrite) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return download(httpGet, fileFullName, overwrite);
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月21日 下午6:59:39
     * @version 1.0
     * @description 下载文件
     *
     * @param httpUrl
     * @param file
     * @param overwrite
     * @return
     */
    public static File download(String httpUrl, File file, boolean overwrite) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return download(httpGet, file.getPath(), overwrite);
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月22日 上午11:00:53
     * @version 1.0
     * @description 上传文件
     *
     * @param httpUrl
     *            url
     * @param file
     *            file
     * @param headers
     *            需要添加的header集合
     * @param textParams
     *            需要添加的text集合
     * @return
     */
    public static HttpResponse upload(String httpUrl, File file, Map<String, String> headers,
                                      Map<String, String> textParams) {
        HttpPost httpPost = new HttpPost(httpUrl);
        setHeader(httpPost, headers);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file);
        if (textParams != null) {
            for (String key : textParams.keySet()) {
                builder.addTextBody(key, textParams.get(key));
            }
        }
        HttpEntity httpEntity = builder.build();
        httpPost.setEntity(httpEntity);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpPost);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年1月16日 上午11:00:40
     * @version 1.0
     * @description 发送post请求
     *
     * @param httpUrl
     * @param params
     *            参数(格式:key1=value1&key2=value2)
     * @return
     */
    public static HttpResponse sendHttpPost(String httpUrl, String contentStr, String contentType) {
        return sendHttpPost(httpUrl,null,contentStr,contentType);
    }

    public static HttpResponse sendHttpPost(String httpUrl, Map<String, String> headers,String contentStr, String contentType) {
        HttpPost httpPost = new HttpPost(httpUrl);
        setHeader(httpPost, headers);
        StringEntity stringEntity = getStringEntity(contentStr, contentType);
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpPost);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月22日 上午11:43:40
     * @version 1.0
     * @description 发送post请求
     *
     * @param httpUrl
     * @param headers
     *            设置的header的map
     * @param params
     *            传递的参数的map
     * @return
     */
    public static HttpResponse sendHttpPost(String httpUrl, Map<String, String> headers, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        setHeader(httpPost, headers);
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = getUrlEncodedFormEntity(params);
            httpPost.setEntity(urlEncodedFormEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpPost);
        return httpResponse;
    }

    /**
     *
     * @author 许稳
     * @date 2017年1月16日 上午11:21:34
     * @version 1.0
     * @description 发送post请求
     *
     * @param httpUrl
     * @param params
     *            为一个map对象
     * @return
     */
    public static HttpResponse sendHttpPost(String httpUrl, Map<String, String> params) {
        return sendHttpPost(httpUrl, null, params);
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月21日 下午3:24:04
     * @version 1.0
     * @description 参数格式，json
     *
     * @param httpUrl
     * @param contentStr
     * @return
     */
    public static HttpResponse sendHttpPostWithJson(String httpUrl, String contentJsonStr) {
        return sendHttpPost(httpUrl, contentJsonStr, "application/json");
    }

    /**
     *
     * @author 许稳
     * @date 2017年4月25日 下午5:59:56
     * @version 1.0
     * @description 参数格式，json，header
     *
     * @param httpUrl
     * @param contentJsonStr
     * @param headers
     * @return
     */
    public static HttpResponse sendHttpPostWithJson(String httpUrl, Map<String, String> headers,String contentJsonStr) {
//		return sendHttpPost(httpUrl, contentJsonStr, "application/json");
        return sendHttpPost(httpUrl, headers,contentJsonStr, "application/json");
    }

    /**
     *
     * @author 许稳
     * @date 2017年1月22日 下午12:04:57
     * @version 1.0
     * @description 发送带参数的delete请求
     *
     * @param httpUrl
     * @param params
     * @return
     */
    public static HttpResponse sendHttpDelete(String httpUrl, Map<String, String> params) {
        if (params != null) {
            UrlEncodedFormEntity urlEncodedFormEntity = null;
            String paramsStr = null;
            try {
                urlEncodedFormEntity = getUrlEncodedFormEntity(params);
                paramsStr = EntityUtils.toString(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpUrl.indexOf("?") < 0) {
                httpUrl = httpUrl + "?" + paramsStr;
            } else {
                httpUrl = httpUrl + "&" + paramsStr;
            }
        }
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpDelete);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年1月22日 下午12:06:04
     * @version 1.0
     * @description 发送不带参数的delete请求
     *
     * @param httpUrl
     * @return
     */
    public static HttpResponse sendHttpDelete(String httpUrl) {
        return sendHttpDelete(httpUrl, null);
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月22日 上午11:58:14
     * @version 1.0
     * @description 发送get请求
     *
     * @param httpUrl
     * @param headers
     * @param params
     * @return
     */
    public static HttpResponse sendHttpGet(String httpUrl, Map<String, String> headers, Map<String, String> params) {
        // todo: extract to func
        if (params != null) {
            UrlEncodedFormEntity urlEncodedFormEntity = null;
            String paramsStr = null;
            try {
                urlEncodedFormEntity = getUrlEncodedFormEntity(params);
                paramsStr = EntityUtils.toString(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpUrl.indexOf("?") < 0) {
                httpUrl = httpUrl + "?" + paramsStr;
            } else {
                httpUrl = httpUrl + "&" + paramsStr;
            }
        }
        HttpGet httpGet = new HttpGet(httpUrl);
        setHeader(httpGet, headers);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpGet);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月22日 上午11:58:14
     * @version 1.0
     * @description 发送get请求
     *
     * @param httpUrl
     * @param headers
     * @param params
     * @return
     */
    public static HttpResponse sendSslHttpGet(SSLConnectionSocketFactory sslConnectionSocketFactory, String httpUrl, Map<String, String> headers, Map<String, String> params) {
        // todo: extract to func
        if (params != null) {
            UrlEncodedFormEntity urlEncodedFormEntity = null;
            String paramsStr = null;
            try {
                urlEncodedFormEntity = getUrlEncodedFormEntity(params);
                paramsStr = EntityUtils.toString(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpUrl.indexOf("?") < 0) {
                httpUrl = httpUrl + "?" + paramsStr;
            } else {
                httpUrl = httpUrl + "&" + paramsStr;
            }
        }
        HttpGet httpGet = new HttpGet(httpUrl);
        setHeader(httpGet, headers);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(sslConnectionSocketFactory, httpGet);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月22日 上午11:58:46
     * @version 1.0
     * @description 发送get请求
     *
     * @param httpUrl
     * @param params
     * @return
     */
    public static HttpResponse sendHttpGet(String httpUrl, Map<String, String> params) {
        return sendHttpGet(httpUrl, null, params);
    }
    public static HttpResponse sendSslHttpGet(String httpUrl, Map<String, String> params) {
        TrustStrategy trustStrategy = getTrustStrategy();
        String[] supportedProtocols = new String[] { "TLSv1" };
        String[] supportedCipherSuites = null;
        HostnameVerifier hostnameVerifier = getHostnameVerifier();

        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(trustStrategy).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                supportedProtocols, supportedCipherSuites, hostnameVerifier);
        return sendSslHttpGet(sslConnectionSocketFactory, httpUrl, null, params);
    }
    public static HttpResponse sendHttpGet(String httpUrl) {
        return sendHttpGet(httpUrl, null);
    }
    public static HttpResponse sendSslHttpGet(String httpUrl) {
        return sendSslHttpGet(httpUrl, null);
    }
    public static HttpResponse sendHttpPut(String httpUrl, String paramStr, String contentType,
                                           Map<String, String> headers) {
        HttpPut httpPut = new HttpPut(httpUrl);
        setHeader(httpPut, headers);
        StringEntity stringEntity = getStringEntity(paramStr, contentType);
        httpPut.setEntity(stringEntity);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpPut);
        return httpResponse;
    }
    public static HttpResponse sendHttpPutWithJson(String httpUrl, String jsonParamStr, Map<String, String> headerMap) {
        return sendHttpPut(httpUrl, jsonParamStr, "application/json", headerMap);
    }
    public static HttpResponse sendHttpPut(String httpUrl, String paramStr, Map<String, String> headerMap) {
        return sendHttpPut(httpUrl, paramStr, null, headerMap);
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月21日 下午3:28:01
     * @version 1.0
     * @description 发送Patch请求
     *
     * @param httpUrl
     * @param contentJsonStr
     * @param contentType
     *            如果不传，则默认为application/x-www-form-urlencoded
     * @return
     */
    public static HttpResponse sendHttpPatch(String httpUrl, String contentStr, String contentType) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        StringEntity stringEntity = getStringEntity(contentStr, contentType);
        httpPatch.setEntity(stringEntity);
        HttpResponse httpResponse = HttpSendUtils.sendHttp(httpPatch);
        return httpResponse;
    }
    /**
     *
     * @author 许稳
     * @date 2017年3月21日 下午3:30:48
     * @version 1.0
     * @description 发送Patch请求，使用json的方式
     *
     * @param httpUrl
     * @param contentJsonStr
     * @return
     */
    public static HttpResponse sendHttpPatchWithJson(String httpUrl, String contentJsonStr) {
        return sendHttpPatch(httpUrl, contentJsonStr, "application/json");
    }

    public static HttpResponse sendSslHttpPostWithJson(String httpsUrl, String contentJsonStr) {
        TrustStrategy trustStrategy = getTrustStrategy();
        String[] supportedProtocols = new String[] { "TLSv1" };
        String[] supportedCipherSuites = null;
        HostnameVerifier hostnameVerifier = getHostnameVerifier();
        HttpResponse httpResponse = sendSslHttpPostWithJson(trustStrategy, supportedProtocols, supportedCipherSuites,
                hostnameVerifier, httpsUrl, contentJsonStr);
        return httpResponse;
    }
    public static HttpResponse sendSslHttpPostWithJson(String httpsUrl, Map<String, String> headers, String contentJsonStr) {
        TrustStrategy trustStrategy = getTrustStrategy();
        String[] supportedProtocols = new String[] { "TLSv1" };
        String[] supportedCipherSuites = null;
        HostnameVerifier hostnameVerifier = getHostnameVerifier();
        HttpPost httpPost = new HttpPost(httpsUrl);
        StringEntity stringEntity = getStringEntity(contentJsonStr, "application/json");
        setHeader(httpPost, headers);
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = sendSslHttp(null, trustStrategy, supportedProtocols, supportedCipherSuites,
                hostnameVerifier, httpPost);
        return httpResponse;
    }
    public static HttpResponse sendSslHttpPostWithJson(TrustStrategy trustStrategy, String[] supportedProtocols,
                                                       String[] supportedCipherSuites, HostnameVerifier hostnameVerifier, String httpsUrl, String contentJsonStr) {
        HttpPost httpPost = new HttpPost(httpsUrl);
        StringEntity stringEntity = getStringEntity(contentJsonStr, "application/json");
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = sendSslHttp(null, trustStrategy, supportedProtocols, supportedCipherSuites,
                hostnameVerifier, httpPost);
        return httpResponse;
    }

    public static HttpResponse sendSslHttpPost(String httpsUrl, Map<String, String> params) {
        TrustStrategy trustStrategy = getTrustStrategy();
        String[] supportedProtocols = new String[] { "TLSv1" };
        String[] supportedCipherSuites = null;
        HostnameVerifier hostnameVerifier = getHostnameVerifier();
        HttpResponse httpResponse = sendSslHttpPost(trustStrategy, supportedProtocols, supportedCipherSuites,
                hostnameVerifier, httpsUrl, params);
        return httpResponse;
    }

    public static HttpResponse sendSslHttpPost(TrustStrategy trustStrategy, String[] supportedProtocols,
                                               String[] supportedCipherSuites, HostnameVerifier hostnameVerifier, String httpsUrl,
                                               Map<String, String> params) {
        HttpPost httpPost = new HttpPost(httpsUrl);
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = getUrlEncodedFormEntity(params);
            httpPost.setEntity(urlEncodedFormEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse httpResponse = sendSslHttp(null, trustStrategy, supportedProtocols, supportedCipherSuites,
                hostnameVerifier, httpPost);
        return httpResponse;
    }

    public static HttpResponse sendSslKeyStoreHttpPostWithJson(String keyStoreFilePath, String keyStorePassword,
                                                               String httpsUrl, String contentJsonStr) {
        String keyStoreType = KeyStore.getDefaultType();
        File keyStoreFile = new File(keyStoreFilePath);
        TrustStrategy trustStrategy = getTrustStrategy();
        String[] supportedProtocols = new String[] { "TLSv1" };
        String[] supportedCipherSuites = null;
        HostnameVerifier hostnameVerifier =getHostnameVerifier();
        HttpResponse httpResponse = sendSslKeyStoreHttpPostWithJson(keyStoreType, keyStoreFile, keyStorePassword,
                trustStrategy, supportedProtocols, supportedCipherSuites, hostnameVerifier, httpsUrl, contentJsonStr);
        return httpResponse;
    }

    public static HttpResponse sendSslKeyStoreHttpPostWithJson(String keyStoreType, File keyStoreFile,
                                                               String keyStorePassword, TrustStrategy trustStrategy, String[] supportedProtocols,
                                                               String[] supportedCipherSuites, HostnameVerifier hostnameVerifier, String httpsUrl, String contentJsonStr) {
        HttpPost httpPost = new HttpPost(httpsUrl);
        StringEntity stringEntity = getStringEntity(contentJsonStr, "application/json");
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = sendSslKeyStoreHttp(keyStoreType, keyStoreFile, keyStorePassword, trustStrategy,
                supportedProtocols, supportedCipherSuites, hostnameVerifier, httpPost);
        return httpResponse;
    }

    public static HttpResponse sendSslKeyStoreHttpPost(String keyStoreType, File keyStoreFile, String keyStorePassword,
                                                       TrustStrategy trustStrategy, String[] supportedProtocols, String[] supportedCipherSuites,
                                                       HostnameVerifier hostnameVerifier, String httpsUrl, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(httpsUrl);
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = getUrlEncodedFormEntity(params);
            httpPost.setEntity(urlEncodedFormEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse httpResponse = sendSslKeyStoreHttp(keyStoreType, keyStoreFile, keyStorePassword, trustStrategy,
                supportedProtocols, supportedCipherSuites, hostnameVerifier, httpPost);
        return httpResponse;
    }

    public static HttpResponse sendSslKeyStoreHttpPost(String keyStoreFilePath, String keyStorePassword,
                                                       String httpsUrl, Map<String, String> params) {
        String keyStoreType = KeyStore.getDefaultType();
        File keyStoreFile = new File(keyStoreFilePath);
        TrustStrategy trustStrategy = getTrustStrategy();
        String[] supportedProtocols = new String[] { "TLSv1" };
        String[] supportedCipherSuites = null;
        HostnameVerifier hostnameVerifier = getHostnameVerifier();
        HttpResponse httpResponse = sendSslKeyStoreHttpPost(keyStoreType, keyStoreFile, keyStorePassword, trustStrategy,
                supportedProtocols, supportedCipherSuites, hostnameVerifier, httpsUrl, params);
        return httpResponse;
    }




    public static HttpResponse sendSslHttp(KeyStore keyStore, TrustStrategy trustStrategy, String[] supportedProtocols,
                                           String[] supportedCipherSuites, HostnameVerifier hostnameVerifier, HttpRequestBase httpRequestBase) {
        SSLContext sslContext = null;
        try {
            if (keyStore != null) {
                sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, trustStrategy).build();
            } else {
                sslContext = SSLContexts.custom().loadTrustMaterial(trustStrategy).build();
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        HttpResponse httpResponse = sendSslHttp(sslContext, supportedProtocols, supportedCipherSuites, hostnameVerifier,
                httpRequestBase);
        return httpResponse;
    }

    public static HttpResponse sendSslHttp(SSLContext sslContext, String[] supportedProtocols,
                                           String[] supportedCipherSuites, HostnameVerifier hostnameVerifier, HttpRequestBase httpRequestBase) {
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                supportedProtocols, supportedCipherSuites, hostnameVerifier);
        HttpResponse httpResponse = sendHttp(sslConnectionSocketFactory, httpRequestBase);
        return httpResponse;
    }



    public static HttpResponse sendSslKeyStoreHttp(String keyStoreType, File keyStoreFile, String keyStorePassword,
                                                   TrustStrategy trustStrategy, String[] supportedProtocols, String[] supportedCipherSuites,
                                                   HostnameVerifier hostnameVerifier, HttpRequestBase httpRequestBase) {
        // 加载keystore证书文件和密码
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            FileInputStream fileInputStream = new FileInputStream(keyStoreFile);
            keyStore.load(fileInputStream, keyStorePassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpResponse httpResponse = sendSslHttp(keyStore, trustStrategy, supportedProtocols, supportedCipherSuites,
                hostnameVerifier, httpRequestBase);
        return httpResponse;
    }
    private static HostnameVerifier getHostnameVerifier() {
        return getAllAllowHostnameVerifier();
    }
    private static HostnameVerifier getAllAllowHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return hostnameVerifier;
    }
    public static HostnameVerifier getDefaultHostnameVerifier() {
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();
        return hostnameVerifier;
    }
    private static TrustStrategy getTrustStrategy() {
        return getNoSignedTrustStrategy();
    }
    public static TrustStrategy getTrustSelfSignedStrategy() {
        TrustStrategy trustStrategy = new TrustSelfSignedStrategy();
        return trustStrategy;
    }
    private static TrustStrategy getNoSignedTrustStrategy() {
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };
        return trustStrategy;
    }
}
