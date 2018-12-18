package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class TopoNode implements AliasesSuperType{

    /**
     * 
     */
    private static final long serialVersionUID = 2409399888271859710L;
    
    private Long topoNodeId;
    private Integer x;
    private Integer y;
    private Boolean fixed=false;
    public Long getTopoNodeId() {
        return topoNodeId;
    }
    public void setTopoNodeId(Long topoNodeId) {
        this.topoNodeId = topoNodeId;
    }
    public Integer getX() {
        return x;
    }
    public void setX(Integer x) {
        this.x = x;
    }
    public Integer getY() {
        return y;
    }
    public void setY(Integer y) {
        this.y = y;
    }
    public Boolean getFixed() {
        return fixed;
    }
    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

}
