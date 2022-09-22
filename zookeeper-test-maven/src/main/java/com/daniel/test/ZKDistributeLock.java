package com.daniel.test;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZKDistributeLock implements Lock {

    private String lockPath;

    private ZkClient client;

    //锁重数计入
    private ThreadLocal<Integer> reentrantCount = new ThreadLocal<>();

    public ZKDistributeLock(String lockPath) {
        super();
        this.lockPath = lockPath;
        client = new ZkClient("175.24.172.160:2181");
        client.setZkSerializer(new SerializableSerializer());
    }

    @Override
    public boolean tryLock() { //不会阻塞
        if (this.reentrantCount.get() != null) {
            int count = this.reentrantCount.get();
            if (count > 0) {
                this.reentrantCount.set(++count);
                return true;
            }
        }
        //创建节点
        try {
            client.createEphemeral(lockPath);
            this.reentrantCount.set(1);
        } catch (ZkNodeExistsException e) {
            return false;

        }
        return true;
    }

    @Override
    public void unlock() {
        if (this.reentrantCount.get() != null) {
            int count = this.reentrantCount.get();
            if (count > 1) {
                this.reentrantCount.set(--count);
                return;
            } else {
                this.reentrantCount.set(null);
            }
        }
        client.delete(lockPath);
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            //没获得锁阻塞自己
            waitForLock();
            //再次尝试
            lock();
        }
    }

    /**
     * 获取锁失败,阻塞等到锁释放
     */
    private void waitForLock() {
        CountDownLatch cd = new CountDownLatch(1);

        IZkDataListener dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("----收到锁释放信息,不再阻塞----");
                cd.countDown();
            }
        };

        client.subscribeDataChanges(lockPath, dataListener);

        //阻塞自己
        if (this.client.exists(lockPath)) {
            try {
                cd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //取消注册
        client.unsubscribeDataChanges(lockPath, dataListener);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }


    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {
        //并发数
        int currency = 50;

        //循环屏障
        CyclicBarrier cb = new CyclicBarrier(currency);

        //多线程模拟高并发
        for (int i = 0; i < currency; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "-----准备好了-----");
                //等到一起执行
                try {
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                ZKDistributeLock zookeeperLock = new ZKDistributeLock("/distLock1");

                try {
                    zookeeperLock.lock();
                    System.out.println(Thread.currentThread().getName() + "获得锁");
                } finally {
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    zookeeperLock.unlock();
                }
            }).start();

        }
    }
}