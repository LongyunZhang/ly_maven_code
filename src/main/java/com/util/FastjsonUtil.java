package com.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Jackson提供了三种可选的Json处理方法：数据绑定(Data Binding)，流式API(Streaming API) 、树模型(Tree Model)。
 *
 * Data Binding我们主要使用ObjectMapper来操作Json，默认情况下会使用BeanSerializer来序列化POJO。
 *
 */
public class FastjsonUtil {
    public static void main(String[] args) throws ParseException, IOException {
        User user = new User();
        user.setName("小民");
        user.setEmail("xiaomin@sina.com");
        user.setAge(20);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        user.setBirthday(dateformat.parse("1996-10-01"));

        /**
         * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
         * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
         * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
         * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
         * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
         * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
         */
        ObjectMapper mapper = new ObjectMapper();//2.7.9版本，会报错

        //(1) User类 转 JSON
        String json = mapper.writeValueAsString(user);
        System.out.println(json);//输出结果：{"name":"小民","age":20,"birthday":844099200000,"email":"xiaomin@sina.com"}

        //(2) Java 集合转 JSON
        //输出结果：[{"name":"小民","age":20,"birthday":844099200000,"email":"xiaomin@sina.com"}]
        List<User> users = new ArrayList<User>();
        users.add(user);
        String jsonlist = mapper.writeValueAsString(users);
        System.out.println(jsonlist);


        //------- JSON转Java类[JSON反序列化] ----
        String json2 = "{\"name\":\"小红\",\"age\":18,\"birthday\":844099200111,\"email\":\"xiaohong@sina.com\"}";
        /**
         * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。
         */
        ObjectMapper mapper2 = new ObjectMapper();
        User user2 = mapper2.readValue(json2, User.class);
        System.out.println(user2);

        //------ Map 转 Json
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("user-1", user);
        userMap.put("user-2", user);
        try {
            System.out.println(mapper.writeValueAsString(userMap));//map转成String

            http://www.cnblogs.com/lexus/archive/2012/03/19/2406355.html
            mapper.writeValue(new File("/Users/longyun/Desktop/r.json"), userMap);//json写到文件里

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //------- JSON注解 -------
        //http://blog.csdn.net/accountwcx/article/details/24585987

        /*
        ObjectMapper mapper = new ObjectMapper();

//        当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，
//        因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //从json映射到java对象，得到country对象后就可以遍历查找,下面遍历部分内容，能说明问题就可以了
        TestJson testJson = mapper.readValue("{\"a\":\"1\",\"b\":\"2\",\"c\":\"test\",\"d\":\"true\",\"e\":\"e\"}", TestJson.class);
        System.out.println("a:" + testJson.getA());
        System.out.println("b:" + testJson.getB());
        System.out.println("c:" + testJson.getC());
        System.out.println("d:" + testJson.getD());
        */
    }

}

class TestJson {
    private Integer a;
    private Integer b;
    private String c;
    private Boolean d;

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public Boolean getD() {
        return d;
    }

    public void setD(Boolean d) {
        this.d = d;
    }
}

class User {
    private String name;
    private Integer age;
    private Date birthday;
    private String email;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}