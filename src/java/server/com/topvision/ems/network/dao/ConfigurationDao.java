package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Config;
import com.topvision.framework.dao.BaseEntityDao;

public interface ConfigurationDao extends BaseEntityDao<Config> {

    boolean isUpLoadAccess(Long entityId);

    boolean isDownLoadAccess(Long entityId);

    List<Config> getUpLoadConfig(Long entityId);

    List<Config> getDownLoadConfig(Long entityId);

    void upLoadConfig(Long entityId);

    void downLoadConfig(Long entityId);
}