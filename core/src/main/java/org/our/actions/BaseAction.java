package org.our.actions;

import com.google.inject.Inject;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Responsible for getting the browser to initial set up before running tests
 * being added by the client. The actions are being performed inside {@code
 * BaseAction#beforeStory} handle.
 */
public class BaseAction extends Steps {
    private static final Logger logger =
            LoggerFactory.getLogger(BaseAction.class);

    protected WebDriver driver;

    @Inject
    public BaseAction(WebDriver driver) {
        logger.debug("inside constructor");
        logger.info("Thread name: " + Thread.currentThread().getName());

        this.driver = driver;
    }

    /**
     * Maximises the driver woindow and adds timeouts to be used by the
     * {@link WebDriver} while running tests.
     */
    @BeforeStory
    public void beforeStory() {
        logger.debug("inside beforeStory. Maximising window");
        this.driver.manage().window().maximize();
        logger.info("Setting Page load timeout: " + 60);
        logger.info("Setting page implicit wait time: " + 20);

        // TODO(kaustav): Get timeouts as input from the user.
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @BeforeScenario
    public void beforeScenario() {
        logger.debug("inside beforeScenario");
    }


}
