/**
 * 
 */
package com.topvision.framework.dao.file;

import java.util.List;
import java.util.Map;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;

/**
 * @author niejun
 * 
 */
public class FileDaoSupport<T> implements BaseEntityDao<T> {
    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByMap(java.util.Map)
     */
    @Override
    public void deleteByMap(Map<String, String> map) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Integer)
     */
    @Override
    public void deleteByPrimaryKey(Integer key) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.util.List)
     */
    @Override
    public void deleteByPrimaryKey(List<?> keys) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Long)
     */
    @Override
    public void deleteByPrimaryKey(Long key) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Object[])
     */
    @Override
    public void deleteByPrimaryKey(Object[] keys) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.String)
     */
    @Override
    public void deleteByPrimaryKey(String key) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#deleteByPrimaryKey(java.lang.Object)
     */
    @Override
    public void deleteByPrimaryKey(T entity) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#empty()
     */
    @Override
    public void empty() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(java.util.List)
     */
    @Override
    public void insertEntity(List<T> entities) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(java.lang.Object)
     */
    @Override
    public void insertEntity(T entity) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#insertEntity(T[])
     */
    @Override
    public void insertEntity(T[] entities) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map)
     */
    @Override
    public List<T> selectByMap(Map<String, String> map) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.domain.Page)
     */
    @Override
    public PageData selectByMap(Map<String, String> map, Page page) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.domain.Page, com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void selectByMap(Map<String, String> map, Page page, MyResultHandler handler) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByMap(java.util.Map,
     * com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void selectByMap(Map<String, String> map, MyResultHandler handler) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByName(java.lang.String)
     */
    @Override
    public T selectByName(String name) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.Integer)
     */
    @Override
    public T selectByPrimaryKey(Integer key) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.Long)
     */
    @Override
    public T selectByPrimaryKey(Long key) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#selectByPrimaryKey(java.lang.String)
     */
    @Override
    public T selectByPrimaryKey(String key) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(java.util.List)
     */
    @Override
    public void updateEntity(List<T> entities) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(java.lang.Object)
     */
    @Override
    public void updateEntity(T entity) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.BaseEntityDao#updateEntity(T[])
     */
    @Override
    public void updateEntity(T[] entities) {
    }
}
