package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.JarFileService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * A handler responsible for creating the `.env` file for the deployment process.
 * The file is created by copying from a template based on the selected database provider.
 */
@Slf4j
@AllArgsConstructor
public class CreateEnvFileHandler implements ChainStepHandler<ServerParam> {

    // Service for handling file operations
    private final JarFileService fileService;

    // Path to the local deployment directory
    private final String deployDirLocalPath;

    /**
     * Provides a description of the current step.
     *
     * @param serverModel the server configuration object
     * @return a string describing the creation of the `.env` file
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        final String destPath = getDestEnvFilePath();
        return String.format("Create '.env' file: `%s`", destPath);
    }

    /**
     * Handles the creation of the `.env` file by copying the appropriate template.
     * The template file is selected based on the database provider configured for the server.
     *
     * @param serverModel the server configuration object containing the database provider
     * @throws Exception if an error occurs during file copy
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        final String srcPath = getSrcEnvFilePath(serverModel);
        final String destPath = getDestEnvFilePath();

        fileService.copyFile(srcPath, destPath);
    }

    /**
     * Returns the path where the `.env` file will be created.
     *
     * @return the destination path for the `.env` file
     */
    private String getDestEnvFilePath() {
        return this.deployDirLocalPath + "/.env";
    }

    /**
     * Returns the path of the template `.env` file to be used based on the database provider.
     *
     * @param serverModel the server configuration object containing the database provider
     * @return the source path of the template `.env` file
     */
    private String getSrcEnvFilePath(ServerParam serverModel) {
        String dbProvider = serverModel.getDbProvider();

        if (dbProvider != null) {
            dbProvider = dbProvider.toLowerCase(Locale.ROOT);
        } else {
            dbProvider = "nodatabase";
        }
        return "templates/" + dbProvider + "/.env";
    }
}