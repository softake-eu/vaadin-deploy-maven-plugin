package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.JarFileService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler that creates the configuration files for the deployment by copying the configurations folder
 * from the source path to the destination deployment directory.
 */
@Slf4j
@AllArgsConstructor
public class CreateConfigsHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final JarFileService fileService;

    // Data
    private final String deployDirLocalPath;

    /**
     * Provides a description of the step: creating the configuration files.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @return the description of the step, including the destination path of the configurations
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        final String configsDestPath = getConfigsDestPath();
        return String.format("Create configurations in: `%s`", configsDestPath);
    }

    /**
     * Handles the creation of the configuration files by copying the configurations folder from the source
     * path to the destination deployment directory.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @throws Exception if an error occurs during the file copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        // Copy configs
        final String configsSourcePath = "configs";
        final String configsDestPath = getConfigsDestPath();

        fileService.copyFolder(configsSourcePath, configsDestPath);
    }

    /**
     * Returns the path where the configuration files will be created in the deployment directory.
     *
     * @return the destination path for the configuration files
     */
    private String getConfigsDestPath() {
        return deployDirLocalPath + "/configs";
    }
}