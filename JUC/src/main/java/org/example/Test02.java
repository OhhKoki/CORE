package org.example;

public class Test02 {

    private static int r1 = 1;
    private static int r2 = 2;

    public static void main(String[] args) {
        method05();
    }

    /**
     * sleep()
     */
    private static void method01() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("子线程执行结束");
        });
        thread.start();
        System.out.println("主线程执行结束");
    }

    /**
     * sleep() 小妙招：死循环防止 CUP 爆满
     */
    private static void method02() {
        Thread thread = new Thread(() -> {
            try {
                // 防止没有事干时的空转，让 CPU 去运行其他线程
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    /**
     * join()
     */
    private static void method03() {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // private static int r1 = 1;
            r1 = 10;
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // private static int r2 = 2;
            r2 = 20;
        });

        thread1.start();
        thread2.start();
        try {
            // 需要等待子线程执行完毕
            thread2.join();
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("r1 = " + r1);
        System.out.println("r2 = " + r2);
    }

    /**
     * interrupt()
     */
    private static void method04() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("子线程执行结束");
        });
        thread.start();
        try {
            // 防止子线程还没休眠就开始打断
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        thread.interrupt();
        // sleep, wait, join 被打断后会清除标记
        System.out.println("子线程是否被 Interrupt 过：" + thread.isInterrupted());
        System.out.println("主线程执行结束");
    }

    /**
     * isInterrupted() 的小妙招：打断死循环
     */
    private static void method05() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("子线程执行结束");
                    break;
                }
            }
        });
        thread.start();
        try {
            // 防止子线程还没休眠就开始打断
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        thread.interrupt();
        // sleep, wait, join 被打断后会清除标记
        System.out.println("子线程是否被 Interrupt 过：" + thread.isInterrupted());
        System.out.println("主线程执行结束");
    }
}
