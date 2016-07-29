import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetClass;

public class TestMapper {
	public TargetClass map(SourceClass source) {
		TargetClass target = new TargetClass();
		target.setName(source.getName());
		target.setAge(source.getAge());
		target.setAddress(source.getAddress());
		target.setTargetC(source.getSourceC());
		target.setTargetB(source.getSourceB());
		target.setTargetA(source.getSourceA());
		return target;
	}
}
