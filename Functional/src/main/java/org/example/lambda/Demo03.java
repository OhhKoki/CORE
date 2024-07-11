package org.example.lambda;

import java.util.function.IntPredicate;

/**
 * Lambda 练习
 */
public class Demo03 {

    public static void main(String[] args) {
        // 使用匿名内部类的方式调用
        printNum(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        });

        // 使用 Lambda 优化匿名内部类
        printNum((int value) -> {
            return value % 2 == 0;
        });

    }

    public static void printNum(IntPredicate predicate) {
        int[] array = {1, 2, 3};
        for (int item : array) {
            if (predicate.test(item)) {
                System.out.println(item);
            }
        }
    }

}
