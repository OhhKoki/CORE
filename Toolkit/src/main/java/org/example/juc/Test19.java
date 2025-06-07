package org.example.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j(topic = "c.Test19")
public class Test19 {
    public static void main(String[] args) {
        // 创建一个信号量，初始许可数为3
        Semaphore semaphore = new Semaphore(3);

        // 创建多个线程，模拟多个线程访问共享资源
        for (int i = 0; i < 5; i++) {
            new Thread(new Worker(semaphore)).start();
        }
    }
}

@Slf4j(topic = "c.Worker")
class Worker implements Runnable {
    private Semaphore semaphore;

    public Worker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            // 获取许可
            semaphore.acquire();
            log.debug("{} is working.", Thread.currentThread().getName());
            // 模拟工作
            Thread.sleep(2000);
            log.debug("{} finished working.", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 释放许可
            semaphore.release();
        }
    }
}
