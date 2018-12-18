/**
 * 
 */
package com.topvision.framework.utils;

/**
 * @author Administrator
 *
 */
public class NameUtils {
	private static final String STRIP_NAME = "[^a-zA-Z\\d\u4e00-\u9fa5-_\\[\\]()\\/\\.:]";
	
	public static String stripSpecialChar(String name){
		return name.replaceAll(STRIP_NAME, "");
	}

}
