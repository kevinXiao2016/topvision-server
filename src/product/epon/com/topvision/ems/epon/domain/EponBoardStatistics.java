package com.topvision.ems.epon.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class EponBoardStatistics implements AliasesSuperType {
    private static final long serialVersionUID = 5364909033648746499L;
    private Long entityId;
    private String ip;
    private String name;
    private int allSlot;
    private int online;
    private int outline;
    private int mpua;
    private int mpub;
    private int epua;
    private int epub;
    private int geua;
    private int geub;
    private int xgua;
    private int xgub;
    private int xguc;
    private int xpua;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public int getAllSlot() {
        return allSlot;
    }

    public void setAllSlot(int allSlot) {
        this.allSlot = allSlot;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOutline() {
        return outline;
    }

    public void setOutline(int outline) {
        this.outline = outline;
    }

    public int getMpua() {
        return mpua;
    }

    public void setMpua(int mpua) {
        this.mpua = mpua;
    }

    public int getEpua() {
        return epua;
    }

    public void setEpua(int epua) {
        this.epua = epua;
    }

    public int getGeua() {
        return geua;
    }

    public void setGeua(int geua) {
        this.geua = geua;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getXgua() {
        return xgua;
    }

    public void setXgua(int xgua) {
        this.xgua = xgua;
    }

    public int getXgub() {
        return xgub;
    }

    public void setXgub(int xgub) {
        this.xgub = xgub;
    }

    public int getEpub() {
        return epub;
    }

    public void setEpub(int epub) {
        this.epub = epub;
    }

    public int getGeub() {
        return geub;
    }

    public void setGeub(int geub) {
        this.geub = geub;
    }

    public int getXguc() {
        return xguc;
    }

    public void setXguc(int xguc) {
        this.xguc = xguc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMpub() {
        return mpub;
    }

    public void setMpub(int mpub) {
        this.mpub = mpub;
    }

    public int getXpua() {
        return xpua;
    }

    public void setXpua(int xpua) {
        this.xpua = xpua;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EponBoardStatistics [entityId=");
        builder.append(entityId);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", name=");
        builder.append(name);
        builder.append(", allSlot=");
        builder.append(allSlot);
        builder.append(", online=");
        builder.append(online);
        builder.append(", outline=");
        builder.append(outline);
        builder.append(", mpua=");
        builder.append(mpua);
        builder.append(", mpub=");
        builder.append(mpub);
        builder.append(", epua=");
        builder.append(epua);
        builder.append(", epub=");
        builder.append(epub);
        builder.append(", geua=");
        builder.append(geua);
        builder.append(", geub=");
        builder.append(geub);
        builder.append(", xgua=");
        builder.append(xgua);
        builder.append(", xgub=");
        builder.append(xgub);
        builder.append(", xguc=");
        builder.append(xguc);
        builder.append(", xpua=");
        builder.append(xpua);
        builder.append("]");
        return builder.toString();
    }

}
