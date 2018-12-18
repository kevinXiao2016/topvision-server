/***********************************************************************
 * $Id: Action.java,v 1.1 Sep 7, 2008 10:53:26 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.domain.TreeEntity;

/**
 * @author kelers
 * @Create Date Sep 7, 2008 10:53:26 AM
 */
public class Action extends BaseEntity implements TreeEntity, AliasesSuperType {
    private static final long serialVersionUID = 2632460988354740762L;

    public static final String MAIL_SERVER = "emailServer";

    private long actionId;
    private int actionTypeId;
    private String name;
    private byte[] params;
    private boolean enabled = true;

    private Long userId;

    /**
     * @return the actionId
     */
    public long getActionId() {
        return actionId;
    }

    /**
     * @return the actionTypeId
     */
    public int getActionTypeId() {
        return actionTypeId;
    }

    @Override
    public String getId() {
        return String.valueOf(actionId);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the params
     */
    public byte[] getParams() {
        return params;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the params
     */
    public Object getParamsObject() {
        ByteArrayInputStream bais = null;
        XMLDecoder de = null;
        Object obj = null;
        try {
            bais = new ByteArrayInputStream(params);
            de = new XMLDecoder(bais);
            obj = de.readObject();
            bais.close();
            de.close();
            return obj;
        } catch (Exception ex) {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException ex1) {
                }
            }
            if (de != null) {
                de.close();
            }
            if (params != null) {
                return new String(params);
            } else {
                return null;
            }
        }
    }

    @Override
    public String getParentId() {
        return String.valueOf(actionTypeId);
    }

    @Override
    public String getText() {
        return name;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param actionId
     *            the actionId to set
     */
    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    /**
     * @param actionTypeId
     *            the actionTypeId to set
     */
    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    /**
     * @param enabled
     *            the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param params
     *            the params to set
     */
    public void setParams(byte[] params) {
        this.params = params;
    }

    /**
     * @param params
     *            the params to set
     */
    public void setParamsObject(Object params) {
        if (params == null) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder en = new XMLEncoder(baos);
        try {
            en.writeObject(params);
            en.flush();
            baos.flush();
            en.close();
            baos.close();
            this.params = baos.toByteArray();
        } catch (Exception ex) {
            if (en != null) {
                en.close();
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex1) {
                }
            }
            this.params = params.toString().getBytes();
        }
    }

    @Override
    public String toString() {
        return "Action{" + "actionId=" + actionId + ", actionTypeId=" + actionTypeId + ", name='" + name + '\'' + ", params=" + params + ", enabled=" + enabled + '}';
    }
}
