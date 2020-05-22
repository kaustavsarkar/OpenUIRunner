package actions.our;

import configuration.OurConfiguration;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OurAction extends Steps {
    private static final String CLASSNAME = OurAction.class.getSimpleName();
    private static final String USERNAME_KEY = "Username";
    private static final String PASSWORD_KEY = "Password";
    private StoryProperties story;
    private WebDriver driver;

    OurAction() {
    }

    private OurAction(OurConfiguration ourConfiguration) {
        System.out.println(CLASSNAME + ": Inside Constructor");
        this.story = StoryProperties.buildFromOurProps(StoryContext.getStory(), ourConfiguration.getProperties());
        //this.story = ourConfiguration.getProperties();
        //		OurConfiguration config = new Con
        //StoryContext.addStory(this.story);
        //OurContext.set(ourConfiguration);
        //OurContext.set(object);

        this.driver = ourConfiguration.getDriver();

    }

    @BeforeStory
    public void fireDriver() {
        System.out.println(CLASSNAME + ": Inside fireDriver");
        System.out.println(CLASSNAME + ": Setting Page Load Time Out(seconds): " + 180);
        System.out.println(CLASSNAME + ": Setting Page Implicit Wait Time(seconds): " + 180);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        System.out.println(CLASSNAME + ": Check URL: " + story.getEnvURL());


    }


    @BeforeScenario
    public void beforeScenario() {
        System.out.println(CLASSNAME + ": inside before scenario");
        driver.get(story.getEnvURL());
        //Deleting cookies so that in same run different users can login

        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println(CLASSNAME + ": Cookies " + cookies);

        String username = StoryContext.getData(USERNAME_KEY);
        String password = StoryContext.getData(PASSWORD_KEY);
        //Scenario based Username/Password Hold Precedence over Story/Execution based Credentials
        if (driver.getTitle().equalsIgnoreCase(" Application Sign on")) {
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                driver.findElement(By.id("username")).sendKeys(username);
                driver.findElement(By.id("password")).sendKeys(password);
            } else if (story.getUserName() != null && !story.getUserName().isEmpty()
                    && story.getPassword() != null && !story.getPassword().isEmpty()) {

                driver.findElement(By.id("username")).sendKeys(story.getUserName());
                driver.findElement(By.id("password")).sendKeys(story.getPassword());

            }
            driver.findElement(By.className("submit_btn")).click();
        }
    }
}
