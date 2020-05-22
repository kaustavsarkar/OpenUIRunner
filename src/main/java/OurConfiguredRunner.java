import configuration.OurConfiguration;
import runner.BaseStory;
import runner.OurContext;

/**
 * @author: Kaustav Sarkar
 * @created: 5/20/2019
 */
public class OurConfiguredRunner {

    // private OurConfiguration ourConfiguration;

    public void executeWithConfig(OurConfiguration ourConfiguration) throws Throwable {

        try {
            if (ourConfiguration == null) {
                throw new Exception("You need to provide your own Configuration.");
            }

            OurContext.initialize();
            OurContext.set(ourConfiguration);

            BaseStory story = new BaseStory();
            story.run();
        } finally {
            OurContext.destroy();
        }

    }
}
