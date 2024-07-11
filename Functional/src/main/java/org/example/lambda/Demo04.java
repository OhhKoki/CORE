package org.example.lambda;

import java.util.function.Function;

/**
 * Lambda 练习
 */
public class Demo04 {

    public static void main(String[] args) {
        int result1 = typeConver(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return Integer.valueOf(s);
            }
        });
        System.out.println(result1);

        Integer result2 = typeConver((x -> Integer.valueOf(x)));
        System.out.println(result2);

    }

    public static <R> R typeConver(Function<String, R> function) {
        String str = "1234";
        R result = function.apply(str);
        return result;
    }

}
