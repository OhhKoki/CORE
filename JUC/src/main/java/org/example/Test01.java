package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的三种方式
 */
@Slf4j(topic = "c.Test01")
public class Test01 {
    public static void main(String[] args) {
        method03();
    }

    /**
     * 方式一，直接通过 Thread 创建
     */
    private static void method01() {
        // 创建线程
        Thread thread = new Thread(){
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                log.debug("通过 Thread 类创建的线程");
            }
        };
        // 启动线程
        thread.start();
    }

    /**
     * 方式二，Runnable 配合 Thread
     */
    private static void method02() {
        // 创建任务
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.debug("通过 Runnable 接口创建的线程");
            }
        };

        // 创建并启动线程
        new Thread(runnable).start();
    }

    /**
     * 方式三，FutureTask 配合 Thread
     */
    private static void method03() {
        // 创建任务
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                log.debug("通过 Callable 接口创建的线程");
                return 1;
            }
        };

        // 将任务封装进 FutureTask 中，以便后续从该线程中获取处理结果
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        // 创建并启动线程
        new Thread(futureTask).start();

        // 获取线程执行的结果
        // 主线程执行到该 get() 方法时，会进入阻塞状态，直到子线程执行完毕
        try {
            System.out.println(futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
