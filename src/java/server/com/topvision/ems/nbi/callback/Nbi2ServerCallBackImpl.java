/***********************************************************************
 * $Id: Nbi2ServerCallBackImpl.java,v1.0 2016年3月18日 上午10:37:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.callback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.nbi.service.NbiCallBackService;
import com.topvision.ems.nbi.service.NbiService;
import com.topvision.framework.service.BaseService;
import com.topvision.performance.nbi.api.MultiPeroidPolicy;
import com.topvision.performance.nbi.api.Nbi2ServerCallBack;
import com.topvision.performance.nbi.api.NbiMultiPeriod;

/**
 * @author Bravin
 * @created @2016年3月18日-上午10:37:27
 *
 */
@Service("Nbi2ServerCallBack")
public class Nbi2ServerCallBackImpl extends BaseService implements Nbi2ServerCallBack {
    @Autowired
    private NbiCallBackService nbiCallBackService;
    @Autowired
    private NbiService nbiService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.performance.nbi.api.Nbi2ServerCallBack#getMultiPeroidPolicies()
     */
    @Override
    public Object getMultiPeroidPolicies() {
        NbiMultiPeriod nbiMultiPeriod = nbiService.getNbiMultiPeriod();
        List<MultiPeroidPolicy> list = new ArrayList<>();
        if (nbiMultiPeriod.getFive_enable()) {
            MultiPeroidPolicy policy = new MultiPeroidPolicy();
            policy.setCollectPeroid(5);
            list.add(policy);
        }
        if (nbiMultiPeriod.getTen_enable()) {
            MultiPeroidPolicy policy = new MultiPeroidPolicy();
            policy.setCollectPeroid(10);
            list.add(policy);
        }
        if (nbiMultiPeriod.getFifteen_enable()) {
            MultiPeroidPolicy policy = new MultiPeroidPolicy();
            policy.setCollectPeroid(15);
            list.add(policy);
        }
        if (nbiMultiPeriod.getThirty_enable()) {
            MultiPeroidPolicy policy = new MultiPeroidPolicy();
            policy.setCollectPeroid(30);
            list.add(policy);
        }
        if (nbiMultiPeriod.getSixty_enable()) {
            MultiPeroidPolicy policy = new MultiPeroidPolicy();
            policy.setCollectPeroid(60);
            list.add(policy);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.performance.nbi.api.Nbi2ServerCallBack#check()
     */
    @Override
    public void check() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.performance.nbi.api.Nbi2ServerCallBack#getNbiGroupIndexs()
     */
    @Override
    public Object getNbiGroupIndexs() {
        return nbiCallBackService.getPerfGroupRow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.performance.nbi.api.Nbi2ServerCallBack#loadNbiFtpConfigParam()
     */
    @Override
    public Object loadNbiFtpConfigParam() {
        return nbiCallBackService.loadNbiFtpConfigParam();
    }

}
