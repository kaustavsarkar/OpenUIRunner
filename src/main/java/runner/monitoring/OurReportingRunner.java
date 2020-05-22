package runner.monitoring;

import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;
import org.junit.runners.parameterized.TestWithParameters;

public class OurReportingRunner extends BlockJUnit4ClassRunnerWithParameters{

	public OurReportingRunner(TestWithParameters test) throws InitializationError {
		super(test);
		// TODO Auto-generated constructor stub
	}


}
