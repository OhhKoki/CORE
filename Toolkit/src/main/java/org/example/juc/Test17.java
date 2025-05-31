package org.example.juc;

import java.util.concurrent.atomic.AtomicInteger;

public class Test17 {
    public static void main(String[] args) {
        // 账户余额
        AtomicInteger balance = new AtomicInteger(1000);
        // 扣除金额
        int amount = 10;
        // 使用 CAS 进行扣钱操作
        while (true) {
            // 当前值：比如拿到了旧值 1000
            int prev = balance.get();
            // 新值：在这个基础上 1000-10 = 990
            int next = prev - amount;
            /**
             * compareAndSet的操作流程：
             *      1、读取：从内存位置读取当前的值（立即从主存获取最新的值）。
             *      2、比较：将当前值与预期值（prev）进行比较。
             *      3、交换：如果当前值等于预期值（prev），则将内存位置的值替换为新值（next）；如果不相等，则什么都不做。
             */
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}
