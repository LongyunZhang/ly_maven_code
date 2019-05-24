package com.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程：
 * <p>
 * 创建httpclient实例
 * 创建httpmethod 方法实例: 最常用的是HttpGet,HttpPost 类
 * httpclient 通过 execute方法  提交Get 或者Post 请求
 * 使用CloseableHttpResponse 来接受服务器返回的状态信息和实体信息
 * 关闭连接
 */
public class HttpUtil {
    public static void main(String[] args) throws Exception {
        //get();
        getDemo();

        proxyDemo();

        //overtimeSettingDemo();//
    }

    public static void get() throws Exception {
        //1-创建CloseableHttpClient对象——方法1
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建CloseableHttpClient对象——方法2，可以添加一些网络访问选项设置
        // CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response1 = httpclient.execute(httpGet); //执行

        // The underlying HTTP connection is still held by the response objec
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
// Please note that if response content is not fully consumed the underlying
// connection cannot be safely re-used and will be shut down and discarded by the connection manager.

        try {
            System.out.println(response1.getStatusLine());//获取status状态码所在行（第一行：协议/版本   状态代码   描述）
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            String content = EntityUtils.toString(entity1, Consts.UTF_8); // 获取网页内容
            System.out.println(content);

            EntityUtils.consume(entity1);

        } finally {
            response1.close();
        }
    }

    public static void post() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://targethost/login");//post请求

        //封装提交到服务器的参数信息
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        //设置参数信息
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        //提交post方法
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body and ensure it is fully consumed
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }

    public static void getDemo() throws Exception {

        //创建httpclient实例，采用默认的参数配置
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //通过URIBuilder类创建URI——  所有的HTTP请求都有一个请求的起始行，由方法名，uri和HTTP协议版本组成
        //URI uri = new URIBuilder().setScheme("http").setHost("http://www.baidu.com/").build();
        //HttpGet httpGet = new HttpGet(uri) ;   //使用Get方法提交

        HttpGet httpGet = new HttpGet("https://www.taobao.com/");

        //请求的参数配置，分别设置连接池获取连接的超时时间、连接上服务器的时间、服务器返回数据的时间
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();
        httpGet.setConfig(config);//这个必须在setHeader之前，否则会报错


        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"); // 设置请求头消息User-Agent
        httpGet.setHeader("Cache-Control", "max-age=0");
        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

        //httpGet.setHeader("Host", "www.csdn.net");

        //httpGet.setHeader("", "");


        try {
            //通过httpclient的execute提交 请求 ，并用CloseableHttpResponse接受返回信息
            CloseableHttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode(); //服务器返回的状态
            HttpEntity entity = response.getEntity(); // 获取返回实体
            String content = EntityUtils.toString(entity, Consts.UTF_8); // 获取网页内容
            //判断返回的状态码是否是200 ，200 代表服务器响应成功，并成功返回信息
            if (statusCode == HttpStatus.SC_OK) {
                //EntityUtils 获取返回的信息。官方不建议使用使用此类来处理信息——除非响应的实体来自于信任的Http服务器，并且知道它的长度
                System.out.println("返回内容 --------" + content);

                //entity.getContentType()     ————返回对象是Head
                System.out.println("Content-Type: " + entity.getContentType().getValue());

                System.out.println("ContentLength --------" + entity.getContentLength());

                //entity.getContent()         ————InputStream
                //entity.getContentEncoding() ————返回对象是Head
            } else {
                System.out.println("获取信息失败");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            httpClient.close();
        }

    }

    public static void postDemo() throws Exception {
        //创建httpclient 实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建post方法实例
        HttpPost post = new HttpPost("http://host/.com");

        //封装提交到服务器的参数信息
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("name", "zhangsan"));
        list.add(new BasicNameValuePair("age", "18"));

        //构建表单
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        //设置参数信息
        post.setEntity(formEntity);

        //提交post方法
        CloseableHttpResponse respone = httpclient.execute(post);

        try {
            int statcode = respone.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(respone.getEntity(), Consts.UTF_8);

            if (statcode == HttpStatus.SC_OK) {
                System.out.println(content);
            } else {
                throw new Exception("http请求返回码错误,statusCode : " + statcode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            httpclient.close();
        }

    }


    //实际开发的话，我们对于每一种异常的抛出，catch里都需要做一些业务上的操作!!!!!
    public static void firstDemo() {
        CloseableHttpClient httpClient = HttpClients.createDefault(); // 创建httpClient实例
        HttpGet httpget = new HttpGet("http://www.open1111.com/"); // 创建httpget实例
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
        } catch (ClientProtocolException e) {  // http协议异常
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) { // io异常
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 执行get请求

        HttpEntity entity = response.getEntity(); // 获取返回实体
        try {
            System.out.println("网页内容：" + EntityUtils.toString(entity, "utf-8"));
        } catch (ParseException e) {  // 解析异常
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) { // io异常
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 指定编码打印网页内容
        try {
            response.close();
        } catch (IOException e) {  // io异常
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 关闭流和释放系统资源


    }

    //使用代理进行访问
    public static void proxyDemo() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault(); // 创建httpClient实例
        HttpGet httpGet = new HttpGet("https://www.taobao.com/"); // 创建httpget实例

        //设置代理地址——创建 HttpHost 对象，并用进行 RequestConfig 进行设置
        HttpHost proxy = new HttpHost("190.104.157.71", 3128);//如果不设置的话，默认就是本机出口的公网地址
        RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
        httpGet.setConfig(requestConfig);

        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        CloseableHttpResponse response = httpClient.execute(httpGet); // 执行http get请求

        HttpEntity entity = response.getEntity(); // 获取返回实体
        System.out.println("------网页内容------：" + EntityUtils.toString(entity, "utf-8")); // 获取网页内容
        response.close(); // response关闭
        httpClient.close(); // httpClient关闭
    }

    public static void overtimeSettingDemo() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault(); // 创建httpClient实例
        HttpGet httpGet = new HttpGet("http://www.google.com/"); //创建httpget实例

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();
        httpGet.setConfig(config);

        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");

        CloseableHttpResponse response = httpClient.execute(httpGet); // 执行http get请求
        HttpEntity entity = response.getEntity(); // 获取返回实体
        System.out.println("网页内容：" + EntityUtils.toString(entity, "utf-8")); // 获取网页内容
        response.close(); // response关闭
        httpClient.close(); // httpClient关闭
    }

    public static void contextDemo() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig config = RequestConfig
                .custom()
                .setConnectionRequestTimeout(3000)
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .build();

        HttpGet get = new HttpGet("http://www.baidu.com");
        get.setConfig(config);

        //设置上下文
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpResponse response = httpclient.execute(get, context); //111

        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("---------------------------------");

        HttpGet get1 = new HttpGet("http://www.qq.com");
        get1.setConfig(config);
        CloseableHttpResponse res = httpclient.execute(get1, context);//111---共享上下文context
        System.out.println(EntityUtils.toString(res.getEntity()));
    }


}

