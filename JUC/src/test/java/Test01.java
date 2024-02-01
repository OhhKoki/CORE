import org.junit.Test;

/**
 * 创建和运行线程的两种方式
 *      1、创建一个 Thread 类的实例，并重写 run() 方法，然后调用 Thread 类的 start() 方法启动线程
 *      2、创建一个 Runnable 接口的实例，然后将该实例传入 Thread 类的构造，最后调用 Thread 类的 start() 方法启动线程
 */
public class Test01 {

    /**
     * 方式一，通过 Thread 创建线程
     *      1、创建 Thread 类的实例 t，并重写 run()
     *      2、使用 t.start() 启动线程
     */
    @Test
    public void test01() {
        Thread t = new Thread(){
            public void run() {
                System.out.println("通过 Thread 类创建的线程");
            }
        };
        t.start();
    }

    /**
     * 方式二，通过 Runnable 接口创建线程
     *      1、创建 Runnable 接口的实例 r，并重写 run()
     *      2、将 r 传入 Thread 的构造得到实例 t
     *      2、使用 t.start() 启动线程
     */
    @Test
    public void test02() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("通过 Runnable 接口创建的线程");
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

}