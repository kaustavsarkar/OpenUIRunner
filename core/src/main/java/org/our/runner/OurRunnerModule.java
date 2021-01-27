package org.our.runner;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.steps.Steps;
import org.our.runner.stepfactory.CustomStepFactory;

public class OurRunnerModule extends AbstractModule {
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

}
