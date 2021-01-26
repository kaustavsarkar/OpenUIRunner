package org.our;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.our.configuration.OurConfiguration;
import org.our.configuration.OurProperties;

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
}
