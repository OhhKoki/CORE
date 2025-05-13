package org.example.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * 在方法声明上加对象锁的注意点
 */
@Slf4j(topic = "c.Test08")
public class Test08 {

    public synchronized void test01() {

    }

    // test01 等同于 test02，锁的是【this】

    public void test02() {
        synchronized (this) {

        }
    }

    public synchronized static void test03() {

    }

    // test03 等同于 test04，锁的是【类对象】

    public static void test04() {
        synchronized (Test08.class) {

        }
    }

}
