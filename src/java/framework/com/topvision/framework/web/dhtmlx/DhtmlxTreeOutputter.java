/**
 * 
 */
package com.topvision.framework.web.dhtmlx;

import java.io.IOException;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;

/**
 * @author niejun
 * 
 */
public class DhtmlxTreeOutputter extends DhtmlxOutputter {
    public static void output(DhtmlxHandler handler, OutputStream out) throws IOException {
        Element root = new DefaultElement("tree");
        root.addAttribute("id", "0");
        Document document = new DefaultDocument(root);
        handler.handle(root);
        XMLWriter outputter = new XMLWriter();
        outputter.setOutputStream(out);
        outputter.write(document);
        outputter.flush();
        out.flush();
    }
}
