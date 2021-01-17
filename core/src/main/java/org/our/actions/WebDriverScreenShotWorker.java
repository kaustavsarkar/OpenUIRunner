package org.our.actions;


import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.our.selenium.webdriver.WebDriverProvider;

import java.text.MessageFormat;
import java.util.UUID;

import static org.jbehave.core.annotations.AfterScenario.Outcome.ANY;
import static org.jbehave.core.annotations.AfterScenario.Outcome.FAILURE;

public class WebDriverScreenShotWorker extends BaseAction {

    protected static final String DEFAULT_SCREENSHOT_PATH_PATTERN =
            "{0}/screenshots/{2}/scenario-{1}.png";
    protected static final String DEFAULT_SOURCECODE_PATH_PATTERN =
            "{0}/sourcecode/{2}/scenario-{1}.html";
    private static final Logger logger =
            LoggerFactory.getLogger(WebDriverScreenShotWorker.class);
    protected final StoryReporterBuilder reporterBuilder;
    protected final String screenshotPathPattern;
    protected WebDriverProvider driverProvider;

    public WebDriverScreenShotWorker(WebDriverProvider driverProvider) {
        this(driverProvider, new StoryReporterBuilder());
    }

    public WebDriverScreenShotWorker(WebDriverProvider driverProvider,
                                     StoryReporterBuilder storyReporterBuilder) {
        this(driverProvider, storyReporterBuilder,
                DEFAULT_SCREENSHOT_PATH_PATTERN);

        logger.debug("inside constructor");
    }

    public WebDriverScreenShotWorker(WebDriverProvider driverProvider,
                                     StoryReporterBuilder storyReporterBuilder,
                                     String screenshotPathPattern) {
        super(driverProvider.get());
        this.reporterBuilder = storyReporterBuilder;
        this.screenshotPathPattern = screenshotPathPattern;
        this.driverProvider = driverProvider;
    }

    @Then("I am logged out")
    public void doLogout() {
        afterScenarioCompletion(null);
        driver.get("https://gmail.com");
        driver.findElement(By.id("Confirm")).click();
    }

    @Then("I am logged out from test")
    public void doLogoutTest() {
        afterScenarioCompletion(null);
        driver.get("https://gmail.com");
        driver.findElement(By.id("Confirm")).click();
    }

    @AfterScenario(uponOutcome = FAILURE)
    public void afterScenarioFailure(UUIDExceptionWrapper uuidWrappedFailure) {
        logger.warn("There is a scenario failure.");
        if (uuidWrappedFailure instanceof PendingStepFound) {
            logger.warn("There are pending steps.");
        }
        String sourceCodePath =
                sourceCodePath().replace(" ", "_").replace("|", "");
        logger.info("Source code shall be saved at: " + sourceCodePath);

        try {
            driverProvider.saveSourceCode(sourceCodePath);
        } catch (Exception e) {
            logger.error("There was a problem while saving the source code.",
                    e);
        }
    }

    @AfterScenario(uponOutcome = ANY)
    public void afterScenarioCompletion(
            UUIDExceptionWrapper uuidWrappedFailure) {

        logger.info("Getting screenshot");
        if (uuidWrappedFailure instanceof PendingStepFound) {
            return; // we don't take screen-shots for Pending Steps
        }

        String screenshotPath = null;
        if (uuidWrappedFailure != null) {
            screenshotPath =
                    screenshotPathFailure(uuidWrappedFailure.getUUID());
        } else {
            screenshotPath =
                    screenshotPath().replace(" ", "_").replace("|", "");
        }
        logger.info("Screenshots shall be saved at: " + screenshotPath);

        String currentUrl = "[unknown page title]";

        try {
            currentUrl = this.driver.getCurrentUrl();
        } catch (Exception e) {
           logger.error("Error while getting the url", e);
        }
        boolean savedIt = false;
        try {
            savedIt = driverProvider.saveScreenshotTo(screenshotPath);
        } catch (WebDriverException e) {
            logger.error("Screenshot of page " + currentUrl + " has NOT been " +
                    "saved.", e);
        } catch (Exception e) {
            logger.error("Screenshot of page: " + currentUrl + " Will try " +
                    "again.", e);
            try {
                savedIt = driverProvider.saveScreenshotTo(screenshotPath);
            } catch (Exception e1) {
                logger.error("Screenshot of page '" + currentUrl +
                        "' has **NOT** been saved to '" +
                        screenshotPath, e);
            }
        }

    }

    protected String sourceCodePath() {
        return MessageFormat.format(DEFAULT_SOURCECODE_PATH_PATTERN,
                reporterBuilder.outputDirectory(), super.driver.getTitle(),
                StoryContext.getStory().getStoryName());
    }

    protected String screenshotPath() {
        return MessageFormat.format(screenshotPathPattern,
                reporterBuilder.outputDirectory(), super.driver.getTitle(),
                StoryContext.getStory().getStoryName());
    }

    protected String screenshotPathFailure(UUID uuid) {
        return MessageFormat.format(screenshotPathPattern,
                reporterBuilder.outputDirectory(), uuid,
                StoryContext.getStory().getStoryName());
    }
}
