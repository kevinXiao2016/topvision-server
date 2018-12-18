/***********************************************************************
 * $ DomainInit.java,v1.0 2012-4-22 10:06:54 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created @2012-4-22-10:06:54
 */
public class DomainInit<T> {
    private static final long serialVersionUID = 7342180206202447869L;

    public static void main(String[] args) {
        // AclListTable aclListTable = new AclListTable();
        DomainBase aclListTable = new DomainBase();
        aclListTable = DomainInit.init(aclListTable);
    }

    public static <T> List<T> initList(T t) {
        ArrayList<T> list = new ArrayList<T>();
        list.add(init(t));
        return list;
    }

    public static <T> T init(T t) {
        Method[] methods = t.getClass().getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set")) {
                Type[] types = method.getGenericParameterTypes();
                if (types.length == 1) {
                    Type type = types[0];
                    System.out.println("type = " + type);
                    if (type.toString().equalsIgnoreCase("class java.lang.String")) {
                        try {
                            method.invoke(t, "1");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("boolean")
                            || type.toString().equalsIgnoreCase("class java.lang.Boolean")) {
                        try {
                            method.invoke(t, true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("class java.lang.Byte")) {
                        try {
                            method.invoke(t, new Byte((byte) 1));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("class java.lang.Integer")) {
                        try {
                            method.invoke(t, new Integer(1));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("class java.lang.Long")) {
                        try {
                            method.invoke(t, new Long(1));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("class java.lang.Double")) {
                        try {
                            method.invoke(t, new Double(1));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("class java.lang.Float")) {
                        try {
                            method.invoke(t, new Float(1));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("char")) {
                        try {
                            method.invoke(t, (char) '1');
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("class java.sql.Timestamp")) {
                        try {
                            method.invoke(t, new Timestamp(System.currentTimeMillis()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    } else if (type.toString().equalsIgnoreCase("byte") || type.toString().equalsIgnoreCase("int")
                            || type.toString().equalsIgnoreCase("long") || type.toString().equalsIgnoreCase("float")
                            || type.toString().equalsIgnoreCase("double")) {
                        try {
                            method.invoke(t, (byte) 1);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace(System.out);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace(System.out);
                        }
                    }
                }
            }
        }
        System.out.println("t = " + t);
        return t;
    }
}
