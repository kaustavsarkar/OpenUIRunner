package actions;


import actions.our.StoryContext;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import selenium.webdriver.WebDriverProvider;

import java.text.MessageFormat;
import java.util.UUID;

import static org.jbehave.core.annotations.AfterScenario.Outcome.ANY;
import static org.jbehave.core.annotations.AfterScenario.Outcome.FAILURE;

public class WebDriverScreenShotWorker extends BaseAction {

    protected static final String DEFAULT_SCREENSHOT_PATH_PATTERN =
            "{0}/screenshots/{2}/scenario-{1}.png";
    protected static final String DEFAULT_SOURCECODE_PATH_PATTERN =
            "{0}/sourcecode/{2}/scenario-{1}.html";
    private static final String CLASSNAME =
            WebDriverScreenShotWorker.class.getSimpleName();
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

        System.out.println(CLASSNAME + ": constrcutor called ");
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
        System.out.println(CLASSNAME + ": There is a scenario failure");
        if (uuidWrappedFailure instanceof PendingStepFound) {
            System.out.println(CLASSNAME + ": There are pending steps");
        }
        String sourceCodePath =
                sourceCodePath().replace(" ", "_").replace("|", "");
        System.out.println(CLASSNAME + ": Source Code shall be saved at : " +
                sourceCodePath);

        try {
            driverProvider.saveSourceCode(sourceCodePath);
        } catch (Exception e) {
            System.err.println(CLASSNAME +
                    ": There was a problem while saving source code");
            e.printStackTrace();
        }
    }

    @AfterScenario(uponOutcome = ANY)
    public void afterScenarioCompletion(
            UUIDExceptionWrapper uuidWrappedFailure) {
        System.out.println(CLASSNAME + ": Getting Screenshot");
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

        System.out.println(CLASSNAME + " Output ");
        System.out.println(CLASSNAME + ": Screenshots shall be saved at : " +
                screenshotPath);

        String currentUrl = "[unknown page title]";

        try {
            currentUrl = this.driver.getCurrentUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean savedIt = false;
        try {
            savedIt = driverProvider.saveScreenshotTo(screenshotPath);
        } catch (WebDriverException e) {
            System.err.println(CLASSNAME + "Screenshot of page '" + currentUrl +
                    "' has **NOT** been saved. ");
            e.printStackTrace();
            return;
        } catch (Exception e) {
            System.out.println(CLASSNAME + "Screenshot of page '" + currentUrl +
                    ". Will try again. Cause: " + e.getMessage());
            try {
                savedIt = driverProvider.saveScreenshotTo(screenshotPath);
            } catch (Exception e1) {
                System.err.println(
                        CLASSNAME + "Screenshot of page '" + currentUrl +
                                "' has **NOT** been saved to '" +
                                screenshotPath + "' because error '" +
                                e.getMessage() +
                                "' encountered. Stack trace follows:");
                e.printStackTrace();
                e1.printStackTrace();
                return;
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
