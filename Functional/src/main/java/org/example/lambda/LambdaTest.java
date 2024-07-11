package org.example.lambda;

import java.util.function.IntFunction;

public class LambdaTest {

    public static void main(String[] args) {
        test02();
    }

    /**
     * Lambda 入门案例
     */
    private static void test01() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程 t1 执行");
            }
        }, "t1").start();


        // 使用 Lambda 表达式简化匿名内部类
        /**
         * 直接把参数列表和方法体拷过来
         *      只关心要操作的数据（参数列表）和要进行什么操作（方法体）
         *      () {
         *          System.out.println("线程 t1 执行");
         *      }
         *
         * Lambda 表达式的格式为 () -> {}，所以只需要再加一个 ->
         *      () -> {
         *          System.out.println("线程 t1 执行");
         *      }
         *
         * 简而言之，将匿名内部类的 参数列表 & 方法体 拷出来，中间再加一个 -> 就构成了 Lambda 表达式
         *
         */
        new Thread(() -> {
            System.out.println("线程 t2 执行");
        }, "t2").start();
    }

    /**
     * Lambda 的省略规则
     *      1、参数类型可以省略
     *      2、形参列表只有一个参数时，小括号可以省略
     *      3、方法体只有一句代码时，大括号 & return & 分号 都可以省略
     */
    private static void test02() {
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

    private static void calculator(IntFunction<Integer> function) {
        int x = 10;
        System.out.println(function.apply(x));
    }

}
