package org.our.example;

import org.our.example.actions.TestGoogleAction;
import org.our.configuration.DriverName;
import org.our.configuration.OurConfiguration;
import org.our.runner.OurConfiguredRunner;

import java.util.Optional;

public class OURTestRunner {
    public static void main(String[] args) throws Throwable {
        OurConfiguration configuration =
                OurConfiguration.newBuilder()
                        .setRootClass(TestGoogleAction.class)
                        .setDriverName(DriverName.CHROME_DRIVER)
                        .setIncludeTags(Optional.empty())
                        .setLaunchUrl("https://www.google.com")
                        .setRelDataPath("google/TestData.csv")
                        .setReportPath("/home/kaustav/OUR")
                        .setStoryRegex(Optional.of("google/test_google.story"))
                        .setWebDriverPath("/home/kaustav/OUR/chromedriver")
                        .build();
        OurConfiguredRunner runner = new OurConfiguredRunner();
        runner.executeWithConfig(configuration);
    }
}
