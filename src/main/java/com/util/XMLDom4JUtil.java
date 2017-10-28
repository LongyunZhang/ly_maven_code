package com.util;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.List;

public class XMLDom4JUtil {
    //http://www.blogjava.net/i369/articles/154264.html
    //http://www.cnblogs.com/fengmingyue/p/5964420.html

    /**
     * 1、从文件读取XML，输入文件名，返回XML文档（Document对象）
     *
     * reader的read方法是重载的，可以从InputStream, File, Url等多种不同的源来读取。
     * 得到的Document对象就带表了整个XML。
     */
    public static Document read(String fileName) throws MalformedURLException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(fileName));
        return document;
    }

    /**
     * 2、取得Root节点（Element对象）
     * @param doc
     * @return
     */
    public static Element getRootElement(Document doc){
        return doc.getRootElement();
    }

    /**
     * 3、遍历XML树
     */
    public static void get() {


    }



    /**
     * 4、访问一个节点
     * @param document
     */
    public static void bar(Document document) throws Exception{

        List<Node> list = document.selectNodes( "/books/book/title");
        for(int i=0;i<list.size();i++)
            System.out.println(list.get(i).getText());

        Node node = document.selectSingleNode("/books/book/title");

        String name = node.valueOf("@show");//获取属性值
        System.out.println(name);
    }



    //------------------------------------------------------
    /**
     * 建立一个XML文档,文档名由输入属性决定
     * @param filename 需建立的文件名
     * @return 返回操作结果, 0表失败, 1表成功
     */
    public static int createXMLFile(String filename){
        /** 返回操作结果, 0表失败, 1表成功 */
        int returnValue = 0;

        /** （1）建立document对象 */
        Document document = DocumentHelper.createDocument();

        /** （2）建立XML文档的根books */
        Element booksElement = document.addElement("books");

        /** （3）加入一行注释 */
        booksElement.addComment("This is a test for dom4j, holen, 2004.9.11");

        /** （4）加入第一个book节点 */
        Element bookElement = booksElement.addElement("book");

        /** （5）为book节点加入show属性内容 */
        bookElement.addAttribute("show","yes");

        /** （6）在book节点中加入title节点 */
        Element titleElement = bookElement.addElement("title");

        /** （7）为title节点设置内容 */
        titleElement.setText("Dom4j Tutorials");

        /** 类似的完成后两个book */
        bookElement = booksElement.addElement("book");
        bookElement.addAttribute("show","yes");//addAttribute：添加属性
        titleElement = bookElement.addElement("title");
        titleElement.setText("Lucene Studing");//setText：设置标签文本值

        bookElement = booksElement.addElement("book");
        bookElement.addAttribute("show","no");
        titleElement = bookElement.addElement("title");
        titleElement.setText("Lucene in Action");

        /** （8）加入owner节点 ---这个节点与book节点属于兄弟(同胞)节点 */
        Element ownerElement = booksElement.addElement("owner");
        ownerElement.setText("O'Reilly");

        try{
            /** （9）将document中的内容写入文件中 */
            //通过XMLWriter生成物理文件，默认生成的XML文件排版格式比较乱，
            //可以通过OutputFormat类的createCompactFormat()方法或createPrettyPrint()方法格式化输出
            XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)));
            writer.write(document);
            writer.close();
            /** 执行成功,需返回1 */
            returnValue = 1;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return returnValue;


    }


    //默认的输出方式为紧凑方式，默认编码为UTF-8，
    // 但对于我们的应用而言，一般都要用到中文，并且希望显示时按[自动缩进]的方式的显示，
    // 这就需用到OutputFormat类。
    /**
     * 格式化XML文档,并解决中文问题
     * @param filename
     * @return
     */
    public int formatXMLFile(String filename){
        int returnValue = 0;
        try{
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new File(filename));
            XMLWriter writer = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** 指定XML编码 */
            format.setEncoding("GBK");
            writer= new XMLWriter(new FileWriter(new File(filename)),format);
            writer.write(document);
            writer.close();
            /** 执行成功,需返回1 */
            returnValue = 1;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return returnValue;
    }

    /**
     * !!!!!!!!!!!!!!!======== 从指定节点开始, 递归遍历所有子节点(包括属性) ========!!!!!!!!!!!!!!!
     * @author chenleixing
     */
    public static void getNodes(Element node){
        System.out.println("--------------------");

        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称：" + node.getName());//当前节点名称
        System.out.println("当前节点的内容：" + node.getTextTrim());//当前节点内容

        /*
        List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name = attr.getName();//属性名称
            String value = attr.getValue();//属性的值
            System.out.println("属性名称：" + name + "属性值：" + value);
        }
        */

        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            getNodes(e);//递归
        }
    }


    public static void main(String[] args){
        String xmlFilePath = "/Users/longyun/Desktop/xml_test.xml";
        createXMLFile(xmlFilePath);
        try {
            Document doc = read(xmlFilePath);
            bar(doc);
            getNodes(doc.getRootElement());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------
    static Element root;
    public static void main1(String[] args) throws Exception {
        add();//在第一个学生里面增加性别标签<sex>男</sex>
        //add2();//在第一个学生的<name>张三</name>标签后增加<home>沭阳</home>
        //delete();//删除上面增加的性别标签<sex>男</sex>
        //modify();//将上面增加的标签<home>沭阳</home>改为<home>江苏</home>
        //select();//查询所有学生姓名
    }
    //在第一个学生里面增加性别标签<sex>男</sex>
    private static void add() throws Exception {
        Document document = getRoot();
        Element s1=root.element("student");
        Element sex1=s1.addElement("sex");
        sex1.setText("男");
        writeBack(document);
    }
    //在第一个学生的<name>张三</name>标签后增加<home>沭阳</home>
    private static void add2() throws Exception {
        Document document = getRoot();
        Element s1=root.element("student");
        List<Element>list=s1.elements();
        Element home=DocumentHelper.createElement("home");
        home.setText("沭阳");
        list.add(1,home );
        writeBack(document);
    }
    //删除上面增加的性别标签<sex>男</sex>
    private static void delete() throws Exception{
        Document document = getRoot();
        Element s1=root.element("student");
        s1.remove(s1.element("sex"));
        writeBack(document);
    }
    //将上面增加的标签<home>沭阳</home>改为<home>江苏</home>
    private static void modify() throws Exception{
        Document document = getRoot();
        Element s1=root.element("student");
        s1.element("home").setText("江苏");
        writeBack(document);
    }
    //查询所有学生姓名
    private static void select() throws Exception{
        Document document = getRoot();
        List<Element> list=root.elements("student");
        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i).elementText("name"));
        }
    }
    //XML回写
    private static void writeBack(Document document)throws Exception {
        OutputFormat format=OutputFormat.createPrettyPrint();
        XMLWriter writer=new XMLWriter(new FileOutputStream("src/1.xml"), format);
        writer.write(document);
        writer.close();
    }
    //初始化
    private static Document getRoot() throws DocumentException {
        SAXReader saxReader=new SAXReader();
        Document document=saxReader.read("src/1.xml");
        root=document.getRootElement();
        return document;
    }
}
