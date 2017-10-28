package com.demo;

public class ExceptionDemo {

    public static void main(String[] args) {
        try{
            fun();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

     public static void fun() throws Exception {
        try {
            int a = 5, b =0;
            System.out.println(5/b);
        } catch (Exception e) {
            throw new Exception("异常啦啦啦");
        }

     }
}
