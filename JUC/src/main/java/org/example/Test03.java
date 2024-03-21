package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test03")
public class Test03 {

    public static void main(String[] args) {
        // New
        Thread t1 = new Thread(() -> {}, "t1");

        // Runnable: Runnable & Running
        Thread t2 = new Thread(() -> {
            synchronized (Test03.class) {
                while (true) {
                    // 分到时间片时为 Running
                    // 没分到时间片为 Runnable
                    try {
                        // 防止空跑循环浪费 CPU
                        // sleep() 不会释放锁
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }, "t2");
        t2.start();

        // Blocked
        Thread t3 = new Thread(() -> {
            // 由于先执行的 t2 线程，所以 t2 线程获取了对象锁
            // 且由于 t2 在死循环，一直没释锁，导致 t3 获取不到锁，t3 进入 Blocked 状态等待 t2 释放锁
            synchronized (Test03.class) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }, "t3");
        t3.start();

        // Waiting
        Thread t4 = new Thread(() -> {
            try {
                // 等待 t2 执行完毕才能接着往下执行，进入阻塞状态
                t2.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }, "t4");
        t4.start();

        // Timed_Waiting
        Thread t5 = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }, "t5");
        t5.start();

        // Terminated
        Thread t6 = new Thread(() -> {}, "t6");
        t6.start();

        log.debug("t1 state: {}", t1.getState());
        log.debug("t2 state: {}", t2.getState());
        log.debug("t3 state: {}", t3.getState());
        log.debug("t4 state: {}", t4.getState());
        log.debug("t5 state: {}", t5.getState());
        log.debug("t6 state: {}", t6.getState());
    }

}
