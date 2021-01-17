package org.our.configuration;

import org.our.actions.OurProperties;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.our.selenium.webdriver.CustomWebDriverProvider;
import org.our.selenium.webdriver.SimpleWebDriverProvider;
import org.our.selenium.webdriver.WebDriverProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class OurConfiguration {
    private static final Logger logger =
            LoggerFactory.getLogger(OurConfiguration.class);
    private OurProperties properties;
    private WebDriverProvider driverProvider;

    OurConfiguration() {
    }

    public OurConfiguration(Map<String, String> configMap) {
        String profileConfig = configMap
                .get(ConfigOptions.PROFILE_CONFIG.getConfigName());
        String userConfig = configMap
                .get(ConfigOptions.USER_CONFIG.getConfigName());

        resolveProperties(profileConfig, userConfig);

        resolveDriverProvider();
    }

    private void resolveDriverProvider() {
        this.driverProvider = new SimpleWebDriverProvider(
                this.properties.getBrowser());
    }

    private void resolveProperties(String profileConfig, String userConfig) {
        logger.debug("inside resolveProperties");
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();

        try (InputStream profileIs = classLoader
                .getResourceAsStream(profileConfig);
             InputStream userIS = classLoader
                     .getResourceAsStream(userConfig)) {
            properties = new OurProperties(profileIs);
            properties.override(userIS);
        } catch (IOException e) {
            logger.error("Error while reading the Properties file", e);
        }
    }

    public OurProperties getProperties() {
        if (this.properties == null) {
            this.properties = new OurProperties();
        }
        return this.properties;
    }

    public WebDriver getDriver() {
        return this.driverProvider.get();
    }

    public WebDriverProvider getDriverProvider() {
        return this.driverProvider;
    }

    public void createCustomWebProviderWithOptions(DriverName driverName,
                                                   MutableCapabilities options) {
        //TODO
    }

    public void createCustomWebProvider(DriverName driverName) {
        CustomWebDriverProvider driverProvider = new CustomWebDriverProvider();
        driverProvider = driverProvider.createProvider(driverName);
        this.driverProvider = driverProvider;
    }

    public void destroyDriver() {
        WebDriver driver = this.driverProvider.get();
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
