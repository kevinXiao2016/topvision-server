/***********************************************************************
 * $Id: ZetaBlock.java,v1.0 2013-3-26 下午4:46:39 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.tag;

import java.io.BufferedReader;
import java.io.Reader;

import javax.servlet.jsp.JspTagException;

import com.topvision.platform.zetaframework.resource.PageContextLanguageAnalyzer;

/**
 * 对block内的元素进行解析
 * 对于某些特殊的场景，可能需要强制指定只使用某一种语言
 * @author Bravin
 * @created @2013-3-26-下午4:46:39
 *
 */
public class ZetaBlock extends ZetaElement {
    private static final long serialVersionUID = 6187959583964082164L;
    //支持 baseModule,如果没有在key中指定模块，可以在block中指定模块
    private String baseModule;

    @Override
    public int doStartTag() throws JspTagException {
        writer = pageContext.getOut();
        lang = (String) pageContext.getAttribute("lang");
        if (lang == null) {
            lang = PageContextLanguageAnalyzer.analyzeAppropriateLanguage(pageContext);
        }
        if (baseModule == null) {
            baseModule = (String) pageContext.getAttribute("com.zetaframework.module");
        }

        return EVAL_BODY_BUFFERED;
    }

    @Override
    public void doInitBody() throws JspTagException {
    }

    /**
     * Block资源块可以独立的使用某个模块
     */
    @Override
    public int doAfterBody() throws JspTagException {
        Reader reader = bodyContent.getReader();
        BufferedReader bufferedReader = new BufferedReader(reader);
        ResourceReader fileReader = new ResourceReader(bufferedReader, writer, baseModule, lang);
        fileReader.read();
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
        resetTag();
        return EVAL_PAGE;
    }

    /**
     * @return the baseModule
     */
    public String getBaseModule() {
        return baseModule;
    }

    /**
     * @param baseModule the baseModule to set
     */
    public void setBaseModule(String baseModule) {
        this.baseModule = baseModule;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

}
