package org.our.actions;


import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.our.configuration.ScenarioProperties;
import org.our.selenium.webdriver.WebDriverProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.UUID;

import static org.jbehave.core.annotations.AfterScenario.Outcome.ANY;
import static org.jbehave.core.annotations.AfterScenario.Outcome.FAILURE;

/**
 * Custom {@link Steps} to take a screenshot and save source code of webpage
 * being tested.
 */
public class WebDriverScreenShotWorker extends Steps {

    protected static final String DEFAULT_SCREENSHOT_PATH_PATTERN =
            "{0}/screenshots/{2}/scenario-{1}.png";
    protected static final String DEFAULT_SOURCECODE_PATH_PATTERN =
            "{0}/sourcecode/{2}/scenario-{1}.html";
    private static final Logger logger =
            LoggerFactory.getLogger(WebDriverScreenShotWorker.class);
    protected final StoryReporterBuilder reporterBuilder;
    private final WebDriver driver;
    private final String storyName;
    protected WebDriverProvider driverProvider;

    /**
     * Injecting required objects.
     *
     * @param driverProvider       - object provided via {@link org.our.runner.OurRunnerModule}.
     * @param storyReporterBuilder - object provided via {@link org.our.runner.OurRunnerModule}.
     * @param scenarioProperties   - object being injected inside {@link
     *                             org.our.runner.OurStoryReporter} using child
     *                             injector inside {@link org.our.runner.OurStoryReporter#beforeScenario(Scenario)}
     */
    WebDriverScreenShotWorker(WebDriverProvider driverProvider,
                              StoryReporterBuilder storyReporterBuilder,
                              ScenarioProperties scenarioProperties) {
        logger.debug("inside constructor");
        this.driverProvider = driverProvider;
        this.driver = driverProvider.get();
        this.reporterBuilder = storyReporterBuilder;
        this.storyName = scenarioProperties.getStoryName();
    }

    /**
     * Runs after every scenario has been executed and has failed in execution.
     * Saves the source code of the webpage being tested. The assumption made
     * here is that the failure has occurred while finding or performing action
     * on an {@link org.openqa.selenium.WebElement}.
     * <p>
     * The source code provided is for the purpose of debugging the failure
     * reason by the developers writing tests.
     */
    @AfterScenario(uponOutcome = FAILURE)
    public void afterScenarioFailure(UUIDExceptionWrapper uuidWrappedFailure) {
        logger.warn("There is a scenario failure.");
        if (uuidWrappedFailure instanceof PendingStepFound) {
            logger.warn("There are pending steps.");
        }
        String sourceCodePath =
                sourceCodePath()
                        .replace(" ", "_")
                        .replace("|", "");
        logger.info("Source code shall be saved at: " + sourceCodePath);

        try {
            driverProvider.saveSourceCode(sourceCodePath);
        } catch (Exception e) {
            logger.error("There was a problem while saving the source code.",
                    e);
        }
    }

    /**
     * Runs after a scenario has been executed irrespective of success or
     * failure of the scenario.
     * <p>
     * It takes a screenshot of the webpage which is shown at the
     * <strong>end<strong/> of the scenario to verify the result or debug for
     * any failure.
     */
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
        boolean isScreenshotSaved = false;
        try {
            isScreenshotSaved = driverProvider.saveScreenshotTo(screenshotPath);
        } catch (WebDriverException e) {
            logger.error("Screenshot of page " + currentUrl + " has NOT been " +
                    "saved.", e);
        } catch (Exception e) {
            logger.error("Screenshot of page: " + currentUrl + " Will try " +
                    "again.", e);
            try {
                isScreenshotSaved =
                        driverProvider.saveScreenshotTo(screenshotPath);
            } catch (Exception e1) {
                logger.error("Screenshot of page '" + currentUrl +
                        "' has **NOT** been saved to '" +
                        screenshotPath, e);
            }
        }
        logger.info("Is the screenshot saved? " + isScreenshotSaved);
    }

    protected String sourceCodePath() {
        return MessageFormat.format(DEFAULT_SOURCECODE_PATH_PATTERN,
                reporterBuilder.outputDirectory(), driver.getTitle(),
                storyName);
    }

    protected String screenshotPath() {
        return MessageFormat.format(DEFAULT_SCREENSHOT_PATH_PATTERN,
                reporterBuilder.outputDirectory(), driver.getTitle(),
                storyName);
    }

    protected String screenshotPathFailure(UUID uuid) {
        return MessageFormat.format(DEFAULT_SCREENSHOT_PATH_PATTERN,
                reporterBuilder.outputDirectory(), uuid, storyName);
    }
}
