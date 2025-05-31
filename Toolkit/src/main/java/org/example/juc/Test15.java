package org.example.juc;

public class Test15 {
    private int count = 0;

    // 增加计数的方法
    public void increment() {
        // 这里是非原子的操作，多个线程同时执行时会发生线程安全问题
        count++;
    }

    // 获取计数值
    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        Test15 counter = new Test15();

        // 启动多个线程并发增加计数
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // 多个线程并发修改count值
                    counter.increment();
                }
            });
            threads[i].start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // 由于没有线程安全保护，输出的结果通常小于预期值1000000
        System.out.println("最终计数值：" + counter.getCount());
    }
}
