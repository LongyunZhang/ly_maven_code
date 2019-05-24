package com.java8;

import com.bean.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Java8Demo {

    public static void main(String[] args) {
        t6();
        //t3();

//        String aa = "getDspStats";
//        System.out.println("结果：" + (aa == "getDspStats"));

        //t5();
        //t3();
//        t1();
//
//        t2();

//        List<String> list = new ArrayList<>();
//        list.add("1");
//
//        System.out.println();
//        t3(list);
//        System.out.println("list.size(): " + list.size());
    }

    private static void t3(List<String> list) {
        list.add("3");
    }

    private static void t1() {

        //String longStr1 = "1,2,3,4,5";
        String longStr1 = "";
        //String longStr1 = "1,2,3,4,5";
        String longStr2 = "1,2,3,4,5,6";
        String[] arr = longStr1.split(",");
        if (arr != null) {
            int[] x = Arrays.stream(arr).mapToInt(Integer::valueOf).toArray();
            Arrays.stream(x).forEach( (i)-> System.out.print(i + " ") );
            System.out.println();
        }


        long[] y = Arrays.stream(longStr2.split(",")).mapToLong(Long::valueOf).toArray();
        Arrays.stream(y).forEach( (i)-> System.out.print(i + " ") );
    }


    //提取出list中bean的某一属性
    public static void tttttt() {

        List<List<String>> allList = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list1.add("1");
        list1.add("zhangsan");

        System.out.println("1： " + list1.contains("zhangsan"));

        List<String> list2 = new ArrayList<>();
        list2.add("2");
        list2.add("liwu");
        allList.add(list1);
        allList.add(list2);

        //1.提取出list对象中的一个属性
        List<Long> stIdList1 = allList.stream().map(it -> Long.valueOf(it.get(0))).collect(toList());
        System.out.println(stIdList1);

        allList.forEach(it->{
            Collections.replaceAll(it, it.get(0), "kkk");
        });

        System.out.println(allList);
    }

    //提取出list中bean的某一属性
    public static void t2() {
        List<Student> stuList = new ArrayList<Student>();
        Student st1 = new Student("123","aaa");
        Student st2 = new Student("234","bbb");
        Student st3 = new Student("345","ccc");
        Student st4 = new Student("345","ccc");
        stuList.add(st1);
        stuList.add(st2);
        stuList.add(st3);
        stuList.add(st4);

        Student new_student = stuList.get(0);
        new_student.setName("new_student");
        System.out.println();


        //1.提取出list对象中的一个属性
        List<String> stIdList1 = stuList.stream()
                .map(Student::getName)
                .collect(toList());
        stIdList1.forEach(s -> System.out.print(s+" "));

        System.out.println("\n----------");
        //2.提取出list对象中的一个属性并去重
        List<String> stIdList2 = stuList.stream()
                .map(Student::getId).distinct()
                .collect(toList());
        stIdList2.forEach(s -> System.out.print(s+" "));
		/*	结果：
			123 234 345 345
			----------
			123 234 345
		*/
    }

    //
    public static void t3() {
        List<String> list1 = new ArrayList();
        list1.add("1111");
        list1.add("2222");
        list1.add("3333");

        List<String> listttt = new ArrayList<>();
        listttt.addAll(list1);
        Collections.replaceAll(listttt, listttt.get(2), "5555");

        System.out.println("listttt:");
        listttt.forEach(it -> {
            System.out.print(it + " ");
        });
        System.out.println();

        System.out.println("list1:");
        list1.forEach(it -> {
            System.out.print(it + " ");
        });
        System.out.println();


        List<String> list2 = new ArrayList<>();
        list2.add("1");
        list2.add("2");
        System.out.println(list2.indexOf("2"));


        List<String> list = null;
        //会空指针异常
        list.forEach(it -> {
            System.out.println();
        });
    }

    //差集
    public static void t5() {
        System.out.println();

        List<String> list1 = new ArrayList();
        list1.add("1111");
        list1.add("2222");
        list1.add("3333");

        List<String> list2 = new ArrayList();
        list2.add("3333");
        list2.add("4444");
        list2.add("5555");

        // 差集 (list1 - list2)
        List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(toList());
        System.out.println("---得到差集 reduce1 (list1 - list2)---");
        reduce1.parallelStream().forEach(System.out :: println);
    }

    public static void t6() {
        String vvv = "1234567890abcdefgh";
        System.out.println(vvv.substring(0, vvv.length() - 10));

        String grade = "aaa";

        switch(grade)
        {
            case "aaa" :
                System.out.println("优秀");
                break;
            case "bbb" :
                System.out.println("良好");
                break;
            default :
                System.out.println("未知等级");
        }
        System.out.println("你的等级是 " + grade);
    }

}
