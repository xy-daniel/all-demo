package com.daniel.test;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @author daniel
 */
public class MyZkClient {

    static String PATH = "/";
    static ZkClient ZKCLIENT = new ZkClient("175.24.172.160:2181", 50000);

    public static void main(String[] args) {
        createNode();
        deleteNode();
        listener();
        children();
        write();
    }

    private static void write() {
        ZKCLIENT.writeData("/a3", "哈哈哈");
    }

    private static void children() {
        List<String> children = ZKCLIENT.getChildren(PATH);
        System.out.println("children:" + children);
    }

    private static void listener() {
        // 注册一个监听事件，subscribeChildChanges，通过使用listen方式来监听来达到消息广播效果，监听子节点变化
        ZKCLIENT.subscribeChildChanges("/a1", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println(">>>进入了handleChildChange");
                System.out.println(">>>s = " + s);
                System.out.println(">>>list = " + list);
            }
        });
        // 注册一个监听事件，subscribeDataChanges 监听节点上数据的变化
        ZKCLIENT.subscribeDataChanges("/a1", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(">>> 进入了handleDataChange");
                System.out.println(">>> dataPath = " + dataPath);
                System.out.println(">>> data = " + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) {
                System.out.println(">>> 进入了handleDataDeleted");
                System.out.println(">>> dataPath = " + dataPath);
            }
        });

        // 监听zookeeper状态变化
        ZKCLIENT.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) {
                System.out.println(">>> 进入了subscribeStateChanges");
                System.out.println(">>> state = " + state);
            }

            @Override
            public void handleNewSession() {
                System.out.println(">>> 进入了handleNewSession");
            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) {

            }
        });
    }

    /**
     * 删除节点
     */
    private static void deleteNode() {
        // 删除节点c，以及节点c下面所有的子节点
        // 节点c下面，还有子节点cc，deleteRecursive方法可以递归的删除子节点，而zookeeper原生api不允许删除存在子节点的节点
        ZKCLIENT.deleteRecursive("/a2");

        // 删除节点a
        ZKCLIENT.delete("/a1");
    }

    /**
     * 新增节点
     */
    private static void createNode() {
        try {
            //创建一个永久的node节点
            ZKCLIENT.createPersistent("/a1");
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }

        try {
            //创建一个永久的node节点,这种创建方式和zookeeper的原生API相似,不太一样的是隐藏了watcher注册和内容可以传object对象,zookeeper原生直接传数组
            //和内容可以传object对象,zookeeper原生直接传数组
            ZKCLIENT.create("/a2", "test", CreateMode.PERSISTENT);// 创建一个永久性node节点c/cc，true代表可以递归创建（如果c不存在，则会先创建c再创建cc节点），默认false（原生zookeeper Api不能跨层级创建，这就是ZKClient优势）
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }

        try {
            //创建一个永久性node节点a3,true代表可以递归创建(如果a3不存在,则会先创建a3再创建test节点),默认false(原生zookeeper Api不能跨层级创建,这就是ZKClient优势)
            ZKCLIENT.createPersistent("/a3/test", true);
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }

        try {
            //创建永久性node节点d，里面内容为‘内容d’
            ZKCLIENT.createPersistent("/a4", "test");
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }

        try {
            //创建一个临时节点a5
            ZKCLIENT.createEphemeral("/a5");
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }

        try {
            //创建一个递增序号临时节点a7
            ZKCLIENT.createEphemeralSequential("/a7", "test");
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }

        try {
            //创建一个临时节点a6,内容为test
            ZKCLIENT.createEphemeral("/a6", "test");
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }
        try {
            //创建一个递增序号的永久节点a8
            ZKCLIENT.createPersistentSequential("/a8", "test");
        } catch (ZkNodeExistsException e) {
            System.out.println("节点已存在");
        }
    }
}
