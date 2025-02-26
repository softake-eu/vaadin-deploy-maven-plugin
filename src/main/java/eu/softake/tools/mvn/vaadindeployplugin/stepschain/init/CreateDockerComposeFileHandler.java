package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.JarFileService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * Handler that creates the 'docker-compose.yaml' file for deploying the application. It copies the appropriate
 * template based on the database provider from the templates directory to the destination deployment directory.
 */
@Slf4j
@AllArgsConstructor
public class CreateDockerComposeFileHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final JarFileService fileService;

    // Data
    private final String deployDirLocalPath;

    /**
     * Provides a description of the step: creating the 'docker-compose.yaml' file.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @return the description of the step, including the destination path of the 'docker-compose.yaml' file
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        final String destPath = getDestDockerComposePath();
        return String.format("Create 'docker-compose.yaml':`%s`", destPath);
    }

    /**
     * Handles the creation of the 'docker-compose.yaml' file by copying the appropriate template based on the
     * database provider from the templates directory to the destination deployment directory.
     *
     * @param serverModel the server configuration containing deployment parameters
     * @throws Exception if an error occurs during the file copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        final String srcPath = getSrcDockerComposePath(serverModel);
        final String destPath = getDestDockerComposePath();
        fileService.copyFile(srcPath, destPath);
    }

    /**
     * Returns the path where the 'docker-compose.yaml' file will be created in the deployment directory.
     *
     * @return the destination path for the 'docker-compose.yaml' file
     */
    private String getDestDockerComposePath() {
        return deployDirLocalPath + "/docker-compose.yaml";
    }

    /**
     * Returns the path of the 'docker-compose.yaml' template located in the templates directory based on the
     * database provider specified in the server configuration.
     *
     * @param serverModel the server configuration containing the database provider
     * @return the source path of the 'docker-compose.yaml' template
     */
    private String getSrcDockerComposePath(ServerParam serverModel) {
        String dbProvider = serverModel.getDbProvider();
        if (dbProvider != null) {
            dbProvider = dbProvider.toLowerCase(Locale.ROOT);
        } else {
            dbProvider = "nodatabase";
        }
        return "templates/" + dbProvider + "/docker-compose.yaml";
    }
}