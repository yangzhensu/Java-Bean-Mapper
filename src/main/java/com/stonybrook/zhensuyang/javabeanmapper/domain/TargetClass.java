package com.stonybrook.zhensuyang.javabeanmapper.domain;

import java.util.List;

/**
 * Created by zhensuyang on 7/28/16.
 */
public class TargetClass {

    // fields with same name
    private String name;
    private int age;
    private String address;

    // fields with different name
    private String targetA;
    private String targetB;
    private String targetC;

    private List<String> listA;
    private List<TargetType> targetListB;
    private List<TargetType> listC;

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

    public String getTargetA() {
        return targetA;
    }

    public void setTargetA(String targetA) {
        this.targetA = targetA;
    }

    public String getTargetB() {
        return targetB;
    }

    public void setTargetB(String targetB) {
        this.targetB = targetB;
    }

    public String getTargetC() {
        return targetC;
    }

    public void setTargetC(String targetC) {
        this.targetC = targetC;
    }

    public List<String> getListA() {
        return listA;
    }

    public void setListA(List<String> listA) {
        this.listA = listA;
    }

    public List<TargetType> getTargetListB() {
        return targetListB;
    }

    public void setTargetListB(List<TargetType> targetListB) {
        this.targetListB = targetListB;
    }

    public List<TargetType> getListC() {
        return listC;
    }

    public void setListC(List<TargetType> listC) {
        this.listC = listC;
    }
}
