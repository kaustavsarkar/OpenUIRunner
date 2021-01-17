package runner;

import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.io.OurFilePrintStreamFactory;

import java.io.File;

/**
 * @author: Kaustav Sarkar
 * @created: 5/20/2019
 */
public class OurStoryReportBuilder extends StoryReporterBuilder {
    private static final Logger logger =
            LoggerFactory.getLogger(OurStoryReportBuilder.class);
    private String outReportDirectory;

    @Override
    public File outputDirectory() {
        if (outReportDirectory == null || this.outReportDirectory.isEmpty()) {
            logger.warn("There is no Output Directory provided for view " +
                    "reports. Falling back to defaults");
            return super.outputDirectory();
        }

        File outDirectory = new File(outReportDirectory);

        if (!outDirectory.exists()) {
            logger.warn("There is no such directory : " +
                    outDirectory.getAbsolutePath());
            return super.outputDirectory();
        }

        if (!outDirectory.isDirectory()) {
            logger.warn("Path provided is not a directory : " +
                    outDirectory.getAbsolutePath());
            return super.outputDirectory();
        }

        return outDirectory;
    }

    @Override
    protected FilePrintStreamFactory filePrintStreamFactory(String storyPath) {

        OurFilePrintStreamFactory factory =
                new OurFilePrintStreamFactory(new StoryLocation(codeLocation(),
                        storyPath), fileConfiguration(""));
        factory.useOutputDirectory(this.outReportDirectory);
        logger.info("factory output file : " + factory.outputFile());
        logger.info("factory output directory : " + factory.outputDirectory());
        return factory;
    }

    public OurStoryReportBuilder withOutReportDirectory(
            String outReportDirectory) {
        this.outReportDirectory = outReportDirectory;
        return this;
    }
}
