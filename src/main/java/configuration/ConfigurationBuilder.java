package configuration;

import actions.our.OurProperties;

import java.util.List;

/**
 * @author: Kaustav Sarkar
 * @created: 5/17/2019
 */
public class ConfigurationBuilder {
    private static final String CLASSNAME = ConfigurationBuilder.class.getSimpleName();
    private static final String NULL_PROFILE_MSG = "There needs to be a profile value mentioned." +
            " This shall be used to determine Stories and Data to be picked";
    private static final String NULL_LAUNCH_URL = "Launch URL is mandatory. There was none provided";
    private String profile;
    private String launchUrl;
    private String relDataPath;
    private String storyRegex;
    private List<String> includeTags;
    //private String browser;
    private DriverName driverName;
    private String userName;
    private String password;
    private String webDriverPath;
    private String reportPath;

    public ConfigurationBuilder() {
    }

    /**
     * Sets the maven profile, if being used by the project.
     */
    public ConfigurationBuilder addProfile(String profile) {
        this.profile = profile;
        return this;
    }

    /**
     * Sets the root url from where the testing needs to begin.
     */
    public ConfigurationBuilder addLaunchUrl(String launchUrl) {
        this.launchUrl = launchUrl;
        return this;
    }

    /**
     * Sets the relative path where data file being saved.
     */
    public ConfigurationBuilder addRelativeDataPath(String relDataPath) {
        this.relDataPath = relDataPath;
        return this;
    }

    public ConfigurationBuilder addStory(String story) {
        if (this.storyRegex != null) {
            //Add a warning here
            System.out.println(CLASSNAME + ": Story has been already provided your provided will not hold precedence " + story);
        }
        this.storyRegex = story;
        return this;
    }

    public ConfigurationBuilder addCSVStories(String stories) {
        if (this.storyRegex != null) {
            System.out.println(CLASSNAME + ": Stor(y/ies) has/have already been added. Your CSV stories may get ignored : " + stories);
        }
        this.storyRegex = stories;
        return this;
    }

    public ConfigurationBuilder addStoryRegex(String stories) {
        if (this.storyRegex != null) {
            System.out.println(CLASSNAME + ": Beware!! You have added stories already, there is the chance of them getting over-ridden");
        }
        this.storyRegex = stories;
        return this;
    }

    public ConfigurationBuilder addIncludeMetaTags(List<String> tags) {
        includeTags = tags;
        return this;
    }

    public ConfigurationBuilder addDriver(DriverName driver) {
        this.driverName = driver;
        return this;
    }

    public ConfigurationBuilder addDriverPath(String driverPath) {
        this.webDriverPath = driverPath;
        return this;
    }

    public ConfigurationBuilder addReportPath(String reportPath) {
        this.reportPath = reportPath;
        return this;
    }

    public ConfigurationBuilder addUserName(String username) {
        this.userName = username;
        return this;
    }

    public ConfigurationBuilder addPassword(String password) {
        this.password = password;
        return this;
    }

    public OurConfiguration buildDefaultConfig() throws ConfigException {

        nullChecks();
        addDefaults();

        OurConfiguration config = new OurConfiguration();
        OurProperties props = config.getProperties();

        props.setEnvName(this.profile);
        props.setEnvURL(this.launchUrl);
        props.setDataPath(this.relDataPath);
        props.setBrowser(this.driverName.getBrowser());
        props.setDriverName(this.driverName.getDriverName());
        if (this.includeTags != null) {
            props.getIncludeTag().addAll(this.includeTags);
        }
        props.setStoryFile(this.storyRegex);
        props.setReportPath(this.reportPath);

        props.setUserName(this.userName);
        props.setPassword(this.password);

        System.setProperty(this.driverName.getPropertyKey(), this.webDriverPath + this.driverName.getDriverName());
        config.createCustomWebProvider(this.driverName);


        return config;
    }

    private void addDefaults() {
        if (nullEmptyCheck(this.relDataPath)) {
            System.out.println(CLASSNAME + ": There is no Data path provided. Falling back to default: /data/{profile}/TestData.csv");
            this.relDataPath = this.profile + "/TestData.csv";
        }

        if (this.driverName == null) {
            System.out.println(CLASSNAME + ": No drivers are provided. Falling back to Chrome");
            this.driverName = DriverName.CHROME_DRIVER;
        }

        if (nullEmptyCheck(this.webDriverPath)) {
            System.out.println(CLASSNAME + ": No drivers path is provided. System shall check in Classpath");
            System.setProperty(this.driverName.getPropertyKey(),
                    Thread.currentThread().getContextClassLoader()
                            .getResource(this.driverName.getDriverName()).getPath());
        }

        if (this.includeTags == null || this.includeTags.isEmpty()) {
            System.out.println(CLASSNAME + ": There are no include tags provided. All scenarios added in stories shall run");
        }

        if (nullEmptyCheck(this.storyRegex)) {
            System.out.println(CLASSNAME + ": There are no stories provided. All stories under /" + this.profile + "/ shall be run");
        }

        if (nullEmptyCheck(this.reportPath)) {
            System.out.println(CLASSNAME + ": No Path for Report Provided. Falling back to jbehave/");
        }
    }

    private void nullChecks() {
        if (nullEmptyCheck(this.profile)) {
            throw new ConfigException(NULL_PROFILE_MSG);
        }

        if (nullEmptyCheck(this.launchUrl)) {
            throw new ConfigException(NULL_LAUNCH_URL);
        }
    }

    private boolean nullEmptyCheck(String attribute) {
        return attribute == null || attribute.isEmpty();
    }
}
