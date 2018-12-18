/***********************************************************************
 * $Id: ClassAware.java,v 1.1 2010-1-17 下午04:02:25 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * @Create Date 2010-1-17 下午04:02:25
 * 
 * @author kelers
 * 
 */
public class ClassAware implements ResourceLoaderAware {
    protected static final Logger logger = LoggerFactory.getLogger(ClassAware.class);
    private static final String RESOURCE_PATTERN = "/**/*.class";
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    public ClassAware() {
    }

    /**
     * 
     * @param excludeFilter
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }

    /**
     * 
     * @param includeFilter
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * 查找basePackage下所有的类
     * 
     * @param basePackage
     * @return
     */
    public Set<Class<?>> doScan(String basePackage) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils.convertClassNameToResourcePath(basePackage)
                    + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource != null && resource.isReadable()) {
                    try {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        String className = reader.getClassMetadata().getClassName();
                        if ((includeFilters.size() == 0 && excludeFilters.size() == 0) || matches(reader)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(className);
                            }
                            classes.add(Class.forName(className));
                        }
                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                        continue;
                    } catch (FileNotFoundException e) {
                    } catch (ClassNotFoundException e) {
                        logger.debug(e.getMessage(), e);
                    } catch (IOException e) {
                        logger.debug(e.getMessage(), e);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return classes;
    }

    /**
     * 
     * @return
     */
    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }

    /**
     * 
     * @param metadataReader
     * @return
     * @throws IOException
     */
    protected boolean matches(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param useDefaultFilters
     */
    public void resetFilters(boolean useDefaultFilters) {
        this.includeFilters.clear();
        this.excludeFilters.clear();
    }

    /**
     * 查找basePackage下面有annotations的所有类
     * 
     * @param basePackage
     * @param annotations
     * @return
     */
    public Set<Class<?>> scanAnnotation(String basePackage, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> anno : annotations)
            addIncludeFilter(new AnnotationTypeFilter(anno));
        return doScan(basePackage);
    }

    /**
     * 查找类和子类
     * @param basePackage 查找的包目录
     * @param c 要查找的类
     * @return
     */
    public Set<Class<?>> scanClass(String basePackage, Class<?> c) {
        addIncludeFilter(new AssignableTypeFilter(c));
        return doScan(basePackage);
    }

    /**
     * 查找所有basePackages下面有annotations的所有类
     * 
     * @param basePackages
     * @param annotations
     * @return
     */
    public Set<Class<?>> scan(String[] basePackages, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> anno : annotations)
            addIncludeFilter(new AnnotationTypeFilter(anno));
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String s : basePackages)
            classes.addAll(doScan(s));
        return classes;
    }

    /**
     * 查找basePackage下面有annotations的所有类
     * 
     * @param basePackage
     * @param annotations
     * @return
     */
    public Set<Class<?>> scanIf(String basePackage, Class<?>... interfazes) {
        for (Class<?> interfaze : interfazes)
            addIncludeFilter(new AssignableTypeFilter(interfaze));
        return doScan(basePackage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core
     * .io.ResourceLoader)
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
}
