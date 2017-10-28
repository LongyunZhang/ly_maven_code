package com.util;

/*
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;*/

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by gongnan on 14-6-5.
 */
public class XMLUtils {

    //private static Log log = LogFactory.getLog(XMLUtils.class);
    private Document doc = null;

    public XMLUtils(String xmlStr)
            throws ParserConfigurationException, SAXException, IOException {
        /*
        xmlStr = repaceBlank(xmlStr);
        if (StringUtils.isNotEmpty(xmlStr)){
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            doc = dbBuilder.parse(new InputSource(new ByteArrayInputStream(xmlStr.toLowerCase().getBytes())));
        }
        */
    }

    public static Document getOriginDocByXmlStr(String xmlStr) throws ParserConfigurationException, SAXException, IOException {

        Document doc = null;
        xmlStr = repaceBlank(xmlStr);
        if (StringUtils.isNotEmpty(xmlStr)){
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            doc = dbBuilder.parse(new InputSource(new ByteArrayInputStream(xmlStr.getBytes())));
        }
        return doc;
    }

    public static Document getOriginDocByXmlStr(String xmlStr, String encoding) throws ParserConfigurationException, SAXException, IOException {
        //xmlStr = repaceBlank(xmlStr);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document doc = dbBuilder.parse(new InputSource(new ByteArrayInputStream(xmlStr.getBytes(encoding))));
        return doc;
    }

    /**
     * 根据tag名称获取---第一个tag值
     * @param tag
     * @return
     */
    public String getValueByTagName(String tag) {
        NodeList nodeList = this.doc.getElementsByTagName(tag);
        if (nodeList.getLength() > 0 && nodeList.item(0).hasChildNodes())
            return nodeList.item(0).getFirstChild().getNodeValue();
        return "";
    }

    public static String getValueByTagName(Document doc, String tag) {
        NodeList nodeList = doc.getElementsByTagName(tag);
        if (nodeList.getLength() > 0 && nodeList.item(0).hasChildNodes())
            return nodeList.item(0).getFirstChild().getNodeValue();
        return "";
    }

