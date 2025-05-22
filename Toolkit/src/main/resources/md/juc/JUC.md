# 1、理论基础

既然多线程并发访问共享资源会导致静态条件的出现，那么为什么还要用多线程呢？

多线程并发访问共享资源导致数据错乱的根本原因是什么？

Java 针对这些问题的解决方案是什么？



## 1.1 并发编程的 Bug 源头

由于 `CPU` 和 `内存` & `I/O 设备` 之间的速度是有极大差异的，为了合理利用 CPU 的高性能，平衡这三者的速度差异，计算机体系结构、操作系统、编译程序都做出了相应的优化，主要体现为:

- CPU 增加了缓存，以均衡与内存的速度差异；// 导致 `可见性`问题
- 操作系统增加了进程、线程，以分时复用 CPU，进而均衡 CPU 与 I/O 设备的速度差异；// 导致 `原子性`问题
- 编译程序优化指令执行次序，使得缓存能够得到更加合理地利用。// 导致 `有序性`问题



### 1.1.1 缓存导致的可见性问题

可见性：一个线程对共享变量的修改，另外一个线程能够立刻看到。



单核时代，不同线程操作同一个 CPU 里面的缓存，不存在可见性问题。

<img src="./assets/picture1.png" alt="image" style="zoom:50%;" />

多核时代，每颗 CPU 都有自己的缓存，不同的线程操作不同 CPU 的缓存时，对彼此之间就不具备可见性了。

<img src="./assets/picture2.png" alt="多核CUP缓存与内存的关系" style="zoom:50%;" />



### 1.1.2 线程切换导致的原子性问题

原子性：即一个操作或者多个操作要么全部执行并且执行的过程不会被任何因素打断，要么就都不执行。



线程只有被分配了 CPU 时间片后才能执行，线程切换的时机大多数是在时间片结束的时候。

<img src="./assets/picture3.png" alt="线程切换" style="zoom:50%;" />

我们现在使用的高级程序语言一条语句往往对应多条 CPU 指令，例如语句：count += 1，至少需要三条 CPU 指令。

- 指令1：首先，需要把变量 count 从内存加载到 CPU 的寄存器；
- 指令2：之后，在寄存器中执行 +1 操作；
- 指令3：最后，将结果写入内存（缓存机制导致可能写入的是 CPU 缓存而不是内存）;



操作系统做任务切换，可以发生在任何一条 CPU 指令执行完。这就可能得到意想不到的结果。

<img src="./assets/picture4.png" alt="非原子操作的执行路径关系" style="zoom: 25%;" />

我们把一个或者多个操作在 CPU 执行的过程中不被中断的特性成为**原子性**。CPU 能够保证原子操作是 CPU 指令级别的，但不是高级语言的操作符，因此很多时候我们需要在高级语言层面保证操作的原子性。



### 1.1.3 编译优化导致的有序性问题

有序性：即程序执行的顺序按照代码的先后顺序执行。编译器为了优化性能，有时候会改变程序中语句的先后顺序。



双重检查创建单例对象

```java
public class Singleton {
  static Singleton instance;
  static Singleton getInstance(){
    if (instance == null) {
      synchronized(Singleton.class) {
        if (instance == null)
          instance = new Singleton();
      }
    }
    return instance;
  }
}
```



instance = new Singleton() 语句经过编译优化重排序后的CPU执行过程可能是：

- 分配一块内存M；
- 将M的地址赋值给instance实例；
- 最后再内存M上初始化Singleton对象。



当 `线程A` 执行完 `指令2` 时，发生 `线程切换`，`线程B` 调用 `getInstance()` 方法，获得未初始化的 `Singleton` 对象，如果此时访问对象成员变量，那么就可能触发空指针异常。

<img src="./assets/picture5.png" alt="双重检查创建单例对象异常执行路径" width="600"  />



## 2. Java 如何解决可见性和有序性

可见行问题是由缓存导致的，有序性问题是由编译优化导致的，因此只要禁用缓存和编译优化就可以解决可见性和有序性问题。考虑到性能问题就需要根据实际情况按需禁用。



那么在 Java 中怎么按需禁用 `缓存` 和 `编译优化` 呢？

> Java 内存模型规范了 JVM 如何提供按需禁用缓存和编译优化的方法。具体来说，这些方法包括 volatile，synchronized 和 final 三个关键字，以及六项 Happens-Before 规则。



