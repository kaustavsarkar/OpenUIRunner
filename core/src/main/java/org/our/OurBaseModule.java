package org.our;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.Steps;
import org.our.actions.BaseAction;
import org.our.configuration.OurConfiguration;
import org.our.configuration.OurProperties;
import org.our.runner.OurStoryReportBuilder;
import org.our.runner.OurStoryReporter;

import java.util.ArrayList;

public class OurBaseModule extends AbstractModule {

    private static final Format[] formats =
            new Format[]{Format.HTML, Format.JSON, Format.XML, Format.TXT,
                    Format.STATS};

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

    @Provides
    SurefireReporter provideSurefireReporter() {
        return new SurefireReporter(
                this.getClass(),
                new SurefireReporter.Options("our-surefire",
                        SurefireReporter.Options.DEFAULT_NAMING_STRATEGY, true,
                        true));
    }

    @Provides
    StoryLoader provideStoryLoader(Class<? extends Steps> rootClass) {
        return new LoadFromClasspath(rootClass);
    }

    @Provides
    StepFailureDecorator provideStepFailureDecorator() {
        return new StepFailureDecorator(
                new OurStoryReporter());
    }

    @Provides
    @Singleton
    Configuration provideJbehaveConfiguration(
            StoryLoader storyLoader,
            OurStoryReportBuilder reportBuilder) {

        return new MostUsefulConfiguration()
                .useStoryLoader(storyLoader)
                .useStoryReporterBuilder(reportBuilder);
    }

    @Provides
    Class<? extends Steps> provideRootAction() {
        return BaseAction.class;
    }

    @Provides
    CrossReference provideCrossReference() {
        return new CrossReference();
    }

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
}
