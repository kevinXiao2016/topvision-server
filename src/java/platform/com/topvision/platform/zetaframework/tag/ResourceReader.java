/***********************************************************************
 * $Id: ResourceFileReader.java,v1.0 2013-5-19 下午5:57:08 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.zetaframework.util.ResourceKeyAnalyzer;
import com.topvision.platform.zetaframework.util.ZetaUtil;
import com.topvision.platform.zetaframework.valuegetter.ZetaValueReflection;

/**
 * 解析其中包含有国际化内容 输入流
 * @author Bravin
 * @created @2013-5-19-下午5:57:08
 *
 */
public class ResourceReader {
    private static final Logger logger = LoggerFactory.getLogger(ResourceReader.class);
    // 需要被读取的reader
    private BufferedReader bufferedReader;
    // 默认的国际化资源模块
    private String resourceModule;
    // 输入流
    private Writer writer;
    private PageContext pageContext;
    private String lang;

    public ResourceReader(BufferedReader reader, String lang, PrintWriter writer) {
        this.bufferedReader = reader;
        this.lang = lang;
        this.writer = writer;
    }

    public ResourceReader(BufferedReader reader, PageContext pageContext, JspWriter writer) {
        this.bufferedReader = reader;
        this.pageContext = pageContext;
        this.lang = (String) pageContext.getAttribute("lang");
        this.resourceModule = (String) pageContext.getAttribute("com.zetaframework.module");
        this.writer = writer;
    }

    public ResourceReader(BufferedReader reader, JspWriter writer, String resourceModule, String lang) {
        this.bufferedReader = reader;
        this.lang = lang;
        this.resourceModule = resourceModule;
        this.writer = writer;
    }

    /**
     * 对reader中的内容今夕解析并输出
     */
    public void read() {
        try {
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                if (data.length() > 0) {
                    handleRow(data);
                    writer.write("\r\n");// 换行，由于下面使用的递归的方式，所以在这里换行比较方便
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }

    }

    private void handleExpression(String sub, String data, int idx) throws IOException {
        ServletRequest request = pageContext.getRequest();
        String[] params = sub.split("\\.");
        if (params.length != 2) {
            logger.debug("two parameters or more is not support!");
        }
        String first = params[0];
        if (first.toLowerCase().startsWith("json:")) {
            String attribute = first.substring(5);
            Object $attr = request.getAttribute(attribute);
            if ($attr != null) {
                boolean primitive = $attr.getClass().isPrimitive();
                if (primitive) {
                    writer.write($attr.toString());
                } else if ($attr instanceof JSONArray || $attr instanceof JSONObject) {
                    writer.write($attr.toString());
                } else if ($attr instanceof Collection<?> || $attr instanceof List) {
                    writer.write(JSONArray.fromObject($attr).toString());
                }
            }
            return;
        }
        if (!sub.contains(".")) {
            writer.write(request.getAttribute(first).toString());
        } else {
            Object $value = ZetaValueReflection.getValue(sub);
            if ($value == null) {
                return;
            }
            writer.write(String.valueOf($value));
        }
        String restData = data.substring(idx + 1);
        handleRow(restData);
    }

    /**
     * 单独处理每一行，对每一行都单独进行处理
     * mark by @bravin 也就是说我们不支持跨行的使用国际化key
     * 
     * @param data
     * @throws IOException
     */
    private void handleRow(String data) throws IOException {
        int idx = data.indexOf(ResourceKeyAnalyzer.KEYSTRING_STOP_TOKENIZER);
        int c = data.length();
        int flag = 0;
        if (idx != -1) {
            /* 先输出@前部分的内容，不可以换行 */
            writer.write(data.substring(0, idx));
            while (idx < c) {
                // 注意，idx表示的是一个被找到是 stop_tokenizer的字符的位置，所以如果找到了第一个key后，idx必须要重新再找一次进行
                flag = idx + 1;
                idx = data.indexOf(ResourceKeyAnalyzer.KEYSTRING_STOP_TOKENIZER, flag);
                if (idx != -1) {
                    /*
                     * 有可能存在
                     * @并非用于国际化，而在第二个@出现的时候才是真正的国际化，而导致误判，所以需要决定sub子串到底是否是为了国际化的意图，如果不是，则需要动态的移动游标
                     */
                    String sub = data.substring(flag, idx);
                    if (isExpression(sub)) {
                        sub = sub.substring(1, sub.length() - 1);
                        handleExpression(sub, data, idx);
                        break;
                    } else if (ResourceKeyAnalyzer.isKeyString(sub)) {
                        // 如果为 null ,则必须再次从 pageContext 中获取一次
                        if (resourceModule == null) {
                            reloadResourceModule();
                        }
                        //界面中如果找不到key,则返回@key@而不是key，防止数据被篡改
                        sub = ZetaUtil.getPageRenderString(sub, resourceModule, lang);
                        writer.write(sub);
                        String restData = data.substring(idx + 1);
                        handleRow(restData);
                        break;
                    } else {// 如果不是想要的key则把这个key以前前面的token输出
                        String notKeyString = data.substring(flag - 1, idx);
                        writer.write(notKeyString);
                    }
                } else {
                    // mark by @bravin: 如果没有找到第二个@符合则要把游标退一步
                    String sub = data.substring(flag - 1, c);
                    /* 由于这是行结束的地方，所以一定可以换行* */
                    writer.write(sub);
                    break;
                }
            }

        } else {
            writer.write(data);
        }
    }

    /**
     * 判断是否需要执行表达式解析
     * @param expression
     * @return
     */
    public static boolean isExpression(String expression) {
        String regex = "^\\{.*\\}$";
        return expression.matches(regex);
    }

    /**
     * 由于 Loader嵌套在 HTML内部，在
     * HTMLstart的时候,loader还没有执行，所以所以在body中第一次使用的时候需要判断是否rm已经加载过，如果没有需要再次赋值
     * 另外，由于只有HTML标签才有这种问题，对于其他标签不会有这种问题,Title标签如果放在loader前面，则肯定会取不到，是个问题
     */
    private void reloadResourceModule() {
        // 如果构造器中包含有pageContext，则进行解析，如果没有包含pageContext，则不解析
        if (pageContext != null) {
            resourceModule = (String) pageContext.getAttribute("com.zetaframework.module");
        }
    }

}
