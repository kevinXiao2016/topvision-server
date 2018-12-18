/*
 * @(#)BaseEntityDao.java
 *
 * Copyright 2011 Topoview All rights reserved.
 */

package com.topvision.framework.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;

/**
 * 数据访问层接口.
 * 
 * @author niejun
 * @version 1.0, 2007-08-23
 */
public interface BaseEntityDao<T> extends Dao {
    /**
     * 通用的根据组合条件删除记录.
     * 
     * @param map
     * @throws DataAccessException
     */
    void deleteByMap(Map<String, String> map);

    /**
     * 通用的根据单主键删除记录.
     * 
     * @param key
     * @throws DataAccessException
     */
    void deleteByPrimaryKey(Integer key);

    /**
     * 通用的根据单主键批量删除记录.
     * 
     * @param keys
     * @throws DataAccessException
     */
    void deleteByPrimaryKey(List<?> keys);

    /**
     * 通用的根据单主键删除记录.
     * 
     * @param key
     * @throws DataAccessException
     */
    void deleteByPrimaryKey(Long key);

    /**
     * 删除给定数组中的记录.
     * 
     * @param keys
     * @throws DataAccessException
     */
    void deleteByPrimaryKey(Object[] keys);

    /**
     * 通用的根据单主键删除记录.
     * 
     * @param key
     * @throws DataAccessException
     */
    void deleteByPrimaryKey(String key);

    /**
     * 根据通用的实体删除记录.
     * 
     * @param entity
     * @throws DataAccessException
     */
    void deleteByPrimaryKey(T entity);

    /**
     * 清空数据记录.
     * 
     * @throws DataAccessException
     */
    void empty();

    /**
     * 通用的批量插入.
     * 
     * @param entiy
     */
    void insertEntity(List<T> entities);

    /**
     * 通用的插入实体.
     * 
     * @param entiy
     */
    void insertEntity(T entity);

    /**
     * 批量插入数组中的记录.
     * 
     * @param entities
     * @throws DataAccessException
     */
    void insertEntity(T[] entities);

    /**
     * 根据map集合查询符合条件的所有实体.
     * 
     * @param map
     * @throws DataAccessException
     */
    List<T> selectByMap(Map<String, String> map);

    /**
     * 根据map集合查询符合条件的某页实体.
     * 
     * @param map
     * @param page
     * @throws DataAccessException
     */
    PageData<T> selectByMap(Map<String, String> map, Page page);

    /**
     * 根据map集合查询符合条件的某页实体, 并用给定的行处理器处理结果.
     * 
     * @param map
     * @param page
     * @throws DataAccessException
     */
    void selectByMap(Map<String, String> map, Page page, MyResultHandler handler);

    /**
     * 根据map集合查询符合条件的所有实体, 并用给定的行处理器处理结果.
     * 
     * @param map
     * @param handler
     * @throws DataAccessException
     */
    void selectByMap(Map<String, String> map, MyResultHandler handler);

    /**
     * 
     * @param name
     * @return
     * @throws DataAccessException
     */
    T selectByName(String name);

    /**
     * 通用的根据主键查询某个实体.
     * 
     * @param key
     */
    T selectByPrimaryKey(Integer key);

    /**
     * 通用的根据主键查询某个实体.
     * 
     * @param key
     */
    T selectByPrimaryKey(Long key);

    /**
     * 通用的根据主键查询某个实体.
     * 
     * @param key
     */
    T selectByPrimaryKey(String key);

    /**
     * 通用的批量更新.
     * 
     * @param entiy
     */
    void updateEntity(List<T> entities);

    /**
     * 通用的根据主键更新实体.
     * 
     * @param entiy
     */
    void updateEntity(T entity);

    /**
     * 批量更新数组中的记录.
     * 
     * @param entities
     * @throws DataAccessException
     */
    void updateEntity(T[] entities);
}
