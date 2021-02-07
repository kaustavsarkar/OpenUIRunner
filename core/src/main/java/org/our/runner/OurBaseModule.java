package org.our.runner;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.WebDriver;
import org.our.actions.BaseAction;
import org.our.configuration.OurConfiguration;
import org.our.configuration.OurProperties;
import org.our.configuration.ScenarioProperties;
import org.our.selenium.webdriver.WebDriverProvider;

import java.util.ArrayList;

/**
 * Provides the objects which are to be used by the application across the
 * application. The {@link com.google.inject.Injector} used to create the module
 * is the parent injector.
 */
public class OurBaseModule extends AbstractModule {
    private static final Format[] formats =
            new Format[]{Format.HTML, Format.JSON, Format.XML, Format.TXT,
                    Format.STATS};
    private final OurConfiguration ourConfiguration;

    OurBaseModule(OurConfiguration ourConfiguration) {
        this.ourConfiguration = ourConfiguration;
    }

    @Override
    protected void configure() {
        bind(OurConfiguration.class).toInstance(ourConfiguration);
        bind(ScenarioProperties.class)
                .toProvider(ScenarioPropertiesProvider.class);
    }

    /**
     * Provides {@link OurProperties} created using {@link OurConfiguration}
     * being provided by the client while running the application. This object
     * shall be used across the application to refer the configuration
     * requirements provided by the client.
     */
    @Provides
    @Singleton
    OurProperties provideOurProperties(OurConfiguration ourConfiguration) {
        return OurProperties.newBuilder()
                .setBrowser(ourConfiguration.getDriverName().getBrowser())
                .setDataPath(ourConfiguration.getRelDataPath())
                .setDriverName(ourConfiguration.getDriverName().getDriverName())
                .setEnvURL(ourConfiguration.getLaunchUrl())
                .setIncludeTags(ourConfiguration.getIncludeTags().orElse(
                        new ArrayList<>()))
                .setReportPath(ourConfiguration.getReportPath())
                .setStoryFile(ourConfiguration.getStoryRegex().orElse(""))
                .build();
    }

    /**
     * Provides the object of {@link SurefireReporter} to be used by jbehave for
     * creating reports. This is used while creating an object of {@link
     * org.jbehave.core.reporters.StoryReporterBuilder} in {@link
     * org.our.runner.OurRunnerModule}.
     */
    @Provides
    SurefireReporter provideSurefireReporter() {
        return new SurefireReporter(
                this.getClass(),
                new SurefireReporter.Options("our-surefire",
                        SurefireReporter.Options.DEFAULT_NAMING_STRATEGY, true,
                        true));
    }

    /**
     * Provides an answer for {@link StoryLoader}.
     * <p>
     * The object shall be available across the application. This is used for
     * creating an object of {@link org.jbehave.core.configuration.Configuration}
     * in {@link org.our.runner.OurRunnerModule}.
     */
    @Provides
    StoryLoader provideStoryLoader(Class<? extends Steps> rootClass) {
        return new LoadFromClasspath(rootClass);
    }

    /**
     * Provides the root action class which shall be used to scan other action
     * classes which are present inside the same package or sub-packages.
     */
    // TODO(kaustav): Get the base action from the user. So that the users
    //  may provide a different package structure than what is being used in
    //  this application.
    @Provides
    Class<? extends Steps> provideRootAction() {
        return BaseAction.class;
    }

    /**
     * Provide an object of {@link CrossReference}.
     * <p>
     * The object is used for building an object of {@link
     * org.jbehave.core.reporters.StoryReporterBuilder} in {@link
     * org.our.runner.OurRunnerModule}.
     */
    @Provides
    CrossReference provideCrossReference() {
        return new CrossReference();
    }


    /**
     * Provides an object of {@link StoryReporterBuilder} to be used while
     * creating an object of {@link Configuration}.
     *
     * @param ourProperties        - object being provided from {@link
     *                             OurBaseModule}.
     * @param surefireReporter     - object being provided from {@link
     *                             OurBaseModule}.
     * @param crossReference       - object being provided by {@link
     *                             OurBaseModule}.
     * @param stepFailureDecorator - object being provided by {@link
     *                             OurBaseModule}.
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
     *                         OurBaseModule}.
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
     *                      OurBaseModule}.
     */
    @Provides
    @Singleton
    OurStoryReporter provideOurStoryReporter(OurProperties ourProperties,
                                             ScenarioPropertiesProvider scenarioPropertiesProvider) {
        return new OurStoryReporter(scenarioPropertiesProvider, ourProperties);
    }


    /**
     * Provides an instance of {@link WebDriver} to be used in action classes.
     * <p>
     * The provided instance needs to be singleton so that it can be quit and
     * closed once the testing is completed.
     *
     * @param ourConfiguration - object being injected by the parent injector
     *                         inside {@link OurConfiguredRunner}.
     */
    @Provides
    @Singleton
    WebDriver provideWebDriver(OurConfiguration ourConfiguration) {
        return ourConfiguration.getWebDriver();
    }

    /**
     * Provides an instance of {@link WebDriver} to be used in action classes.
     * <p>
     * The provided instance needs to be singleton to avoid multiple instances
     * being created.
     *
     * @param ourConfiguration - object being injected by the parent injector
     *                         inside {@link OurConfiguredRunner}.
     */
    @Provides
    @Singleton
    WebDriverProvider provideWebDriverProvider(
            OurConfiguration ourConfiguration) {
        return ourConfiguration.getWebDriverProvider();
    }

    @Provides
    @Singleton
    ScenarioPropertiesProvider provideScenarioPropertyProvider() {
        return new ScenarioPropertiesProvider();
    }
}
