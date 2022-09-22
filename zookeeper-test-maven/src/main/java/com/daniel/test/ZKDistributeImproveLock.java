package com.daniel.test;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZKDistributeImproveLock implements Lock {

    private String lockPath;

    private ZkClient client;

    private final ThreadLocal<String> currentPath = new ThreadLocal<>();

    private final ThreadLocal<String> beforePath = new ThreadLocal<>();

    private final ThreadLocal<Integer> reentrantCount = new ThreadLocal<>();

    public ZKDistributeImproveLock(String lockPath) {
        super();
        this.lockPath = lockPath;
        client = new ZkClient("175.24.172.160:2181");
        client.setZkSerializer(new SerializableSerializer());

        if (!this.client.exists(lockPath)) {
            try {
                this.client.createPersistent(lockPath);
            } catch (ZkNodeExistsException e) {
                System.out.println(lockPath + "节点已经存在");
            }
        }
    }

    @Override
    public boolean tryLock() {
        if (this.reentrantCount.get() != null) {
            int count = this.reentrantCount.get();
            if (count > 0) {
                this.reentrantCount.set(++count);
                return true;
            }
        }

        if (this.currentPath.get() == null) {
            currentPath.set(this.client.createEphemeralSequential(lockPath + "/", "1"));
        }

        //获取所有的子节点
        List<String> children = this.client.getChildren(lockPath);

        //排序list
        Collections.sort(children);

        //判断当前节点是否是最小的
        if (currentPath.get().equals(lockPath + "/" + children.get(0))) {
            this.reentrantCount.set(1);
            return true;
        } else {
            //取前一个
            int curIndex = children.indexOf(currentPath.get().substring(lockPath.length() + 1));
            beforePath.set(lockPath + "/" + children.get(curIndex - 1));
            return false;
        }
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            waitForLock();
            lock();
        }
    }

    private void waitForLock() {
        CountDownLatch cd = new CountDownLatch(1);

        IZkDataListener dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("----收到前一个锁释放信息,不再阻塞----");
                cd.countDown();
            }
        };

        String beforePathStr = beforePath.get();

        client.subscribeDataChanges(beforePathStr, dataListener);

        //阻塞自己
        if (this.client.exists(beforePathStr)) {
            try {
                cd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //取消注册
        client.unsubscribeDataChanges(beforePathStr, dataListener);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        if (this.reentrantCount.get() != null) {
            int count = this.reentrantCount.get();
            if (count > 1) {
                this.reentrantCount.set(--count);
                return;
            } else {
                client.delete(this.currentPath.get());
                this.reentrantCount.set(null);
                this.currentPath.set(null);
                this.beforePath.set(null);
            }
        }

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

                ZKDistributeImproveLock zookeeperLock = new ZKDistributeImproveLock("/distLock3");

                try {
                    zookeeperLock.lock();
                    System.out.println(Thread.currentThread().getName() + "获得锁");
                } finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    zookeeperLock.unlock();
                }
            }).start();

        }
    }
}
