package com.spider.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import com.spider.bean.ResultCode;
import com.spider.bean.StatusContent;
import com.spider.bean.ZKInfo;

@Service("zookeeperTools")
public class ZookeeperTools {

	private volatile static ZKInfo zkInfo = null;
	private volatile static CuratorFramework curatorClient = null;
	private PathChildrenCacheListener childListener = null;

	public void startLeaderSelect(String sysid, String zkUrls) {
		startLeaderSelect(sysid, zkUrls, new CuratorListener());
	}

	public void startLeaderSelect(String sysid, String zkUrls, PathChildrenCacheListener childListener) {
		if (this.childListener == null) {
			zkInfo = ZKInfo.initZKInfo(sysid, null, zkUrls, null);
			this.childListener = childListener;
			startZk(sysid, zkUrls, childListener);
		}
	}

	public static ResultCode<String> getLeader() {
		ResultCode result = new ResultCode();
		String code = StatusContent.CODE_FAIL;
		String msg = "";
		if (zkInfo == null) {
			msg = "Zookeeper初始化失败！";
		} else {
			if (curatorClient == null) {
				msg = "curatorClient初始化失�?";
			} else {
				if (zkInfo.getLeaderLatch() == null || (!zkInfo.getLeaderLatch().hasLeadership())) {
					msg = "未获取Leader权限";
				} else {
					msg = "已经获取Leader权限";
					code = StatusContent.CODE_SUCCESS;
				}
			}
		}
		return new ResultCode<String>(code, msg);
	}

	/**
	 * zk链接，并监控子节点变�?
	 * 
	 * @param sysid
	 *            系统主键�?/命名空间
	 * @param zkPool
	 *            zk链接
	 * @param childrenCacheListener
	 *            子节点监听器
	 */
	private void startZk(String sysid, String zkPool, PathChildrenCacheListener childrenCacheListener) {
		// 建立基础链接配置
		curatorClient = CuratorFrameworkFactory.builder().connectString(zkPool).sessionTimeoutMs(3000) // 会话超时
				.connectionTimeoutMs(3000) // 链接超时
				.canBeReadOnly(false) // ？？�?
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE)) // ?
				.namespace(sysid) // 命名空间
				.defaultData(null).build();
		// �?始链�?
		curatorClient.start();
		try {
			Stat isExists = curatorClient.checkExists().forPath("/");
			if (isExists == null) {
				curatorClient.create().withMode(CreateMode.PERSISTENT).forPath("/");
			}
			PathChildrenCache childrenCache = new PathChildrenCache(curatorClient, "/", true);
			childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
			childrenCache.getListenable().addListener(childrenCacheListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ZKInfo getZkInfo() {
		return zkInfo;
	}

}
