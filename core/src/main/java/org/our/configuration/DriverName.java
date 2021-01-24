package org.our.configuration;

/**
 * @author: Kaustav Sarkar
 * @created: 5/17/2019
 */
public enum DriverName {
    CHROME_DRIVER("chromedriver", "chrome", "webdriver.chrome.driver"),
    GECKO_DRIVER("geckodriver.exe", "firefox", "webdriver.gecko.driver");

    private final String driverName;
    private final String browser;
    private final String systemProp;

    DriverName(String name, String browser, String systemProp) {
        this.driverName = name;
        this.browser = browser;
        this.systemProp = systemProp;
    }

    public String getDriverName() {
        return this.driverName;
    }

    public String getBrowser() {
        return this.browser;
    }

    public String getPropertyKey() {
        return this.systemProp;
    }
}
