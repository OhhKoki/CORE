package org.example;

import lombok.extern.slf4j.Slf4j;

/**
 * 两个线程对初始值为 0 的静态变量一个做自增，一个做自减，各做 5000 次，结果是 0 吗?
 */
@Slf4j(topic = "c.Test05")
public class Test05 {

    static int counter = 0;

    /**
     * 执行结果可能是正数、负数、零。为什么呢？
     *      因为 Java 中对静态变量的自增，自减并不是原子操作，要彻底理解，必须从字节码来进行分析：
     *          对于静态变量而言（i 为静态变量），实际会产生如下的 JVM 字节码指令:
     *              getstatic   i   // 获取静态变量 i 的值（从主存获取值到工作内存）
     *              iconst_i        // 准备一个常量，且值为 1，用于准备进行自增操作
     *              iadd            // 进行自增操作
     *              putstatic   i   // 将自增后的值保存到静态变量 i 中（将值从工作内存保存到主存）
     *          又由于计算机是采用分时系统，所以多个线程间会存在上下文切换，所以会出现自增字节码指令还没执行完毕，进行上下文切换，导致数据错乱
     *              比如 t1 线程执行自增操作，执行 iadd 完成后，cup 时间片使用完毕，需要进行上下文切换（修改后的值还没从工作内存保存到主存
     *              这时 t2 线程执行自减操作，执行 getstatic 时，从主存中获取到的值为 0，而不是 1，然后进行自减后将 -1 从工作内存保存到主存
     *              然后 t2 运行完毕，轮到 t1 运行，t1 从上下文信息得知自己该执行 putstatic 指令了
     *              此时 t1 往下运行 putstatis 指令，将之前的运算结果 1 保存到主存，这样就出现了数据错乱！！！
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter++;
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                counter--;
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("{}",counter);

    }

}
