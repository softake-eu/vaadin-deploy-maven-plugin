package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.JarFileService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler that creates the "apps_data" folder for the deployment by copying the folder from the source path
 * to the destination directory.
 */
@Slf4j
@AllArgsConstructor
public class CreateAppsDataHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final JarFileService fileService;

    // Data
    private final String appsDataDirLocalPath;

    /**
     * Provides a description of the step: creating the "apps_data" folder.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @return the description of the step, including the destination path of the "apps_data" folder
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        return String.format("Create apps_data folder: `%s`...", appsDataDirLocalPath);
    }

    /**
     * Handles the creation of the "apps_data" folder by copying it from the source path to the destination directory.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @throws Exception if an error occurs during the folder copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {

        // Copy apps_data
        final String appsDataFolderName = "apps_data";

        fileService.copyFolder(appsDataFolderName, appsDataDirLocalPath);
    }
}