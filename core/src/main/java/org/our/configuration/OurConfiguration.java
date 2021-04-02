package org.our.configuration;

import com.google.auto.value.AutoValue;
import org.jbehave.core.steps.Steps;
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
     *
     * @return {@link WebDriverProvider}.
     */
    public abstract WebDriverProvider getWebDriverProvider();

    /**
     * Returns an object of {@link WebDriver} depending upon the driver being
     * used by the user.
     *
     * @return {@link WebDriver}.
     */
    public abstract WebDriver getWebDriver();

    /**
     * Returns the launch URL to be tested by the user. This is the URL which
     * shall be launched first while testing an application.
     *
     * @return launchUrl - String.
     */
    public abstract String getLaunchUrl();

    /**
     * Returns the path where data for testing environment has been saved.
     *
     * @return relative path of csv files.
     */
    public abstract String getRelDataPath();

    /**
     * Returns a regular expression for the path from where story files shall be
     * read from. If a path is not provided, all the story files are read and
     * executed.
     *
     * @return {@link Optional<String>}
     */
    public abstract Optional<String> getStoryRegex();

    /**
     * Returns a list of tags added to the story file.
     *
     * @return {@link Optional<List<String>>}.
     */
    public abstract Optional<List<String>> getIncludeTags();

    /**
     * Returns an enum {@link DriverName} as provided by the client while
     * running OUR.
     *
     * @return {@link DriverName}.
     */
    public abstract DriverName getDriverName();

    /**
     * Returns the path where the driver is present on the file system.
     *
     * @return - path of web driver.
     */
    public abstract String getWebDriverPath();

    /**
     * Returns the path where the reports shall be saved.
     *
     * @return path to save reports.
     */
    public abstract String getReportPath();

    /**
     * Returns the root action class provided by the user. OUR shall be using
     * the classes present in the same package as the root action class and
     * all the sub-packages inside it.
     */
    public abstract Class<? extends Steps> getRootClass();

    @AutoValue.Builder
    public abstract static class Builder {
        /**
         * To be used internally in the builder for setting a custom
         * implementation of {@link WebDriverProvider} created based on the
         * value provided by the user via {@link DriverName}.
         *
         * @param webDriverProvider - custom implementation of {@link
         *                          WebDriverProvider}.
         */
        abstract Builder setWebDriverProvider(
                WebDriverProvider webDriverProvider);

        /**
         * To be used internally to set the {@link WebDriver} client intends to
         * use for testing the application. It shall be determined by the input
         * value of {@link DriverName}.
         *
         * @param webDriver - driver based on the browser the client shall be
         *                  testing their application on.
         */
        abstract Builder setWebDriver(WebDriver webDriver);

        /**
         * Sets the Url which shall be launched when the web driver starts. This
         * should be the entry point from where the testing is required to be
         * done.
         *
         * @param launchUrl - entry point of the application from where the
         *                  testing needs to start.
         */
        public abstract Builder setLaunchUrl(String launchUrl);

        /**
         * // TODO(kaustav): Add some doc here.
         *
         * @param storyRegex
         * @return
         */
        public abstract Builder setStoryRegex(Optional<String> storyRegex);

        public abstract Builder setIncludeTags(
                Optional<List<String>> includeTags);

        /**
         * Sets the path in the file system where the reports are required to be
         * saved. Note: This method is not platform agnostic, hence, the format
         * to provide the FS path shall be different for, say, Windows, Mac and
         * linux.
         */
        public abstract Builder setReportPath(String reportPath);

        abstract DriverName getDriverName();

        /**
         * Sets the driver which the client needs to use for testing purpose.
         */
        public abstract Builder setDriverName(DriverName driverName);

        abstract String getWebDriverPath();

        public abstract Builder setWebDriverPath(String webDriverPath);

        /**
         * Sets the path where the data shall be found. The data needs to be
         * stored inside the resources/data/ folder. The method assumes this
         * shall be the base path for the data files. Client may add further
         * nested folders for storing data for different test cases.
         *
         * @param relDataPath - data path relative to /resources/data.
         */
        public abstract Builder setRelDataPath(String relDataPath);

        /**
         * Sets the path of the root class where all the action classes are
         * placed. All the actions in the same package and the sub-packages
         * shall be executed by the OUR.
         */
        public abstract Builder setRootClass(Class<? extends Steps> rootClass);

        abstract OurConfiguration autoBuild();

        public OurConfiguration build() {
            System.setProperty(
                    getDriverName().getPropertyKey(),
                    getWebDriverPath());
            CustomWebDriverProvider driverProvider =
                    new CustomWebDriverProvider();
            if (getDriverName() == null) {
                driverProvider =
                        driverProvider.createProvider(DriverName.CHROME_DRIVER);
            } else {
                driverProvider = driverProvider.createProvider(getDriverName());
            }
            setWebDriverProvider(driverProvider);
            setWebDriver(driverProvider.get());
            return autoBuild();
        }
    }
}
