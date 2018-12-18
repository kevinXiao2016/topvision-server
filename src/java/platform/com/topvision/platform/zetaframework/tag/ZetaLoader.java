/***********************************************************************
 * $Id: EmsTag.java,v1.0 2013-1-11 上午11:19:44 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.ThemeManager;
import com.topvision.platform.zetaframework.javascript.JavaScriptLibrary;
import com.topvision.platform.zetaframework.javascript.JavaScriptPlugin;
import com.topvision.platform.zetaframework.resource.PageContextLanguageAnalyzer;
import com.topvision.platform.zetaframework.resource.ResourceMapper;
import com.topvision.platform.zetaframework.resource.ResourceModule;
import com.topvision.platform.zetaframework.resource.ResourceRepository;
import com.topvision.platform.zetaframework.resource.ZetaResourceBundle;
import com.topvision.platform.zetaframework.stylesheet.CssStyleLoader;

/**
 * 统一资源管理器，对于JSP编译过程中自动发现的依赖库，在合适的地方自动加载
 * @author Bravin
 * @created @2013-1-11-上午11:19:44
 * 
 */
public class ZetaLoader extends BodyTagSupport {
    private static final long serialVersionUID = -6141576479688349279L;
    private static final Logger logger = LoggerFactory.getLogger(ZetaLoader.class);
    private JspWriter writer;
    private String language;
    public static final String BLANK_ROW_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String LIBRARY_PREFIX = "library ";
    public static final String IMPORT_PREFIX = "import ";
    public static final String MODULE_PREFIX = "module ";
    public static final String PLUGIN_PREFIX = "plugin ";
    public static final String CSS_PREFIX = "css ";

    public ZetaLoader() {
        super();
    }

    @Override
    public int doStartTag() throws JspTagException {
        writer = pageContext.getOut();
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    @Override
    public void doInitBody() throws JspTagException {
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        response.addHeader("pragma", "no-cache");
        response.addHeader("cache-control", "no-cache");
        response.addDateHeader("expries", 0);
        String cssStyleName = ThemeManager.getDefaultTheme();
        UserContext uc = (UserContext) request.getSession().getAttribute(UserContext.KEY);
        if (uc != null && uc.getStyleName() != null) {
            cssStyleName = uc.getStyleName();
        }
        language = PageContextLanguageAnalyzer.analyzeAppropriateLanguage(pageContext);
        pageContext.setAttribute("lang", language);
        pageContext.setAttribute("uc", uc);
        pageContext.setAttribute("cssStyleName", cssStyleName);
        JavaScriptLibrary.doNecessaryDependanceImport(pageContext, writer, request, language, uc, cssStyleName);
    }

    @Override
    public int doAfterBody() throws JspTagException {
        Reader reader = bodyContent.getReader();
        BufferedReader bufferedReader = new BufferedReader(reader);
        // JspWriter enclosingWriter = bodyContent.getEnclosingWriter();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (BLANK_ROW_STRING.equals(line)) {
                    continue;
                }
                line = line.trim();
                String content = line.substring(line.indexOf(" ") + 1);
                if (content != null) {
                    content = content.trim();
                    if (line.toLowerCase().startsWith(IMPORT_PREFIX)) {
                        doImpotHandler(content);
                    } else if (line.toLowerCase().startsWith(MODULE_PREFIX)) {
                        doResourceManagerHandler(content);
                    } else if (line.toLowerCase().startsWith(LIBRARY_PREFIX)) {
                        doLibraryHandler(content);
                    } else if (line.toLowerCase().startsWith(PLUGIN_PREFIX)) {
                        doPluginHandler(content);
                    } else if (line.toLowerCase().startsWith(CSS_PREFIX)) {
                        doCssStyleHandler(content);
                    }
                } else {
                    logger.error("load : {} error", line);
                }

            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return SKIP_BODY;
    }

