package com.topvision.ems.enginemgr;

import java.io.Serializable;

public class EngineServerStatus implements Serializable {

	private static final long serialVersionUID = 7357068171023799581L;
	
	public int id;
	public String name;
	public String runTime;
	public String memUsage;
	public int threadNumber;
	
	public EngineServerStatus() {
		super();
	}

	public EngineServerStatus(int id, String name, String runTime,
			String memUsage, int threadNumber) {
		super();
		this.id = id;
		this.name = name;
		this.runTime = runTime;
		this.memUsage = memUsage;
		this.threadNumber = threadNumber;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(String memUsage) {
		this.memUsage = memUsage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
