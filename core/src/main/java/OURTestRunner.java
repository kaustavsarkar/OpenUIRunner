import configuration.OurConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.BaseStory;
import runner.OurContext;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link OURTestRunner} is to be used with Maven Plugin
 * (exec-maven-plugin). While using this file, configuration data needs to be
 * passed via properties file/pom
 */
public class OURTestRunner {
    private static final Logger logger =
            LoggerFactory.getLogger(OURTestRunner.class);

    public static void main(String[] args) throws Throwable {
        logger.info("Provided args: " + Arrays.toString(args));

        Map<String, String> ourConfig = getArgs(args);

        initializeContext(ourConfig);

        BaseStory story = new BaseStory();
        story.run();

        destroyContext();
        logger.debug("End of Main Thread");
    }

    private static void destroyContext() {
        OurContext.destroy();
    }


    private static void initializeContext(Map<String, String> ourConfig) {
        OurConfiguration configuration = new OurConfiguration(ourConfig);
        OurContext.initialize();
        OurContext.set(configuration);
    }

    private static Map<String, String> getArgs(String[] args) {

        return Arrays
                .stream(args)
                .map(argument -> argument.split("="))
                .collect(Collectors
                        .toMap(config -> config[0], config -> config[1]));
    }
}
