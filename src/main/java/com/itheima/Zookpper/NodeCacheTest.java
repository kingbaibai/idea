package com.itheima.Zookpper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

public class NodeCacheTest {
    @Test
    public void test01() throws Exception {
        //创建失败重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);

        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 3000, retryPolicy);
        client.start();
        System.out.println("连接成功");
        //创建节点数据监听对象
        NodeCache nodeCache = new NodeCache(client, "/hello");

        //开始监听
        nodeCache.start(true);

        System.out.println(nodeCache.getCurrentData());

        //添加监听对象
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                String data = new String(nodeCache.getCurrentData().getData());
                System.out.println("wather路径"+new String(nodeCache.getCurrentData().getData())+"data:"+data);
            }
        });
        System.in.read();
    }
}
