package org.our.runner;

import org.our.actions.OurProperties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OURTestSuite {
    private static final Logger logger =
            LoggerFactory.getLogger(OURTestSuite.class);
    private static final String CONFIG_FILE = System.getProperty("config");
    private static final String LOCAL_CONFIG_FILE =
            System.getProperty("local.config");
    public static OurProperties ourProperties;
    private static WebDriver driver;

    public OURTestSuite(OurProperties props) {
        super();
        ourProperties = props;
        if (ourProperties.getBrowser().equalsIgnoreCase("chrome")) {
            if (System.getProperty("webdriver.chrome.driver") == null) {
                System.setProperty("webdriver.chrome.driver",
                        Thread.currentThread().getContextClassLoader()
                                .getResource("chromedriver.exe").getPath());
            }
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(false);
            driver = new ChromeDriver(options);
        }
    }

    /**
     * 1. Check Environment Details : Name, URL, Name shall decide which Data to
     * pick 2. Check if story file is provided 3. Check if Scneario is provided
     * 4. Check if Browser is provided
     */
    @Parameters
    public static Collection<Object[]> config() {
        // Read Configuration File
        ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
        OurProperties props = null;
        try (InputStream is = classLoader.getResourceAsStream(CONFIG_FILE);
             InputStream localIs = classLoader
                     .getResourceAsStream(LOCAL_CONFIG_FILE)) {
            props = new OurProperties(is);
            props.override(localIs);
        } catch (IOException e) {
            logger.error("Failed while reading the config file", e);
        }
        Object[] object = {props};
        List<Object[]> list = new ArrayList<>();
        list.add(object);

        return list;
    }

    @AfterClass
    public static void clearUp() {
        OurContext.destroy();
    }

    @Before
    public void fireUp() {
        OurContext.initialize();
        Object[] object = {driver, ourProperties};

    }

    @Test
    public void runStories() throws Throwable {
        BaseStory story = new BaseStory();
        story.run();
    }

    @After
    public void tearDown() {
        OurContext.destroy();
        driver.close();
        driver.quit();
    }
}
