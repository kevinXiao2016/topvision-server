/***********************************************************************
 * $Id: ZetaPasswordField.java,v1.0 2013-4-3 下午4:26:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.zetaframework.javascript.JavaScriptPlugin;

/**
 * 提供 加密/明文 显示的密码输入框.
 * 目前所有输入框的高度都是固定的，所以不对高度进行设置，设置了会出问题
    <div class='zeta-password-field' style='width:180+3px'>
        <input type='text' class='zeta-password-text' style='width:180-20'/>
        <input type='password' class='zeta-password-text' style='width:180-20'/>
        <a href='javascript:void(0);' class='zeta-password-icon' />
    </div>
    
    <div>
	    <input type="text" class="normalInput floatLeft" >
	    <input type="password" class="floatLeft normalInput">
	    <a href="javascript:;" class="nearInputBtn"><span><i class="eyeCloseIco"></i></span></a>
	    <div style="clear:both; width:1px; height:0px; overflow:hidden;"></div>
    </div>
 * @author Bravin
 * @created @2013-4-3-下午4:26:51
 *
 */
public class ZetaPasswordField extends ZetaElement {
    private static final Logger logger = LoggerFactory.getLogger(ZetaPasswordField.class);
    private static final long serialVersionUID = 3496048381747464865L;
    /*private static final String PASSWORD_FIELD_TEMPLATE = "<div fieldId='%s' fieldName='%s' class='zeta-password-field' inputType='password' style='width:%spx'>";
    private static final String PASSWORD_FIELD_TAIL = "<img src='/js/zetaframework/passwordField/password.png' class='zeta-password-icon' /></div>";*/
    private static final String PASSWORD_FIELD_TEMPLATE = "<div fieldId='%s' fieldName='%s' class='zeta-password-field' inputType='password' style='width:%spx'>";
    private static final String PASSWORD_FIELD_TAIL = "<a href='javascript:;' class='nearInputBtn'><span><i class='eyeCloseIco'></i></span></a><div style='clear:both; width:0px; height:0px; overflow:hidden;'></div>";
    /*private static final Integer INPUT_OFFSET = 27;*/
    private static final Integer INPUT_OFFSET = 44;
    String width = "180";
    String value;
    Integer maxlength;
    String name;
    boolean readonly;
    Integer size;
    String tagType = "input";
    String type = "text";
    String tooltip;

    @Override
    public int doStartTag() throws JspTagException {
        writer = pageContext.getOut();
        //如果是以独立组件运行，则导入所需的依赖库
        if (pageContext.getAttribute("com.zetaframework.html") == null) {
            try {
                JavaScriptPlugin.dispatch(JavaScriptPlugin.PASSWORD_FIELD, pageContext, writer);
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        try {
            String widthNoStatus = width.replace("px", "");
            int widthNum = Integer.parseInt(widthNoStatus);
            Integer inputWidth = widthNum - INPUT_OFFSET;
            StringBuffer passwordLayout = new StringBuffer();
            passwordLayout.append(String.format(PASSWORD_FIELD_TEMPLATE, id, name, widthNoStatus));
            passwordLayout.append(generateInputField("text", inputWidth).toString());
            passwordLayout.append(generateInputField("password", inputWidth).toString());
            passwordLayout.append(PASSWORD_FIELD_TAIL);
            writer.print(passwordLayout.toString());
        } catch (IOException e) {
            logger.error("", e);
        }
        // CSS不应该放在后面，否则会导致样式出错，即时渲染的组件都应该立即导入相关的资源，而不应该放在其后，非即时渲染的组件可以放在BODY后面
        try {
            JavaScriptPlugin.dispatch(JavaScriptPlugin.PASSWORD_FIELD, pageContext, writer);
        } catch (IOException e) {
            logger.error("", e);
        }
        //JavaScriptLibrary.addAutoDependancePlugin(pageContext, JavaScriptPlugin.PASSWORD_FIELD);
        return SKIP_BODY;
    }

    /**
     * 生成输入框的内容
     * @param type
     * @param inputWidth
     * @return
     */
    private String generateInputField(String type, Integer inputWidth) {
        sb = new StringBuffer(); //reset StringBuffer
        sb.append("<input type=" + type);
        String style = "";
        String cssClass = "zeta-password-text normalInput floatLeft";
        if (css != null) {
            cssClass += css;
        }
        //默认只允许 密码输入框可显
        if (type.equals("text")) {
            style = "display:none;";
        } else {
            addProperty("name", name);
            addProperty("id", id);
        }
        style += "width:" + inputWidth + "px;";
        addProperty("style", style);
        addProperty("maxlength", maxlength);
        addProperty("value", value);
        if (readonly) {
            sb.append(" readonly");
        }
        if (disabled) {
            sb.append(" disabled");
        }
        if (tooltip != null) {
            addProperty("tooltip", tooltip);
        }
        addProperty("class", cssClass);
        sb.append(" />");
        return sb.toString();
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
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
        resetTag();
        return EVAL_PAGE;
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the maxlength
     */
    public Integer getMaxlength() {
        return maxlength;
    }

    /**
     * @param maxlength the maxlength to set
     */
    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the readonly
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * @param readonly the readonly to set
     */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * @return the tagType
     */
    public String getTagType() {
        return tagType;
    }

    /**
     * @param tagType the tagType to set
     */
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}
