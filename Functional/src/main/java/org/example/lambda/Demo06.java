package org.example.lambda;

import java.util.function.IntFunction;

/**
 * Lambda 的省略规则
 *      1、参数类型可以省略
 *      2、形参列表只有一个参数时，小括号可以省略
 *      3、方法体只有一句代码时，大括号 & return & 分号 都可以省略
 */
public class Demo06 {

    public static void main(String[] args) {
        System.out.println("----- 匿名内部类形式-----");
        calculator((new IntFunction<Integer>() {
            @Override
            public Integer apply(int x) {
                return x * 2;
            }
        }));

        System.out.println("----- Lambda 形式-----");
        calculator((int x) -> {
            return x * 3;
        });

        System.out.println("----- Lambda 省略参数列表形式-----");
        calculator(x -> {
            return x * 4;
        });

        System.out.println("----- Lambda 省略大括号形式-----");
        calculator(x -> x * 5);
    }

    public static void calculator(IntFunction<Integer> function) {
        int x = 10;
        System.out.println(function.apply(x));
    }

}
