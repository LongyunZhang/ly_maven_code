package com.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

    public static void  main(String[] args){
        //-----------字符串判空-----------
        String s1 = null;
        String s2 = "";
        String s3 = "  ";
        System.out.println(StringUtils.isNotEmpty(s1));//false
        System.out.println(StringUtils.isNotEmpty(s2));//false
        System.out.println(StringUtils.isNotEmpty(s3));//true

        System.out.println(StringUtils.isEmpty(s1));//true
        System.out.println(StringUtils.isEmpty(s2));//true
        System.out.println(StringUtils.isEmpty(s3));//false

        //-----------字符串连接-----------
        // print str1
        String ss1 = "self";
        System.out.println(ss1);

        // print str2 concatenated with str1
        String ss2 = ss1.concat(" learning");
        System.out.println(ss2);

        //-----------填充-----------
        //左填充
        String str1 = "12345";
        String str2 = StringUtils.leftPad(str1, 10, '0');
        System.out.println("str1:" + str1);
        System.out.println("str2:" + str2);

        //右补, 补齐的长度小于原字符串长度，返回原字符串
        String str3 = "12345";
        String str4 = StringUtils.rightPad(str3, 10, ' ');
        System.out.println("str3:" + str3 + "---");
        System.out.println("str4:" + str4 + "---");

        //原字符串为null，返回null
        String str5 = null;
        String str6 = StringUtils.rightPad(str5, 10, ' ');
        System.out.println("str5:" + str5 + "---");
        System.out.println("str6:" + str6 + "---");

        String str7 = "";
        String str8 = StringUtils.rightPad(str7, 10, ' ');
        System.out.println("str7:" + str7 + "---");
        System.out.println("str8:" + str8 + "---");
    }

    public static String addZero(String str, int length) {
        int strlen = str.length();
        for (int i = 0; i < length - strlen; i++) {
            str = "0" + str;
        }
        return str;
    }

    public static String addSpace(String str, int length, String encoding) {
        int strlen = 0;
        StringBuffer sb = new StringBuffer(str);
        try {
            if (encoding == null || encoding.trim().equals(""))
                strlen = str.getBytes().length;
            else
                strlen = str.getBytes(encoding).length;
        } catch (Exception e) {
            return str;
        }
        for (int i = 0; i < length - strlen; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
