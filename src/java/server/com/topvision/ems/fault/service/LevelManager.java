package com.topvision.ems.fault.service;

import java.util.HashMap;
import java.util.Map;

import com.topvision.ems.fault.domain.Level;

public final class LevelManager {

    private Map<Byte, Level> levelMapping = new HashMap<Byte, Level>();
    private static LevelManager levelManager = new LevelManager();

    /**
     * 
     * @return levelManager
     */
    public static LevelManager getInstance() {
        return levelManager;
    }

    /**
     * 
     * @param level
     * @return Level
     */
    public Level getLevel(byte level) {
        return levelMapping.get(level);
    }

    /**
     * 
     * @param level
     * @return String
     */
    public String getLevelName(byte level) {
        Level l = levelMapping.get(level);
        return l == null ? "" : l.getName();
    }

    /**
     * 
     * @param level
     * @param value
     */
    public void putLevel(byte level, Level value) {
        levelMapping.put(level, value);
    }

}