    //去掉空格回车
    public static String repaceBlank(String str) {
        String dest = null;
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 获取一个xml 的指定node
     * @param document
     * @param parentTree 指定的节点路径
     * @return
     * @throws Exception
     */
    public static Node getTargetNode(Document document, String[] parentTree) throws Exception {
        String parent = parentTree[0];
        Node currentNode = null;
        NodeList nodeList = document.getElementsByTagName(parent);

        if (nodeList.getLength() < 1) {
            throw new Exception("Element " + parent + " not found");
        }
        if(1 == parentTree.length){
            return nodeList.item(0);
        }

        nodeList = nodeList.item(0).getChildNodes();
        for (int i = 1; i < parentTree.length; i++) {
            parent = parentTree[i];
            for (int j = 0; j < nodeList.getLength(); j++) {
                currentNode = nodeList.item(j);
                if (currentNode.getNodeName().equalsIgnoreCase(parent)) {
                    if (i < parentTree.length - 1) {
                        nodeList = currentNode.getChildNodes();
                        break;
                    } else {
                        return currentNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 取得XML文档中目标节点的值，并去掉特殊符号CDATA
     *
     * @param doc
     * @param nodePath
     * @return
     */
    public static String getTargetNodeValue(Document doc, String[] nodePath) throws Exception {
        Node node = null;
        try {
            node = XMLUtils.getTargetNode(doc, nodePath);
        } catch (Exception ex) {
            throw ex;
        }
        if (node != null)
            return XMLUtils.removeCDATA(node.getTextContent());//去掉特殊符号CDATA
        else //如果节点不存在则返回空串
            return "";
    }

    /**
     * 在指定的 节点路径下新建一个node
     *
     * @param docXmlStr  document
     * @param parentTree 节点路径
     * @param nodeName   新节点名字
     * @param nodeValue  节点value
     * @return
     * @throws Exception
     */
    public static String addSubNode(String docXmlStr, String[] parentTree, String nodeName, String nodeValue) throws Exception {
        Document document = getOriginDocByXmlStr(docXmlStr);
        Element newNode = document.createElement(nodeName);//创建一个节点
        newNode.appendChild(document.createTextNode(nodeValue));
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();
    }

    /**
     * 根据指定节点路径删除指定 nodeName节点
     *
     * @param docXmlStr
     * @param parentTree
     * @param nodeName
     * @return
     * @throws Exception
     */
    public static String removeNode(String docXmlStr, String[] parentTree, String nodeName) throws Exception {
        Document document = getOriginDocByXmlStr(docXmlStr);
        Node parentNode = getTargetNode(document, parentTree);
        if (parentNode == null){
            return "";
        }
        NodeList nodeList = parentNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equalsIgnoreCase(nodeName)) {
                parentNode.removeChild(node);
            }
        }
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        return writer.toString();

    }


    /**
     * 根据Map生成xml格式的字符串
     *
     * @param root     根节点
     * @param infoMap  子节点k-v
     * @param encoding 编码方式
     * @return xml String
     */
    public static String getXMLFromMap(String root, Map<String, String> infoMap, String encoding) {
        if (root == null || root.equals("")) {
            return null;
        }
        StringBuilder res = new StringBuilder(200);
        res.append("<?xml version=\"1.0\" encoding=\"").append(encoding).append("\"?>");
        res.append("<").append(root).append(">");
        res.append(getEleStrFromMap(infoMap));
        res.append("</").append(root).append(">");
        res.trimToSize();
        return res.toString();
    }

    /**
     * 根据map生成xml片段，不包含根节点
     * 空值标签使用简写形式，例如<tag/>
     * @param infoMap
     * @return
     */
    public static String getEleStrFromMap(Map<String, String> infoMap) {
        StringBuilder res = new StringBuilder(200);
        if (infoMap != null) {
            for (String key : infoMap.keySet()) {
                if (infoMap.get(key) == null || infoMap.get(key).length() == 0) {
                    res.append("<").append(key).append("/>");
                } else {
                    res.append("<").append(key).append(">");
                    res.append(infoMap.get(key));
                    res.append("</").append(key).append(">");
                }
            }
        }
        res.trimToSize();
        return res.toString();
    }

    /**
     * 根据map生成xml片段，不包含根节点
     * 空值标签使用完整形式，例如<tag></tag>
     *
     * @param infoMap
     * @return
     */
    public static String getEleStrFromMap2(Map<String, String> infoMap) {
        StringBuilder res = new StringBuilder(200);
        if (infoMap != null) {
            for (String key : infoMap.keySet()) {
                if (infoMap.get(key) == null || infoMap.get(key).length() == 0) {
                    res.append("<").append(key).append(">");
                    res.append("");
                    res.append("</").append(key).append(">");
                } else {
                    res.append("<").append(key).append(">");
                    res.append(infoMap.get(key));
                    res.append("</").append(key).append(">");
                }
            }
        }
        res.trimToSize();
        return res.toString();
    }

    /**
     * 获取某个标签下的内容(不区分大小写)
     * @param str
     * @param tag
     */
    public static String getXmlNodeVal(String str, String tag) {

        if (tag == null || tag.equalsIgnoreCase("")) {
            return "";
        }

        String statTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";

        int starTagNum = str.indexOf(statTag);
        int endTagNum = str.indexOf(endTag);

        if (starTagNum == -1 || endTagNum == -1) {
            // 考虑可能出现大小写而不匹配的情况
            String strTmp = str.toUpperCase();
            String starTagTmp = statTag.toUpperCase();
            String endTagTmp = endTag.toUpperCase();

            int starTagNumTmp = strTmp.indexOf(starTagTmp);
            int endTagNumTmp = strTmp.lastIndexOf(endTagTmp);

            if (starTagNumTmp == -1 || endTagNumTmp == -1) {
                return "";
            } else {
                return str.substring(starTagNumTmp + starTagTmp.getBytes().length, endTagNumTmp);
            }
        }

        return str.substring(starTagNum + statTag.getBytes().length, endTagNum);

    }

    /**
     * 格式化输出XML字符串
     * @param str
     * @return
     *
     * e.g.
     *  转换前:  <Head><name>john</name><age>10</age></Head>
    转换后:
    <?xml version="1.0" encoding="UTF-8"?>
    <Head>
    <name>john</name>
    <age>10</age>
    </Head>
     */
    /*
    public static String xmlPrettyPrint(String str) {
        //首先转义双引号 部署程序后发现不需要转义
        //str = str.replaceAll("\"","\\\\\"");
        XMLWriter xmlWriter = null;
        try {
            org.dom4j.Document document = null;
            document = DocumentHelper.parseText(str);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            //format.setEncoding("utf-8");
            StringWriter writer = new StringWriter();
            // 格式化输出流
            xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            return writer.toString();
        } catch (Exception ex) {
            log.error("xmlPrettyPrint error:", ex);
            return str;
        } finally {
            if(xmlWriter != null ){
                try{
                    xmlWriter.close();
                }catch(Exception e){
                    log.error("writer close error:", e);
                }
            }
        }
    }
    */

    /**
     * 为避免乱码，使用<![CDATA[]]>标签包装map的每个value域
     *
     * @param map
     */
    public static void addCDATA(HashMap<String, String> map) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            map.put(key, "<![CDATA[" + value + "]]>");
        }
    }

    /**
     * 去除特殊符号CDATA
     *
     * @param xmlNode
     * @return
     */
    public static String removeCDATA(String xmlNode) {
        if (xmlNode.startsWith("<![CDATA[")) {
            xmlNode = xmlNode.substring(9, xmlNode.length() - 3);
        }
        return xmlNode;
    }

    /**
     * 把node的值填充到map里
     * @param noteList
     * @param map
     */
    public static void getValues(NodeList noteList,Map map){
        if(noteList == null){
            return;
        }
        int nodeLength = noteList.getLength();
        if(nodeLength==0){
            return;
        }
        for (int i = 0; i < nodeLength; i++) {
            Node node = noteList.item(i);
            map.put(node.getNodeName(),node.getTextContent());
        }
    }

    /**
     * 返回相同标签组成的Node集合
     *
     * @param document   XML文档
     * @param parentTree 路径
     * @return
     * @throws Exception <br>
     *                   e.g. document: <p1><p2><a></a><b></b></p2><p2><a></a><b></b></p2><p2><a></a><b></b></p2><</p1>
     *                   parentTree:  new String[]{"p1", "p2"}
     *                   return:   <a></a><b></b>构成的Node组成的集合List
     */
    public static List<Node> getTargetNodeList(Document document, String[] parentTree) throws Exception {
        String parent = parentTree[0];
        Node currentNode = null;
        NodeList nodeList = document.getElementsByTagName(parent);

        List<Node> ls = new ArrayList<Node>();

        if (nodeList.getLength() < 1) {
            throw new Exception("Element " + parent + " not found");
        }
        nodeList = nodeList.item(0).getChildNodes();
        for (int i = 1; i < parentTree.length; i++) {
            parent = parentTree[i];
            //System.out.println("i=" + i + ", " + parent);
            for (int j = 0; j < nodeList.getLength(); j++) {
                currentNode = nodeList.item(j);
                // System.out.println("j=" + j + "," + currentNode.getNodeName());
                if (currentNode.getNodeName().equalsIgnoreCase(parent)) {
                    if (i < parentTree.length - 1) {
                        //System.out.println("i=" + i + ", < parentTree.length - 1=" + (parentTree.length - 1));
                        nodeList = currentNode.getChildNodes();
                        break;
                    } else if (i == (parentTree.length - 1)) {//到了最后一个节点
                        //System.out.println("i=" + i + ", == parentTree.length - 1=" + (parentTree.length - 1));
                        ls.add(currentNode);
                    }
                }
            }
        }
        return ls;
    }


    /**
     * Add by wujiazhi 2015.7.21
     * 取得XML文档中目标节点的值
     * @param doc
     * @param nodePath
     * @return
     * @throws Exception
     */
    public static String getTargetNodeText(Document doc, String[] nodePath) throws Exception {
        Node node = XMLUtils.getTargetNode(doc, nodePath);
        if (node != null) {
            return node.getTextContent();
        }
        return "";
    }

    /**
     * Add by wujiazhi 2015.7.21
     * 取得XML文档中目标节点的属性值
     * @param doc
     * @param nodePath
     * @return
     */
    public static String getTargetNodeAttr(Document doc, String[] nodePath, String attr) throws Exception {
        Element node = (Element)XMLUtils.getTargetNode(doc, nodePath);
        if (node != null) {
            return node.getAttribute(attr);
        }
        return "";
    }

    /**
     * Add by wujiazhi 2015.7.21
     * 根据父节点和子节点名获取子节点内容
     * @param parent
     * @param childName
     * @return
     */
    public static String getTargetChildNodeText(Node parent, String childName) {
        if (parent == null) {
            return "";
        }

        NodeList childNodes = parent.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node currentNode = childNodes.item(j);
            if (currentNode.getNodeName().equalsIgnoreCase(childName)) {
                return currentNode.getTextContent();
            }
        }
        return "";
    }

    /**
     * 转换银行返回的响应消息，一般为xml字符串
     *
     * @param root    根节点
     * @param hashmap 转成的map
     * @return
     */
    public static HashMap makeMap(Element root, HashMap hashmap) {
        NodeList rootList = root.getChildNodes();
        if (null == rootList) {
            return null;
        }
        int length = rootList.getLength();
        for (int i = 0; i < length; i++) {
            if (rootList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element node = (Element) rootList.item(i);
                hashmap = makeMap(node, hashmap);

                if (null != node.getChildNodes().item(0) && node.getChildNodes().item(0).getNodeType() != Node.ELEMENT_NODE) {
                    if (node.getTextContent()==null) {
                        hashmap.put(node.getNodeName(), "");
                    } else {
                        hashmap.put(node.getNodeName(), node.getTextContent());
                    }
                }
            }
        }
        return hashmap;
    }


    public static HashMap getDataFromXml(String resXml) {
        HashMap data = new HashMap();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            Document doc = null;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new ByteArrayInputStream(resXml.getBytes("GBK")));
            Element root = doc.getDocumentElement();
            makeMap(root, data);
        }catch (Exception e){
            //throw new BankException(e,ResponseUtils.BANKGATE_COMMON_ERR, "解析银行返回报文异常");
        }
        return data;
    }
