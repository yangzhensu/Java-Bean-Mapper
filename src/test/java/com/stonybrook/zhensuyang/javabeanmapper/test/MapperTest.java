package com.stonybrook.zhensuyang.javabeanmapper.test;

import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetClass;
import com.stonybrook.zhensuyang.javabeanmapper.tool.Mapper;

import java.io.IOException;

/**
 * Created by zhensuyang on 7/28/16.
 */
public class MapperTest {
    public static void main(String[] args) throws Exception {
        Mapper mapper = new Mapper(SourceClass.class, TargetClass.class, "Test");
        mapper.mapClass();
    }
}
