/***********************************************************************
 * $Id: JavaScriptPlugin.java,v1.0 2013-3-30 下午7:10:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.javascript;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 插件管理不同于javascript管理，javascript库一般在文档前加载，plugin主要以自动依赖的方式在文档后加载 plugin的种类一般较多，不便于将
 * plugin与库混合在一个类中处理，所以单独提出来，并继承自 library mark by @bravin: 依赖关系最好通过配置文档指定，总以程序的方式维护不太方便
 * 
 * @author Bravin
 * @created @2013-3-30-下午7:10:42
 * 
 */
public class JavaScriptPlugin extends JavaScriptLibrary {
    public static final Logger logger = LoggerFactory.getLogger(JavaScriptPlugin.class);
    public static final String VBOX_PLUGIN = "Vbox";
    public static final String PORLET_PLUGIN = "Portlet";
    public static final String HIGHCHART_EXT_PLUGIN = "Highchart-Ext";
    public static final String PASSWORD_FIELD = "PasswordFiled";
    public static final String DATETIME_FIELD = "DateTimeField";
    public static final String LOV_COMBO = "LovCombo";
    public static final String COMBO_TREE = "ComboTree";
    public static final String REGION_SELECTOR = "RegionSelector";
    public static final String NM3K_TAB_BTN = "Nm3kTabBtn";
    public static final String DROPDOWN_TREE = "DropdownTree";
    public static final String NM3K_DROP_DOWN_TREE = "Nm3kDropdownTree";
    public static final String NM3K_DROP_TREE = "Nm3kDropTree";

    /**
     * 完成javascript plugin的分发
     * 
     * @param plugin
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    public static void dispatch(String plugin, PageContext pageContext, JspWriter writer)
            throws IOException {
        if (VBOX_PLUGIN.equalsIgnoreCase(plugin)) {
            doVboxPluginDispatch(pageContext, writer);
        } else if (PORLET_PLUGIN.equalsIgnoreCase(plugin)) {
            doExtPorletDispatch(pageContext, writer);
        } else if (HIGHCHART_EXT_PLUGIN.equalsIgnoreCase(plugin)) {
            doHighchartExtDispatch(pageContext, writer);
        } else if (PASSWORD_FIELD.equalsIgnoreCase(plugin)) {
            doPasswordFieldDispatch(pageContext, writer);
        } else if (DATETIME_FIELD.equalsIgnoreCase(plugin)) {
            doDateTimeFieldDispatch(pageContext, writer);
        } else if (LOV_COMBO.equals(plugin)) {
            doLovComboBoxDispatch(pageContext, writer);
        } else if (COMBO_TREE.equals(plugin)) {
            doComboTreeDispath(pageContext, writer);
        } else if (REGION_SELECTOR.equals(plugin)) {
            doRegionSelectorDispath(pageContext, writer);
        } else if (NM3K_TAB_BTN.equals(plugin)) {
            doNm3kTabBtnDispatch(pageContext, writer);
        } else if (DROPDOWN_TREE.equals(plugin)) {
            doDropdownTreeDispatch(pageContext, writer);
        } else if (NM3K_DROP_DOWN_TREE.equals(plugin)) {
            doNm3kDropDownTreeDispatch(pageContext, writer);
        }else if(NM3K_DROP_TREE.equals(plugin)){
        	doNm3kDropTreeDispatch(pageContext, writer);
        }
    }

	/**
     * Nm3kTabBtn组件的导入
     * 
     * @param pageContext
     * @param writer
     */
    private static void doNm3kTabBtnDispatch(PageContext pageContext, JspWriter writer) {
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        load("/js/jquery/Nm3kTabBtn.js", writer);
    }

