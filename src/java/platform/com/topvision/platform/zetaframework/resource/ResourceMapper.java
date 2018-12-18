/***********************************************************************
 * $Id: Mapper.java,v1.0 2013-3-25 下午7:35:40 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.Stack;

import com.topvision.platform.zetaframework.exception.ReousrceKeyNotFoundException;

/**
 * 一个Mapper就是一种语言对应的资源文件栈，一个模块内包含有若干个Map，一个MapItem表示一种语言，MapItem的value就是Mapper
 * 不同MapItem之间并不是一起加载的，比如 OltVlan，一般只会加载 ch_CN,而 en_US则没有必要加载进来
 * @author Bravin
 * @created @2013-3-25-下午7:35:40
 *
 */
public class ResourceMapper {
    /**
     * 一种语言
     */
    private String language;
    /**
     * 资源文件队列
     */
    private Stack<ZetaResourceBundle> bundleStack;

    /**
     * 所属的module
     */
    private String module;

    public ResourceMapper(){
        bundleStack = new Stack<ZetaResourceBundle>();
    }

    public ResourceMapper(String module, String language) {
        this(language);
        this.module = module;
    }

    public ResourceMapper(String language) {
        this();
        this.language = language;
    }

    /**
     * 将一个ResourceBundle加载进入bundleStack中
     * @param bundle
     */
    public void pushBundle(ZetaResourceBundle bundle) {
        bundleStack.push(bundle);
    }


    /**
     * 得到国际化key的值，如果没有找到的话则使用key
     * @param key
     * @return
     */
    public String getString(String key) {
        Iterator<ZetaResourceBundle> it = bundleStack.iterator();
        while (it.hasNext()) {
            try {
                return it.next().getString(key);
            } catch (MissingResourceException mrex) {
                // continue
            }
        }
        throw new ReousrceKeyNotFoundException(key);
    }

    /**
     * @return the langguage
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param langguage the langguage to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the bundleStack
     */
    public Stack<ZetaResourceBundle> getBundleStack() {
        return bundleStack;
    }

    /**
     * @param bundleStack the bundleStack to set
     */
    public void setBundleStack(Stack<ZetaResourceBundle> bundleStack) {
        this.bundleStack = bundleStack;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

}
