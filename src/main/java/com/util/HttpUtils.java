package com.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static Logger logHandler = Logger.getLogger(HttpUtils.class);

    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 7000;
    private static final String DEFAULT_ENCODING = "UTF-8";

    public static void main(String[] args) throws Exception{
        String url = "http://www.baidu.com";
        int connectTimeout = 3000;
        int socketTimeout = 3000;
        String encoding = "UTF-8";

        String res = doGet(url, connectTimeout, socketTimeout, encoding);
        System.out.println(res);

        test1();
    }


    public static void test1() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://targethost/homepage");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager.
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }

        HttpPost httpPost = new HttpPost("http://www.baidu.com");
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }

    }

    /**
     * get方法
     * @param url
     * @param connectTimeout
     * @param socketTimeout
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String doGet(String url, int connectTimeout, int socketTimeout, String encoding) throws IOException {

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_CONNECT_TIMEOUT).build();

        // CloseableHttpClient client = HttpClientBuilder.create().build();
        // 2016-01-04
        CloseableHttpClient client = HttpClients.custom().setDefaultSocketConfig(socketConfig).build();
        HttpGet request = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .setConnectTimeout(connectTimeout).build();
        request.setConfig(requestConfig);
        int statusCode;
        String rs;
        try {
            HttpResponse response = client.execute(request);
            statusCode = response.getStatusLine().getStatusCode();  //获取状态
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, encoding);
            if (statusCode == 200) {
                rs = content;
                return rs;
            } else {
                throw new IOException("http请求返回码错误,statusCode : " + statusCode);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                client.close();
            } catch(Exception e) {
                logHandler.error("http 连接客户端关闭出现错误：",e);
                client = null;
            }
        }
    }

    public static String doGet(String url, int connectTimeout, int socketTimeout) throws IOException {
        return doGet(url, connectTimeout, socketTimeout, DEFAULT_ENCODING);
    }

    public static String doGet(String url) throws IOException{
        return doGet(url, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    public static String doPost(String url, int connectTimeout, int socketTimeout, Map<String, String> data, String encoding) throws IOException {

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_CONNECT_TIMEOUT).build();

        // CloseableHttpClient client = HttpClientBuilder.create().build();
        // 2016-01-04
        CloseableHttpClient client = HttpClients.custom().setDefaultSocketConfig(socketConfig).build();
        HttpPost request = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .setConnectTimeout(connectTimeout).build();
        request.setConfig(requestConfig);
        int statusCode = 0;
        String rs = null;

        try {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (String k : data.keySet()) {
                formParams.add(new BasicNameValuePair(k, data.get(k)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            request.setEntity(entity);
            logHandler.info("Http Post Execute Begin.");
            long beginTime = System.currentTimeMillis();
            HttpResponse response = client.execute(request);
            long endTime = System.currentTimeMillis();
            logHandler.info("Http Post Execute End. CostTime=" + (endTime - beginTime));

            statusCode = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity(), encoding);

            if (statusCode == 200) {
                rs = content;
                return rs;
            } else {
                throw new IOException("http请求返回码错误,statusCode : " + statusCode);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                client.close();
            } catch(Exception e) {
                logHandler.error("http 连接客户端关闭出现错误：", e);
                client = null;
            }
        }
    }

    public static String doPost(String url, int connectTimeout, int socketTimeout, Map<String, String> data) throws IOException {
        return doPost(url, connectTimeout, socketTimeout, data, DEFAULT_ENCODING);
    }

    public static String doPost(String url, Map<String, String> data) throws IOException {
        return doPost(url, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, data);
    }

    /**
     * 实现采用POST方法向目标地址进行HTTP连接
     * @param uri 目标地址
     * @param nvps KEY/VALUE对
     * @return utf-8 string of response
     * @throws Exception
     */
    /*
    public static String post(String uri, List<NameValuePair> nvps) throws Exception {
        return post(uri, nvps, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }

    /**
     * 实现采用POST方法向目标地址进行HTTP连接
     * @param uri 目标地址
     * @param nvps KEY/VALUE对
     * @return utf-8 string of response
     * @throws Exception
     */
    public static String post(String uri, List<NameValuePair> nvps, int connTime, int readTime) throws Exception {
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        // 修复用于https通信时SSL握手期间没有超时设置的问题
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(connTime).build();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultSocketConfig(socketConfig).build();
        try {
            HttpPost httpPost = new HttpPost(uri);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(readTime)
                    .setConnectionRequestTimeout(connTime)
                    .setConnectTimeout(connTime).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return content;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /*
    public static String doPostContentType(String url, int connectTimeout, int socketTimeout, Map<String, Object> data, String contentType) throws IOException {
        Gson gson = new Gson();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(connectTimeout).build();
        CloseableHttpClient client = HttpClients.custom().setDefaultSocketConfig(socketConfig).build();
        HttpPost request = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        request.setConfig(requestConfig);
        // 添加BA认证
        int statusCode = 0;
        String rs = null;
        try {
            String s = gson.toJson(data);
            StringEntity entity = new StringEntity(s, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType(contentType);
            request.setEntity(entity);
            logHandler.info("Http Post Execute Begin.");
            long beginTime = System.currentTimeMillis();
            HttpResponse response = client.execute(request);
            long endTime = System.currentTimeMillis();
            logHandler.info("Http Post Execute End. CostTime=" + (endTime - beginTime));
            statusCode = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (statusCode == 200) {
                rs = content;
                return rs;
            } else {
                throw new IOException("http请求返回码错误,statusCode : " + statusCode);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                client.close();
            } catch(Exception e) {
                logHandler.error("http 连接客户端关闭出现错误：", e);
                client = null;
            }
        }
    }
    */

    /**
     * 实现采用POST方法向目标地址进行HTTP连接
     * @param uri 目标地址
     * @param nvps KEY/VALUE对
     * @return byte array of response
     * @throws Exception
     */
    public static byte[] post2(String uri, List<NameValuePair> nvps) throws Exception {

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_CONNECT_TIMEOUT).build();

        // CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2016-01-04
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultSocketConfig(socketConfig).build();
        try {
            HttpPost httpPost = new HttpPost(uri);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_CONNECT_TIMEOUT)
                    .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                byte[] content = EntityUtils.toByteArray(response.getEntity());
                EntityUtils.consume(entity);
                return content;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求，一般化指可以自己进行http报文头部设置以及发送报文数据编码格式
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param data
     *            http头部的格式设置
     * @param setEncoding
     *            设置输出的格式
     * @return 所代表远程资源的响应结果
     */

    /*
    public static String generalSendPost(String url, String param, Map<String, String> data,String setEncoding, boolean setTime)throws Exception{
        return generalSendPost(url,param,data,setEncoding,DEFAULT_CONNECT_TIMEOUT,DEFAULT_SOCKET_TIMEOUT,setTime);
    }
    */

    /*
    public static String generalSendPost(String url, String param, Map<String, String> data,String setEncoding, int connectTimeout, int socketTimeout, boolean setTime) throws Exception{
        PrintStream out = null;
        BufferedReader in = null;
        //URLConnection conn = null;
        URLConnection httpURLConnection = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpURLConnection = realUrl.openConnection();
            httpURLConnection.setConnectTimeout(connectTimeout);
            if(setTime){
                httpURLConnection.setReadTimeout(socketTimeout);
            }
            //conn.setReadTimeout(socketTimeout);//对账文件报文的接收时间较长，故将它删除
            // 设置通用的请求属性
            for (String k : data.keySet()) {
                httpURLConnection.setRequestProperty(k,data.get(k));
            }
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintStream(httpURLConnection.getOutputStream());
            byte[] bytes=param.getBytes(setEncoding);
            // 发送请求参数
            out.write(bytes);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),setEncoding));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            throw e;
        }
        //使用finally块来关闭输出流、输入流
        finally{
            IOUtils.closeOutPutStream(out);
            IOUtils.closeBufferedReader(in);
        }
        return result;
    }
    */

    /**
     * 进行http通信,获取同步响应
     * @param urlStr 目标地址
     * @param reqContent 报文内容
     * @param connectionTimeout 连接超时时间
     * @param readTimeout 读取超时时间
     */
    public static String sendByHttp(String urlStr, String reqContent, String encoding, int connectionTimeout, int readTimeout) throws Exception {
        String reStr = "";
        HttpURLConnection connection = null;
        OutputStream out = null;
        BufferedReader in = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("accept", "*/*");


            connection.setRequestMethod("POST");
            connection.connect();
            out = connection.getOutputStream();
            out.write(reqContent.getBytes(encoding));
            logHandler.info("请求报文已成功发送");
            String respCode = Integer.toString(connection.getResponseCode());
            if (!"200".equals(respCode)) {
                throw new Exception("HTTP通信失败:" + respCode);
            }
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            String line;
            while((line = in.readLine()) !=null) {
                reStr += line;
            }
        } catch(Exception e) {
            throw e;
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch(Exception e) {
                logHandler.error("关闭输出流出现异常！", e);
                out = null;
            }
            try {
                if(in != null) {
                    in.close();
                }
            } catch(Exception e) {
                logHandler.error("关闭输入流出现异常！", e);
                in = null;
            }
            try {
                if(connection != null) {
                    connection.disconnect();
                }
            } catch(Exception e) {
                logHandler.error("关闭http连接出现异常！", e);
                connection = null;
            }
        }
        return reStr;
    }
}
