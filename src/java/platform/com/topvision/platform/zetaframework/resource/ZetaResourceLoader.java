/***********************************************************************
 * $Id: ZetaModuleLoader.java,v1.0 2013-3-25 下午7:54:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import java.util.Stack;

/**
 * @author Bravin
 * @created @2013-3-25-下午7:54:47
 *
 */
public class ZetaResourceLoader {
    private ZetaResourcePathMatchResolver resolver;

    /**
     * 将一个模块导入到WareHouse中，解析得出其继承路径的堆栈
     * @param module
     * @return
     */
    public void loadModule(String module, ResourceModule rm) {
        Stack<String> stack = resolver.getModuleResourcePathCollection(module);
        //栈顶就是这个资源文件不包含继承链的路径
        String peekPath = stack.pop();
        if(peekPath.endsWith(ZetaResourcePathMatchResolver.NON_STANDARD_RESOURCE_FILE_MARKER)){
            String dir=peekPath.substring(0,peekPath.length()-1);
            rm.setPath(dir.concat(".").concat(module.toLowerCase()));
        }else{
            rm.setPath(peekPath+".resources");
        }
        while (!stack.empty()) {
            rm.pushPath(stack.pop() + ".resources");
        }
    }
    
    /**
     * 将一个Mapper对应的ResourceBundle加载进入内存中
     * @param mapper
     * @param extendedPaths
     */
    public static void loadResourceBundle(ResourceMapper mapper, Stack<String> extendedPaths) {
        Stack<ZetaResourceBundle> stack = mapper.getBundleStack();
        for (String path : extendedPaths) {
            ZetaResourceBundle bundle = new ZetaResourceBundle(path, mapper.getLanguage());
            stack.push(bundle);
        }
    }

    /**
     * @return the resolver
     */
    public ZetaResourcePathMatchResolver getResolver() {
        return resolver;
    }

    /**
     * @param resolver the resolver to set
     */
    public void setResolver(ZetaResourcePathMatchResolver resolver) {
        this.resolver = resolver;
    }

}
