/***********************************************************************
 * $Id: JavaScriptLibrary.java,v1.0 2013-3-30 上午11:12:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.javascript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.FileUtils;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.tag.ResourceReader;

/**
 * 1.   通用javascript库的别名定义
 * 2.   javascript插件标记
 * 由于JspWriter自身的bug，当多次调用pageContext.getOut()方法后，writer中的内容无法输出到前端，
 * 故全局只使用一个writer，每个方法传递至少PageContext与JspWriter两个对象
 * @author Bravin
 * @created @2013-3-30-上午11:12:58
 *
 */
public class JavaScriptLibrary extends JavascriptLoader {
    public static final Logger logger = LoggerFactory.getLogger(JavaScriptLibrary.class);

    public static final String EXT_LIBRARY = "Ext";
    public static final String JQUERY_LIBRARY = "Jquery";
    public static final String ZETA_LIBRARY = "Zeta";
    public static final String HIGHCHART_LIBRARY = "Highchart";
    public static final String HIGHSTOCK_LIBRARY = "Highstock";
    public static final String FILEUPLOAD_LIBRARY = "FileUpload";
    public static final String SOCKET_LIBRARY = "Socket";
    public static final String HIGHCHART_LIBRARY_FOR_CCMTS = "HighchartForCcmts";

