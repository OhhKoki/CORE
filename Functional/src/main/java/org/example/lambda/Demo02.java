package org.example.lambda;

import lombok.val;
import java.util.function.IntBinaryOperator;

/**
 * Lambda 练习
 */
public class Demo02 {

    public static void main(String[] args) {
        // 使用匿名内部类的方式调用方法
        val result1 = calculateNum(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        });
        System.out.println(result1);

        // 使用 Lambda 优化匿名内部类
        val result2 = calculateNum((int left, int right) -> {
            return left + right;
        });
        System.out.println(result2);


    }

    public static int calculateNum(IntBinaryOperator operator) {
        int x = 10;
        int y = 20;
        return operator.applyAsInt(x, y);
    }

}
