package org.our.example.actions;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.our.configuration.ScenarioProperties;
import org.our.example.pages.google.SearchGooglePage;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.Assert.*;

public class TestGoogleAction extends Steps {
    private static final Logger logger =
            LoggerFactory.getLogger(TestGoogleAction.class);
    private final WebDriver driver;
    private final Map<String, String> scenarioData;

    @Inject
    public TestGoogleAction(WebDriver webDriver,
                            ScenarioProperties scenarioProperties) {
        this.driver = webDriver;
        this.scenarioData = scenarioProperties.getData();
        logger.info("inside TestGoogleAction " + scenarioProperties);
    }

    @Given("Google is Open")
    public void checkGoogleIsOpen() {
        String title = driver.getTitle();
        assertEquals("Google", title);
    }

    @When("I type $searchString")
    public void typeSearchString(String searchString) {

        String search = scenarioData.get(searchString);

        SearchGooglePage page = new SearchGooglePage();
        WebElement box = driver.findElement(page.getSearchBox());

        logger.info(String.valueOf(box));
        box.sendKeys(search);
        box.sendKeys(Keys.TAB);
        box.sendKeys(Keys.TAB);
        box.sendKeys(Keys.ENTER);

    }

    @Then("I can see search options")
    public void checkSearchOptions() {
        SearchGooglePage page = new SearchGooglePage();

        assertFalse(driver.findElements(page.getSearchOptions()).isEmpty());
    }
}