    /**
     * ext 库导入 
     * ext-all.css 必须要放在 xTheme和myTheme后面，后续考虑怎么能更加简单的整合
     * $mark by bravin base必须放在 ext之后
     * @param pageContext
     * @param writer
     */
    public static void importExt(PageContext pageContext, JspWriter writer) {
        pageContext.setAttribute("com.zetaframework.ext", true);
        try {
            writer.println(String.format(STYLE_TEMPLATE, "/css/ext-all.css"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/ext-all-debug.js"));
            String lang = (String) pageContext.getAttribute("lang");
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/ext-lang-" + lang + ".js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/nm3k/Nm3K_" + lang + ".js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/ExtAdapter.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 导入基本外部库文件，每个JSP都必须导入的
     * @param pageContext
     * @param writer
     * @param cssStyleName
     */
    public static void importBasicLibrary(PageContext pageContext, JspWriter writer, String cssStyleName) {
        try {
            String xTheme = String.format("/css/%s/xtheme.css", cssStyleName);
            String myTheme = String.format("/css/%s/mytheme.css", cssStyleName);
            writer.println(String.format(STYLE_TEMPLATE, xTheme));
            writer.println(String.format(STYLE_TEMPLATE, myTheme));
            //writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/Validator.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 导入Jquery库
     * @param pageContext
     * @param writer
     */
    public static void importJquery(PageContext pageContext, JspWriter writer) {
        pageContext.setAttribute("com.zetaframework.jquery", true);
        try {
            /** jquery成为基本包固定被引入  */
            //writer.println(String.format(SCRIPT_TEMPLATE, "/js/jquery/jquery.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/jquery/nm3kToolTip.jsr"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/jquery/jquery.validate.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/JqueryAdapter.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 导入 Zeta 库
     * TODO zeta-core.js 需要改为 zetaframwork.js，拆分出其核心功能
     * @param pageContext
     * @param writer
     */
    public static void importZeta(PageContext pageContext, JspWriter writer) {
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/zeta-core.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/zeta-versioncontrol.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 导入 Highchart 库
     * @param pageContext
     * @param writer
     */
    public static void importHighchart(PageContext pageContext, JspWriter writer) {
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/highcharts/highcharts.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/Highchart-Adapter.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 统一处理需要自动导入库的plugin
     * @param pageContext
     * @param plugin
     */
    @SuppressWarnings("unchecked")
    public static void addAutoDependancePlugin(PageContext pageContext, String plugin) {
        List<String> autoDependanceList = new ArrayList<String>();
        Object obj = pageContext.getAttribute("com.zetaframework.autoDependanceList");
        if (obj != null) {
            autoDependanceList = (List<String>) obj;
        }
        if (!autoDependanceList.contains(plugin)) {
            autoDependanceList.add(plugin);
            pageContext.setAttribute("com.zetaframework.autoDependanceList", autoDependanceList);
        }
    }

    /**
     * 从pageContext中获取出需要自动加载文档的库
     * @param pageContext
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getAutoDependanePluginList(PageContext pageContext) {
        Object obj = pageContext.getAttribute("com.zetaframework.autoDependanceList");
        if (obj == null) {
            return null;
        }
        List<String> autoDependanceList = (List<String>) obj;
        return autoDependanceList;
    }

    /**
     * 必须导入的依赖文件，而且必须在document开头导入的文件
     * 由于basic有加载的先后顺序，必须在ext之后，所以单独提这一次方法
     * @param pageContext
     * @param writer
     * @param cssStyleName 
     * @param uc 
     * @param language 
     * @param request2 
     */
    public static void doNecessaryDependanceImport(PageContext pageContext, JspWriter writer,
            HttpServletRequest request, String language, UserContext uc, String cssStyleName) {
        try {
            /** 基础部分 ***/
            String userName = uc == null ? "" : uc.getUser().getUserName();
            boolean displayInputTip = uc == null ? true : uc.isDisplayInputTip();
            String entityId = request.getParameter("entityId");
            StringBuilder sb = new StringBuilder();
            sb.append("var lang ='" + language + "';").append("\n");
            sb.append("var userName = '" + userName + "';").append("\n");
            sb.append("var cssStyleName = '" + cssStyleName + "';").append("\n");
            sb.append("var displayInputTip = " + displayInputTip + ";").append("\n");
            sb.append("var isEmsDevelopment = " + SystemConstants.isDevelopment + ";").append("\n");
            if (entityId != null) {
                sb.append("var CONTROL_ENTITYID = " + entityId + ";").append("\n");
            }
            if (uc != null) {
                sb.append("var operationDevicePower = " + uc.hasPower("operationDevice") + ";").append("\n");
            }
            JavaScriptLibrary.writerJavascript(writer, sb.toString());
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
            writer.println(String.format(STYLE_TEMPLATE, "/css/gui.css"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/jquery/jquery.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/ext/ext-base.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/ZetaAdapter.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/JavascriptApater.js"));
        } catch (IOException e) {
            logger.error("load ext-base resource error: {}", e);
        }
    }

    /**
     * 引入 HighchartForCcmts的依赖包
     * @param pageContext
     * @param writer
     */
    public static void importHighchartForCcmts(PageContext pageContext, JspWriter writer) {
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/highcharts/highcharts.src.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/Highchart-Adapter.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 引入Highstock类库
     * @param pageContext
     * @param writer
     */
    public static void importHighstock(PageContext pageContext, JspWriter writer) {
        if (pageContext.getAttribute("com.zetaframework.jquery") == null) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        }
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/highstock/highstock.src.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/zetaframework/adapaters/Highchart-Adapter.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 引入文件上传组件
     * @param pageContext
     * @param writer
     */
    public static void importFileUpload(PageContext pageContext, JspWriter writer) {
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/FileUpload/TopvisionUpload.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 输入一段javascript代码
     * @param writer
     * @param js
     */
    public static void writerJavascript(JspWriter writer, String js) {
        try {
            writer.println("<script type=\"text/javascript\">");
            writer.println(js);
            writer.println("</script>");
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 导入外部Javascript文件，直接加载到响应中
     * @FIXME 目前支持单行最大1024，多了就超出bufferedreader的范围了，需要考虑按字符读取
     * @param path
     * @param pageContext
     * @param writer
     * @param keyword 
     * @param language 
     * @param object 
     */
    public static void imports(String path, PageContext pageContext, JspWriter writer, String keyword, String language) {
        StringBuffer sb = new StringBuffer();
        String[] urls;
        if (keyword != null) {
            if (path.indexOf(PATH_SEPAROTR) > -1) {
                urls = path.split(PATH_SEPAROTR);
            } else {
                urls = path.split("\\.");
            }
            sb.append(PATH_SEPAROTR);
            for (String url : urls) {
                if (!"".equals(url)) {
                    sb.append(url);
                    sb.append(PATH_SEPAROTR);
                }
            }
            path = sb.substring(0, sb.length() - 1).concat(".jsr?language=").concat(language);
            if ("static".equalsIgnoreCase(keyword)) {
                try {
                    writer.println(String.format(SCRIPT_TEMPLATE, path));
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            return;
        }
        if (path.indexOf(PATH_SEPAROTR) > -1) {
            urls = path.split(PATH_SEPAROTR);
            sb.append(SystemConstants.ROOT_REAL_PATH);
        } else {
            urls = path.split("\\.");
            sb.append(SystemConstants.ROOT_REAL_PATH);
        }
        for (String url : urls) {
            if (!"".equals(url)) {
                sb.append(url);
                sb.append(File.separator);
            }
        }
        path = sb.substring(0, sb.length() - 1) + JAVASCRIPT_SUFFIX;

        File file = new File(path);
        FileInputStream in = null;
        try {
            writer.println("<script type=\"text/javascript\">");
            try {
                in = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                ResourceReader fileReader = new ResourceReader(bufferedReader, pageContext, writer);
                fileReader.read();
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                FileUtils.closeQuitely(in);
            }
            writer.println("</script>");
        } catch (IOException e1) {
            logger.error("", e1);
        }
    }

    /**
     * socket库
     * @param pageContext
     * @param writer
     */
    public static void importSocket(PageContext pageContext, JspWriter writer) {
        try {
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/TopvisionSocket/TopvisionSocket.js"));
            writer.println(String.format(SCRIPT_TEMPLATE, "/js/TopvisionSocket/TopvisionWebSocket.js"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
