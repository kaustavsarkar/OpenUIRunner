package org.our.actions;

import com.google.auto.value.AutoValue;
import org.our.configuration.OurConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * Properties used by OUR for running JBehave and Selenium. The properties
 * provided by this class are generated based on the input provided in {@link
 * OurConfiguration}. Values provided in {@link OurConfiguration} shall be
 * translated to {@link OurProperties} and these values shall be used by the
 * application.
 */
@AutoValue
public abstract class OurProperties {

    public static Builder newBuilder() {
        return new AutoValue_OurProperties.Builder();
    }

    /**
     * Returns the URL provided in {@link OurConfiguration#getLaunchUrl()}.
     */
    public abstract String getEnvURL();

    /**
     * Returns the path of csv file for running OUR.
     */
    public abstract String getDataPath();

    /**
     * Returns the path of the story file.
     */
    public abstract String getStoryFile();

    /**
     * Returns the browser where OUR shall be run.
     */
    public abstract String getBrowser();

    /**
     * Returns a list of include tags being added to the story files.
     */
    public abstract List<String> getIncludeTags();

    /**
     * Returns the name of the driver for which the browser is being run.
     */
    public abstract String getDriverName();

    /**
     * Returns the path where reports shall be saved.
     */
    public abstract String getReportPath();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setEnvURL(String envURL);

        public abstract Builder setDataPath(String dataPath);

        public abstract Builder setStoryFile(String storyFile);

        public abstract Builder setBrowser(String browser);

        public abstract Builder setDriverName(String driverName);

        public abstract Builder setReportPath(String reportPath);

        abstract List<String> getIncludeTags();

        public abstract Builder setIncludeTags(List<String> includeTags);

        abstract OurProperties autoBuild();

        public OurProperties build() {
            List<String> immutableIncludeTags =
                    Collections.unmodifiableList(getIncludeTags());
            setIncludeTags(immutableIncludeTags);
            return autoBuild();
        }
    }

}
