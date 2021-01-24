package org.our.configuration;

import com.google.auto.value.AutoValue;
import org.openqa.selenium.WebDriver;
import org.our.selenium.webdriver.CustomWebDriverProvider;
import org.our.selenium.webdriver.WebDriverProvider;

import java.util.List;
import java.util.Optional;

/**
 * Allows the clients to provide configurations for running OUR. The class is
 * immutable and shall only be required to be created once in an application
 * lifetime (when the application launches).
 */
@AutoValue
public abstract class OurConfiguration {

    /**
     * Provides a new object of {@link Builder}.
     *
     * @return {@link Builder}.
     */
    public static Builder newBuilder() {
        return new AutoValue_OurConfiguration.Builder();
    }

    /**
     * Returns a custom implementation of {@link WebDriverProvider}.
     * @return {@link WebDriverProvider}.
     */
    public abstract WebDriverProvider getWebDriverProvider();

    /**
     * Returns an object of {@link WebDriver} depending upon the driver being
     * used by the user.
     * @return {@link WebDriver}.
     */
    public abstract WebDriver getWebDriver();

    /**
     * Returns the launch URL to be tested by the user. This is the URL which
     * shall be launched first while testing an application.
     * @return launchUrl - String.
     */
    public abstract String getLaunchUrl();

    /**
     * Returns the path where data for testing environment has been saved.
     * @return relative path of csv files.
     */
    public abstract String getRelDataPath();

    /**
     * Returns a regular expression for the path from where story files shall be
     * read from. If a path is not provided, all the story files are read and
     * executed.
     * @return {@link Optional<String>}
     */
    public abstract Optional<String> getStoryRegex();

    /**
     * Returns a list of tags added to the story file.
     * @return {@link Optional<List<String>>}.
     */
    public abstract Optional<List<String>> getIncludeTags();

    /**
     * Returns an enum {@link DriverName} as provided by the client while
     * running OUR.
     * @return {@link DriverName}.
     */
    public abstract DriverName getDriverName();

    /**
     * Returns the path where the driver is present on the file system.
     * @return - path of web driver.
     */
    public abstract String getWebDriverPath();

    /**
     * Returns the path where the reports shall be saved.
     * @return path to save reports.
     */
    public abstract String getReportPath();

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setWebDriverProvider(
                WebDriverProvider webDriverProvider);

        abstract Builder setWebDriver(WebDriver webDriver);

        public abstract Builder setProfile(Optional<String> profile);

        public abstract Builder setLaunchUrl(String launchUrl);

        public abstract Builder setRelDataPath(String relDataPath);

        public abstract Builder setStoryRegex(Optional<String> storyRegex);

        public abstract Builder setIncludeTags(
                Optional<List<String>> includeTags);

        public abstract Builder setWebDriverPath(String webDriverPath);

        public abstract Builder setReportPath(String reportPath);

        abstract DriverName getDriverName();

        public abstract Builder setDriverName(DriverName driverName);

        abstract OurConfiguration autoBuild();

        public OurConfiguration build() {
            CustomWebDriverProvider driverProvider =
                    new CustomWebDriverProvider();
            if (getDriverName() == null) {
                driverProvider =
                        driverProvider.createProvider(DriverName.CHROME_DRIVER);
            } else {
                driverProvider = driverProvider.createProvider(getDriverName());
            }
            setWebDriverProvider(driverProvider);
            return autoBuild();
        }
    }
}
