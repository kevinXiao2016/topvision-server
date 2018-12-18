/***********************************************************************
 * $Id: BaseHtmlTag.java,v1.0 2013-1-14 上午9:41:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.zetaframework.resource.PageContextLanguageAnalyzer;

/**
 * JSP TAG是单例 的，必须保证每个属性是无状态的，所以不要试图对属性加状态。
 * @author Bravin
 * @created @2013-1-14-上午9:41:42
 * 
 */
public abstract class ZetaElement extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ZetaElement.class);
    // tag标签的类型
    String tagType;
    String id;
    String css;
    String style;
    String width;
    Integer height;
    boolean disabled;
    // 事件处理建议做MVC，不允许在标签中使用
    String onclick;
    String onmouseover;
    String onmouseout;
    String onmousedown;
    String onkeyup;
    String handler;
    // 对于非input类的标签，key在body中做，input标签在init中做
    String key;
    String title;
    // tooltip建议做封装
    String tooltip;
    JspWriter writer;
    String lang;
    protected StringBuffer sb;

    /**
     * 保持无状态
     */
    protected void resetTag() {
        sb = null;
        css = null;
    }

    @Override
    public int doStartTag() throws JspTagException {
        writer = pageContext.getOut();
        lang = (String) pageContext.getAttribute("lang");
        /**
         * 对于有的页面不一定存在 zetaLoader，故如果 在 pageContext中取不到 language的话就分析一次
         */
        if (lang == null) {
            lang = PageContextLanguageAnalyzer.analyzeAppropriateLanguage(pageContext);
        }
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    /**
     * 子类都必须调用的,">" or "/>"由子类自己实现
     * 
     * @return
     * @throws JspTagException
     */
    @Override
    public void doInitBody() throws JspTagException {
        StringBuffer sb = getCommonBody();
        sb.append(" >");
        try {
            writer.print(sb.toString());
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 可以供所有子类使用的公共方法
     * 
     * @return
     */
    StringBuffer getCommonBody() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("<%s", tagType));
        addProperty("id", id);
        addProperty("width", width);
        addProperty("height", height);
        addProperty("style", style);
        addProperty("title", title);
        addProperty("class", css);
        addProperty("onclick", handler);
        if (disabled) {
            sb.append(String.format(" disabled"));
        }
        return sb;
    }

    /**
     * 添加属性
     * @param property
     * @param value
     */
    protected void addProperty(String property, Object value) {
        if (value != null) {
            sb.append(String.format(" %s=\"%s\"", property, value));
        }
    }

    @Override
    public int doAfterBody() throws JspTagException {
        Reader reader = bodyContent.getReader();
        BufferedReader bufferedReader = new BufferedReader(reader);
        ResourceReader fileReader = new ResourceReader(bufferedReader, pageContext, writer);
        fileReader.read();
        return SKIP_BODY;
    }



    @Override
    public int doEndTag() throws JspTagException {
        try {
            writer.println(String.format("</%s>", tagType));
        } catch (IOException e) {
            logger.error("", e);
        }
        resetTag();
        return EVAL_PAGE;
    }



    /**
     * @return the tagType
     */
    public String getTagType() {
        return tagType;
    }

    /**
     * @param tagType
     *            the tagType to set
     */
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the css
     */
    public String getCss() {
        return css;
    }

    /**
     * @param css
     *            the css to set
     */
    public void setCss(String css) {
        this.css = css;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     *            the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled
     *            the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the writer
     */
    public JspWriter getWriter() {
        return writer;
    }

    /**
     * @param writer
     *            the writer to set
     */
    public void setWriter(JspWriter writer) {
        this.writer = writer;
    }


    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the onclick
     */
    public String getOnclick() {
        return onclick;
    }

    /**
     * @param onclick
     *            the onclick to set
     */
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
     * @return the onmouseover
     */
    public String getOnmouseover() {
        return onmouseover;
    }

    /**
     * @param onmouseover
     *            the onmouseover to set
     */
    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    /**
     * @return the onmouseout
     */
    public String getOnmouseout() {
        return onmouseout;
    }

    /**
     * @param onmouseout
     *            the onmouseout to set
     */
    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    /**
     * @return the onmousedown
     */
    public String getOnmousedown() {
        return onmousedown;
    }

    /**
     * @param onmousedown
     *            the onmousedown to set
     */
    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    /**
     * @return the onkeyup
     */
    public String getOnkeyup() {
        return onkeyup;
    }

    /**
     * @param onkeyup
     *            the onkeyup to set
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    /**
     * @return the handler
     */
    public String getHandler() {
        return handler;
    }

    /**
     * @param handler
     *            the handler to set
     */
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip
     *            the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * class 属性的 setter
     * 
     * @param clazz
     */
    public void setClass(String clazz) {
        this.css = clazz;
    }

    public void setclass(String clazz) {
        this.css = clazz;
    }
}
