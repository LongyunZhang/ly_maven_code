package com.util;


import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PorpertiesUtil {

    static {
        try {
            Properties prop = new Properties();
            prop.load(new InputStreamReader(PorpertiesUtil.class.getClassLoader().getResourceAsStream("err_code.properties"), "UTF-8"));
            Iterator iterator =prop.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                System.out.println(entry.getKey().toString() +  entry.getValue().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
