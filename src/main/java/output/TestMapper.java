package output;

import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceType;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetType;
import java.util.ArrayList;
import java.util.List;

public class TestMapper {
	public TargetClass map(SourceClass source) {
		TargetClass target = new TargetClass();
		target.setName(source.getName());
		target.setAge(source.getAge());
		target.setAddress(source.getAddress());
		target.setTargetA(source.getSourceA());
		target.setTargetB(source.getSourceB());
		target.setTargetC(source.getSourceC());
		target.setListA(source.getListA());
		return target;
	}

	private List<TargetType> mapListCToListC(List<SourceType> list) {
		List<TargetType> res = new ArrayList<>();
		for (SourceType src : list) {
			TargetType tgt = new TargetType();
			tgt.setTypeA(src.getTypeA());
			tgt.setTypeB(src.getTypeB());
			res.add(tgt);
		}
		return res;
	}

	private List<TargetType> mapListBToListB(List<SourceType> list) {
		List<TargetType> res = new ArrayList<>();
		for (SourceType src : list) {
			TargetType tgt = new TargetType();
			tgt.setTypeA(src.getTypeA());
			tgt.setTypeB(src.getTypeB());
			res.add(tgt);
		}
		return res;
	}
}
