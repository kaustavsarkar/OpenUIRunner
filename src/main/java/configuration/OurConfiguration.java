package configuration;

import actions.our.OurProperties;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import selenium.webdriver.CustomWebDriverProvider;
import selenium.webdriver.SimpleWebDriverProvider;
import selenium.webdriver.WebDriverProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class OurConfiguration {
    private static final String CLASSNAME = OurConfiguration.class.getSimpleName();
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
        WebDriverProvider provider = new SimpleWebDriverProvider(
                this.properties.getBrowser());
        this.driverProvider = provider;
    }

    private void resolveProperties(String profileConfig, String userConfig) {
        System.out.println(CLASSNAME
                + ": Inside resolveProperties(profileConfig,userConfig)");
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();

        try (InputStream profileIs = classLoader
                .getResourceAsStream(profileConfig);
             InputStream userIS = classLoader
                     .getResourceAsStream(userConfig)) {
            properties = new OurProperties(profileIs);
            properties.override(userIS);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void createCustomWebProviderWithOptions(DriverName driverName, MutableCapabilities options) {
        //TODO
    }

    public void createCustomWebProvider(DriverName driverName) {
        WebDriverProvider driverProvider = new CustomWebDriverProvider();
        driverProvider = ((CustomWebDriverProvider) driverProvider).createProvider(driverName);
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
