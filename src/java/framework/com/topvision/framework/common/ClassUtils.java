/***********************************************************************
 * $Id: ClassUtils.java,v1.0 2011-3-31 下午04:40:55 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Victor
 * 
 */
public class ClassUtils {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private URLClassLoader loader;
    private String[] packages = null;

    public ClassUtils() {
        loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        if (logger.isTraceEnabled()) {
            logger.trace(loader.toString());
            for (URL url : loader.getURLs()) {
                logger.trace(url.toString());
            }
        }
    }

    /**
     * 
     * 在目录中查找
     * 
     * @param root
     * @param file
     * @param impls
     * @param superClass
     */
    private void findAnnotationInDirectory(File root, File file, List<Class<?>> clazzes,
            Class<? extends Annotation> annotation) {
        if (file == null || !file.exists()) {
            return;
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                findAnnotationInDirectory(root, files[i], clazzes, annotation);
            }
        } else if (file.getName().endsWith(".jar")) {
            findAnnotationInJar(file, clazzes, annotation);
        } else if (file.getName().endsWith(".class") && file.getName().indexOf('$') == -1) {
            try {
                String name = file.getPath();
                name = name.substring(root.getPath().length() + 1, name.lastIndexOf('.'));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < name.length(); i++) {
                    if (name.charAt(i) == '\\') {
                        sb.append('.');
                    } else {
                        sb.append(name.charAt(i));
                    }
                }
                match(clazzes, annotation, sb.toString());
            } catch (Throwable ex) {
                logger.error("Error:" + file);
            }
        }
    }

    private void findAnnotationInJar(File file, List<Class<?>> clazzes, Class<? extends Annotation> annotation) {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("findInJar:{}", file);
            }
            @SuppressWarnings("resource")
            JarFile jar = new JarFile(file);
            for (Enumeration<JarEntry> en = jar.entries(); en.hasMoreElements();) {
                JarEntry entry = en.nextElement();
                String name = entry.getName();
                if (entry.isDirectory() || !name.endsWith(".class") || name.indexOf('$') != -1) {
                    continue;
                }
                match(clazzes, annotation, name.substring(0, name.lastIndexOf('.')).replaceAll("/", "."));
            }
        } catch (Exception ex) {
            logger.error(file.toString(), ex);
        }
    }

    public List<Class<?>> findClassWithAnnotation(Class<? extends Annotation> annotation, String... pkgs) {
        packages = pkgs;
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        if (annotation == null) {
            logger.info("The annotation is null!");
            return clazzes;
        }
        loader = (URLClassLoader) annotation.getClassLoader();
        URL[] urls = loader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            try {
                if (logger.isTraceEnabled()) {
                    logger.trace("FileName:{}", urls[i].getFile());
                }
                File file = new File(URLDecoder.decode(urls[i].getPath(), "UTF-8"));
                if (logger.isTraceEnabled()) {
                    logger.trace("FilePath:{}", URLDecoder.decode(urls[i].getPath(), "UTF-8"));
                }
                if (file == null || !file.exists()) {
                    continue;
                } else if (file.isDirectory()) {
                    findAnnotationInDirectory(file, file, clazzes, annotation);
                } else if (file.getName().endsWith(".jar")) {
                    findAnnotationInJar(file, clazzes, annotation);
                } else if (file.getName().endsWith(".class") && file.getName().indexOf('$') == -1) {
                    try {
                        String name = file.getName().substring(0, file.getName().lastIndexOf('.'));
                        StringBuffer sb = new StringBuffer();
                        for (int j = 0; j < name.length(); j++) {
                            if (name.charAt(j) == '\\') {
                                sb.append('.');
                            } else {
                                sb.append(name.charAt(j));
                            }
                        }
                        match(clazzes, annotation, sb.toString());
                    } catch (Throwable ex) {
                        logger.debug("Find class[{}] error", file, ex);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace(clazzes.toString());
        }
        return clazzes;
    }

    private void match(List<Class<?>> clazzes, Class<? extends Annotation> annotation, String clazz) {
        try {
            boolean matched = false;
            for (int i = 0; !matched && packages != null && i < packages.length; i++) {
                if (clazz.indexOf(packages[i]) != -1) {
                    matched = true;
                }
            }
            if (!matched) {
                return;
            }
            Class<?> c = loader.loadClass(clazz);
            if (c.getAnnotation(annotation) != null) {
                clazzes.add(c);
            }
            if (logger.isTraceEnabled()) {
                logger.trace("Find class {}", clazz);
            }
        } catch (Throwable th) {
            logger.error(clazz);
        }
    }
}
