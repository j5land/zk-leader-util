package com.spider;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.spider.util.ZookeeperTools;


public class ZookeeperLeaderTestCase {
	
	private static Logger logger = Logger.getLogger(ZookeeperLeaderTestCase.class);

	@Test
	public void testLeader(){
		ZookeeperTools zookeeperTools = new ZookeeperTools();
		logger.info("开始ZK");
		String zkUrl = "192.168.3.203:2181,192.168.3.205:2181,192.168.3.207:2181";
		String configStr = "{\"port\":8080,\"basePath\":\"spiderticket\",\"isSender\":true,\"isConsumer\":true}";
		JSONObject config = null;
		try {
			config = JSONObject.fromObject(configStr);
		} catch (Exception e) {
			logger.error("zookpeer leader config fail!");
		}
		if (config!=null && config.getBoolean("isSender")) {
			// 启动定时推送信息到队列JOB，由ZK选举Master
			zookeeperTools.startLeaderSelect(config.getString("basePath"), zkUrl);
		}else{
			logger.error("zookpeer leader is follow!");
		}
//		try {
//			//while(true){
//			//	Thread.sleep(1000L*5);
//				logger.info("zookpeer leader status: "+zookeeperTools.getLeader().isSucces());
//			//}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
