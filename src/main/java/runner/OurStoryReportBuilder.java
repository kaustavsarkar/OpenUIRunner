package runner;

import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import runner.io.OurFilePrintStreamFactory;

import java.io.File;

/**
 * @author: Kaustav Sarkar
 * @created: 5/20/2019
 */
public class OurStoryReportBuilder extends StoryReporterBuilder {
    private static final String CLASSNAME = OurStoryReportBuilder.class.getSimpleName();
    private String outReportDirectory;

    @Override
    public File outputDirectory() {
        if (outReportDirectory == null || this.outReportDirectory.isEmpty()) {
            System.out.println(CLASSNAME + ": There is no Output Directory provided for view reports. Falling back to defaults");
            return super.outputDirectory();
        }

        File outDirectory = new File(outReportDirectory);

        if (!outDirectory.exists()) {
            System.err.println(CLASSNAME + ": There is no such directory : " + outDirectory.getAbsolutePath());
            return super.outputDirectory();
        }

        if (!outDirectory.isDirectory()) {
            System.err.println(CLASSNAME + ": Path provided is not a directory : " + outDirectory.getAbsolutePath());
            return super.outputDirectory();
        }

        return outDirectory;
    }

    @Override
    protected FilePrintStreamFactory filePrintStreamFactory(String storyPath) {

        OurFilePrintStreamFactory factory = new OurFilePrintStreamFactory(new StoryLocation(codeLocation(),
                storyPath), fileConfiguration(""));
        factory.useOutpurDirectory(this.outReportDirectory);
        System.out.println(CLASSNAME + ": factor output file : " + factory.outputFile());
        System.out.println(CLASSNAME + ": factor output directory : " + factory.outputDirectory());
        return factory;
    }

    public OurStoryReportBuilder withOutReportDirectory(String outReportDirectory) {
        this.outReportDirectory = outReportDirectory;
        return this;
    }
}
