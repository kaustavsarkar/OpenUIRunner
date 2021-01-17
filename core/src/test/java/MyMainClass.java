import org.our.configuration.ConfigurationBuilder;
import org.our.configuration.DriverName;
import org.our.configuration.OurConfiguration;
import org.our.OurConfiguredRunner;

/**
 * @author: Kaustav Sarkar
 * @created: 5/20/2019
 */
public class MyMainClass {
    public static void main(String[] args) throws Throwable {


        runSAASTests();


    }

    private static void runSAASTests() throws Throwable {
        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.addDriver(DriverName.CHROME_DRIVER);
        builder.addStoryRegex("catfactrest/cat_fact.story,");
        builder.addDriverPath("/home/kaustav/OUR/");
        builder.addLaunchUrl("https://www.google.com");
        builder.addReportPath("/home/kaustav/OUR");
        builder.addRelativeDataPath("google/TestData.csv");
        builder.addProfile("as");

        OurConfiguration ourConfiguration = builder.buildDefaultConfig();

        OurConfiguredRunner configuredRunner = new OurConfiguredRunner();

        configuredRunner.executeWithConfig(ourConfiguration);
    }
}
