package org.our;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StepFailureDecorator;
import org.jbehave.core.reporters.SurefireReporter;
import org.jbehave.core.steps.Step;
import org.jbehave.core.steps.Steps;
import org.our.actions.BaseAction;
import org.our.configuration.OurConfiguration;
import org.our.configuration.OurProperties;
import org.our.runner.OurStoryReportBuilder;
import org.our.runner.OurStoryReporter;
import org.our.runner.stepfactory.CustomStepFactory;

import java.util.ArrayList;

public class OurBaseModule extends AbstractModule {

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
    @Singleton
    Configuration provideJbehaveConfiguration(OurProperties ourProperties) {
        SurefireReporter surefireReporter = new SurefireReporter(
                this.getClass(),
                new SurefireReporter.Options("our-surefire",
                        SurefireReporter.Options.DEFAULT_NAMING_STRATEGY, true,
                        true));

        CrossReference crossReference = new CrossReference();
        Format[] formats =
                new Format[]{Format.HTML, Format.JSON, Format.XML, Format.TXT,
                        Format.STATS};
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(BaseAction.class))
                .useStoryReporterBuilder(
                        new OurStoryReportBuilder()
                                .withOutReportDirectory(
                                        ourProperties.getReportPath())
                                .withFormats(formats)
                                .withReporters(new StepFailureDecorator(
                                        new OurStoryReporter()))
                                .withFailureTrace(true)
                                .withSurefireReporter(surefireReporter)
                                .withCrossReference(crossReference));
    }

    @Provides
    Class<? extends Steps> provideRootAction() {
        return BaseAction.class;
    }
}
