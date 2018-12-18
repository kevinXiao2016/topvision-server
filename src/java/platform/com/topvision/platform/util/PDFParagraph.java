/**
 *
 */
package com.topvision.platform.util;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

/**
 * @author niejun
 */
public class PDFParagraph extends Paragraph {

    private static final long serialVersionUID = -3025618963156084501L;

    public PDFParagraph(String content) {
        super(content, PDFChineseFont.createChineseFont(12, Font.BOLD));
    }

    public PDFParagraph(String content, Font font, Integer alignment) {
        super(content, font);
        setAlignment(alignment);
    }

    public PDFParagraph(String content, Integer alignment, Integer fontSize) {
        super(content, null);
        setAlignment(alignment);
    }

}
