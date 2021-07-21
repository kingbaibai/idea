package com.itheima.Zookpper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

public class TreeCache1 {
    @Test
    public void test01() throws Exception {
        //创建失败重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 3000, retryPolicy);

        client.start();

        //创建监听器
        TreeCache treeCache = new TreeCache(client, "/hello");
        treeCache.start();

        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent event) throws Exception {
                if(event.getType() == TreeCacheEvent.Type.NODE_ADDED){
                    System.out.println(event.getData().getPath() + "节点添加");
                }else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED){
                    System.out.println(event.getData().getPath() + "节点移除");
                }else if(event.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                    System.out.println(event.getData().getPath() + "节点修改");
                }else if(event.getType() == TreeCacheEvent.Type.INITIALIZED){
                    System.out.println("初始化完成");
                }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_SUSPENDED){
                    System.out.println("连接过时");
                }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_RECONNECTED){
                    System.out.println("重新连接");
                }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_LOST){
                    System.out.println("连接过时一段时间");
                }
            }
        });
        System.in.read();
    }
}
