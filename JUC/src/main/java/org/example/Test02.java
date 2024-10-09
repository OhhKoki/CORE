package org.example;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.LockSupport;

/**
 * Thread 类的常用方法
 */
@Slf4j(topic = "c.Test02")
public class Test02 {

    private static int r1 = 1;
    private static int r2 = 2;

    public static void main(String[] args) {
        method08();
    }

    /**
     * sleep()
     */
    private static void method01() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            log.debug("子线程执行结束");
        });
        thread.start();
        log.debug("主线程执行结束");
    }

    /**
     * sleep() 小妙招：死循环防止 CUP 爆满
     */
    private static void method02() {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    // 防止没有事干时的空转，让 CPU 去运行其他线程
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
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
                log.error(e.getMessage());
            }
            // private static int r1 = 1;
            r1 = 10;
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
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
            log.error(e.getMessage());
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
                log.debug("子线程进入休眠");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            log.debug("子线程执行结束");
        });
        thread.start();
        try {
            // 防止子线程还没休眠就开始打断
            log.debug("主线程进入休眠");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        log.debug("主线程休眠结束");
        thread.interrupt();
        // sleep, wait, join 被打断后会清除标记
        log.debug("子线程是否被 Interrupt 过：{}", thread.isInterrupted());
        log.debug("主线程执行结束");
    }

    /**
     * isInterrupted() 的小妙招：打断死循环
     */
    private static void method05() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    log.debug("子线程执行结束");
                    break;
                }
            }
        });
        thread.start();
        try {
            log.debug("主线程进入休眠");
            // 防止子线程还没休眠就开始打断
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        log.debug("打断子线程休眠");
        thread.interrupt();
        // sleep, wait, join 被打断后会清除标记
        log.debug("子线程是否被 Interrupt 过：{}", thread.isInterrupted());
        log.debug("主线程执行结束");
    }

    /**
     * 两阶段终止模式
     */
    private static void method06() {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        tpt.stop();
    }

    static class TwoPhaseTermination {
        private Thread monitor;

        private void start() {
            monitor = new Thread(() -> {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        log.debug("暂停监控");
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                        // 在执行这句语句的时候，有可能会被打断，这种情况不用额外处理，此时打断标记不会被清空（休眠过程中打断才会抛出 sleep interrupted 异常
                        log.debug("正在执行监控");
                    } catch (InterruptedException e) {
                        log.error("监控休眠时被打断，异常信息为：{}", e.getMessage());
                        // sleep, wait, join 执行过程中被打断时，打断标记会被清空
                        // 重置打断标记（醒着的时候执行，保证打断标记为 true
                        monitor.interrupt();
                    }
                }
            });
            monitor.setName("monitor");
            monitor.start();
        }

        private void stop() {
            monitor.interrupt();
        }
    }

    /**
     * 打断正在 park 的线程
     */
    private static void method07() {
        Thread thread = new Thread(() -> {
            log.debug("执行 LockSupport.park()，子线程处于 Blocked 状态");
            LockSupport.park();

            log.debug("主线程执行了 interrupt()，打断了子线程的 Blocked 状态");

            // 不会清除打断标记，返回 true，后续无法继续 park
            log.debug("子线程执行 isInterrupted()，返回的打断标记状态为：{}", Thread.currentThread().isInterrupted());

            // 会清除打断标记（返回 false，后续可以继续 park
//            log.debug("子线程执行 Thread.interrupted()，返回的打断标记状态为：{}", Thread.interrupted());

            // 执行 Thread.interrupted() 会清空状态，重新置为 false
            log.debug("此时打断标记为：{}", Thread.currentThread().isInterrupted());

            // 如果打断标记已经是 true, 则 park 会失效
            LockSupport.park();
            log.debug("子线程继续执行 LockSupport.park()");
        });
        thread.start();

        try {
            log.debug("主线程进入休眠");
            Thread.sleep(2000);
            log.debug("主线程执行 interrupt()");
            thread.interrupt();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        log.debug("主线程执行结束");
    }

    /**
     * setDaemon()
     */
    private static void method08() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("子线程执行结束");
        });
        thread.setDaemon(true);
        thread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        log.debug("主线程执行结束");
    }

}
