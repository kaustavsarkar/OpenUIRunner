package runner;

import actions.BaseAction;
import actions.our.OurProperties;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StepFailureDecorator;
import org.jbehave.core.reporters.SurefireReporter;
import org.jbehave.core.steps.InjectableStepsFactory;
import runner.stepfactory.CustomStepFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseStory extends JUnitStories {
    private static final String CLASSNAME = BaseStory.class.getSimpleName();
    protected Configuration configuration;
    //private WebDriver driver;
    private final OurProperties ourProperties;

    public BaseStory() {
        System.out.println(CLASSNAME + ": entered constructor");
        //this.driver = OurContext.getDriver();
        this.ourProperties = OurContext.getProperties();

        System.out.println(
                CLASSNAME + ": Properties : " + this.ourProperties.toString());
    }

    @Override
    public Configuration configuration() {
        System.out.println(CLASSNAME + ": entered configuration");
        SurefireReporter surefireReporter = new SurefireReporter(
                this.getClass(),
                new SurefireReporter.Options("our-surefire",
                        SurefireReporter.Options.DEFAULT_NAMING_STRATEGY, true,
                        true));

        CrossReference crossReference = new CrossReference();
        Format[] formats =
                new Format[]{Format.HTML, Format.JSON, Format.XML, Format.TXT,
                        Format.STATS};
        configuration = new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(BaseAction.class))
                .useStoryReporterBuilder(
                        new OurStoryReportBuilder()
                                .withOutReportDirectory(
                                        this.ourProperties.getReportPath())
                                .withFormats(formats)
                                .withReporters(new StepFailureDecorator(
                                        new OurStoryReporter()))
                                .withFailureTrace(true)
                                .withSurefireReporter(surefireReporter)
                                .withCrossReference(crossReference));

        return configuration;
    }

    @Override
    // @Test
    public void run() throws Throwable {
        System.out.println(CLASSNAME + ": Thread Name: " +
                Thread.currentThread().getName());
        System.out.println(CLASSNAME + ": entered run()");

        //super.useEmbedder(new OurEmbedder());
        Embedder embedder = configuredEmbedder();
        try {
            System.out.println(CLASSNAME + ": Include Tags : " +
                    ourProperties.getIncludeTag());
            embedder.useMetaFilters(
                    ourProperties.getIncludeTag());
            embedder.useEmbedderControls(new EmbedderControls()
                    .doIgnoreFailureInStories(true).useThreads(1)
                    .doIgnoreFailureInView(true)
                    .doFailOnStoryTimeout(true)
                    .useStoryTimeouts("300"));
            List<String> storyPath = storyPaths();
            System.out.println(CLASSNAME + ": Story PAth: " + storyPath);
            embedder.runStoriesAsPaths(storyPath);
        } finally {
            embedder.generateCrossReference();
            embedder.generateSurefireReport();
            embedder.generateReportsView();
        }
    }

    protected List<String> storyPaths() {
        System.out.println(CLASSNAME + ": entered storyPaths()");
//        String codeLocation = CodeLocations
//                .codeLocationFromClass(this.getClass()).getFile();
        String codeLocation = CodeLocations
                .codeLocationFromPath(Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResource("")
                        .getPath())
                .getFile();

        System.out
                .println(CLASSNAME + ": Using Code Location: " + codeLocation);

        List<String> storyList = new ArrayList<>();
        String storyFiles = ourProperties.getStoryFile();

        if (storyFiles != null && !storyFiles.isEmpty()) {

            String[] storyFileArr = storyFiles.split(",");

            for (String storyFile : storyFileArr) {
                storyList.add("**/" + storyFile);
            }
        } else {
            storyList.add("**/**.story");
        }

        System.out.println(CLASSNAME + ": Story List" + storyList);
        return new StoryFinder().findPaths(codeLocation, storyList, null);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        System.out.println(CLASSNAME + ": entered stepsFactory()");
        InjectableStepsFactory stepsFactory =
                new CustomStepFactory(
                        this.configuration,
                        BaseAction.class,
                        OurContext.get(),
                        configuration.storyReporterBuilder()
                );
        //  new CustomStepFactory(this.configuration, BaseAction.class,
        //   OurContext.get());
        return stepsFactory;
    }
}