[https://blog.csdn.net/c15158032319/article/details/117361782#:~:text=Java内存模型与硬](https://blog.csdn.net/c15158032319/article/details/117361782#:~:text=Java内存模型与硬)



# 2、线程基础

线程有哪几种状态? 分别说明从一种状态到另一种状态转变有哪些方式?

通常线程有哪几种使用方式?

基础线程机制有哪些?

线程的中断方式有哪些?

线程的互斥同步方式有哪些? 如何比较和选择?

线程之间有哪些协作方式?



## 2.1 线程状态转换

线程各个状态转换的示意图如下

![image](./assets/picture6.png)

### 2.1.1 新建(New)

创建后尚未启动。

调用了 start()，仅仅是在语言层面创建了一个线程，此时还没有与操作系统进行关联。



### 2.1.2 可运行(Runnable)

可能正在运行，也可能正在等待 CPU 时间片。

包含了操作系统线程状态中的 Running 和 Ready。



### 2.1.3 阻塞(Blocking)

等待获取一个排它锁，如果其线程释放了锁就会结束此状态。



### 2.1.4 无限期等待(Waiting)

等待其它线程显式地唤醒，否则不会被分配 CPU 时间片。

| 进入方法                                   | 退出方法                             |
| ------------------------------------------ | ------------------------------------ |
| 没有设置 Timeout 参数的 Object.wait() 方法 | Object.notify() / Object.notifyAll() |
| 没有设置 Timeout 参数的 Thread.join() 方法 | 被调用的线程执行完毕                 |
| LockSupport.park() 方法                    | LockSupport.unpark()                 |



### 2.1.5 限期等待(Timed Waiting)

无需等待其它线程显式地唤醒，在一定时间之后会被系统自动唤醒。

调用 Thread.sleep() 方法使线程进入限期等待状态时，常常用 “使一个线程睡眠” 进行描述。

调用 Object.wait() 方法使线程进入限期等待或者无限期等待时，常常用 “挂起一个线程” 进行描述。

睡眠和挂起是用来描述行为，而阻塞和等待用来描述状态。

阻塞和等待的区别在于，阻塞是被动的，它是在等待获取一个排它锁。而等待是主动的，通过调用 Thread.sleep() 和 Object.wait() 等方法进入。

| 进入方法                                 | 退出方法                                        |
| ---------------------------------------- | ----------------------------------------------- |
| Thread.sleep() 方法                      | 时间结束                                        |
| 设置了 Timeout 参数的 Object.wait() 方法 | 时间结束 / Object.notify() / Object.notifyAll() |
| 设置了 Timeout 参数的 Thread.join() 方法 | 时间结束 / 被调用的线程执行完毕                 |
| LockSupport.parkNanos() 方法             | -                                               |
| LockSupport.parkUntil() 方法             | -                                               |



### 2.1.6 死亡(Terminated)

可以是线程结束任务之后自己结束，或者产生了异常而结束。



## 2.2 三种使用线程的方法

在 Java 中，创建线程有三种方法：

- 实现 Runnable 接口；
- 实现 Callable 接口；
- 继承 Thread 类。

实现 Runnable 和 Callable 接口的类只能当做一个可以在线程中运行的任务，不是真正意义上的线程，因此最后还需要通过 Thread 来调用。可以说任务是通过线程驱动从而执行的。



继承与实现接口相比，实现接口会更好一些，因为:

- Java 不支持多重继承，因此继承了 Thread 类就无法继承其它类，但是可以实现多个接口；
- 类可能只要求可执行就行，继承整个 Thread 类开销过大。



### 2.2.1 实现 Runnable 接口

需要实现 run() 方法。

通过 Thread 调用 start() 方法来启动线程。

```java
// 创建任务
Runnable runnable = new Runnable() {
    @Override
    public void run() {
        log.debug("通过 Runnable 接口创建的线程");
    }
};

// 创建并启动线程
new Thread(runnable).start();
```



### 2.2.2 实现 Callable 接口

与 Runnable 相比，Callable 可以有返回值，返回值通过 FutureTask 进行封装。

```java
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
```



### 2.2.3 继承 Thread 类

```java
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
```



## 2.3 线程的常用方法

线程的方法，指的就是 Thread 中定义的方法



### 2.3.1 线程休眠

`Thread.sleep(millisec)` 用于休眠当前正在执行的线程，millisec 单位为毫秒。

sleep() 可能会抛出 InterruptedException，因为异常不能跨线程传播回 main() 中，因此必须在本地进行处理。线程中抛出的其它异常也同样需要在本地进行处理。

```java
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
```

日志输出

```java
19:30:29 [main] - 主线程执行结束
19:30:32 [Thread-0] - 子线程执行结束
```



### 2.3.2 线程中断

一个线程执行完毕之后会自动结束，如果在运行过程中发生异常会提前结束。



Java 提供了线程中断以及判断某个线程是否被中断过的方法：

- interrupt()：给调用了该方法的线程设置一个中断标记，此时
    - 如果调用该方法的线程处于等待或阻塞状态，则会抛出 InterruptedException，并中断线程的执行；
    - 如果调用该方法的线程没有处于等待或阻塞状态，则线程会继续运行（只是设置了一个中断标记
- interrupted()：判断调用了该方法的线程是否被中断过（检查中断标记）
    - 注意，**Thread.interrupted() 的调用线程是当前正在执行的线程**！！！
    - 返回一个 boolean 值，并清除标记位（即：再次调用时，中断标记已经被清除，此时会返回一个 falase）
- isInterrupted()：判断调用了该方法的线程是否被中断过（检查中断标记）
    - 不会清除标记位



总的来说

- interrupt()：设置一个中断标记，如果处于等待或阻塞状态，则会抛出异常并中断线程；
- interrupted()：检测并清除中断标记；
- isInterrupted()：只检测中断标记；



```java
Thread t1 = new Thread(() -> {
    log.debug("t1 线程启动");
}, "t1");
t1.start();
t1.interrupt();

log.debug("第一次调用 t1.isInterrupted(): {}", t1.isInterrupted());
log.debug("第二次调用 t1.isInterrupted(): {}", t1.isInterrupted());

// 注意，Thread.interrupted() 作用于当前正在执行的线程，此处是 main 线程，而不是 t1 线程！！！
log.debug("第一次调用 Thread.interrupted(): {}", Thread.interrupted());
log.debug("第二次调用 Thread.interrupted(): {}", Thread.interrupted());

// interrupt() 作用于 main 线程
Thread.currentThread().interrupt();
log.debug("第一次调用 Thread.interrupted(): {}", Thread.interrupted());
log.debug("第二次调用 Thread.interrupted(): {}", Thread.interrupted());

log.debug("main 线程执行结束");
```

日志输出

```java
19:10:29 [t1] - t1 线程启动
19:10:29 [main] - 第一次调用 t1.isInterrupted(): true
19:10:29 [main] - 第二次调用 t1.isInterrupted(): true
19:10:29 [main] - 第一次调用 Thread.interrupted(): false
19:10:29 [main] - 第二次调用 Thread.interrupted(): false
19:10:29 [main] - 第一次调用 Thread.interrupted(): true
19:10:29 [main] - 第二次调用 Thread.interrupted(): false
19:10:29 [main] - main 线程执行结束
```



### 2.3.3 线程协作

`Thread.join()` 调用此方法的线程被阻塞，仅当该方法完成以后，才能继续运行。

```java
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

log.debug("r1 = {}", r1);
log.debug("r2 = {}", r2);
```

日志输出

```java
10:47:44 [main] - r1 = 10
10:47:44 [main] - r2 = 20
```



### 2.3.4 线程调度

`yield()` 用于提示线程调度器让出当前线程对 CPU 的使用。

告知 CPU 调度器，当前线程已经完成了生命周期中最重要的部分，可以切换给其它线程来执行。该方法只是对线程调度器的一个建议，而且也只是建议具有相同优先级的其它线程可以运行，是否执行还是看 CUP 的调度策略。



### 2.3.5 守护线程

`setDaemon()` 用于将一个线程设置为守护线程。

守护线程是程序运行时在后台提供服务的线程，不属于程序中不可或缺的部分。

当所有非守护线程结束时，程序也就终止，同时会杀死所有守护线程。

main() 属于非守护线程。

```java
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
```

日志输出

```java
19:31:40 [main] - 主线程执行结束
```



# 3、共享模型

在 Java 中，**共享模型**（Shared Memory Model）指的是多个线程共享同一内存区域，通过读写共享数据来进行通信。Java 的并发编程基于这种模型，多个线程可以访问相同的变量、对象或数据结构。该模型的关键在于线程之间如何协调和同步对共享资源的访问，以避免出现竞态条件和数据不一致的问题。



**共享模型的特点：**

1. **共享内存**：不同线程可以访问同一个内存区域中的数据。
2. **线程间通信**：线程通过共享数据进行信息交换。
3. **同步控制**：为了防止多个线程同时修改共享资源，通常需要通过同步（`synchronized`）或其他锁机制来保证线程安全。



**常见的共享问题：**

- **竞态条件**：多个线程同时修改共享数据，导致不可预测的结果。
- **内存可见性问题**：一个线程的修改可能在其他线程中不可见，导致数据不一致。
- **死锁**：多个线程相互等待对方释放资源，造成程序无法继续执行。



**解决共享问题的常用方式：**

- **同步**（`synchronized`）：保证同一时间只有一个线程访问共享资源。

- **原子操作**（如`AtomicInteger`）：确保操作的原子性，不需要显式加锁。

- **内存屏障**：通过`volatile`关键字或显式锁来保证共享数据的可见性。

    

总的来说，Java 的共享模型要求开发者小心管理多线程对共享资源的访问，使用合适的同步机制避免出现并发问题。



## 3.1 管程-悲观锁（阻塞）

悲观锁（Pessimistic Locking）是一种数据库或多线程编程中的锁机制，其核心思想是假设数据会被多个进程或线程同时修改，因此在操作数据时，会采取锁的方式，确保其他线程或进程不能对数据进行并发修改，直到当前操作完成。



**悲观锁的特点：**

1. **锁定数据**：在读取或修改数据时，先加锁，防止其他事务或线程对数据进行修改，直到当前操作完成。
2. **阻塞性**：其他请求该数据的操作会被阻塞，直到当前操作释放锁。
3. **性能开销**：由于锁的存在，可能会导致性能问题，尤其是在高并发场景下，多个线程需要等待锁的释放。



### 3.1.1 共享问题

共享问题指的是多个线程在并发执行时，访问和修改同一共享资源时可能出现的数据不一致或竞态条件问题。



比如以下案例：两个线程对初始值为 0 的静态变量一个做自增，一个做自减，各做 5000 次，结果为什么不一定是 0 ？

```java
public class Test05 {

    static int counter = 0;

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

        log.debug("{}", counter);
    }
}
```



执行结果可能是正数、负数、零。为什么呢？



因为 Java 中对静态变量的自增，自减并不是原子操作，要彻底理解，必须从字节码来进行分析：



对于静态变量而言，实际会产生如下的 JVM 字节码指令:

```java
- getstatic      i			//	获取静态变量 i 的值（从主存获取值到工作内存）
- iconst_i							//	准备一个常量，且值为 1，用于准备进行自增操作
- iadd									//	进行自增操作
- putstatic      i			//	将自增后的值保存到静态变量 i 中（将值从工作内存保存到主存）
```



又由于计算机是采用分时系统，所以多个线程间会存在上下文切换，所以会出现自增字节码指令还没执行完毕时，线程就进行上下文切换，导致数据错乱。

```java
- 比如 t1 线程执行自增操作，执行 iadd 完成后，cup 时间片使用完毕，然后进行上下文切换（修改后的值还没从工作内存保存到主存
- 这时 t2 线程执行自减操作，执行 getstatic 时，从主存中获取到的值为 0，而不是 1，然后进行自减后将 -1 从工作内存保存到主存
- 然后 t2 运行完毕，轮到 t1 运行，t1 从上下文信息得知自己该执行 putstatic 指令了
- 此时 t1 往下运行 putstatis 指令，将之前的运算结果 1 保存到主存，这样就出现了数据错乱！！！
```



以上图解如下。



线程先从主存将变量复制到工作内存

<img src="./assets/picture7.png" alt="image" style="zoom:50%;" />

在单线程环境下运行时，以上 8 行代码是没有问题的

<img src="./assets/picture8.png" alt="image" style="zoom:50%;" />

但是在多线程环境下，这 8 行代码可能会交错执行（上下文切换导致），出现正负数情况

<img src="./assets/picture9.png" alt="image" style="zoom:50%;" />

出现正数的情况

<img src="./assets/picture10.png" alt="image" style="zoom:50%;" />

多线程操作共享变量出现数据错乱的原因：在多个线程对共享资源读写操作时发生指令交错（上下文切换导致指令错乱），就会出现问题



什么是临界区：一段代码块内如果存在对共享资源的多线程读写操作，称这段代码块为临界区

```java
static int counter = 0;

static void increment() {   // 临界区 
    counter++;
}

static void decrement() {   // 临界区 
    counter--;
}
```



什么是竞态条件：多个线程在临界区内执行，由于代码的执行序列不同而导致结果无法预测，称之为发生了竞态条件

```java
static int counter = 0;

// 线程1对临界区进行操作
Thread t1 = new Thread(() -> {
    for (int i = 0; i < 5000; i++) {
        counter++;
    }
}, "t1");

// 线程2对临界区进行操作
Thread t2 = new Thread(() -> {
    for (int i = 0; i < 5000; i++) {
        counter--;
    }
}, "t2");
```



### 3.1.2 synchronized 入门

为了避免临界区的竞态条件发生，有多种手段可以达到目的。

- 阻塞式的解决方案：synchronized，Lock

- 非阻塞式的解决方案：原子变量



`synchronized` 是 Java 中的一种关键字，用于控制对共享资源的访问，以保证线程安全。在多线程程序中，当多个线程访问同一个资源时，可能会发生竞争条件，导致数据不一致。通过使用 `synchronized`，可以确保同一时刻只有一个线程能够访问被同步的代码块或方法，从而避免数据冲突。



`synchronized` 关键字可以加在方法上或者代码块上，它们的区别如下：



**同步方法（方法级别）**
`synchronized` 可以用来修饰方法，表示该方法在同一时刻只能被一个线程执行。

```java
public synchronized void method() {
    // 同步代码块
}
```

这种方式是同步整个方法，在调用该方法的线程执行期间，其他线程无法访问该方法。



**同步代码块（代码块级别）**
`synchronized` 也可以用来修饰代码块，这样可以只同步部分代码，而不是整个方法。它需要一个锁对象，只有获取到这个锁的线程才能执行代码块中的代码。

```java
public void method() {
    synchronized (lockObject) {
        // 同步代码块
    }
}
```

在这个例子中，`lockObject` 是一个对象锁，只有当线程获得 `lockObject` 锁时，才能执行代码块中的内容。



**关键点：**

- 位置

    - 同步方法： 在方法上加 `synchronized`，表示对整个方法进行同步。

    - 同步代码块： 使用 `synchronized` 修饰代码块，可以在方法内对特定代码区域进行同步。通常会使用一个对象作为锁。

- 锁对象

    - 类锁（静态方法）：如果是静态方法（`static`），锁对象是类的 `Class` 对象。
    - 实例锁： 如果是实例方法（非静态），锁对象是当前实例（即 `this`）。

    

示例：同步实例方法和同步代码块

```java
public class SyncExample {

    // 同步实例方法
    public synchronized void instanceMethod() {
        // 这段代码在同一时刻只能被一个线程访问
        System.out.println("同步实例方法");
    }

    // 使用同步代码块
    public void methodWithBlock() {
        synchronized (this) {
            // 这段代码在同一时刻只能被一个线程访问
            System.out.println("同步代码块");
        }
    }

    // 使用类锁
    public static synchronized void staticMethod() {
        // 静态同步方法，锁的是 Class 对象
        System.out.println("同步静态方法");
    }
}
```



在多线程环境中，使用 `synchronized` 可以有效避免并发问题，但也要注意使用过多的同步可能会影响性能。



使用 `synchronized` 来决解此前的问题

```java
static int counter = 0;
static final Object room = new Object();

public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 5000; i++) {
            synchronized (room) {
                counter++;
            }
        }
    }, "t1");
    Thread t2 = new Thread(() -> {
        for (int i = 0; i < 5000; i++) {
            synchronized (room) {
                counter--;
            }
        }
    }, "t2");
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    log.debug("{}", counter);
}
```



代码的执行的时序图如下：

<img src="./assets/picture11.png" alt="image" style="zoom:50%;" />



### 3.1.3 线程安全分析

在Java多线程环境中，不同类型的变量具有不同的线程安全性。我们可以从成员变量、静态变量和局部变量的角度进行分析。



**成员变量的线程安全性**

成员变量（即实例变量）在多线程环境中通常不是线程安全的。这是因为每个线程可能会访问同一个对象的成员变量，多个线程可能同时对成员变量进行读写操作，导致数据不一致或竞态条件（race conditions）。

- 如果多个线程修改同一个实例的成员变量，而这些线程没有使用适当的同步机制（如 `synchronized` 关键字、`Lock` 接口或原子变量等），则可能出现线程不安全的问题。
- 线程安全的实现方法包括使用同步方法、`synchronized` 块或者使用 `java.util.concurrent` 包中的线程安全类（如 `AtomicInteger`、`ReentrantLock` 等）。



**静态变量的线程安全性**

静态变量（即类变量）也是多线程环境中常见的线程安全问题来源。由于静态变量属于类的所有实例，多个线程可能会同时访问同一个类的静态变量，因此它们也不具备线程安全性，除非采取适当的同步措施。

- 静态变量如果没有同步机制保护，也可能会出现竞态条件。
- 同样，使用 `synchronized`，`Atomic` 类或者 `volatile` 关键字来确保线程安全。
- 比如，`AtomicInteger` 是线程安全的，可以用来替代普通的 `int` 类型静态变量，确保操作的原子性。



**局部变量的线程安全性**

局部变量通常是方法内的变量，它们的生命周期仅限于方法调用的过程中，因此局部变量在线程安全性方面通常不需要额外考虑。

- **局部变量是线程安全的**，因为每个线程都有自己的栈空间，每个线程在调用方法时都会在自己的栈上创建局部变量的副本。因此，多个线程调用同一个方法时，局部变量不会相互干扰。
- 但是，如果局部变量是引用类型对象并且该对象被共享或者该对象本身存在非线程安全的问题，则仍然需要考虑线程安全性。



**总结**

- **成员变量和静态变量**在多线程环境下通常不是线程安全的。它们可能需要同步机制来保证线程安全。

- **局部变量**是线程安全的，因为每个线程都有独立的栈空间来存储局部变量的副本，除非局部变量是引用类型对象并且这些对象在多个线程间共享。

    

为了确保多线程环境中的安全性，通常需要使用 `synchronized`、`volatile`、`Atomic` 类、`Lock` 等机制来保证数据一致性和原子性。



以下是一些常见的安全类

```java
String
Integer
StringBuffer
Random
Vector
Hashtable
java.util.concurrent 包下的类
```



这里说它们是线程安全的是指，多个线程调用它们同一个实例的某个方法时，是线程安全的。比如：

```java
Hashtable table = new Hashtable();

new Thread(() -> {
    table.put("key", "value1");
}).start();

new Thread(() -> {
    table.put("key", "value2");
}).start();
```



需要注意是：它们的每个方法是原子的，但它们多个方法的组合不是原子的，所以不是安全的，比如：

```java
Hashtable table = new Hashtable();

// 线程1，线程2 同时访问如下方法时，就是不安全的
if (table.get("key") == null) {
    table.put("key", value);
}
```



时序图如下：

<img src="./assets/picture12.png" alt="image" style="zoom: 25%;" />



### 3.1.4 Monitor

Java 的 monitor 是一种同步机制，用于控制多个线程对共享资源的访问，确保线程安全。



#### 3.1.4.1 对象头

在介绍对象在内存中的组成结构前，我们先简要回顾一个对象的创建过程：

- JVM 将对象所在的 class 文件加载到方法区中；
- JVM 读取 main 方法入口，将 main 方法入栈，执行创建对象代码;
- 在 main 方法的栈内存中分配对象的引用，在堆中分配内存放入创建的对象，并将栈中的引用指向堆中的对象;



所以当对象在实例化完成之后，是被存放在堆内存中的，这里的对象由3部分组成，如下图所示：

![image](./assets/picture13.png)

对各个组成部分的功能简要进行说明

- 对象头：对象头存储的是对象在运行时状态的相关信息、指向该对象所属类的元数据的指针，如果对象是数组对象那么还会额外存储对象的数组长度；

- 实例数据：实例数据存储的是对象的真正有效数据，也就是各个属性字段的值，如果在拥有父类的情况下，还会包含父类的字段。字段的存储顺序会受到数据类型长度、以及虚拟机的分配策略的影响；

- 对齐填充字节：在 Java 对象中，需要对齐填充字节的原因是，64 位的 JVM 中对象的大小被要求向 8 字节对齐，因此当对象的长度不足8字节的整数倍时，需要在对象中进行填充操作;

    

对于学习多线程来说，最主要的部分就是对象头中的 Markword，所以仅先学习对象头。

 

对象头（Object header）的组成部分，根据普通对象和数组对象的不同，结构将会有所不同。只有当对象是数组对象才会有数组长度部分，普通对象没有该部分，如下图所示

 

普通对象

```java
|--------------------------------------------------------------|
| 												Object Header (64 bits) 						 |
|------------------------------------|-------------------------|
| 					     Mark Word (32 bits) | Klass Word (32 bits)    |
|------------------------------------|-------------------------|
```



数组对象

```java
|---------------------------------------------------------------------------------|
| 																Object Header (96 bits)  												|
|--------------------------------|-----------------------|------------------------|
| 						 Mark Word(32bits) | Klass Word(32bits)    | array length(32bits)   |
|--------------------------------|-----------------------|------------------------|
```



其中，Klass Word 是用于存储对象的类型指针，该指针指向它所属类的元数据（指向被 JVM 加载到方法区中的 Class 对象），这样 JVM 才能知道对象是哪个类的实例。

 

而 Mark Word 是用于存储对象在运行时的一些数据，如：hashcode，GC 分代年龄等。Mark Word 的长度为 JVM 的一个 Word 的大小，也就是说 32 位的 JVM 的 Mark Word 为 32 位，64 位的 JVM 的 Mark Word 为 64 位。

 

为了让一个 Word 能存储更多信息，JVM 将 Word 的最低两位设置为标记为，不同的标记为表示对象处于不同的状态，具体如下

```java
|-------------------------------------------------------|--------------------|
| 						Mark Word (32 bits) 											| 			State 			 |
|-------------------------------------------------------|--------------------|
| hashcode:25 					| age:4 | biased_lock:0 	| 01 	| 			Normal 			 |
|-------------------------------------------------------|--------------------|
| thread:23 	| epoch:2 | age:4 | biased_lock:1 	| 01 	| 			Biased 			 |
|-------------------------------------------------------|--------------------|
| ptr_to_lock_record:30 													| 00 	| Lightweight Locked |
|-------------------------------------------------------|--------------------|
| ptr_to_heavyweight_monitor:30 									| 10 	| Heavyweight Locked |
|-------------------------------------------------------|--------------------|
| 																								| 11 	| Marked for GC 		 |
|-------------------------------------------------------|--------------------|
```



标记信息的含义如下：

1. **Hashcode**

    - 当你调用`Object.hashCode()`方法时，JVM会计算并返回对象的哈希值。为了优化性能，JVM会将对象的哈希码缓存起来，在`Mark Word`中存储。

    - 如果对象没有显式的哈希码值，JVM会计算它的哈希码。

2. **Age**
    - 这个字段与垃圾回收（GC）相关，特别是与年轻代（Young Generation）中的对象生命周期有关。它记录了对象的年龄，即对象经历的GC轮次数。
    - 在对象晋升到老年代之前，年龄用于决定何时晋升对象。

3. **Biased Lock**
    - **偏向锁**是一种优化，旨在减少锁竞争。偏向锁假定只有一个线程会访问该对象，因此它在`Mark Word`中存储了线程ID。如果对象的锁没有竞争，JVM会偏向于当前线程，这样可以避免每次获取锁时的复杂性。
    - 如果有其他线程争用锁，偏向锁会被撤销并转为轻量级锁（Spin Lock）。

4. **Thread**
    - 这个字段存储了当前线程的ID，在偏向锁（Biased Lock）模式下使用。当对象加锁时，如果启用了偏向锁，JVM会记录哪个线程持有锁，以便后续的访问可以快速判断是否由该线程访问。

5. **Epoch**
    - 在JVM中，`Epoch`字段与偏向锁的撤销有关。它用于管理锁的偏向状态，通常与锁的撤销和重置操作相关。

6. **ptr_to_lock_record**

    - 这是指向锁记录的指针，用于在对象锁定时存储锁的相关信息。锁记录包含了对象的锁信息，可能用于管理不同的锁状态（如轻量级锁、重量级锁等）。

    - 这个指针的存在是为了帮助JVM管理锁的状态转换，例如从偏向锁转换到轻量级锁或重量级锁。

7. **ptr_to_heavyweight_monitor**
    - 当对象的锁变得非常争用（即多个线程试图访问同一个对象），对象的锁会变成重量级锁，这时JVM会将`Mark Word`中的相关信息转换为指向一个重量级监视器（heavyweight monitor）的指针。这个监视器用于管理线程的排队和阻塞。



#### 3.4.1.2 Monitor

什么是 Monitor？可以把它理解为一个同步工具，也可以描述为一种同步机制，它通常被描述为一个对象。与一切皆对象一样，所有的 Java 对象是天生的 Monitor，每一个 Java 对象都有成为Monitor 的潜质，因为在 Java 的设计中 ，每一个 Java 对象自打娘胎里出来就带了一把看不见的锁，它叫做内部锁或者 Monitor 锁。Moniter 也就是通常说 Synchronized 的对象锁，MarkWord 锁标识位为 10，其中指针指向的是 Monitor 对象的起始地址。

在 Java 虚拟机（HotSpot）中，Monitor 是由 ObjectMonitor 实现的，其主要数据结构如下（位于HotSpot虚拟机源码ObjectMonitor.hpp文件，C++实现的）：

```java
ObjectMonitor() {
    _header       = NULL;
    _count        = 0;    //记录个数
    _waiters      = 0,
    _recursions   = 0;
    _object       = NULL;
    _owner        = NULL;
    _WaitSet      = NULL; //处于wait状态的线程，会被加入到_WaitSet
    _WaitSetLock  = 0 ;
    _Responsible  = NULL ;
    _succ         = NULL ;
    _cxq          = NULL ;
    FreeNext      = NULL ;
    _EntryList    = NULL ; //处于等待锁block状态的线程，会被加入到该列表
    _SpinFreq     = 0 ;
    _SpinClock    = 0 ;
    OwnerIsThread = 0 ;
}
```



ObjectMonitor 中有两个队列，WaitSet 和 EntryList，用来保存 ObjectWaiter 对象列表（ 每个等待锁的线程都会被封装成 ObjectWaiter 对象 ），Owner 指向持有 ObjectMonitor 对象的线程，用于表示该对象锁已经被线程持有。图列：

<img src="./assets/picture14.png" alt="image" style="zoom:50%;" />



上图的线程获取 Monitor 的流程如下：

- 开始时     Monitor 中 Owner 为 null；

- 当 Thread-2 进入临界区并执行 synchronized(obj) 时，就会将     Monitor 的所有者 Owner 置为     Thread-2（Owner 指向线程对象，obj 对象的 Mark Word 指向 Monitor），把对象原有的     MarkWord 存入线程栈中的锁记录中；
- 在     Thread-2 上锁的过程，Thread-3、Thread-4、Thread-5 页进入临界区并执行     synchronized(obj)，就会进入 EntryList（双向链表），线程状态变为 BLOCKED；

- Thread-2 执行完临界区的代码后，根据 obj 对象的 Mark Word 中的引用地址找到 Monitor 对象，设置 Owner 为空，并把此前保存在栈帧的锁记录中的     obj Mark Word     信息还原到 obj 的 Mark Word 中；
- 然后 Thread-2 唤醒 EntryList     中等待的线程，被唤醒的线程开始竞争锁，竞争是非公平的，如果这时有新的线程想要获取锁，可能直接就抢占到了，阻塞队列的线程就会继续阻塞；
- WaitSet 中的 Thread-0 和 Thread-1 是以前获得过锁，但由于不满足继续执行的条件，从而进入 WAITING 状态的线程（wait-notify 机制）；

 

注意：synchronized 必须是进入同一个对象的 Monitor 才有上述的效果，不加 synchronized 的对象不会关联监视器，不遵从以上规则。



#### 3.1.4.3 synchronized 进阶

synchronized 的实现方式涉及偏向锁、轻量级锁和重量级锁。这些锁的设计目的是提升性能，减少线程争用时的开销。



首先，先从字节码的角度理解 synchronize 的执行流程。执行如下代码：

```java
static final Object lock = new Object();
static int counter = 0;

public static void main(String[] args) {
    synchronized (lock) {
        counter++;
    }
}
```



得到的字节码如下：

```java
0: 	getstatic     #2		// 获取 lock 对象的引用，这个引用是 static final 类型的。#2 表示 lock 变量在常量池中的索引。
3: 	dup									// dup 指令复制栈顶的对象引用（即 lock），为 monitorenter 指令提供必要的操作数。
4: 	astore_1						// 将栈顶的 lock 引用存储到本地变量 1 位置。这个位置用于存储锁对象的副本。
5: 	monitorenter				// 获取 lock 对象的监视器锁，确保同步代码块中代码的原子性。
6: 	getstatic     #3    // 获取静态变量 counter 的值。#3 是 counter 变量在常量池中的索引。            
9: 	iconst_1						// 将常量 1 推送到栈顶，准备与 counter 进行加法操作。
10: iadd								// 将栈顶的两个整数相加（counter 和 1），并将结果压回栈顶。
11: putstatic     #3    // 将栈顶的值存回 counter 变量中，完成 counter++ 操作。
14: monitorexit					// 释放 lock 对象的监视器锁，退出同步块，确保在多个线程中不会发生竞争条件。
15: return							// 返回方法，程序执行结束。

```



这段字节码展示了同步块的执行过程，锁定 `lock` 对象、修改 `counter` 的值并解锁。注意：方法级别的 synchronized 不会在字节码指令中有所体现。



##### 3.1.4.3.1 偏向锁

很多时候，同步代码其实只有一个线程在执行，并不存在竞争锁的情况。这时候直接加锁就会导致性能问题。偏向锁是为了优化只有一个线程访问同步代码块的情况。线程第一次访问时会获得偏向锁，并且在后续的访问中，不需要竞争锁。如果没有其他线程竞争，它就不会发生升级为轻量级锁或重量级锁的操作。











### 3.1.5 wait/nofity



### 3.1.6 线程状态转换



### 3.1.7 活跃性



### 3.1.8 Lock



## 3.2 JMM（java 内存模型）



## 3.3 无锁-乐观锁（非阻塞）



## 3.4 不可变



## 3.5 并发工具



## 3.6 异步编程
