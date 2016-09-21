package com.spider.bean;

import org.apache.curator.framework.recipes.leader.LeaderLatch;
/**
 * zk基本信息
 * @author zhouxiang sai
 *
 */
public class ZKInfo {
	//double key
	private volatile static ZKInfo zkinfo = null;
	private String sysid = "";       // 系统主键
	private String childSysid = "";  // 子系统主�?
	private String zkUrls = "";      //zk地址
	private Integer sessionTimeout = 3000; // sessionTimeout
	private String curMasterSysId = "";//当前leaderId
	private LeaderLatch leaderLatch = null; //阻塞选举机制
	private Boolean runJob = false;//是否执行job
	//单例
	private ZKInfo(String sysid, String childSysid, String zkUrls, String curMasterSysId){
		this.sysid = sysid;
		this.childSysid = childSysid;
		this.zkUrls = zkUrls;
		this.curMasterSysId = curMasterSysId;
	}
	
	public static ZKInfo initZKInfo(String sysid, String childSysid, String zkUrls, String curMasterSysId) {
		if(zkinfo == null){
			synchronized (ZKInfo.class) {
				if(zkinfo == null){
					zkinfo = new ZKInfo(sysid, childSysid, zkUrls, curMasterSysId);
				}
			}
		}
		return zkinfo;
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getZkUrls() {
		return zkUrls;
	}

	public void setZkUrls(String zkUrls) {
		this.zkUrls = zkUrls;
	}

	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getChildSysid() {
		return childSysid;
	}

	public void setChildSysid(String childSysid) {
		this.childSysid = childSysid;
	}

	public String getCurMasterSysId() {
		return curMasterSysId;
	}

	public void setCurMasterSysId(String curMasterSysId) {
		this.curMasterSysId = curMasterSysId;
	}

	public LeaderLatch getLeaderLatch() {
		return leaderLatch;
	}

	public void setLeaderLatch(LeaderLatch leaderLatch) {
		this.leaderLatch = leaderLatch;
	}

	public static ZKInfo getZkinfo() {
		return zkinfo;
	}

	public Boolean getRunJob() {
		return runJob;
	}

	public void setRunJob(Boolean runJob) {
		this.runJob = runJob;
	}

}
