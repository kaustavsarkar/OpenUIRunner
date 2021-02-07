package org.our.runner;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.Steps;
import org.our.runner.stepfactory.CustomStepFactory;

/**
 * Provides objects to be used by the instance of {@link BaseStory} being
 * created inside {@link OurConfiguredRunner} and further objects which
 * are created and used inside {@link BaseStory}.
 */
public class OurRunnerModule extends AbstractModule {

    /**
     * A child injector being provided while creating an object of {@link
     * OurRunnerModule} inside {@link OurConfiguredRunner}.
     */
    private final Injector childInjector;

    public OurRunnerModule(Injector childInjector) {
        this.childInjector = childInjector;
    }

    @Provides
    CustomStepFactory provideStepsFactory(Configuration configuration,
                                          Class<? extends Steps> root) {
        return new CustomStepFactory(configuration, root,
                childInjector);
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
     *                      OurBaseModule}.
     * @param reportBuilder - object is being injected from {@link
     *                      OurBaseModule}.
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
}
