package org.our;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.SurefireReporter;
import org.jbehave.core.steps.Steps;
import org.our.actions.BaseAction;
import org.our.configuration.OurConfiguration;
import org.our.configuration.OurProperties;

import java.util.ArrayList;

/**
 * Provides the objects which are to be used by the application across the
 * application. The {@link com.google.inject.Injector} used to create the module
 * is the parent injector.
 */
public class OurBaseModule extends AbstractModule {
    private final OurConfiguration ourConfiguration;

    OurBaseModule(OurConfiguration ourConfiguration) {
        this.ourConfiguration = ourConfiguration;
    }

    @Override
    protected void configure() {
        bind(OurConfiguration.class).toInstance(ourConfiguration);
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
}
