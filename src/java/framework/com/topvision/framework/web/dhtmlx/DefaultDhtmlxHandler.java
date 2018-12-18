/**
 * 
 */
package com.topvision.framework.web.dhtmlx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.topvision.framework.domain.TreeEntity;

/**
 * @author niejun
 * 
 */
public abstract class DefaultDhtmlxHandler implements DhtmlxHandler {
    private List<? extends TreeEntity> data = null;

    public DefaultDhtmlxHandler() {
    }

    /**
     * 
     * @param list
     */
    public DefaultDhtmlxHandler(List<? extends TreeEntity> list) {
        setData(list);
    }

    @Override
    public abstract Element buildElement(Object obj);

    public List<? extends TreeEntity> getData() {
        return data;
    }

    @Override
    public void handle(Element root) {
        if (data == null) {
            return;
        }
        Map<String, Element> map = new HashMap<String, Element>();
        map.put("", root);
        map.put("0", root);
        map.put("-1", root);
        int size = data.size();
        for (int i = 0; i < size; i++) {
            Object obj = data.get(i);
            if (obj instanceof TreeEntity) {
                TreeEntity entity = (TreeEntity) obj;
                Element cElement = buildElement(obj);
                if (cElement == null) {
                    continue;
                }
                map.put(entity.getId(), cElement);
                Element pElement = map.get(entity.getParentId());
                if (pElement == null) {
                    root.add(cElement);
                } else {
                    pElement.add(cElement);
                }
            } else {
                throw new DhtmlxTreeException(obj.getClass() + " is not TreeEntity.");
            }
        }
        map.clear();
        map = null;
    }

    public void setData(List<? extends TreeEntity> data) {
        this.data = data;
    }
}