    /**
     * 对各种插件进行处理
     * @param plugin
     */
    private void doPluginHandler(String plugin) {
        try {
            JavaScriptPlugin.dispatch(plugin, pageContext, writer);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * javascript库文件导入
     * @FIXME 反复的将pageContxt做参数传到处理器中会导致 writer状态异常，目前的做法是只传writer，有关pageContext的处理在writer之前处理，后期处理
     * @param library
     */
    private void doLibraryHandler(String library) {
        // @mark by bravin 可以判断当前版本是否是最近发布的内容，如果是最近发布的则使用服务器上的内容，否则使用缓存中的内容，或者测试时使用
        if (JavaScriptLibrary.EXT_LIBRARY.equalsIgnoreCase(library)) {
            JavaScriptLibrary.importExt(pageContext, writer);
        } else if (JavaScriptLibrary.JQUERY_LIBRARY.equalsIgnoreCase(library)) {
            JavaScriptLibrary.importJquery(pageContext, writer);
        } else if (JavaScriptLibrary.ZETA_LIBRARY.equalsIgnoreCase(library)) {
            JavaScriptLibrary.importZeta(pageContext, writer);
        } else if (JavaScriptLibrary.HIGHCHART_LIBRARY.equalsIgnoreCase(library)) {
            JavaScriptLibrary.importHighchart(pageContext, writer);
        } else if (JavaScriptLibrary.HIGHSTOCK_LIBRARY.equalsIgnoreCase(library)) {
            JavaScriptLibrary.importHighstock(pageContext, writer);
        } else if (JavaScriptLibrary.FILEUPLOAD_LIBRARY.equals(library)) {
            JavaScriptLibrary.importFileUpload(pageContext, writer);
        } else if (JavaScriptLibrary.SOCKET_LIBRARY.equals(library)) {
            JavaScriptLibrary.importSocket(pageContext, writer);
        }
        //@TODO ,CCMTS模块里面使用的是较老一个版本的highchart，先跑兼容模式，之后再修改
        else if (JavaScriptLibrary.HIGHCHART_LIBRARY_FOR_CCMTS.equalsIgnoreCase(library)) {
            JavaScriptLibrary.importHighchartForCcmts(pageContext, writer);
        }
    }

    /**
     * 导入基本的库，必须放在EXT导入之后
     * 
     * @throws IOException
     */
    public void doImportBasicLibrary() {
        String cssStyleName = (String) pageContext.getAttribute("cssStyleName");
        JavaScriptLibrary.importBasicLibrary(pageContext, writer, cssStyleName);
    }

    /**
     * CSS层叠样式表的导入
     * @param content
     */
    private void doCssStyleHandler(String content) {
        CssStyleLoader.load(writer, content);
    }

    /**
     * 资源导入管理
     * @param line
     * @throws IOException
     */
    private void doResourceManagerHandler(String moduleName) throws IOException {
        pageContext.setAttribute("com.zetaframework.module", moduleName);
        /****** @Deprecated 以下部分只是为了兼容老的国际化 *******/
        ResourceModule resourceModule = ResourceRepository.getModule(moduleName);
        Map<String, Object> re = new HashMap<String, Object>();
        ResourceMapper mapper = resourceModule.getMapper((String) pageContext.getAttribute("lang"));
        Stack<ZetaResourceBundle> stack = mapper.getBundleStack();
        Iterator<ZetaResourceBundle> it = stack.iterator();
        while (it.hasNext()) {
            try {
                ZetaResourceBundle zetaBundle = it.next();
                PropertyResourceBundle bundle = zetaBundle.getBundle();
                Set<String> keys = bundle.keySet();
                for (String key : keys) {
                    String value = bundle.getString(key);
                    makeReMap(key, value, re);
                }
            } catch (MissingResourceException mrex) {
                // continue
            }
        }
        /*  ResourceManager resourceManager = ResourceManager.getResourceManager(resourceModule.getPath());
          Properties properties = resourceManager.getProperties();
          for (String key : properties.stringPropertyNames()) {
              String value = properties.getProperty(key, key);
              makeReMap(key, value, re);
          }*/
        JSONObject i18n = JSONObject.fromObject(re);
        JavaScriptLibrary.writerJavascript(writer, " var I18N = " + i18n);
    }

    /**
     * 资源导入管理.为了兼容老的国际化
     * @param key
     * @param value
     * @param p
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    private void makeReMap(String key, String value, Map<String, Object> p) {
        int i = 0;
        String[] keys = key.split("\\.");
        for (; i < keys.length - 1; i++) {
            Map<String, Object> tmp;
            if (p.containsKey(keys[i]) && p.get(keys[i]) instanceof Map) {
                tmp = (Map<String, Object>) p.get(keys[i]);
            } else {
                tmp = new HashMap<String, Object>();
                p.put(keys[i], tmp);
            }
            p = tmp;
        }
        p.put(keys[i], value);
    }

    /**
     * 外部javascript文件的导入
     * @param path
     * @throws IOException
     */
    private void doImpotHandler(String path) throws IOException {
        String resourcePath = null, keyword = null;
        if (hasSpace(path)) {
            String[] array = path.split(SPACE_STRING);
            if (array.length > 1) {
                resourcePath = array[0];
                keyword = array[1];
            }
        } else {
            resourcePath = path;
        }
        JavaScriptLibrary.imports(resourcePath, pageContext, writer, keyword, this.language);
    }

    @Override
    public int doEndTag() throws JspTagException {
        /** modified by @bravin:  由于V2.0中要求myTheme.css必须在所有CSS后面，所以这里不动态依赖，而是在全部加载完之后才加入 **/
        doImportBasicLibrary();
        return EVAL_PAGE;
    }

    private static boolean hasSpace(String string){
        return string.matches("^(\\s|.*\\s+.*)$");
    }
}
