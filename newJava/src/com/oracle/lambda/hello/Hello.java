package com.oracle.lambda.hello;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class Hello {

    public static void main(String[] args) {

        List<String> names = new ArrayList<String>();
        names.add("ZhangSan");
        names.add("LiSi");
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // lambda表达式
        Collections.sort(names, (a, b) -> a.compareTo(b));
        System.out.println(names);

        // lambda+stream
        List<String> list = names.stream().map((String name) -> {
            return name.toLowerCase();
        }).collect(Collectors.toList());
        // 简化的lambda表达式。。。。。。。。
        names.stream().map(name -> name.toLowerCase()).collect(Collectors.toList());
        // 使用Method Reference
        names.stream().map(String::toLowerCase).collect(Collectors.toList());
        // Google Guava
        List<String> lowercaseList = FluentIterable.from(names).transform(new Function<String, String>() {
            @Override
            public String apply(String name) {
                return name.toLowerCase();
            }
        }).toList();

        System.out.println(lowercaseList);

        String[] array = { "a", "b", "c" };
        for (Integer i : Lists.newArrayList(1, 2, 3)) {
            Stream.of(array).map(item -> Strings.padEnd(item, i, '@')).forEach(System.out::println);
        }

        // for (int i = 0; i < 4; i++) {
        // Stream.of(array).map(item -> Strings.padEnd(item, i,
        // '@')).forEach(System.out::println);
        // }

        /**
         * stream
         * 
         */

        ArrayList<Integer> l = Lists.newArrayList(1, null, 2, null, 5);
        long count = l.stream().filter(num -> num != null).count();
        System.out.println(count);

        Stream<String> stringStream = Stream.of("taobao");
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6);

        Stream.generate(new Supplier<Double>() {

            @Override
            public Double get() {
                return Math.random();
            }
        });
        Stream.generate(() -> Math.random());

        Stream.generate(Math::random);

        // Stream.iterate(1, item -> item +
        // 1).limit(10).forEach(System.out::println);

        List<Integer> nums = Lists.newArrayList(1, 1, 2, null, 3, null, 4, 4, 5, 5, 5, null, 6, 7, 8, 8, 8, 0);

        int sum = nums.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2).peek(System.out::println)
                .skip(2).limit(33).sum();
        System.out.println(sum);

        while (true) {
            new Thread().start();
        }

    }

}
