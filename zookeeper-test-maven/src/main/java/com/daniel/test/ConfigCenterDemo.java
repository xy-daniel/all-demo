package com.daniel.test;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigCenterDemo {

    final static String domain = "175.24.172.160:2181";

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigCenterDemo configCenterDemo = new ConfigCenterDemo();
        configCenterDemo.putSingleConfigToZk();
        configCenterDemo.putFileConfigToZk();
        configCenterDemo.getConfigFromZk();
    }

    /**
     * 获取配置
     */
    private void getConfigFromZk() throws InterruptedException {
        ZkClient zkClient = new ZkClient(domain);
        zkClient.setZkSerializer(new SerializableSerializer());
        String configPath = "/singleConfig";
        String data = zkClient.readData(configPath);
        System.out.println("配置:" + data);

        zkClient.subscribeDataChanges(configPath, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("获得更新的配置项:" + data);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {

            }
        });

        Thread.sleep(5 * 60 * 1000);
    }

    /**
     * 将配置文件放到zookeeper
     */
    private void putFileConfigToZk() throws IOException {
        System.out.println(123);
        File file = new File(this.getClass().getResource("/config.xml").getFile());
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int read = inputStream.read(data);
        System.out.println(read);
        inputStream.close();

        ZkClient zkClient = new ZkClient("175.24.172.160:2181");
        zkClient.setZkSerializer(new BytesPushThroughSerializer());
        String configPath = "/fileConfig";
        if (zkClient.exists(configPath)) {
            zkClient.writeData(configPath, data);
        } else {
            zkClient.createPersistent(configPath, data);
        }
        zkClient.close();
    }

    /**
     * 将单个配置项放到zookeeper
     */
    private void putSingleConfigToZk() {
        ZkClient zkClient = new ZkClient("175.24.172.160:2181");
        zkClient.setZkSerializer(new SerializableSerializer());
        String configPath = "/singleConfig";
        String configValue = "test";
        if (zkClient.exists(configPath)) {
            zkClient.writeData(configPath, configValue);
        } else {
            zkClient.createPersistent(configPath, configValue);
        }
        zkClient.close();
    }
}
