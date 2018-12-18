/***********************************************************************
 * $Id: ButtonGroup.java,v1.0 2013-8-1 下午5:13:37 $
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

/**
 * @author Bravin
 * @created @2013-8-1-下午5:13:37
 *
 */
public class ButtonGroup extends ZetaElement {
    private static final Logger logger = LoggerFactory.getLogger(ButtonGroup.class);
    private static final long serialVersionUID = 4077786672855888621L;

    public ButtonGroup() {
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
        try {
            writer.print("<div class=\"noWidthCenterOuter clearBoth\"><ol class=\"noWidthCenterOl pB0 pT10 noWidthCenter\">");
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    @Override
    public int doAfterBody() throws JspTagException {
    	 Reader reader = bodyContent.getReader();
         BufferedReader bufferedReader = new BufferedReader(reader);
         try {
             String data;
             while ((data = bufferedReader.readLine()) != null) {
                 if (data.length() > 0) {
                	 writer.write(data);
                     //writer.newLine();//换行，由于下面使用的递归的方式，所以在这里换行比较方便
                 }
             }
         } catch (IOException e) {
             logger.error("", e);
         }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
    	 try {
             writer.print("</ol></div>");
         } catch (IOException e) {
             logger.error("", e);
         }
        return EVAL_PAGE;
    }
}
