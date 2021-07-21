package com.itheima.Zookpper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

public class PathChirldCacheTest {
    @Test
    public void test01() throws Exception {
        //创建失败重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,1000);
        //创建客户端
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 3000, 3000, retryPolicy);
        //开启客户端
        client.start();

        //创建子节点的监听对象
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/hello",true);

        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
//监听对象
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
                    System.out.println("子节点更新");
                    System.out.println("节点:"+event.getData().getPath());
                    System.out.println("数据" + new String(event.getData().getData()));
                }else if(event.getType() == PathChildrenCacheEvent.Type.INITIALIZED ){
                    System.out.println("初始化操作");
                }else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED ){
                    System.out.println("删除子节点");
                    System.out.println("节点:"+event.getData().getPath());
                    System.out.println("数据" + new String(event.getData().getData()));
                }else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED ){
                    System.out.println("添加子节点");
                    System.out.println("节点:"+event.getData().getPath());
                    System.out.println("数据" + new String(event.getData().getData()));
                }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED ){
                    System.out.println("连接失效");
                }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED ){
                    System.out.println("重新连接");
                }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_LOST ){
                    System.out.println("连接失效后稍等一会儿执行");
                }
            }
        });
        System.in.read();
    }
}
