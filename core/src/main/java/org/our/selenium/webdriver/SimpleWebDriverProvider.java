package org.our.selenium.webdriver;

import org.our.configuration.DriverName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWebDriverProvider extends DelegateWebDriverProvider {
    private static final Logger logger =
            LoggerFactory.getLogger(SimpleWebDriverProvider.class);

    public SimpleWebDriverProvider(String browser) {
        logger.debug("Inside constructor");
        logger.info("Driver: " + driver);
        logger.debug("Thread: " + Thread.currentThread().getName());

        if (driver != null) {
            return;
        }

        if (DriverName.GECKO_DRIVER.getBrowser()
                .equalsIgnoreCase(browser)) {
            System.setProperty("webdriver.gecko.driver",
                    Thread.currentThread().getContextClassLoader()
                            .getResource("geckodriver.exe").getPath());
            driver = new FirefoxDriver();
        } else if (DriverName.CHROME_DRIVER.getBrowser()
                .equalsIgnoreCase(browser)) {
            String driverPath = Thread.currentThread().getContextClassLoader()
                    .getResource("chromedriver.exe").getPath();
            logger.info("Driver path: " + driverPath);
            System.setProperty("webdriver.chrome.driver",
                    driverPath);
            driver = new ChromeDriver();
        } else {
            logger.warn(browser + " is not supported");
            throw new WebDriverNotSupported(browser);
        }
    }

    public SimpleWebDriverProvider() {
        // Passing default browser
        this(DriverName.GECKO_DRIVER.getBrowser());
    }

    @Override
    public WebDriver get() {
        if (driver == null) {
            throw new WebDriverNotSupported("Driver not Initialized");
        }
        return driver;
    }

    @Override
    public void end() {
        logger.debug("called end()");
        driver.close();
        driver.quit();
    }

}
