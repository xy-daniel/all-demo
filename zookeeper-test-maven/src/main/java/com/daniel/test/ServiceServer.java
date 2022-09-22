package com.daniel.test;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.CountDownLatch;

public class ServiceServer {

    private String path, value;

    private String master;

    public ServiceServer(String cluster, String name, String address) {
        path = "/" + cluster + "Master";
        value = "name:" + name + ",address:" + address;

        ZkClient zkClient = new ZkClient("175.24.172.160:2181");
        zkClient.setZkSerializer(new SerializableSerializer());

        new Thread(() -> electionMaster(zkClient)).start();
    }

    private void electionMaster(ZkClient zkClient) {
        try {
            zkClient.createEphemeral(path, value);
            master = zkClient.readData(path);
            System.out.println(value + "创建节点成功,成功成为Master");
        } catch (ZkNodeExistsException e) {
            master = zkClient.readData(path);
            System.out.println("Master为:" + master);
        }

        //1
        CountDownLatch cd = new CountDownLatch(1);

        //注册监听
        IZkDataListener dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) {

            }

            @Override
            public void handleDataDeleted(String s) {
                System.out.println("监听到节点被删除");
                //3
                cd.countDown();
            }
        };

        zkClient.subscribeDataChanges(path, dataListener);

        // 让自己阻塞
        if (zkClient.exists(path)) {
            try {
                //2
                cd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //醒来后取消watcher
        zkClient.unsubscribeDataChanges(path, dataListener);

        //递归自己(下一次选举)
        electionMaster(zkClient);
    }

    public static void main(String[] args) {
        new ServiceServer("cluster", "server1", "192.168.0.1");
//        new ServiceServer("cluster", "server2", "192.168.0.2");
//        new ServiceServer("cluster", "server3", "192.168.0.3");
    }
}
