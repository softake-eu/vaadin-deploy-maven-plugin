package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.LocalFileService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Handler that checks if the built JAR file exists in the target directory of the Vaadin project.
 * <p>
 * This step ensures that the Vaadin project has been built successfully before proceeding with further deployment steps.
 * It verifies the presence of the target directory and the specific packaging file (e.g., JAR). If the directory or file is missing,
 * or if there is ambiguity (multiple files found), an exception is thrown.
 * </p>
 */
@AllArgsConstructor
public class CheckBuiltJarFileExistsHandler implements ChainStepHandler<ServerParam> {

    // Constants
    public static final String EXCEPTION_NO_TARGET_FOLDER = "Folder `%s` doesn't exist. Please build the project first!";
    public static final String EXCEPTION_NO_PACKAGE = "Built `%s` file doesn't exist in 'target' folder. Please build the project first!";
    public static final String EXCEPTION_AMBIGUITY = "Ambiguity! More than `.%s` file is found in the 'target' folder.";

    // Data
    private final String targetDirPath;
    private final String packaging;

    // Services
    private final LocalFileService fileService;

    /**
     * Provides a description of the step that will be executed in the chain.
     *
     * @param dataObj the server model containing configuration information.
     * @return a string describing the step, which verifies that the Vaadin project has been built.
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return "Check the Vaadin project has been built...";
    }

    /**
     * Verifies that the built JAR file exists in the target directory.
     *
     * @param dataObj the server model containing configuration information.
     * @throws FileNotFoundException if the target folder does not exist.
     * @throws IllegalArgumentException if the built JAR file does not exist or if there is ambiguity with multiple files.
     */
    @Override
    public void handle(ServerParam dataObj) throws Exception {
        final File targetFolder = new File(targetDirPath);

        if (!targetFolder.exists()) {
            throw new FileNotFoundException(String.format(EXCEPTION_NO_TARGET_FOLDER, targetDirPath));
        }

        List<File> jarFiles = fileService.getChildren(targetFolder, ".*\\." + packaging + "$");

        if (jarFiles.isEmpty()) {
            throw new IllegalArgumentException(String.format(EXCEPTION_NO_PACKAGE, packaging));
        } else if(jarFiles.size() > 1) {
            throw new IllegalArgumentException(String.format(EXCEPTION_AMBIGUITY, packaging));
        }
    }
}