/*
    public static HashMap getRepFromXml(String resXml,String encoding) {
        HashMap data = new HashMap();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            Document doc = null;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new ByteArrayInputStream(resXml.getBytes(encoding)));
            Element root = doc.getDocumentElement();
            makeMap(root, data);
        }catch (Exception e){
            throw new BankException(e,ResponseUtils.BANKGATE_COMMON_ERR, "解析银行返回报文异常");
        }
        return data;
    }
*/

    //测试数据
    public static final String xmlStr = "<?xml version=\"1.0\"?>" +
            "<AP>\r\n" +
            "<TRANSTYPE>1001</TRANSTYPE>\r\n" +
            "<REQTIME>164930</REQTIME>" +
            "<REQDATE>20140605</REQDATE>" +
            "<MERCHNO>123123</MERCHNO>" +
            "<ACC>" +
            "<ACCNO>65</ACCNO>" +
            "<ACCNAME>安</ACCNAME>" +
            "<CERTNO>65012</CERTNO>" +
            "<MOBILEPHONE>134</MOBILEPHONE>" +
            "<PROTOCOLNO>mt</PROTOCOLNO>" +
            "<CARDTYPE>1</CARDTYPE>" +
            "<EXPDATE>2014</EXPDATE>" +
            "</ACC>" +
            "</AP>";

    public static final String xmlsign =
            "\n<?xml version=\"1.0\"?>\n\r" +
                    "<ap>\r\n" +
                    "<RespTime>150012</RespTime>\r\n" +
                    "<RespDate>20140612</RespDate>" +
                    "<RespCode>0000</RespCode>" +
                    "<Acc>" +
                    "<AccNo>65</AccNo>" +
                    "</Acc>" +
                    "</ap>\n";

    public static void main(String[] args) {
        try {


            String xmlDemo = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Tran><Header><txcode><![CDATA[AL0003]]></txcode><txseq><![CDATA[W20140808200806]]></txseq><txdate><![CDATA[20140808]]></txdate><tminf><![CDATA[13580018001]]></tminf><tran_response><status><![CDATA[FAIL]]></status><code><![CDATA[YDCP1KJZ0008]]></code><desc><![CDATA[流水记录不存在]]></desc></tran_response><txsign>HKqR6QzQ9Rkp6Sfrfl02m0SZxEAAf7DDz6vSrGZaIVako90OFMNfAaCf6Xt/7+yVM9I7+lvDX+Dg66VWAdeN2G3BeaAbs8rhujY3QvKVAygsK43ib160Fj67kz7nMH7DGo3pV4rlIZqjG2FLEhnCXHg0EjcoJveHvCmxcLRxfHg=</txsign></Header><Body><response/></Body></Tran>";

            String[] tree = new String[]{"Tran", "Header", "tran_response"};
//          String[] tree = new String[]{"Tran", "Header", "tran_response", "status"};


            // String value = addSubNode(xmlDemo, tree, "textName", "testValue");
         //   String value = removeNode(xmlDemo, tree, "status");

           // System.out.println(value);

            // Document document = getOriginDocByXmlStr(xmlDemo);

//              Node node = getTargetNode(document, tree);
//          System.out.println(node.getNodeName());
//            System.out.println(node.getTextContent());

            //System.out.println(node.getNodeName() + ":" + node.getTextContent());

//            XMLUtils xmlUtils = new XMLUtils(xmlStr);
//            String str = xmlUtils.getValueByTagName("orderid");
//            System.out.println(str);

            System.out.println("--------");
            String merId = "110";
            String outno = "123456";
            Map<String, String> xmlMap = new LinkedHashMap<String, String>();
            xmlMap.put("ThirdVoucher", outno);//批次凭证号
            xmlMap.put("SrcAccNo", merId);//企业账号
            xmlMap.put("TotalNum", "1");//总笔数
            xmlMap.put("TotalAmount", "222.23");//总金额
            Map<String, String> detailMap = new LinkedHashMap<String, String>();
            detailMap.put("SThirdVoucher", outno);//单笔凭证号
            detailMap.put("CstInnerFlowNo", outno);//自定义流水号
            detailMap.put("OppAccNo", "110");//付款人帐号
            detailMap.put("OppAccName", "110");//付款人户名
            detailMap.put("OppBank", "110");//付款人银行
            detailMap.put("CardAcctFlag", "110");//卡折标志
            detailMap.put("Amount", "110");//金额
            detailMap.put("IdType", "110");//证件类型
            detailMap.put("IdNo", "110");//证件号码
            xmlMap.put("HOResultSet4032R", XMLUtils.getEleStrFromMap(detailMap));
            String str =  XMLUtils.getXMLFromMap("Result", xmlMap, "GBK");

            System.out.println(str);
            String msg = generateMsg("666", str);

            System.out.println(msg);

            Map resMap = getResMap(msg);

            System.out.print("s");
        } catch (Exception e) {
            //log.error(e);
        }
    }

    private static Map getResMap(String resContent) throws Exception {
        byte[] bytes = resContent.getBytes("GBK");
        int len = bytes.length;
        byte[] bodyBytes = new byte[len - 222];
        System.arraycopy(bytes, 222, bodyBytes, 0, len - 222);
        String resXml = new String(bodyBytes);
        return XMLUtils.getDataFromXml(resXml);
    }

    private static String generateMsg(String outno, String body) throws Exception {
        int bodyLen = body.getBytes("GBK").length;//报文体长度
        outno = "666";

        StringBuilder msgSb = new StringBuilder();
        msgSb.append("A001");//1-报文版本，Char(4)
        //TODO 目标系统ID, 做成可配置的形式
        msgSb.append("11");//2-目标系统，Char(2)

        msgSb.append("01");//3-报文编码，Char(2)  01:GBK 02:UTF8 03:unicode 04:iso-8859-1,建议使用GBK
        msgSb.append("02");//4-通讯协议，Char(2)  01:tcpip(缺省) 02:http 03:webservice

        //TODO 外联客户代码，做成可配置的形式
        msgSb.append(padRight("999999", 20));//5-外联客户代码，Char(20)

        msgSb.append(padLeft(String.valueOf(bodyLen), 10));//6-接收报文长度，Num(10)，报文体数据的字节长度（提示：不是字符串的长度）

        //TODO 交易码-可配置形式
        msgSb.append(padRight("444", 6));//7-交易码，Char(6)

        msgSb.append("00000");//8-操做员代码，Char(5)，未启用检验域，建议送00000
        msgSb.append("01");//9-服务类型，Char(2)，01:请求 02:应答

        msgSb.append("20171010");//10-交易日期，Char(8)，yyyymmdd
        msgSb.append("121212");//11-交易时间，Char(6)，hhmmss

        //TODO 补齐？
        msgSb.append(padRight(outno, 20));//12-请求方系统流水号，Char(20)，唯一标识一笔交易（企业随机生成唯一流水号）

        msgSb.append("000000");//13-返回码，Char(6)，请求时必须填写000000，返回时非“000000”代表交易受理异常或失败

        //TODO 补齐
        msgSb.append(padRight(null, 100));//14-返回描述，Char(100)，格式为 “:交易成功”；其中冒号为英文格式半角。

        msgSb.append("0");//15-后续包标志，Char(1)，目前仅支持0
        msgSb.append("000");//16-请求次数，Num(3)，目前仅支持000
        msgSb.append("0");//17-签名标识，Char(1)，目前仅支持填0
        msgSb.append("1");//18-签名数据包格式，Char(1)，目前仅支持送1

        //TODO 补齐
        msgSb.append(padRight("RSA-SHA1", 12));//19-签名算法，Char(12)，RSA-SHA1

        //TODO 签名数据长度？？？
        msgSb.append(padLeft(null, 10));//20-签名数据长度，Num(10)，签名报文数据长度, 目前仅支填写0
        msgSb.append("0");//21-附件数目，Num(1)，0没有,默认为0；有的话填具体个数，最多9个

        msgSb.append(body);


        return msgSb.toString();
    }

    /**
     * 字符串右补空格
     *
     * @param str 原字符串
     * @param len 补齐后的字符串长度
     * @return    补齐后的字符串
     * @throws Exception
     */
    private static String padRight(String str, int len) throws Exception {
        if (str == null) {
            str = "";
        }
        if (len < 1) {
            throw new Exception("长度不能小于1");
        }
        int padLen = len - str.length();
        return str + getPadStr(" ", padLen);
    }

    /**
     * 数字左补0
     * @param str 原始数字对应的字符串
     * @param len 补齐后的字符串长度
     * @return    补齐后的字符串
     * @throws Exception
     */
    private static String padLeft(String str, int len) throws Exception {
        if (str == null) {
            str = "";
        }
        if (len < 1) {
            throw new Exception("长度不能小于1");
        }
        int padLen = len - str.length();
        return getPadStr("0", padLen) + str;
    }

    /**
     * 获取补齐的字符串
     *
     * @param padChar 字符
     * @param len 长度
     * @return
     * @throws Exception
     */
    private static String getPadStr(String padChar, int len) throws Exception {
        if (len == 0) {
            return "";
        }
        if (len < 1) {
            throw new Exception("补位长度不能为null或小于1");
        }
        if (padChar == null || "".equals(padChar)) {
            throw new Exception("补位字符不能为空");
        }
        int padLen = padChar.length();
        if (padLen > len) {
            throw new Exception("补位字符长度不能大于len");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }
}