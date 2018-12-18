/**
 *
 */
package com.topvision.platform.util;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Font;

/**
 * @author niejun
 */
public class PDFCell extends Cell {

    public PDFCell(String content, Integer rowspan, Integer colspan) throws BadElementException {

        super(new Chunk(content, PDFChineseFont.createChineseFont(10, Font.NORMAL)));
        setRowspan(rowspan);
        setColspan(colspan);
        setHeader(false);
    }

}
