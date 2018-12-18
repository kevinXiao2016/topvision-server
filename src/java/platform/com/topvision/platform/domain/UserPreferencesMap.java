package com.topvision.platform.domain;

import java.util.HashMap;

public class UserPreferencesMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 1407227431066299318L;

    public boolean getBoolean(String key, boolean dValue) {
        String temp = (String) super.get(key);
        return temp == null ? dValue : "true".equalsIgnoreCase(temp);
    }

    public int getInt(String key, int dValue) {
        String temp = (String) super.get(key);
        return temp == null ? dValue : Integer.parseInt(temp);
    }

    public String getString(String key, String dValue) {
        String temp = (String) super.get(key);
        return temp == null ? dValue : temp;
    }
}
