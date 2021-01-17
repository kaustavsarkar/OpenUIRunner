package runner.io;

import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: Kaustav Sarkar
 * @created: 5/21/2019
 */
public class OurFilePrintStreamFactory extends FilePrintStreamFactory {
    private static final Logger logger =
            LoggerFactory.getLogger(OurFilePrintStreamFactory.class);
    private final StoryLocation storyLocation;
    private String outputDirectory;

    public OurFilePrintStreamFactory(StoryLocation storyLocation,
                                     FileConfiguration configuration) {
        super(storyLocation, configuration);
        this.storyLocation = storyLocation;
    }

    public File outputFile() {
        return super.outputFile();
    }

    public void useOutputDirectory(String outputDirectory) {
        this.outputDirectory =
                outputDirectory + "/" + configuration().getRelativeDirectory() +
                        "/" +
                        this.storyLocation.getStoryPath().replace(".", "_");
    }

    public File outputDirectory() {
        FileConfiguration configuration = super.configuration();
        logger.info("configuration extension " +
                configuration.getExtension());
        logger.info("configuration relative directory " +
                configuration.getRelativeDirectory());
        logger.info("configuration relative directory " +
                configuration.getPathResolver());
        logger.info("Story Location path: " +
                storyLocation.getPath());
        logger.info("Story Location Story Path : " +
                storyLocation.getStoryPath());

        String outputPath = configuration.getPathResolver()
                .resolveDirectory(storyLocation,
                        configuration.getRelativeDirectory());

        logger.info("output path " + outputPath);

        if (this.outputDirectory != null && !this.outputDirectory.isEmpty()) {
            return new File(this.outputDirectory);
        }

        return super.outputDirectory();
    }

}
