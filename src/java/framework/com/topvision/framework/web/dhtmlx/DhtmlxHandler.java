/**
 * 
 */
package com.topvision.framework.web.dhtmlx;

import org.dom4j.Element;

/**
 * @author niejun
 * 
 */
public interface DhtmlxHandler {

    /**
     * 根据用户对象产生结点.
     * 
     * @param userObject
     * @return
     */
    Element buildElement(Object userObject);

    /**
     * dhtmlx处理.
     * 
     * @param root
     */
    void handle(Element root);

}
