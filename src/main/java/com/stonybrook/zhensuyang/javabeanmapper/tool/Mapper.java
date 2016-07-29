package com.stonybrook.zhensuyang.javabeanmapper.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by zhensuyang on 7/28/16.
 */
public class Mapper <S, T> {

    private PrintWriter writer;
    private String filename;
//    private Logger logger; // learn how to configure the logger
    private String prefix;
    private Class<S> source;
    private Class<T> target;
    private Map<String, String> fieldMap;
    private Field[] sourceFields;
    private Field[] targetFields;


    public Mapper(Class<S> source, Class<T> target, String prefix) throws IOException {
        this.prefix = capitalizeFirstLetter(prefix);
        filename = this.prefix + "Mapper.java";
        writer = new PrintWriter(filename, "UTF-8");
        this.source = source;
        this.target = target;
        sourceFields = source.getDeclaredFields();
        targetFields = target.getDeclaredFields();
        fieldMap = buildFieldMap();
    }

    public void mapClass() throws Exception {
        Set<String> targetFieldNameSet = new HashSet<String>(targetFields.length*2);
        for (Field field : targetFields) {
            targetFieldNameSet.add(field.getName());
        }
        generateCodeHead();
        Map<String, String> listFieldMap = new HashMap<>();

        for (Field field : sourceFields) {
            String srcFieldName = field.getName();
            if (field.getType() != List.class) { // not list
                mapProperty(srcFieldName, fieldMap.get(srcFieldName));
            } else { // list
                listFieldMap.put(srcFieldName, fieldMap.get(srcFieldName));
            }
        }
        generateCodeTail();
        mapListField(listFieldMap);
        writer.println("}");

        // if the name are not the same
        writer.close();
    }

    private void generateCodeHead() {
        String sourceName = source.getSimpleName();
        String targetName = target.getSimpleName();
        String fullSourceName = source.getCanonicalName();
        String fullTargetName = target.getCanonicalName();

        writer.println(String.format("import %s;", fullSourceName));
        writer.println(String.format("import %s;", fullTargetName));
        writer.println(String.format("\npublic class %sMapper {", prefix));
        writer.println(String.format("\tpublic %s map(%s source) {", targetName, sourceName));
        writer.println(String.format("\t\t%s target = new %s();", targetName, targetName));

    }

    private void mapProperty(String sourceProp, String targetProp) {
        sourceProp = capitalizeFirstLetter(sourceProp);
        targetProp = capitalizeFirstLetter(targetProp);
        writer.println(String.format("\t\ttarget.set%s(source.get%s());", targetProp, sourceProp));
    }

    private void warning(Set<String> set) {
        for (String str : set) {
            writer.println(String.format("// Field %s is not mapped.", str));
        }
    }

    private void generateCodeTail() {
        writer.println("\t\treturn target;");
        writer.println("\t}");
    }

    private void mapListField(Map<String, String> listFieldMap) throws NoSuchFieldException {
        for (Map.Entry<String, String> entry : listFieldMap.entrySet()) {
            mapPropertyList(entry.getKey(), entry.getValue());
        }
    }

    private String capitalizeFirstLetter(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private Map<String, String> buildFieldMap() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("map.properties");
        properties.load(inputStream);
        Map<String, String> map = new HashMap<>((Map)properties);
        for (Field field : sourceFields) {
            String fieldName = field.getName();
            if (!map.containsKey(fieldName)) {
                map.put(fieldName, fieldName);
            }
        }
        return map;
    }

    /**
     *
     * @param srcFieldName
     * @param tgtFieldName
     */
    private void mapPropertyList(String srcFieldName, String tgtFieldName) throws NoSuchFieldException  {//,ParameterizedType genericType) {
        ParameterizedType srcGeneType = (ParameterizedType) source.getDeclaredField(srcFieldName).getGenericType();
        ParameterizedType tgtGeneType = (ParameterizedType) target.getDeclaredField(tgtFieldName).getGenericType();
        if (srcGeneType.equals(tgtGeneType)) {
            mapProperty(srcFieldName, tgtFieldName);
        } else {
            mapPropertyListWithDiffGenericType(srcFieldName, srcGeneType, tgtFieldName, tgtGeneType);
        }
    }

    private void mapPropertyListWithDiffGenericType(String srcFieldName, ParameterizedType srcGeneType, String tgtFieldName, ParameterizedType tgtGeneType) {
        Class srcGeneClass = (Class) srcGeneType.getActualTypeArguments()[0];
        Class tgtGeneClass = (Class) tgtGeneType.getActualTypeArguments()[0];
        String srcGeneClassName = srcGeneClass.getSimpleName();
        String tgtGeneClassName = tgtGeneClass.getSimpleName();
        writer.println(String.format("\n\tprivate List<%s> map%sTo%s(List<%s> list) {", tgtGeneClassName, capitalizeFirstLetter(srcFieldName), capitalizeFirstLetter(tgtFieldName), srcGeneClassName));
        writer.println(String.format("\t\tList<%s> res = new ArrayList<>();", tgtGeneClassName));
        writer.println(String.format("\t\tfor (%s src : list) {", srcGeneClassName));
        writer.println(String.format("\t\t\t%s tgt = new %s();", tgtGeneClassName, tgtGeneClassName));
        mapGenericType(srcGeneClass, tgtGeneClass);
        writer.println(String.format("\t\t\tres.add(tgt);"));
        writer.println(String.format("\t\t}"));
        writer.println(String.format("\t\treturn res;"));
        writer.println(String.format("\t}"));
    }

    private void mapGenericType(Class srcGeneClass, Class tgtGeneClass) {
        Field[] srcFields = srcGeneClass.getDeclaredFields();
        for (Field field : srcFields) {
            String name = capitalizeFirstLetter(field.getName());
            writer.println(String.format("\t\t\ttgt.set%s(src.get%s);", name, name));
        }
    }

}
