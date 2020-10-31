package selenium.webdriver;

import configuration.DriverName;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CustomWebDriverProvider extends DelegateWebDriverProvider {
    private final MutableCapabilities options;

//	public CustomWebDriverProvider(String browser) {
//		if (DriverName.GECKO_DRIVER.getBrowser()
//				.equalsIgnoreCase(browser)) {
//			super.driver = new FirefoxDriver((FirefoxOptions) options);
//		} else if (DriverName.CHROME_DRIVER.getBrowser()
//				.equalsIgnoreCase(browser)) {
//			super.driver = new ChromeDriver((ChromeOptions) options);
//		} else {
//			throw new WebDriverNotSupported(browser);
//		}
//	}

    public CustomWebDriverProvider(MutableCapabilities options) {
        this.options = options;
    }

    public CustomWebDriverProvider() {
        this(new MutableCapabilities());
    }

    public CustomWebDriverProvider createProvider(DriverName driverName) {
        if (DriverName.GECKO_DRIVER.getBrowser()
                .equalsIgnoreCase(driverName.getBrowser())) {
            super.driver = new FirefoxDriver();
        } else if (DriverName.CHROME_DRIVER.getBrowser()
                .equalsIgnoreCase(driverName.getBrowser())) {
            super.driver = new ChromeDriver();
        } else {
            throw new WebDriverNotSupported(driverName.getBrowser());
        }


        return this;
    }

    @Override
    public WebDriver get() {
        if (super.driver == null) {
            throw new WebDriverNotSupported("Driver not Initialized");
        }
        return super.driver;
    }

    @Override
    public void end() {
        driver.close();
        driver.quit();
    }

}
