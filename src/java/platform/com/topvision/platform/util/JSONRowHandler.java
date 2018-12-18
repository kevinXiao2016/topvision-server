/**
 *
 */
package com.topvision.platform.util;

import org.apache.ibatis.session.ResultContext;

import com.topvision.framework.event.MyResultHandler;

import net.sf.json.JSONArray;

/**
 * @author kelers
 */
public class JSONRowHandler implements MyResultHandler {
    protected JSONArray array = null;

    public JSONRowHandler(JSONArray array) {
        this.array = array;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.event.RowHandler#complete()
     */
    @Override
    public void complete() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.event.RowHandler#prepare()
     */
    @Override
    public void prepare() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.ibatis.session.ResultHandler#handleResult(org.apache.ibatis.session.ResultContext)
     */
    @Override
    public void handleResult(ResultContext resultcontext) {
    }
}
