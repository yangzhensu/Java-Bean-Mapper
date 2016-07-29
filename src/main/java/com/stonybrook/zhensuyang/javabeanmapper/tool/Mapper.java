package com.stonybrook.zhensuyang.javabeanmapper.tool;

import java.io.*;
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
    private LinkedList<String> output;
    private LinkedList<String> importClass;
    private Set<Class> unimportClass;
    private final String packagePath = "output";

    public Mapper(Class<S> source, Class<T> target, String prefix) throws Exception {
        this.prefix = capitalizeFirstLetter(prefix);
        File file = new File("src/main/java/" + packagePath + "/" + this.prefix + "Mapper.java");
        if (!file.exists()) {
            file.getParentFile().createNewFile();
        }
        writer = new PrintWriter(file);
        filename = this.prefix + "Mapper.java";
        this.source = source;
        this.target = target;
        sourceFields = source.getDeclaredFields();
        targetFields = target.getDeclaredFields();
        fieldMap = buildFieldMap();
        output = new LinkedList<>();
        importClass = new LinkedList<>();
        unimportClass = buildUnimportClass();
    }

    public void mapClass() throws Exception {
        Set<String> targetFieldNameSet = new HashSet<>(targetFields.length*2);
        for (Field field : targetFields) {
            targetFieldNameSet.add(field.getName());
        }
        generateCodeHead();
        Map<String, String> listFieldMap = new HashMap<>();

        for (Field field : sourceFields) {
            String srcFieldName = field.getName();
            if (field.getType() != List.class) { // not list
                mapProperty(srcFieldName, fieldMap.get(srcFieldName), false);
            } else { // list
                listFieldMap.put(srcFieldName, fieldMap.get(srcFieldName));
            }
        }
        generateCodeTail();
        mapListField(listFieldMap);
        output.add("}");

        // if the name are not the same
        outputToFile();
    }

    private void addToImport(Class clazz) {
        String oneImport = String.format("import %s;", clazz.getCanonicalName());
        if (!unimportClass.contains(clazz) && !importClass.contains(oneImport)) {
            this.importClass.add(oneImport);
        }
    }

    private void generateCodeHead() {
        String sourceName = source.getSimpleName();
        String targetName = target.getSimpleName();
        addToImport(source);
        addToImport(target);
        output.add(String.format("\npublic class %sMapper {", prefix));
        output.add(String.format("\tpublic %s map(%s source) {", targetName, sourceName));
        output.add(String.format("\t\t%s target = new %s();", targetName, targetName));
    }

    private void mapProperty(String sourceProp, String targetProp, boolean isList) {
        sourceProp = capitalizeFirstLetter(sourceProp);
        targetProp = capitalizeFirstLetter(targetProp);
        String line = String.format("\t\ttarget.set%s(source.get%s());", targetProp, sourceProp);
        if (!isList) {
            output.add(line);
        } else { // TODO Optimize the code here: put the list in another linked list and added them together
            int index = output.indexOf("\t\treturn target;");
            output.add(index, line);
        }
    }

    private void generateCodeTail() {
        output.add("\t\treturn target;");
        output.add("\t}");
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

    private Set<Class> buildUnimportClass() {
        Set<Class> set = new HashSet<>();
        set.add(String.class);
        set.add(Integer.class);
        set.add(Long.class);
        set.add(Character.class);
        set.add(Double.class);
        return set;
    }

    /**
     *
     * @param srcFieldName
     * @param tgtFieldName
     */
    private void mapPropertyList(String srcFieldName, String tgtFieldName) throws NoSuchFieldException  {//,ParameterizedType genericType) {
        ParameterizedType srcGeneType = (ParameterizedType) source.getDeclaredField(srcFieldName).getGenericType();
        ParameterizedType tgtGeneType = (ParameterizedType) target.getDeclaredField(tgtFieldName).getGenericType();
        addToImport((Class) srcGeneType.getActualTypeArguments()[0]);
        addToImport((Class) tgtGeneType.getActualTypeArguments()[0]);
        addToImport(List.class);
        addToImport(ArrayList.class);
        if (srcGeneType.equals(tgtGeneType)) {
            mapProperty(srcFieldName, tgtFieldName, true);
        } else {
            mapPropertyListWithDiffGenericType(srcFieldName, srcGeneType, tgtFieldName, tgtGeneType);
        }
    }

    private void mapPropertyListWithDiffGenericType(String srcFieldName, ParameterizedType srcGeneType, String tgtFieldName, ParameterizedType tgtGeneType) {
        Class srcGeneClass = (Class) srcGeneType.getActualTypeArguments()[0];
        Class tgtGeneClass = (Class) tgtGeneType.getActualTypeArguments()[0];
        String srcGeneClassName = srcGeneClass.getSimpleName();
        String tgtGeneClassName = tgtGeneClass.getSimpleName();
        output.add(String.format("\n\tprivate List<%s> map%sTo%s(List<%s> list) {", tgtGeneClassName, capitalizeFirstLetter(srcFieldName), capitalizeFirstLetter(tgtFieldName), srcGeneClassName));
        output.add(String.format("\t\tList<%s> res = new ArrayList<>();", tgtGeneClassName));
        output.add(String.format("\t\tfor (%s src : list) {", srcGeneClassName));
        output.add(String.format("\t\t\t%s tgt = new %s();", tgtGeneClassName, tgtGeneClassName));
        mapGenericType(srcGeneClass, tgtGeneClass);
        output.add(String.format("\t\t\tres.add(tgt);"));
        output.add(String.format("\t\t}"));
        output.add(String.format("\t\treturn res;"));
        output.add(String.format("\t}"));
    }

    private void mapGenericType(Class srcGeneClass, Class tgtGeneClass) {
        Field[] srcFields = srcGeneClass.getDeclaredFields();
        for (Field field : srcFields) {
            String name = capitalizeFirstLetter(field.getName());
            output.add(String.format("\t\t\ttgt.set%s(src.get%s());", name, name));
        }
    }

    private void outputToFile() {
        Collections.sort(importClass); // TODO Use merge sort to accelerate sorting
        importClass.addFirst(String.format("package %s;\n", packagePath));
        importClass.addAll(output);
        for (String line : importClass) {
            writer.println(line);
        }
        writer.close();
    }

}
