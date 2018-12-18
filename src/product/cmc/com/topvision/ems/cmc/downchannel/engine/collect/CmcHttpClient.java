package com.topvision.ems.cmc.downchannel.engine.collect;

import com.topvision.framework.http.HttpClientBean;

/**
 * @author bryan
 * @created @2013-10-11-下午08:21:05
 *
 */
public class CmcHttpClient extends HttpClientBean {

	/**
     * @param domain
     */
    public CmcHttpClient(String domain) {
        super(domain);
        // TODO Auto-generated constructor stub
    }

	/* (non-Javadoc)
	 * @see com.topvision.framework.http.HttpClientBean#checkAndReConnect()
	 */
	@Override
	protected void checkAndReConnect() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void shutdown() {
		// TODO Auto-generated method stub
		super.shutdown();
	}
	

}
