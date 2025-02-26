package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.LocalFileService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler that creates the Dockerfile for deploying the application. It copies the Dockerfile template from
 * the Vaadin project directory to the destination deployment directory. If the server type is LOCAL, the
 * creation of the Dockerfile is skipped.
 */
@Slf4j
@AllArgsConstructor
public class CreateDockerFileHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final LocalFileService fileService;

    // Data
    private final String deployDirLocalPath;
    private final String vaadinProjectDirLocalPath;

    /**
     * Provides a description of the step: creating the Dockerfile.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @return the description of the step, including the destination path of the Dockerfile
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        final String destPath = getDestEnvFilePath();
        return String.format("Create Dockerfile: `%s`", destPath);
    }

    /**
     * Handles the creation of the Dockerfile by copying the appropriate template from the Vaadin project directory
     * to the destination deployment directory. If the server type is LOCAL, the process is skipped.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @throws Exception if an error occurs during the file copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        // Skip Dockerfile creation for LOCAL configurations
        if (ServerTypeParam.LOCAL.equals(serverModel.getType())) {
            log.info("Skip creating Dockerfile. It is not required for LOCAL servers");
            return;
        }

        final String srcPath = getSrcEnvFilePath();
        final String destPath = getDestEnvFilePath();

        fileService.copyFile(srcPath, destPath);
    }

    /**
     * Returns the path where the Dockerfile will be created in the deployment directory.
     *
     * @return the destination path for the Dockerfile
     */
    private String getDestEnvFilePath() {
        return deployDirLocalPath + "/Dockerfile";
    }

    /**
     * Returns the path of the Dockerfile template located in the Vaadin project directory.
     *
     * @return the source path of the Dockerfile template
     */
    private String getSrcEnvFilePath() {
        return vaadinProjectDirLocalPath + "/Dockerfile";
    }
}