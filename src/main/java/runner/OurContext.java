package runner;

import configuration.OurConfiguration;
import org.openqa.selenium.WebDriver;
import actions.our.OurProperties;

public class OurContext {
	private static final String CLASSNAME = OurContext.class.getSimpleName();
	private static ThreadLocal<OurConfiguration> currentConfig;

	private OurContext() {
	}

	public static void initialize() {
		if (currentConfig == null)
			currentConfig = new ThreadLocal<>();
	}

	static OurConfiguration get() {
		return currentConfig.get();
	}

	public static void set(OurConfiguration configuration) {
		currentConfig.set(configuration);
	}

	public static WebDriver getDriver() {
		return OurContext.get().getDriver();
	}

	public static OurProperties getProperties() {
		return OurContext.get().getProperties();
	}

	public static void destroy() {
		currentConfig.get().destroyDriver();
		currentConfig.remove();
	}





}
