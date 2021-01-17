package actions.our;

import configuration.OurConfiguration;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OurAction extends Steps {
    private static final Logger logger =
            LoggerFactory.getLogger(OurAction.class);
    private StoryProperties story;
    private WebDriver driver;

    OurAction() {
    }

    private OurAction(OurConfiguration ourConfiguration) {
        logger.debug("Inside Constructor");
        this.story = StoryProperties.buildFromOurProps(StoryContext.getStory(),
                ourConfiguration.getProperties());

        this.driver = ourConfiguration.getDriver();

    }

    @BeforeStory
    public void fireDriver() {
        logger.debug("Inside fireDriver");
        logger.info("Setting Page load timeout: " + 60);
        logger.info("Setting page implicit wait time: " + 20);

        // TODO(kaustav): Get timeouts as input from the user.
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        logger.info("Check url: " + story.getEnvURL());
    }


    @BeforeScenario
    public void beforeScenario() {
        logger.debug("Inside before scenario");
        driver.get(story.getEnvURL());
        //Deleting cookies so that in same run different users can login

        Set<Cookie> cookies = driver.manage().getCookies();
        logger.debug("Cookies: " + cookies);
    }
}
