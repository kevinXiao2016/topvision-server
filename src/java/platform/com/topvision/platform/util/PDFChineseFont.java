/**
 *
 */
package com.topvision.platform.util;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

/**
 * @author niejun
 */
public class PDFChineseFont {

    public final static Font createChineseFont(Integer size, Integer style) {
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return new com.lowagie.text.Font(bfChinese, size, style);
        } catch (Exception ex) {

        }
        return null;
    }

}
