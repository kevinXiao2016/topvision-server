package com.topvision.ems.mibble.dao;

import java.util.List;

import com.topvision.ems.mibble.domain.MibbleMessage;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * 
 * @author Bravin
 *
 */
public interface MibbleBrowserDao extends BaseEntityDao<MibbleMessage> {
    /**
     *  获取用户已选择的Mib
     * @param userId
     * @return
     */
    List<String> loadMibbles(long userId);

    /**
     * 保存用户选择的mib文件
     * @param mibs
     * @param userId
     */
    void saveSelectedMib(String[] mibs, long userId);
}
