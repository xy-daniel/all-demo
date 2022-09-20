package com.daniel.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author daniel
 */
public class MyCuratorClient {

    static CuratorFramework curatorFramework;

    /**
     * 初始化客户端
     */
    public static void init() {
        //1 重试策略：初试时间为3s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        //2 通过工厂创建连接
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("175.24.172.160:2181")
                .sessionTimeoutMs(50000)
                .retryPolicy(retryPolicy)
                .build();
        //3 开启连接
        curatorFramework.start();
    }

    /**
     * 创建节点
     */
    public static void create() throws Exception {
        System.out.println("--------------------create--------------------");
        curatorFramework.create()
                .withMode(CreateMode.PERSISTENT)
                .forPath("/test");

        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath("/test/1", "hello daniel".getBytes());

        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath("/test/2/3", "hello daniel".getBytes());

//        curatorFramework.create()
//                .withMode(CreateMode.PERSISTENT)
//                .inBackground((client, event) -> {
//                    String path = event.getPath();
//                    String data = new String(event.getData());
//                    System.out.println("create data end, path:" + path + ", data:" + data);
//                })
//                .forPath("/test/2", "hello daniel".getBytes());
    }

    /**
     * 节点查询
     */
    public static void get() throws Exception {
        System.out.println("--------------------get--------------------");
        String test1 = new String(curatorFramework.getData().forPath("/test/1"));
        System.out.println("/test/1:" + test1);

        Stat stat = new Stat();
        String test2 = new String(curatorFramework.getData().storingStatIn(stat).forPath("/test/2/3"));
        System.out.println("/test/2/3:" + test2 + ",version:" + stat.getVersion());

//        curatorFramework.getData()
//                .inBackground((client, event) -> {
//                    String path = event.getPath();
//                    String data = new String(event.getData());
//                    System.out.println("get data end, path:" + path + ", data:" + data);
//                })
//                .forPath("/test1");
    }

    /**
     * 修改节点
     */
    public static void update() throws Exception {
        System.out.println("--------------------update--------------------");
        curatorFramework.setData().forPath("/test/1", "root update".getBytes());
        curatorFramework.setData().forPath("/test/2/3", "root update".getBytes());
//        curatorFramework.setData().withVersion(1).forPath("/test/2/3", "root update".getBytes());
//        curatorFramework.setData()
//                .inBackground((client, event) -> {
//                    String path = event.getPath();
//                    String data = new String(event.getData());
//                    System.out.println("update data end, path:" + path + ", data:" + data);
//                })
//                .forPath("/test1", "root update".getBytes());
    }

    /**
     * 删除节点
     */
    public static void delete() throws Exception {
        curatorFramework.delete().forPath("/test/1");

        curatorFramework.delete().withVersion(1).forPath("/test/2/3");

//        curatorFramework.delete()
//                .inBackground((client, event) -> {
//                    String path = event.getPath();
//                    String data = new String(event.getData());
//                    System.out.println("delete data end, path:" + path + ", data:" + data);
//                })
//                .forPath("/test1");

        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/test");
    }

    /**
     * 获取子节点
     */
    public static void getChildren() throws Exception {
        System.out.println("--------------------getChildren--------------------");
        List<String> list = curatorFramework.getChildren().forPath("/test");
        System.out.println(list);

//        curatorFramework.getChildren()
//                .inBackground((client, event) -> {
//                    String path = event.getPath();
//                    String data = new String(event.getData());
//                    System.out.println("get data children end, path:" + path + ", data:" + data + " children:" + event.getChildren());
//                })
//                .forPath("test2");
    }

    /**
     * 查询节点是否存在
     */
    public static void isExists() throws Exception {
        System.out.println("--------------------isExists--------------------");
        Stat stat = curatorFramework.checkExists().forPath("/test");
        System.out.println(stat == null ? "null" : stat.toString());

        stat = curatorFramework.checkExists().forPath("/test/1");
        System.out.println(stat == null ? "null" : stat.toString());

        stat = curatorFramework.checkExists().forPath("/test/2/3");
        System.out.println(stat == null ? "null" : stat.toString());

//        stat = curatorFramework.checkExists()
//                .inBackground((client, event) -> {
//                    System.out.println("check exists end, stat:" + event.getStat());
//                })
//                .forPath("/test");
//        System.out.println(stat.toString());
    }

    /**
     * 监听事件
     */
    public static void listen() throws Exception {
        final NodeCache cache = new NodeCache(curatorFramework, "/test", false);
        cache.start(true);
        cache.getListenable().addListener(() -> {
            ChildData childData = cache.getCurrentData();
            if (Objects.isNull(childData)) {
                System.out.println("data removed");
            } else {
                System.out.println("data changed, path:" + childData.getPath() + ", data:" + Arrays.toString(childData.getData()) + ", status:" + childData.getStat());
            }
        });


        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, "/test", true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getListenable().addListener((cf, event) -> {
            ChildData data = event.getData();
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("child added, path:" + data.getPath() + ", data:" + Arrays.toString(data.getData()));
                    break;
                case CHILD_UPDATED:
                    System.out.println("child updated, path:" + data.getPath() + ", data:" + Arrays.toString(data.getData()));
                    break;
                case CHILD_REMOVED:
                    System.out.println("child removed, path:" + data.getPath() + ", data:" + Arrays.toString(data.getData()));
                    break;
                default:
                    break;
            }
        });
    }

    public static void main(String[] args) throws Exception {
        init();
//        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/test");
        create();
        isExists();
        listen();
        get();
        getChildren();
        update();
        get();
        delete();
        isExists();
    }


}
