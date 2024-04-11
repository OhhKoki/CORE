package org.example;

import lombok.extern.slf4j.Slf4j;

/**
 * 多线程练习题：模拟泡茶
 */
@Slf4j(topic = "c.Test04")
public class Test04 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                log.debug("洗水壶");
                Thread.sleep(1000);
                log.debug("烧开水");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "老王");
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                log.debug("洗茶壶");
                Thread.sleep(1000);
                log.debug("洗茶杯");
                Thread.sleep(1000);
                log.debug("拿茶叶");
                Thread.sleep(1000);
                log.debug("等开水");
                t1.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }, "小王");
        t2.start();
    }

}
