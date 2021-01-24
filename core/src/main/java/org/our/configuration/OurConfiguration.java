package org.our.configuration;

import com.google.auto.value.AutoValue;
import org.openqa.selenium.WebDriver;
import org.our.selenium.webdriver.CustomWebDriverProvider;
import org.our.selenium.webdriver.WebDriverProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@AutoValue
public abstract class OurConfiguration {
    private static final Logger logger =
            LoggerFactory.getLogger(OurConfiguration.class);

    public abstract WebDriverProvider getWebDriverProvider();

    public abstract WebDriver getWebDriver();

    public abstract Optional<String> getProfile();

    public abstract String getLaunchUrl();

    public abstract String getRelDataPath();

    public abstract Optional<String> getStoryRegex();

    public abstract Optional<List<String>> getIncludeTags();

    public abstract DriverName getDriverName();

    public abstract String getWebDriverPath();

    public abstract String getReportPath();

    public static Builder newBuilder() {
        return new AutoValue_OurConfiguration.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setWebDriverProvider(WebDriverProvider webDriverProvider);

        abstract Builder setWebDriver(WebDriver webDriver);

        public abstract Builder setDriverName(DriverName driverName);

        public abstract Builder setProfile(Optional<String> profile);

        public abstract Builder setLaunchUrl(String launchUrl);

        public abstract Builder setRelDataPath(String relDataPath);

        public abstract Builder setStoryRegex(Optional<String> storyRegex);

        public abstract Builder setIncludeTags(Optional<List<String>> includeTags);

        public abstract Builder setWebDriverPath(String webDriverPath);

        public abstract Builder setReportPath(String reportPath);

        abstract DriverName getDriverName();

        abstract OurConfiguration autoBuild();

        public OurConfiguration build() {
            CustomWebDriverProvider driverProvider = new CustomWebDriverProvider();
            if (getDriverName() == null) {
                driverProvider = driverProvider.createProvider(DriverName.CHROME_DRIVER);
            } else {
                driverProvider = driverProvider.createProvider(getDriverName());
            }
            setWebDriverProvider(driverProvider);
            return autoBuild();
        }
    }
}
