package org.our.runner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.openqa.selenium.WebDriver;
import org.our.configuration.OurConfiguration;

/**
 * Handles execution of stories and web driver code.
 */
public class OurConfiguredRunner {

    /**
     * Executes Open UI Runner.
     * <p>
     * This is the entry point for executing the stories and web driver code.
     *
     * @param ourConfiguration {@link OurConfiguration} object sent by the
     *                         client.
     * @throws Throwable
     */
    public void executeWithConfig(OurConfiguration ourConfiguration)
            throws Throwable {
        try {
            if (ourConfiguration == null) {
                throw new Exception(
                        "You need to provide your own Configuration.");
            }

            Injector injector =
                    Guice.createInjector(new OurBaseModule(ourConfiguration));
            Injector childInjector =
                    injector.createChildInjector(new OurRunnerModule(injector));
            BaseStory story = childInjector.getInstance(BaseStory.class);
            story.run();
        } finally {
            if (ourConfiguration != null) {
                WebDriver webDriver = ourConfiguration.getWebDriver();
                webDriver.close();
                webDriver.quit();
            }
        }

    }
}
