//package com.util.htmlunit;
//
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlDivision;
//import com.gargoylesoftware.htmlunit.html.HtmlInput;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//
//import java.util.List;
//
//public class HtmlUnitUtil {
//
//    //http://www.cnblogs.com/cation/p/3933408.html
//    //https://www.2cto.com/kf/201706/554589.html
//    //http://tianxingzhe.blog.51cto.com/3390077/1755511
//    //http://m635674608.iteye.com/blog/2151480
//    public static void main(String[] args) throws Exception{
//
//
//        String str;
//        //创建一个webclient
//        WebClient webClient = new WebClient();
//        //htmlunit 对css和javascript的支持不好，所以请关闭之
//        webClient.getOptions().setJavaScriptEnabled(false);
//        webClient.getOptions().setCssEnabled(false);
//
//        //获取页面
//        HtmlPage page = webClient.getPage("http://www.baidu.com/");
//
//        //获取页面的TITLE
//        System.out.println("----------------------------------- 页面的TITLE ------------------------------------");
//        str = page.getTitleText();
//        System.out.println(str);
//
//        //获取页面的XML代码
//        System.out.println("----------------------------------- 页面的HTML代码 ------------------------------------");
//        str = page.asXml();
//        System.out.println(str);
//
//        //获取页面的文本
//        System.out.println("----------------------------------- 页面文本(除去标签的内容) ------------------------------------");
//        str = page.asText();
//        System.out.println(str);
//
//        System.out.println("----------------------------------- 通过id获得input元素 ------------------------------------");
//        //通过id获得"百度一下"按钮
//        HtmlInput btn = (HtmlInput)page.getHtmlElementById("su");
//        System.out.println(btn.getDefaultValue());
//
//
//        System.out.println("----------------------------------- 查找所有div ------------------------------------");
//        //查找所有div
//        List<?> hbList = page.getByXPath("//div");
//        HtmlDivision hb = (HtmlDivision)hbList.get(0);
//        System.out.println(hb.toString());
//
//        System.out.println("----------------------------------- 查找并获取特定input ------------------------------------");
//        //查找并获取特定input
//        List<?> inputList = page.getByXPath("//input[@id='su']");
//        HtmlInput input = (HtmlInput)inputList.get(0);
//        System.out.println(input.toString());
//
//
//        System.out.println("----------------------------------- 搜索 ------------------------------------");
//        //获取搜索输入框并提交搜索内容
//        HtmlInput input2 = (HtmlInput)page.getHtmlElementById("kw");
//        System.out.println(input2.toString());
//        input.setValueAttribute("雅蠛蝶");
//        System.out.println(input.toString());
//        //获取搜索按钮并点击
//        HtmlInput btn2 = (HtmlInput)page.getHtmlElementById("su");
//        HtmlPage page2 = btn2.click();
//        //输出新页面的文本
//        System.out.println(page2.asText());
//
//        //关闭webclient
//        webClient.close();
//
//
//        // 创建webclient
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        // 获取搜索输入框并提交搜索内容
//        HtmlPage page1 = (HtmlPage)
//                webClient.getPage("http://www.baidu.com/");
//        HtmlInput input = (HtmlInput) page1.getHtmlElementById("王岐山");
//        System.out.println(input.toString());
//        input.setValueAttribute("HtmlUnit");
//        System.out.println(input.toString());
//
//        // 获取搜索按钮并点击
//        HtmlInput btnSearch = (HtmlInput) page1.getHtmlElementById("su");
//        HtmlPage page2 = btnSearch .click();
//
//        // 输出新页面的文本
//        System.out.println(page2.asXml());
//
//
//    }
//}
