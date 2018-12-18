package com.topvision.platform.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ObjectUtils {

    /**
     * 将对象转化到数组.
     * 
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] result = null;
        if (obj == null) {
            return result;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder en = new XMLEncoder(baos);
        try {
            en.writeObject(obj);
            en.flush();
            baos.flush();
            en.close();
            baos.close();
            result = baos.toByteArray();
        } catch (Exception ex) {
            if (en != null) {
                en.close();
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex1) {
                }
            }
            result = obj.toString().getBytes();
        }
        return result;
    }

    public static Object toObject(byte[] array) {
        ByteArrayInputStream bais = null;
        XMLDecoder de = null;
        Object obj = null;
        try {
            bais = new ByteArrayInputStream(array);
            de = new XMLDecoder(bais);
            obj = de.readObject();
            bais.close();
            de.close();
        } catch (Exception ex) {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException ex1) {
                }
            }
            if (de != null) {
                de.close();
            }
            if (array != null) {
                return new String(array);
            }
        }
        return obj;
    }

}