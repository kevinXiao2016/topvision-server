package com.topvision.ems.google.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.google.dao.GoogleEntityDao;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.google.service.GoogleEntityService;
import com.topvision.ems.network.domain.Link;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.CurrentRequest;

@Service("googleEntityService")
public class GoogleEntityServiceImpl extends BaseService implements GoogleEntityService {
    @Autowired
    private GoogleEntityDao googleEntityDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.service.GoogleEntityService#deleteGoogleEntity (long)
     */
    @Override
    public void deleteGoogleEntity(long entityId) {
        googleEntityDao.deleteByPrimaryKey(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.service.GoogleEntityService#getGoogleEntityById
     * (java.lang.Long)
     */
    @Override
    public GoogleEntity getGoogleEntityById(Long entityId) {
        return googleEntityDao.selectByPrimaryKey(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.service.GoogleEntityService#getAllGoogleEntity (java.util.Map)
     */
    @Override
    public List<GoogleEntity> getAllGoogleEntity(Map<String, String> filter) {
        filter.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return googleEntityDao.selectByMap(filter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.service.GoogleEntityService#getAllLinks()
     */
    @Override
    public List<Link> getAllLinks() {
        return googleEntityDao.getLinkInGoogleMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.service.GoogleEntityService#
     * insertGoogleEntity(com.topvision.ems.google.domain.GoogleEntity)
     */
    @Override
    public void insertGoogleEntity(GoogleEntity entity) {
        googleEntityDao.insertEntity(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.service.GoogleEntityService#
     * insertOrUpdateGoogleEntity(com.topvision.ems.google.domain.GoogleEntity)
     */
    @Override
    public void insertOrUpdateGoogleEntity(GoogleEntity entity) {
        googleEntityDao.insertOrUpdateGoogleEntity(entity);
    }

    /**
     * @param googleEntityDao
     *            the googleEntityDao to set
     */
    public void setGoogleEntityDao(GoogleEntityDao googleEntityDao) {
        this.googleEntityDao = googleEntityDao;
    }

    /**
     * @return the googleEntityDao
     */
    public GoogleEntityDao getGoogleEntityDao() {
        return googleEntityDao;
    }

    @Override
    public List<Entity> queryAvaibleDevice() {
        return googleEntityDao.queryAvaibleDevice();
    }

    @Override
    public void fixlocation(Long entityId, boolean fixed) {
        googleEntityDao.fixlocation(entityId, fixed);
    }

    @Override
    public String queryForGeoLocationFromLatLng(double latitude, double longitude) {
        String addr = "";
        // 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址
        // 密钥可以随便写一个key=abc
        // output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析
        String url = String.format("http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s", latitude, longitude);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "NetworkGoogleMapAction.LocationCannotGet";
        }
        try {
            httpsConn = myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    String[] retList = data.split(",");
                    if (retList.length > 2 && ("200".equals(retList[0]))) {
                        addr = retList[2];
                        addr = addr.replace("", "");
                    } else {
                        addr = "";
                    }
                }
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "NetworkGoogleMapAction.LocationCannotGet";
        }
        return addr;
    }

    @Override
    public String queryForLatLngFromGeoLocation() {
        // TODO 如果有需求就做
        return null;
    }

    @Override
    public void batchInsertOrUpdateGoogleEntity(List<GoogleEntity> googleEntities) {
        googleEntityDao.batchInsertOrUpdateGoogleEntity(googleEntities);
    }
}
