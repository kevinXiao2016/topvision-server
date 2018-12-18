/***********************************************************************
 * $Id: HtmlTag.java,v1.0 2013-3-13 下午7:01:04 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspTagException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.zetaframework.javascript.JavaScriptLibrary;
import com.topvision.platform.zetaframework.javascript.JavaScriptPlugin;

/**
 * @author Bravin
 * @created @2013-3-13-下午7:01:04
 *
 */
public class ZetaHtml extends ZetaElement {
    private static final Logger logger = LoggerFactory.getLogger(ZetaHtml.class);
    private static final long serialVersionUID = 1236995850347400109L;
    String tagType = "html";
    String dir;
    String lang;
    String xml_lang;
    String xmlns;
    boolean vmlSupport;
    
    public ZetaHtml() {
        super.tagType = this.tagType;
    }

    /**
     * HTML 的实现
     * 
     * @return
     * @throws JspTagException
     */
    @Override
    public void doInitBody() throws JspTagException {
        //标记这儿是在HTML中运行的，而不是以一个block的方式独立运行的
        pageContext.setAttribute("com.zetaframework.html", true);
        StringBuffer sb = getCommonBody();
        if (dir != null) {
            sb.append(String.format(" dir=%s", dir));
        }
        if (lang != null) {
            sb.append(String.format(" lang=%s", lang));
        }
        if (xml_lang != null) {
            sb.append(String.format(" xml:lang=%s", lang));
        }
        if (xmlns != null) {
            sb.append(String.format(" xmlns= %s", xmlns));
        }
        if (vmlSupport) {
            sb.append(" xmlns:v=\"urn:schemas-microsoft-com:vml\"");
        }
        sb.append(" >");
        try {
            writer.write(sb.toString());
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * HTML 处理完毕后，检验有哪些资源需要被自动导入
     */
    @Override
    public int doEndTag() throws JspTagException {
        List<String> autoDependanePluginList = JavaScriptLibrary.getAutoDependanePluginList(pageContext);
        if (autoDependanePluginList != null) {
            for (String plugin : autoDependanePluginList) {
                try {
                    JavaScriptPlugin.dispatch(plugin, pageContext, writer);
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        return super.doEndTag();
    }
    
    /**
     * @return the tagType
     */
    @Override
    public String getTagType() {
        return tagType;
    }

    /**
     * @param tagType
     *            the tagType to set
     */
    @Override
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    /**
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * @param dir
     *            the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang
     *            the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @return the xml_lang
     */
    public String getXml_lang() {
        return xml_lang;
    }

    /**
     * @param xml_lang
     *            the xml_lang to set
     */
    public void setXml_lang(String xml_lang) {
        this.xml_lang = xml_lang;
    }

    /**
     * @return the xmlns
     */
    public String getXmlns() {
        return xmlns;
    }

    /**
     * @param xmlns
     *            the xmlns to set
     */
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * @return the vmlSupport
     */
    public boolean isVmlSupport() {
        return vmlSupport;
    }

    /**
     * @param vmlSupport the vmlSupport to set
     */
    public void setVmlSupport(boolean vmlSupport) {
        this.vmlSupport = vmlSupport;
    }


}