    /**
     * loveBox插件引入
     * 
     * @param pageContext
     * @param writer
     */
    private static void doLovComboBoxDispatch(PageContext pageContext, JspWriter writer) {
        if (pageContext.getAttribute("com.zetaframework.ext") == null) {
            JavaScriptLibrary.importExt(pageContext, writer);
        }
        try {
            writer.println(String.format(STYLE_TEMPLATE, "/css/Ext.ux.form.LovCombo.css"));
            //writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/Ext.ux.form.LovCombo.js"));
            analyzerAndWrite(pageContext,writer,"/js/ext/Ext.ux.form.LovCombo");
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 引入passwordField所依赖的文件
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doPasswordFieldDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        // modify by @bravin:
        // 即时渲染的组件，为了确保CSS在HTML之前加载，故不能进行依赖管理，为了防止其组件被绑定上多个处理器，所以必须确保界面中只有一个js文件被加载
        if (pageContext.getAttribute("com.zetaframework.password") == null) {
            pageContext.setAttribute("com.zetaframework.password", true);
        } else {
            return;
        }
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        writer.println(String.format(STYLE_TEMPLATE, "/js/zetaframework/passwordField/PasswordField.css"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/passwordField/PasswordField.js"));
    }

    /**
     * Ext for Highchart 插件，依赖于Ext库,以及Ext的porlet插件
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doHighchartExtDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        if (pageContext.getAttribute("com.zetaframework.ext") == null) {
            JavaScriptLibrary.importExt(pageContext, writer);
        }
        if (pageContext.getAttribute("com.zetaframework.porlet") == null) {
            doExtPorletDispatch(pageContext, writer);
        }
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/adapter/highcharts/adapter-extjs.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/adapter/highcharts/Ext.ux.HighchartsPanelJson.js"));
    }

    /**
     * Ext Porlet插件导入
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doExtPorletDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        pageContext.setAttribute("com.zetaframework.porlet", true);
        if (pageContext.getAttribute("com.zetaframework.ext") == null) {
            JavaScriptLibrary.importExt(pageContext, writer);
        }
        writer.println(String.format(STYLE_TEMPLATE, "/css/ext-plugin.css"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/ux/Portal.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/ux/PortalColumn.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/ux/Portlet.js"));
    }

    /**
     * vbox插件依赖库加载
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doVboxPluginDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        writer.println(String.format(STYLE_TEMPLATE, "/css/vbox/jquery.vbox.css"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/vbox/jquery.vbox.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/vbox/jquery.vbox.lang.js"));
    }

    /**
     * 时间输入框
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doDateTimeFieldDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        if (pageContext.getAttribute("com.zetaframework.ext") == null) {
            JavaScriptLibrary.importExt(pageContext, writer);
        }
        writer.println(String.format(STYLE_TEMPLATE, "/js/zetaframework/datatimefield/css/Spinner.css"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/datatimefield/Spinner.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/datatimefield/SpinnerField.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/datatimefield/DateTimeField.js"));
    }

    /**
     * 引入下拉选择树
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doComboTreeDispath(PageContext pageContext, JspWriter writer) throws IOException {
        if (pageContext.getAttribute("com.zetaframework.ext") == null) {
            JavaScriptLibrary.importExt(pageContext, writer);
        }
        // 标记comboTree已被引入
        pageContext.setAttribute("com.zetaframework.comboTree", true);
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/plugins/Ext.form.ComboBoxTree.js"));
    }

    /**
     * 引入地域选择器
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doRegionSelectorDispath(PageContext pageContext, JspWriter writer) throws IOException {
        if (pageContext.getAttribute("com.zetaframework.comboTree") == null) {
            doComboTreeDispath(pageContext, writer);
        }
        pageContext.setAttribute("com.zetaframework.regionSelector", true);
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/component/RegionCombo.js"));
    }

    /**
     * 引入下拉树控件
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doDropdownTreeDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        pageContext.setAttribute("com.zetaframework.dropdownTree", true);
        writer.println(String.format(STYLE_TEMPLATE, "/js/dropdownTree/css/zTreeStyle/zTreeStyle.css"));
        writer.println(String.format(STYLE_TEMPLATE, "/js/dropdownTree/css/dropdowntree.css"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/dropdownTree/jquery.ztree.all-3.5.min.js"));
        analyzerAndWrite(pageContext,writer,"/js/dropdownTree/dropdowntree");
        //writer.println(String.format(SCRIPT_TEMPLATE, "/js/dropdownTree/dropdowntree.js"));
    }
    
    private static void analyzerAndWrite(PageContext pageContext,JspWriter writer, String javascript) {
    	String language = (String) pageContext.getAttribute("lang");
        JavaScriptLibrary.imports(javascript, pageContext, writer, null, language);
	}

	/**
     * 
     * @param pageContext
     * @param writer
     */
    private static void doNm3kDropTreeDispatch (PageContext pageContext, JspWriter writer) throws IOException {
		// TODO Auto-generated method stub
    	 pageContext.setAttribute("com.zetaframework.nm3kDropTree", true);
         writer.println(String.format(STYLE_TEMPLATE, "/js/dropdownTree/css/zTreeStyle/zTreeStyle.css"));
         writer.println(String.format(STYLE_TEMPLATE, "/js/dropdownTree/css/zTreeStyle/nm3kDropTree.css"));
         writer.println(String.format(SCRIPT_TEMPLATE, "/js/dropdownTree/jquery.ztree.all-3.5.min.js"));
         writer.println(String.format(SCRIPT_TEMPLATE, "/js/dropdownTree/jquery.ztree.exhide-3.5.min.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/dropdownTree/jquery.nm3kDropTree.js"));
	}

    /**
     * Ztree
     * 
     * @param pageContext
     * @param writer
     * @throws IOException
     */
    private static void doNm3kDropDownTreeDispatch(PageContext pageContext, JspWriter writer) throws IOException {
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        pageContext.setAttribute("com.zetaframework.ZTree", true);
        writer.println(String.format(STYLE_TEMPLATE, "/js/dropdownTree/css/zTreeStyle/zTreeStyle.css"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/dropdownTree/jquery.ztree.all-3.5.min.js"));
        writer.println(String.format(SCRIPT_TEMPLATE, "/js/nm3k/Nm3kDropDownTree.js"));
    }
}
