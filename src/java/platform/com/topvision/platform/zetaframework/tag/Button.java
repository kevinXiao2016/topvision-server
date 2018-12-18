/***********************************************************************
 * $Id: Button.java,v1.0 2013-8-1 下午2:57:22 $
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
import javax.servlet.jsp.tagext.BodyContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author Bravin
 * @created @2013-8-1-下午2:57:22
 *
 */
public class Button extends ZetaElement {
    private static final Logger logger = LoggerFactory.getLogger(Button.class);
    private static final long serialVersionUID = 5479197772911485116L;
    private String id;
    private String icon;
    private String clazz;
    private String style;
    private String onClick;
    private boolean disabled;
    private boolean ignoreLi;
    private boolean checkOperatePower;
    private static final String TEMPLATE = "<a href=\"javascript:;\" tpvButton='true' class=\"%s\" style=\"%s\">";

    public Button() {
        super();
    }

    @Override
    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    /**
     * @return
     * @throws JspTagException
     */
    @Override
    public void doInitBody() throws JspTagException {

    }

    @Override
    public int doAfterBody() throws JspTagException {
        if (checkOperatePower) {
            UserContext context = CurrentRequest.getCurrentUser();
            if (!context.hasPower("operationDevice")) {
                return SKIP_PAGE;
            }
        }
        StringBuffer sb = new StringBuffer();
        String _clazz, _icon, _style, _id, _disabled;
        if (clazz == null) {
            _clazz = "normalBtnBig";
        } else if (!clazz.contains("normalBtnBig") && clazz.contains("normalBtn")) {
            _clazz = clazz;
        } else {
            _clazz = "normalBtnBig ".concat(clazz);
        }
        if (icon == null) {
            _icon = "miniIcoEmpty";
        } else {
            _icon = "miniIcoSaveOK " + icon;
        }
        if (style == null) {
            _style = "";
        } else {
            _style = style;
        }
        if (id == null) {
            _id = "BT_" + (int) (Math.random() * 100000000);
        } else {
            _id = id;
        }
        if (disabled) {
            _disabled = "disabled";
        } else {
            _disabled = "";
        }
        String thisTemplate = TEMPLATE;
        //如果ignoreLi没有配置或者配置为false,则都加上li标签
        if (ignoreLi) {
            thisTemplate = "<dd>".concat(TEMPLATE);
        } else {
            thisTemplate = "<li>".concat(TEMPLATE);
        }
        sb.append(String.format(thisTemplate, _clazz, _style));
        Reader reader = bodyContent.getReader();
        BufferedReader bufferedReader = new BufferedReader(reader);
        String row;
        StringBuffer data = new StringBuffer();
        try {
            while ((row = bufferedReader.readLine()) != null) {
                if (row.length() > 0) {
                    data.append(row);
                }
            }
        } catch (IOException e1) {
            logger.error("", e1);
        }
        sb.append(String.format("<span><i class=\"%s\"></i><label class='BUTTON-TEXT'>%s</label></span>", _icon,
                data.toString()));
        sb.append("</a>");
        sb.append(String.format("<span id=\"%s\" onclick=\"%s\" %s></span>", _id, onClick, _disabled));
        sb.append(String.format("<script type='text/javascript'>R.%s = new Button('%s',%b);</script>", _id, _id,
                disabled));
        sb.append("</a>");
        //如果ignoreLi没有配置或者配置为false,则都加上li标签
        if (ignoreLi) {
            sb.append("</dd>");
        } else {
            sb.append("</li>");
        }
        try {
            writer.print(sb.toString());
        } catch (IOException e) {
            logger.error("", e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the clazz
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the style
     */
    @Override
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the onClick
     */
    public String getOnClick() {
        return onClick;
    }

    /**
     * @param onClick the onClick to set
     */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /**
     * @return the disabled
     */
    @Override
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isIgnoreLi() {
        return ignoreLi;
    }

    public void setIgnoreLi(boolean ignoreLi) {
        this.ignoreLi = ignoreLi;
    }

    @Override
    public String getOnclick() {
        return onClick;
    }

    @Override
    public void setOnclick(String onclick) {
        this.onClick = onclick;
    }

    public boolean isCheckOperatePower() {
        return checkOperatePower;
    }

    public void setCheckOperatePower(boolean checkOperatePower) {
        this.checkOperatePower = checkOperatePower;
    }

}
