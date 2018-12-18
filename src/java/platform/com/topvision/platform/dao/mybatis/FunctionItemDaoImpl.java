/**
 *
 */
package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.FunctionItemDao;
import com.topvision.platform.domain.FunctionItem;

/**
 * @author Administrator
 */
@Repository("functionItemDao")
public class FunctionItemDaoImpl extends MyBatisDaoSupport<FunctionItem> implements FunctionItemDao {
    @Override
    public String getDomainName() {
        return FunctionItem.class.getName();
    }
}
