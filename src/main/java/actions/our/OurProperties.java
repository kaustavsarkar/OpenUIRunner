package actions.our;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OurProperties {
    private static final Logger logger =
            LoggerFactory.getLogger(OurProperties.class);
    String envName;
    String envURL;
    String dataPath;
    String storyFile;
    List<String> tags;
    String browser;
    String driverName;
    String reportPath;

    /**
     * instance.name=${env.name} instance.url=${env.url}
     * instance.data=${env.data} our.browser=${test.browser}
     * our.story=${test.story} our.scenario=${test.scenario}
     *
     * @param is
     * @throws IOException
     */
    public OurProperties(InputStream is) throws IOException {
        Properties props = new Properties();
        props.load(is);

        logger.info("Base Properties: "+props);

        this.envName = props.getProperty("run.profile");
        this.envURL = props.getProperty("run.url");
        this.dataPath = props.getProperty("run.data");
        this.browser = props.getProperty("run.browser");
        this.driverName = props.getProperty("run.driver");
    }

    public OurProperties() {

    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getEnvURL() {
        return envURL;
    }

    public void setEnvURL(String envURL) {
        this.envURL = envURL;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getStoryFile() {
        return storyFile;
    }

    public void setStoryFile(String storyFile) {
        this.storyFile = storyFile;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public List<String> getIncludeTag() {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        return tags;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public void override(InputStream localIs) throws IOException {
        Properties props = new Properties();
        props.load(localIs);

        String aEnvName = props.getProperty("local.name");
        String aEnvUrl = props.getProperty("local.url");
        String aDataPath = props.getProperty("local.data");
        String aBrowser = props.getProperty("local.browser");
        String includeTags = props.getProperty("local.tags");
        String aStoryFile = props.getProperty("local.story");
        String driverName = props.getProperty("local.driver");


        this.envName = aEnvName != null && !aEnvName.isEmpty() ? aEnvName
                : this.envName;
        this.envURL = aEnvUrl != null && !aEnvUrl.isEmpty() ? aEnvUrl
                : this.envURL;
        this.dataPath = aDataPath != null && !aDataPath.isEmpty() ? aDataPath
                : this.dataPath;
        this.browser = aBrowser != null && !aBrowser.isEmpty() ? aBrowser
                : this.browser;
        this.storyFile = aStoryFile != null && !aStoryFile.isEmpty()
                ? aStoryFile
                : this.storyFile;
        this.tags = includeTags != null && !includeTags.isEmpty() ?
                Arrays.asList(includeTags.split(","))
                : this.tags;
        this.driverName = driverName != null && !driverName.isEmpty()
                ? driverName
                : this.driverName;

        logger.info("User Provided : OurProperties [envName=" +
                aEnvName + ", envURL=" + aEnvUrl
                + ", dataPath=" + aDataPath + ", storyFile=" +
                aStoryFile
                + ", tags=" + includeTags + ", aBrowser=" + aBrowser +
                ", driverName="
                + driverName);

    }

    @Override
    public String toString() {
        return "OurProperties [envName=" + envName + ", envURL=" + envURL
                + ", dataPath=" + dataPath + ", storyFile=" + storyFile
                + ", tags=" + tags + ", browser=" + browser + ", driverName="
                + driverName;
    }

}
