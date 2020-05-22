package selenium.webdriver;

import configuration.DriverName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SimpleWebDriverProvider extends DelegateWebDriverProvider {
    private static final String CLASSNAME = SimpleWebDriverProvider.class.getSimpleName();

    public SimpleWebDriverProvider(String browser) {
        System.out.println(CLASSNAME + ": Inside Constructor");
        System.out.println(CLASSNAME + ": Driver: " + driver);
        System.out.println(CLASSNAME + ": Thread: " + Thread.currentThread().getName());
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
            System.out.println(CLASSNAME+": driver path: "+driverPath);
            System.setProperty("webdriver.chrome.driver",
                    driverPath);
            driver = new ChromeDriver();
        } else {
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
        System.out.println(CLASSNAME + ": end()");
        driver.close();
        driver.quit();
    }

}
