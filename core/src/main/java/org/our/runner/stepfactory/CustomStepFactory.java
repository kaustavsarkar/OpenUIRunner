package org.our.runner.stepfactory;

import org.our.actions.WebDriverScreenShotWorker;
import org.our.actions.OurAction;
import org.our.configuration.OurConfiguration;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ScanningStepsFactory;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.our.selenium.webdriver.WebDriverProvider;

import java.lang.reflect.Constructor;

public class CustomStepFactory extends ScanningStepsFactory {
    private static final Logger logger =
            LoggerFactory.getLogger(CustomStepFactory.class);
    private final OurConfiguration ourConfiguration;
    private final StoryReporterBuilder storyReporterBuilder;

    public CustomStepFactory(Configuration configuration, Class<?> root,
                             OurConfiguration ourConfiguration) {
        this(configuration, root, ourConfiguration, new StoryReporterBuilder());
    }

    public CustomStepFactory(Configuration configuration, Class<?> root,
                             OurConfiguration ourConfiguration,
                             StoryReporterBuilder storyReporterBuilder) {
        super(configuration, root);
        this.ourConfiguration = ourConfiguration;
        this.storyReporterBuilder = storyReporterBuilder;
    }

    @Override
    public Object createInstanceOfType(Class<?> type) {
        Object instance;
        Constructor<?> constructor;
        try {
            if (type.equals(WebDriverScreenShotWorker.class)) {
                constructor =
                        type.getDeclaredConstructor(WebDriverProvider.class,
                                StoryReporterBuilder.class);
                instance = constructor
                        .newInstance(this.ourConfiguration.getDriverProvider(),
                                storyReporterBuilder);
            } else if (type.equals(OurAction.class)) {
                constructor =
                        type.getDeclaredConstructor(OurConfiguration.class);
                constructor.setAccessible(true);
                instance = constructor.newInstance(ourConfiguration);
            } else {
                constructor = type.getDeclaredConstructor(WebDriver.class);
                instance = constructor
                        .newInstance(this.ourConfiguration.getDriver());
            }
        } catch (Exception e) {
            logger.error("Failed while creating instance of Actions", e);
            throw new StepsInstanceNotFound(type, this);
        }
        return instance;
    }
}
