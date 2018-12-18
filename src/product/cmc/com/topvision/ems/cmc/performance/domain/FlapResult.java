package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
/**
 * 这个类是定时采集器每次采集完成后封装返回数据的对象
 * @author smsx
 *
 */
public class FlapResult extends PerformanceResult<OperClass> {

    private Long entityId;
    
	private List<CmFlap> cmFlapList = null;
	/**
	 * 采集完成时间
	 */
	private Long collectFinishTime = null;
	public FlapResult(OperClass domain) {
		super(domain);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6981836820314970988L;
	
	
	/**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public List<CmFlap> getCmFlapList() {
		return cmFlapList;
	}

	public void setCmFlapList(List<CmFlap> cmFlapList) {
		this.cmFlapList = cmFlapList;
	}

	public Long getCollectFinishTime() {
		return collectFinishTime;
	}

	public void setCollectFinishTime(Long collectFinishTime) {
		this.collectFinishTime = collectFinishTime;
	}
	
	

}
