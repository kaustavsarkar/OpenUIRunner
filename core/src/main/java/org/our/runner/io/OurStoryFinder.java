package org.our.runner.io;

import org.jbehave.core.io.StoryFinder;

import java.util.List;

/**
 * TODO Implement this class only if you need to check this jar for custom
 * stories
 *
 * @author: Kaustav Sarkar
 * @created: 05/16/2019
 */
public class OurStoryFinder extends StoryFinder {

    @Override
    public List<String> findPaths(String rootFolder, List<String> includes,
                                  List<String> excludes) {
        scanRoot(rootFolder, includes, excludes);
        return null;
    }

    private List<String> scanRoot(String rootFolder, List<String> includes,
                                  List<String> excludes) {
        return null;
    }
}
