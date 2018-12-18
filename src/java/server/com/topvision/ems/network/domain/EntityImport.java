/***********************************************************************
 * $Id: EntityImport.java,v1.0 2013-10-29 下午5:10:07 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.domain;

/**
 * @author loyal
 * @created @2013-10-29-下午5:10:07
 *
 */
public class EntityImport {
    private Integer rowId;
	private Integer id;
	private Long entityId;
	private String ip;
	private String name;
	private String mac;
	private String sysName;
	private String contact;
	private String location;
	private String note;
	private Integer ipStatus = 0;// 0正常，1格式错误，2重复
	private Integer nameStatus = 0;// 0正常，1格式错误，2重复
	private Integer macStatus = 0;// 0正常，1格式错误，2重复
	private Integer contactStatus = 0;// 0正常，1格式错误，2重复
	private Integer locationStatus = 0;// 0正常，1格式错误，2重复
	private Integer noteStatus = 0;// 0正常，1格式错误，2重复

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getIpStatus() {
		return ipStatus;
	}

	public void setIpStatus(Integer ipStatus) {
		this.ipStatus = ipStatus;
	}

	public Integer getNameStatus() {
		return nameStatus;
	}

	public void setNameStatus(Integer nameStatus) {
		this.nameStatus = nameStatus;
	}

	public Integer getMacStatus() {
		return macStatus;
	}

	public void setMacStatus(Integer macStatus) {
		this.macStatus = macStatus;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getContactStatus() {
		return contactStatus;
	}

	public void setContactStatus(Integer contactStatus) {
		this.contactStatus = contactStatus;
	}

	public Integer getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(Integer locationStatus) {
		this.locationStatus = locationStatus;
	}

	public Integer getNoteStatus() {
		return noteStatus;
	}

	public void setNoteStatus(Integer noteStatus) {
		this.noteStatus = noteStatus;
	}

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

}
