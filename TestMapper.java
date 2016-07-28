import com.stonybrook.zhensuyang.javabeanmapper.domain.SourceClass;
import com.stonybrook.zhensuyang.javabeanmapper.domain.TargetClass;

public class TestMapper {
	public TargetClass map(SourceClass source) {
		TargetClass target = new TargetClass();
		target.setName(source.getName());
		target.setAge(source.getAge());
		target.setAddress(source.getAddress());
		return target;
	}
}
// Field sourceA is not mapped.
// Field sourceB is not mapped.
// Field sourceC is not mapped.
