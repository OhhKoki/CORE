package org.example.juc;

import java.util.concurrent.locks.LockSupport;

public class Test13 {
    public static void main(String[] args) {
        SyncPark syncPark = new SyncPark(5);

        Thread t1 = new Thread(() -> {
            syncPark.print("a");
        });

        Thread t2 = new Thread(() -> {
            syncPark.print("b");
        });

        Thread t3 = new Thread(() -> {
            syncPark.print("c");
        });

        syncPark.setThreads(t1, t2, t3);
        syncPark.start();
    }
}

class SyncPark {
    private int loopNumber;
    private Thread[] threads;

    public SyncPark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void setThreads(Thread... threads) {
        this.threads = threads;
    }

    public void print(String str) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(nextThread());
        }
    }

    private Thread nextThread() {
        Thread current = Thread.currentThread();
        int index = 0;
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] == current) {
                index = i;
                break;
            }
        }
        if (index < threads.length - 1) {
            return threads[index + 1];
        } else {
            return threads[0];
        }
    }

    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
        LockSupport.unpark(threads[0]);
    }
}
