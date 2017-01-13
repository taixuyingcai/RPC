package com.srq.rpc.zookeeper;

import com.srq.rpc.constant.ZooKeeperEnum;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.CountDownLatch;

/**
 * 注册zookeeper服务
 * <p/>
 * User: ryanshi@tcl.com
 * Date: 2017-01-09
 * Time: 15:25
 */
public class ServiceRegistry {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String data) {
        if (!StringUtils.isEmpty(data)) {
            ZooKeeper zooKeeper = connectServer();
            if (zooKeeper != null) {
                createNode(zooKeeper, data);
            }
        }
    }

    private ZooKeeper connectServer() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(registryAddress, Integer.parseInt(ZooKeeperEnum.ZK_SESSION_TIME.value()), new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
        } catch (Exception e) {
            LOGGER.error("connect zookeeper error ", e);
        }
        return zooKeeper;
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(ZooKeeperEnum.ZK_DATA_PATH.value(), bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            LOGGER.debug("create zookeeper node {} ==> {}", path, data);
        } catch (Exception e) {
            LOGGER.error("createNode error ", e);
        }
    }
}
