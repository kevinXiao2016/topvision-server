/***********************************************************************
 * $Id: SaveAsImage.java,v1.0 2013-9-14 下午4:09:36 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.net.util.Base64;
import org.apache.fop.svg.PDFTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.util.highcharts.MimeType;

/**
 * @author fanzidong
 * @created @2013-9-14-下午4:09:36
 * 
 */
public class ExportHighChartGraphServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(ExportHighChartGraphServlet.class);

    public ExportHighChartGraphServlet() {
        super();
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码，解决乱码问题
        String encoding = request.getCharacterEncoding();
        request.setCharacterEncoding("utf-8");
        // 获取该highchart图表想要生成图片的格式等信息
        String type = request.getParameter("type");
        String svg = request.getParameter("svg");
        String filename = request.getParameter("filename");
        filename = (filename == null) ? "chart" : filename;
        filename = new String(filename.getBytes("GBK"), "iso-8859-1");
        ServletOutputStream out = response.getOutputStream();

        // 可能无法通过request.getParameter获取
        if (svg == null) {
            try {
                // 通过commons-fileupload来代替servlet 3.0中新的用于文件上传的API(getPart)
                @SuppressWarnings("unchecked")
                List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        // Procesd
                        String fieldName = item.getFieldName();
                        // 经过fileupload处理过的request中的表单值需要转码, 否则中文会乱码, DefaultEncodingFilter对此无效
                        String fieldValue = new String(item.getString().getBytes("ISO-8859-1"), "UTF-8");

                        if ("svg".equals(fieldName)) {
                            svg = fieldValue;
                        } else if ("filename".equals(fieldName)) {
                            filename = getFilename(fieldValue);
                        } else if ("width".equals(fieldName)) {
                            // width = getWidth(fieldValue);
                        } else if ("type".equals(fieldName)) {
                            MimeType mime = getMime(fieldValue);
                            type = mime.getType();
                        }
                    }
                }
            } catch (FileUploadException e) {
                logger.debug("Oops something happened here redirect to error-page, " + e.getMessage());
                // sendError(request, response, e, svg);
            }
        }

        // 只支持SVG格式
        if (null != type && null != svg) {
            svg = svg.replaceAll(":rect", "rect");
            String ext = "";
            Transcoder transcoder = null;
            if (type.equals("image/png")) {
                ext = "png";
                transcoder = new PNGTranscoder();
            } else if (type.equals("image/jpeg")) {
                ext = "jpg";
                transcoder = new JPEGTranscoder();
            } else if (type.equals("application/pdf")) {
                ext = "pdf";
                transcoder = (Transcoder) new PDFTranscoder();
            } else if (type.equals("image/svg+xml"))
                ext = "svg";
            //
            response.setCharacterEncoding("utf-8");
            String agent = (String) request.getHeader("USER-AGENT");
            if (agent.indexOf("Firefox") != -1) {
                // firefox
                filename = "=?UTF-8?B?" + (new String(Base64.encodeBase64(filename.getBytes("UTF-8")))) + "?=";
            } else {
                filename = URLEncoder.encode(filename, "UTF-8");
            }
            response.addHeader("Content-Disposition", "attachment; filename=" + filename + "." + ext);
            response.addHeader("Content-Type", type + "; charset=UTF-8");

            if (null != transcoder) {
                TranscoderInput input = new TranscoderInput(new StringReader(svg));
                TranscoderOutput output = new TranscoderOutput(out);

                try {
                    transcoder.transcode(input, output);
                } catch (TranscoderException e) {
                    out.print("Problem transcoding stream. See the web logs for more details.");
                    e.printStackTrace();
                }
            } else if (ext.equals("svg")) {
                OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
                writer.append(svg);
                writer.close();
            } else
                out.print("Invalid type: " + type);
        } else {
            response.addHeader("Content-Type", "text/html");
            out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted."
                    + "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
        }
        out.flush();
        out.close();
    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *             if an error occurs
     */
    public void init() throws ServletException {
    }

    private String getFilename(String name) {
        return (name != null) ? name : "chart";
    }

    private static MimeType getMime(String mime) {
        MimeType type = MimeType.get(mime);
        if (type != null) {
            return type;
        }
        return MimeType.PNG;
    }

}
