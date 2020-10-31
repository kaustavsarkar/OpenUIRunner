package runner.io;

import org.jbehave.core.io.StoryLocation;
import org.jbehave.core.reporters.FilePrintStreamFactory;

import java.io.File;

/**
 * @author: Kaustav Sarkar
 * @created: 5/21/2019
 */
public class OurFilePrintStreamFactory extends FilePrintStreamFactory {
    private static final String CLASSNAME =
            OurFilePrintStreamFactory.class.getSimpleName();
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

    public void useOutpurDirectory(String outputDirectory) {
        this.outputDirectory =
                outputDirectory + "/" + configuration().getRelativeDirectory() +
                        "/" +
                        this.storyLocation.getStoryPath().replace(".", "_");
    }

    public File outputDirectory() {
        FileConfiguration configuration = super.configuration();
        System.out.println(CLASSNAME + ": configuration extention " +
                configuration.getExtension());
        System.out.println(CLASSNAME + ": configuration relative directory " +
                configuration.getRelativeDirectory());
        System.out.println(CLASSNAME + ": configuration relative directory " +
                configuration.getPathResolver());
        System.out.println(CLASSNAME + ": Story Location path: " +
                storyLocation.getPath());
        System.out.println(CLASSNAME + ": Story Location Story Path : " +
                storyLocation.getStoryPath());

        String outputPath = configuration.getPathResolver()
                .resolveDirectory(storyLocation,
                        configuration.getRelativeDirectory());

        System.out.println(CLASSNAME + ": output path " + outputPath);

        if (this.outputDirectory != null && !this.outputDirectory.isEmpty()) {
            return new File(this.outputDirectory);
        }

        return super.outputDirectory();
    }

}
