package org.example.juc;
import java.util.concurrent.atomic.AtomicInteger;

public class Test16 {
    private AtomicInteger count = new AtomicInteger(0);

    // 增加计数的方法，使用CAS确保线程安全
    public void increment() {
        count.incrementAndGet(); // CAS操作，原子地增加count
    }

    // 获取计数值
    public int getCount() {
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Test16 counter = new Test16();

        // 启动多个线程并发增加计数
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment(); // 使用CAS保证线程安全
                }
            });
            threads[i].start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // 由于使用CAS保护，最终计数值应该是1000000
        System.out.println("最终计数值：" + counter.getCount());
    }
}
