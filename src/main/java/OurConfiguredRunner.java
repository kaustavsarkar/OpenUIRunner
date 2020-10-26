import configuration.OurConfiguration;
import runner.BaseStory;
import runner.OurContext;

/**
 * Handles execution of stories and web driver code.
 *
 * @author: Kaustav Sarkar
 * @created: 5/20/2019
 */
public class OurConfiguredRunner {

    /**
     * Executes Open UI Runner.
     * <p>
     * This is the entry point for executing the stories and web driver code.
     *
     * @param ourConfiguration {@link OurConfiguration} object sent by the
     *                         client.
     * @throws Throwable
     */
    public void executeWithConfig(OurConfiguration ourConfiguration)
            throws Throwable {

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
