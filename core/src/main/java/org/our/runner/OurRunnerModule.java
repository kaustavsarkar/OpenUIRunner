package org.our.runner;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;
import org.our.configuration.OurConfiguration;
import org.our.configuration.OurProperties;
import org.our.runner.stepfactory.CustomStepFactory;

/**
 * Provides objects to be used by the instance of {@link BaseStory} being
 * created inside {@link org.our.OurConfiguredRunner} and further objects which
 * are created and used inside {@link BaseStory}.
 */
public class OurRunnerModule extends AbstractModule {
    private static final Format[] formats =
            new Format[]{Format.HTML, Format.JSON, Format.XML, Format.TXT,
                    Format.STATS};

    /**
     * A child injector being provided while creating an object of {@link
     * OurRunnerModule} inside {@link org.our.OurConfiguredRunner}.
     */
    private final Injector injector;

    public OurRunnerModule(Injector injector) {
        this.injector = injector;
    }

    @Provides
    CustomStepFactory provideStepsFactory(Configuration configuration,
                                          Class<? extends Steps> root) {
        return new CustomStepFactory(configuration, root,
                injector);
    }

    /**
     * Provides an object of {@link Configuration} to be used by {@link
     * BaseStory}.
     * <p>
     * This method is marked as {@link Singleton} since only one object of this
     * shall be required throughout the lifetime of this application which shall
     * be used by JBehave.
     *
     * @param storyLoader   - object is being injected from {@link
     *                      org.our.OurBaseModule}.
     * @param reportBuilder - object is being injected from {@link
     *                      OurRunnerModule}.
     */
    @Provides
    @Singleton
    Configuration provideJbehaveConfiguration(
            StoryLoader storyLoader,
            StoryReporterBuilder reportBuilder) {

        return new MostUsefulConfiguration()
                .useStoryLoader(storyLoader)
                .useStoryReporterBuilder(reportBuilder);
    }

    /**
     * Provides an object of {@link StoryReporterBuilder} to be used while
     * creating an object of {@link Configuration}.
     *
     * @param ourProperties        - object being provided from {@link
     *                             org.our.OurBaseModule}.
     * @param surefireReporter     - object being provided from {@link
     *                             org.our.OurBaseModule}.
     * @param crossReference       - object being provided by {@link
     *                             org.our.OurBaseModule}.
     * @param stepFailureDecorator - object being provided by {@link
     *                             OurRunnerModule}.
     * @return
     */
    @Provides
    StoryReporterBuilder provideStoryReportBuilder(OurProperties ourProperties,
                                                   SurefireReporter surefireReporter,
                                                   CrossReference crossReference,
                                                   StepFailureDecorator stepFailureDecorator) {
        return new OurStoryReportBuilder()
                .withOutReportDirectory(
                        ourProperties.getReportPath())
                .withFormats(formats)
                .withReporters(stepFailureDecorator)
                .withFailureTrace(true)
                .withSurefireReporter(surefireReporter)
                .withCrossReference(crossReference);
    }

    /**
     * Provides an object for {@link StepFailureDecorator} to be while building
     * an object for {@link StoryReporterBuilder}.
     *
     * @param ourStoryReporter - object being provided by {@link
     *                         OurRunnerModule}.
     */
    @Provides
    StepFailureDecorator provideStepFailureDecorator(
            OurStoryReporter ourStoryReporter) {
        return new StepFailureDecorator(ourStoryReporter);
    }

    /**
     * Provides an object of {@link OurStoryReporter} to be used while creating
     * an object of {@link StepFailureDecorator}.
     * <p>
     * The method is marked as singleton as the object shall only be required
     * once in the application lifetime.
     *
     * @param ourProperties - object is being provided by {@link
     *                      org.our.OurBaseModule}.
     */
    @Provides
    @Singleton
    OurStoryReporter provideOurStoryReporter(OurProperties ourProperties) {
        return new OurStoryReporter(injector, ourProperties);
    }

    /**
     * Provides an instance of {@link WebDriver} to be used in action classes.
     * <p>
     * The provided instance needs to be singleton so that it can be quit and
     * closed once the testing is completed.
     *
     * @param ourConfiguration - object being injected by the parent injector
     *                         inside {@link org.our.OurConfiguredRunner}.
     */
    @Provides
    @Singleton
    WebDriver provideWebDriver(OurConfiguration ourConfiguration) {
        return ourConfiguration.getWebDriver();
    }

}
