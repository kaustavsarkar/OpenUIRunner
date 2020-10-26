package runner;

import actions.our.OurProperties;
import configuration.OurConfiguration;
import org.openqa.selenium.WebDriver;

/**
 * @author kaustav
 */
public class OurContext {
    private static final String CLASSNAME = OurContext.class.getSimpleName();

    /**
     * A {@link ThreadLocal} instance of {@link OurConfiguration} which can be
     * used to get the required configurations throughout the application.
     */
    private static ThreadLocal<OurConfiguration> currentConfig;

    private OurContext() {
    }

    /**
     * Creates a new {@link ThreadLocal} instance for {@link OurConfiguration}.
     */
    public static void initialize() {
        if (currentConfig == null) {
            currentConfig = new ThreadLocal<>();
        }
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
