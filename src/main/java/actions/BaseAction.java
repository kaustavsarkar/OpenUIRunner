package actions;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

public class BaseAction extends Steps {

    private static final String CLASSNAME = BaseAction.class.getSimpleName();

    protected WebDriver driver;

    public BaseAction(WebDriver driver) {
        System.out.println(CLASSNAME + ": BaseAction constructor()");
        System.out.println(CLASSNAME + ": Thread Name: " + Thread.currentThread().getName());

        this.driver = driver;
//		OurConfiguration config = new Con

        // OurContext.initialize();
        // OurContext.set(ourConfiguration);
        // OurContext.set(object);

        //this.driver = OurContext.getDriver();

    }

    @BeforeStory
    public void beforeStory() {
        System.out.println(CLASSNAME + ": beforeStory");
        this.driver.manage().window().maximize();
    }

    @BeforeScenario
    public void beforeScenario() {
        System.out.println(CLASSNAME + ": entered BeforeScenario::beforeScenario()");
    }


}
