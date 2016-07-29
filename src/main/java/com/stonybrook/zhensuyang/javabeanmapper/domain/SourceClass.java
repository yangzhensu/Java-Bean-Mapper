package com.stonybrook.zhensuyang.javabeanmapper.domain;

import java.util.List;

/**
 * Created by zhensuyang on 7/28/16.
 */
public class SourceClass {

    // fields with same name
    private String name;
    private int age;
    private String address;

    // fields with different name
    private String sourceA;
    private String sourceB;
    private String sourceC;

    // fields with list type
    private List<String> listA;
    private List<SourceType> sourceListB;
    private List<SourceType> listC;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSourceA() {
        return sourceA;
    }

    public void setSourceA(String sourceA) {
        this.sourceA = sourceA;
    }

    public String getSourceB() {
        return sourceB;
    }

    public void setSourceB(String sourceB) {
        this.sourceB = sourceB;
    }

    public String getSourceC() {
        return sourceC;
    }

    public void setSourceC(String sourceC) {
        this.sourceC = sourceC;
    }

    public List<String> getListA() {
        return listA;
    }

    public void setListA(List<String> listA) {
        this.listA = listA;
    }

    public List<SourceType> getSourceListB() {
        return sourceListB;
    }

    public void setSourceListB(List<SourceType> sourceListB) {
        this.sourceListB = sourceListB;
    }

    public List<SourceType> getListC() {
        return listC;
    }

    public void setListC(List<SourceType> listC) {
        this.listC = listC;
    }
}
