package com.stonybrook.zhensuyang.javabeanmapper;

import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetClass;

/**
 * Created by zhensuyang on 7/28/16.
 */
public class SampleMapper {

    public TargetClass map(SourceClass source) {
        TargetClass target = new TargetClass();
        target.setName(source.getName());
        target.setAge(source.getAge());
        target.setAddress(source.getAddress());
        return null;
    }
}
