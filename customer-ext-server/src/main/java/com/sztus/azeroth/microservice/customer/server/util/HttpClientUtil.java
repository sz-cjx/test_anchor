package com.sztus.azeroth.microservice.customer.server.util;

import com.sztus.framework.component.core.constant.GlobalConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpClientUtil {

    private static String rootResourceUrl = GlobalConst.OSS_ESW_SECURITY;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 表单请求 post
     *
     * @param headerMap
     * @param uri
     * @param jsonBody
     * @param pairsMap
     * @return
     */
    public static String postRequest(Map<String, String> headerMap, String uri, String jsonBody, Map<String, String> pairsMap) {
        String responseStr = null;
        try {
            boolean isSSL = false;
            CloseableHttpClient httpClient = getHttpClient(isSSL);
            HttpPost httpPost = new HttpPost(uri);
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            List<NameValuePair> pairs = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pairsMap)) {
                for (Map.Entry<String, String> entry : pairsMap.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
            }

            if (!Objects.isNull(jsonBody)) {
                StringEntity requestEntity = new StringEntity(jsonBody, "utf-8");
                requestEntity.setContentEncoding("UTF-8");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setEntity(requestEntity);
            }

            responseStr = httpClient.execute(httpPost, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, GlobalConst.DEFAULT_CHARSET);
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                    return null;
                }
            });
            return responseStr;
        } catch (Exception e) {
            LOGGER.error("[Http Client Util]Http Request connection", e);
            return null;
        }
    }

    public static String postRequest(Map<String, String> headerMap, String uri, String jsonBody) {
        String responseStr = null;
        boolean isSSL = false;
        if (StringUtils.isNotEmpty(uri) && uri.startsWith("https")) {
            isSSL = true;
        }

        try {
            CloseableHttpClient httpClient = getHttpClient(isSSL);
            HttpPost httpPost = new HttpPost(uri);
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (!Objects.isNull(jsonBody)) {
                StringEntity requestEntity = new StringEntity(jsonBody, "utf-8");
                requestEntity.setContentEncoding("UTF-8");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setEntity(requestEntity);
            }

            responseStr = httpClient.execute(httpPost, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, GlobalConst.DEFAULT_CHARSET);
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                    return null;
                }
            });
            return responseStr;
        } catch (Exception e) {
            LOGGER.error("[Http Client Util]Post Http Request connection .error", e);
            return null;
        }
    }

    public static String getRequest(Map<String, String> headerMap, String uri, Map<String, String> map, boolean isSSL) {
        String responseStr = null;

        if (!CollectionUtils.isEmpty(map)) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for (String key : map.keySet()) {
                sb.append(key).append("=").append(map.get(key)).append("&");
            }
            String condition = sb.substring(0, sb.length() - 1);
            uri = uri + condition;
        }

        try {
            CloseableHttpClient httpClient = getHttpClient(isSSL);
            HttpUriRequest httpGet = new HttpGet(uri);
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            responseStr = httpClient.execute(httpGet, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            return EntityUtils.toString(entity, GlobalConst.DEFAULT_CHARSET);
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                    return null;
                }
            });
            return responseStr;
        } catch (Exception e) {
            LOGGER.error("[Http Client Util]Get Http Request connection", e);
        }

        return responseStr;
    }

    private static CloseableHttpClient getHttpClient(boolean isSSL) {
        CloseableHttpClient httpClient = null;
        try {
            if (isSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

                InputStream inputStream = null;
                URLConnection urlConnection = null;
                URL url = null;
                try {
                    url = new URL(rootResourceUrl + "truststore.jks");
                    urlConnection = url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    trustStore.load(inputStream, "Arb@8888".toCharArray());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }

                SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

                // 只允许使用TLSv1协议
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null,
                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
                httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } else {
                httpClient = HttpClients.createDefault();
            }
        } catch (Exception e) {
            LOGGER.error("[Http Client Util]Get Http Request connection.error", e);
        }

        return httpClient;
    }
}
