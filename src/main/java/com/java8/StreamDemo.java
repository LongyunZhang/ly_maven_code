package com.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StreamDemo {
    public static void main(String[] args) {
        test1();
    }

    public static void test1() {

        List<String> myList =
                Arrays.asList("a1", "a2", "b1", "c2", "c1");
        //filter, map ，sorted是中间操作，而forEach是一个最终操作
        myList.stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);
        //输出 C1、C2

        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);  // a1


        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);


        List<String> strList = Arrays.asList("1", "2", "3");

        List<Long> selectedIds1 = strList.stream().map(it -> Long.valueOf(it)).collect(toList());
        List<Long> selectedIds2 = strList.stream().map(Long::parseLong).collect(Collectors.toList());
        System.out.println();
    }
}
