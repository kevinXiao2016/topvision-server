/**
 * 
 */
package com.topvision.ems.tl1;

/**
 * @author Administrator
 *
 */
public interface TL1ExecutorService<K> {

    /**
     * @param entityId
     * @param index
     * @param string
     */
    void execute(Long entityId, Long index, K config);

}
