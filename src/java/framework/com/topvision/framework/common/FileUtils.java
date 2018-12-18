package com.topvision.framework.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final int BUFFER_SIZE = 16 * 1024;

    public static void closeQuitely(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static void closeQuitely(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static void closeQuitely(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static void closeQuitely(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static void copy(File src, File dest) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dest), BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void copy(InputStream src, OutputStream dest) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(src, BUFFER_SIZE);
            out = new BufferedOutputStream(dest, BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static byte[] fileToByte(File file) {
        byte[] b = null;
        BufferedInputStream is = null;
        try {
            b = new byte[(int) file.length()];
            is = new BufferedInputStream(new FileInputStream(file));
            is.read(b);
        } catch (FileNotFoundException e) {
            logger.error(file.getName(), e);
        } catch (IOException e) {
            logger.error(file.getName(), e);
        } finally {
            closeQuitely(is);
        }
        return b;
    }

    /**
     * 输出给定的输出流.
     * 
     * @param is
     * @throws Exception
     */
    public static void output(String content, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
        } catch (IOException e) {
            logger.warn(file.getName(), e);
        } finally {
            closeQuitely(out);
        }
    }

}
