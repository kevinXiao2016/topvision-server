package com.topvision.ems.cmc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IbatisMybatis {

    public static void main(String[] args) throws IOException {

        File importFile = new File(
                "D:/EMS/workspace_V2/topvision-server/src/product/epon/com/topvision/ems/epon/dao/ibatis/maps/mysql/OltAlert.xml");
        @SuppressWarnings("resource")
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(importFile),
                "UTF-8"));
        File outFile = new File(
                "D:/EMS/workspace_V2/topvision-server/src/product/epon/com/topvision/ems/epon/dao/ibatis/maps/mysql/OltAlert.xml");
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                "UTF-8"));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            if (!AnnotationHandler.handler(line)) {

                line = MapperStartHandler.handle(line);
                line = TypeHandler.handler(line);
                line = PrefixHandler.handler(line);
                line = InplaceHandler.handler(line);
                line = EndHandler.handler(line);
                System.out.println(line);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            } else {
                System.out.println(line);
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

}

class MapperStartHandler {
    private static String origin = "<sqlMap namespace=";
    private static String replace = "<mapper namespace=";

    public static String handle(String line) {
        if (line.indexOf(origin) != -1) {
            line = line.replaceAll(origin, replace);
        }
        return line;
    }
}

class TypeHandler {
    private static String parameter = "parameterClass";
    private static String result = "resultClass";
    private static String parameter_replace = "parameterType";
    private static String reuslt_replace = "resultType";

    public static String handler(String line) {
        if (line.indexOf(parameter) != -1) {
            line = line.replaceAll(parameter, parameter_replace);
        }
        if (line.indexOf(result) != -1) {
            line = line.replaceAll(result, reuslt_replace);
        }
        return line;
    }
}

class AnnotationHandler {
    private static String flag = "<!--";

    public static boolean handler(String line) {
        if (line.indexOf(flag) != -1) {
            return true;
        }
        return false;
    }
}

class PrefixHandler {
    private static String flag = "#";
    private static String tmp = "@";
    private static String start_replace = "@{";
    private static String end_replace = "}";

    public static String handler(String line) {
        while (line.indexOf(flag) != -1) {
            line = line.replaceFirst(flag, start_replace);
            line = line.replaceFirst(flag, end_replace);
        }
        return line.replaceAll(tmp, flag);
    }
}

class InplaceHandler {
    private static String flag = "$";
    private static String escape_flag = "\\$";
    private static String tmp = "@";
    private static String start_replace = "@{";
    private static String end_replace = "}";

    public static String handler(String line) {
        while (line.indexOf(flag) != -1) {
            line = line.replaceFirst(escape_flag, start_replace);
            line = line.replaceFirst(escape_flag, end_replace);
        }
        return line.replaceAll(tmp, escape_flag);
    }

}

class EndHandler {
    private static String flag = "</sqlMap>";
    private static String replace = "</mapper>";

    public static String handler(String line) {
        while (line.indexOf(flag) != -1) {
            return replace;
        }
        return line;
    }

}

class TypeAliasHandler {
    @SuppressWarnings("unused")
    private static String flag = "";
}
