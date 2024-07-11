package org.example.lambda;

import java.util.function.IntConsumer;

/**
 * Lambda 练习
 */
public class Demo05 {

    public static void main(String[] args) {
        foreachArray(new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.println(value * 2);
            }
        });

        System.out.println("----------");

        foreachArray(x -> System.out.println(x * 3));
    }

    public static void foreachArray(IntConsumer consumer) {
        int[] array = {2, 3};
        for (int item : array) {
            consumer.accept(item);
        }
    }

}
