package actions.our;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OurProperties extends Object {
    private static final String CLASSNAME = OurProperties.class.getSimpleName();
    String envName;
    String envURL;
    String dataPath;
    String storyFile;
    List<String> tags;
    String browser;
    String driverName;
    String userName;
    String password;
    String reportPath;

    /**
     * instance.name=${env.name} instance.url=${env.url} instance.data=${env.data} our.browser=${test.browser}
     * our.story=${test.story} our.scenario=${test.scenario}
     *
     * @param is
     *
     * @throws IOException
     */
    public OurProperties(InputStream is) throws IOException {
        Properties props = new Properties();
        props.load(is);

        System.out
                .println(CLASSNAME + ": Base Properties : " + props);

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

    String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        this.tags = includeTags != null && !includeTags.isEmpty() ? Arrays.asList(includeTags.split(","))
                : this.tags;
        this.driverName = driverName != null && !driverName.isEmpty()
                ? driverName
                : this.driverName;

        // ----- Think how it can be removed ------- //
        this.userName = props.getProperty("local.username");
        this.password = props.getProperty("local.password");
        // ----- Think how it can be removed ------- //

        System.out.println(CLASSNAME + ": User Provided : OurProperties [envName=" + aEnvName + ", envURL=" + aEnvUrl
                + ", dataPath=" + aDataPath + ", storyFile=" + aStoryFile
                + ", tags=" + includeTags + ", aBrowser=" + aBrowser + ", driverName="
                + driverName + ", userName=" + this.userName + ", password="
                + this.password + "]");

    }

    @Override
    public String toString() {
        return "OurProperties [envName=" + envName + ", envURL=" + envURL
                + ", dataPath=" + dataPath + ", storyFile=" + storyFile
                + ", tags=" + tags + ", browser=" + browser + ", driverName="
                + driverName + ", userName=" + userName + ", password="
                + password + "]";
    }

}
