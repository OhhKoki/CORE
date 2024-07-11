package org.example.lambda;

/**
 * Lambda 表达式入门
 */
public class Demo01 {

    public static void main(String[] args) {
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

}
