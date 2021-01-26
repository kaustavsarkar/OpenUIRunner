package org.our.actions;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseAction extends Steps {
    private static final Logger logger =
            LoggerFactory.getLogger(BaseAction.class);

    protected WebDriver driver;

    public BaseAction(WebDriver driver) {
        logger.debug("inside constructor");
        logger.info("Thread name: " + Thread.currentThread().getName());

        this.driver = driver;
    }

    @BeforeStory
    public void beforeStory() {
        logger.debug("inside beforeStory");
        this.driver.manage().window().maximize();
    }

    @BeforeScenario
    public void beforeScenario() {
        logger.debug("inside beforeScenario");
    }


}
