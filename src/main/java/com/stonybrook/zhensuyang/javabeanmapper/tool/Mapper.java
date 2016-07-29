package com.stonybrook.zhensuyang.javabeanmapper.tool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by zhensuyang on 7/28/16.
 */
public class Mapper <S, T> {

    private PrintWriter writer;
    private String filename;
    private Logger logger; // learn how to configure the logger
    private String prefix;
    private Class<S> source;
    private Class<T> target;

    public Mapper(Class<S> source, Class<T> target, String prefix) throws IOException {
        this.prefix = firstLetterToUpperCase(prefix);
        filename = this.prefix + "Mapper.java";
        writer = new PrintWriter(filename, "UTF-8");
        logger = Logger.getLogger("Map Logger");
        this.source = source;
        this.target = target;
    }

    public void mapClass() throws IOException {
        Field[] sourceFields = source.getDeclaredFields();
        Field[] targetFields = target.getDeclaredFields();
        Set<String> targetFieldNameSet = new HashSet<String>(targetFields.length*2);
        for (Field field : targetFields) {
            targetFieldNameSet.add(field.getName());
        }
        Set<String> unmappedFields = Sets.newHashSet();
        generateCodeHead();
        for (Field field : sourceFields) {
            if (field.getType() == List.class) {
                mapPropertyList(field.getName(), (ParameterizedType) field.getGenericType());
            } else if (targetFieldNameSet.contains(field.getName())) {
                mapProperty(field.getName());
            } else {
                unmappedFields.add(field.getName());
            }
        }
        mapOtherProperties(unmappedFields);
        generateCodeTail();
        warning(unmappedFields);

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

    private void mapProperty(String fieldName) {
        fieldName = firstLetterToUpperCase(fieldName);
        writer.println(String.format("\t\ttarget.set%s(source.get%s());", fieldName, fieldName));
    }

    private void mapProperty(String sourceProp, String targetProp) {
        sourceProp = firstLetterToUpperCase(sourceProp);
        targetProp = firstLetterToUpperCase(targetProp);
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
        writer.println("}");
    }

    private String firstLetterToUpperCase(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     *
     * @param set
     * @throws IOException
     */
    private void mapOtherProperties(Set<String> set) throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("map.properties");
        properties.load(inputStream);
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement().toString();
            set.remove(key);
            String val = properties.getProperty(key);
            mapProperty(key, val);
        }
    }

    /**
     * If the generic type of the lists are the same, just use setter(getter());
     * If not,
     * @param name
     * @param genericType
     */
    private void mapPropertyList(String name, ParameterizedType genericType) {

    }

    private void mapPropertyListWithSameGenericType() {

    }

    private void mapPropertyListWithDiffGenericType(String name, ParameterizedType genericType) {
        name = firstLetterToUpperCase(name);
        Class genericClass = (Class) genericType.getActualTypeArguments()[0];
        String genericName = genericClass.getSimpleName();
        writer.println(String.format("\nprivate void map%name(List<%s> list) {", name, genericName));
        writer.println(String.format("\n\nfor (%s temp : list) {"));
        writer.println(String.format("\n\n\n"));
        writer.println(String.format("\n\n\n"));
        writer.println(String.format("\n\n}"));
        writer.println(String.format("\n}"));
    }

}
