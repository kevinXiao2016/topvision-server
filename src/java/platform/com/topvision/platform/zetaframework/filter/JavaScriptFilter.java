/***********************************************************************
 * $Id: JavaScriptFilter.java,v1.0 2013-3-16 下午4:22:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.common.FileUtils;
import com.topvision.framework.web.filter.TemplateFilter;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.resource.PageContextLanguageAnalyzer;
import com.topvision.platform.zetaframework.tag.ResourceReader;
import com.topvision.platform.zetaframework.util.ResourceKeyAnalyzer;
import com.topvision.platform.zetaframework.util.ZetaUtil;

/**
 * @author Bravin
 * @created @2013-3-16-下午4:22:09
 *
 */
public class JavaScriptFilter extends TemplateFilter {
    private static Logger logger = LoggerFactory.getLogger(JavaScriptFilter.class);
    private static final int REQUEST_URL_STARTOR_INDEX = 1;
    private static final String DWR_REQUEST_MARKER = "dwr";


    @Override
    public void doPostProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    }

    @Override
    public boolean doPreProcessing(ServletRequest request, ServletResponse response) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        /* 为了方便处理，以及代码美观，将请求的第一个字符 / 去掉，一方面可方便 dwr 请求的处理，另一方面组装 请求地址的时候也更方便，因为 ROOT_REAL_PATH已自带 */
        String requestUrl = httpRequest.getRequestURI().substring(REQUEST_URL_STARTOR_INDEX);
        response.setCharacterEncoding(STANDARD_ENCODING);
        if (requestUrl.startsWith(DWR_REQUEST_MARKER)) {
            return true;
        }
        UserContext uc = (UserContext) httpRequest.getSession().getAttribute(UserContext.KEY);
        Object language = request.getParameter("language");
        if (language == null) {
            language = PageContextLanguageAnalyzer.analyzeAppropriateLanguage(uc);
        }
        doLoadJavascript(requestUrl, response, (String) language);
        return false;
    }

    /**
     * 加载，处理javascript文件
     * @param requestURI  javascript请求的url
     * @param response 包含writer的response
     * @param language 使用指定的语言解析javascript内部的key
     * @throws IOException
     */
    private void doLoadJavascript(String requestURI, ServletResponse response, String language) throws IOException {
        InputStream in = null;
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            in = new FileInputStream(SystemConstants.ROOT_REAL_PATH
                    + requestURI.replace("/", File.separator).replace("jsr", "js"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, STANDARD_ENCODING));
            ResourceReader fileReader = new ResourceReader(bufferedReader, language, writer);
            fileReader.read();
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(in);
            if (null != writer) {
                //writer.flush();
            }
        }
    }

    /**
     * 单独处理每一行，对每一行都单独进行处理 mark by @bravin 也就是说我们不支持跨行的使用国际化key
     * 
     * @param data 行数据
     * @param language 使用指定的语言解析javascript中的key
     * @throws IOException
     */
    public void handleRow(String data, PrintWriter writer, String language) throws IOException {
        int idx = data.indexOf(ResourceKeyAnalyzer.KEYSTRING_STOP_TOKENIZER);
        int c = data.length();
        int flag = 0;
        if (idx != -1) {
            /* 先输出@前部分的内容，不可以换行  */
            writer.print(data.substring(0, idx));
            while (idx < c) {
                //注意，idx表示的是一个被找到是 stop_tokenizer的字符的位置，所以如果找到了第一个key后，idx必须要重新再找一次进行
                flag = idx + 1;
                idx = data.indexOf(ResourceKeyAnalyzer.KEYSTRING_STOP_TOKENIZER, flag);
                if (idx != -1) {
                    /*有可能存在 @并非用于国际化，而在第二个@出现的时候才是真正的国际化，而导致误判，所以需要决定sub子串到底是否是为了国际化的意图，如果不是，则需要动态的移动游标*/
                    String sub = data.substring(flag, idx);
                    if (ResourceKeyAnalyzer.isKeyString(sub)) {
                        /** 需要保证是按照 "@module/keySuper.keySubA.keySubB@" 这样的形式组成的key，可以在这里抛出描述性更强的异常  */
                        sub = ZetaUtil.getStaticString(sub, language);
                        writer.print(sub);
                        String restData = data.substring(idx + 1);
                        handleRow(restData, writer, language);
                        break;
                    }
                } else {
                    String sub = data.substring(flag, c);
                    /*由于这是行结束的地方，所以一定可以换行**/
                    writer.print(sub);
                    break;
                }
            }

        } else {
            writer.print(data);
        }
    }



    @Override
    public void initParameter(FilterConfig filterConfig) throws ServletException {
    }


}
