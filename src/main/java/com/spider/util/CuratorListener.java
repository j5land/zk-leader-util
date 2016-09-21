package com.spider.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

import com.spider.bean.ZKInfo;

/**
 * JOB ZK监听Service
 * 
 * @author sai
 *
 */
public class CuratorListener implements PathChildrenCacheListener {

	@Override
	public void childEvent(CuratorFramework curator, PathChildrenCacheEvent event) throws Exception {
		System.out.println("事件触发: " + event.getType());
		System.out.println("子节点数量: " + curator.getChildren().forPath("/").size());
		switch (event.getType()) {
		case CHILD_ADDED:
			childAddEvent(curator, event);
			break;
		case CHILD_REMOVED:
			childRemoveEvent(curator, event);
			break;
		case CHILD_UPDATED:
			childUpdateEvent(curator, event);
			break;
		case INITIALIZED:
			initialized(curator, event);
			break;
		case CONNECTION_SUSPENDED:
			connectionSuspended(curator, event);
			break;
		default:
			break;
		}
	}

	// 初始化方法
	private void initialized(CuratorFramework curator, PathChildrenCacheEvent event) {
		System.out.println("initialized");
		// 发起选举
		LeaderLatch leaderLatch = new LeaderLatch(curator, "/" + ZKInfo.getZkinfo().getSysid());
		try {
			// 发送选举请求
			leaderLatch.start();
			System.out.println("等待获取Leader权限");
			// 保存选举机制
			ZKInfo.getZkinfo().setLeaderLatch(leaderLatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void childAddEvent(CuratorFramework curator, PathChildrenCacheEvent event) {
		System.out.println("CHILD_ADDED: " + event.getData().getPath());
	}

	private void childRemoveEvent(CuratorFramework curator, PathChildrenCacheEvent event) {
		System.out.println("CHILD_REMOVED: " + event.getData().getPath());
	}

	private void childUpdateEvent(CuratorFramework curator, PathChildrenCacheEvent event) {
		System.out.println("CHILD_UPDATED: " + event.getData().getPath());
	}

	private void connectionSuspended(CuratorFramework curator, PathChildrenCacheEvent event) {
		System.out.println("CONNECTION_SUSPENDED: " + event.getData().getPath());
	}

}
