package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsible for creating a backup of the deployment directory on the server.
 * It checks if the deployment directory exists, and if so, it copies it to the backup directory.
 * If the backup directory is not configured, the backup step is skipped.
 */
@AllArgsConstructor
@Slf4j
public class DockerComposeBackupHandler implements ChainStepHandler<ServerParam> {

    // Constants
    public static final String CMD_COPY_TEMPLATE = "cp -r %s %s";  // Command to copy files recursively

    // Services
    private final SshService sshService;

    // Data
    private final String serverDeployDirPath;  // Path to the deployment directory on the server
    private final String serverBackupDirPath;  // Path to the backup directory on the server

    /**
     * Provides a description of the backup step, including the source and destination directories.
     *
     * @param dataObj the server configuration object
     * @return the description of the backup step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return String.format("Backup dir on server from `%s` to `%s`", serverDeployDirPath, serverBackupDirPath);
    }

    /**
     * Handles the backup of the deployment directory on the server.
     * If the deployment directory exists and the backup directory is configured,
     * it copies the deployment directory to the backup directory.
     *
     * @param serverModel the server configuration object
     * @throws Exception if an error occurs while executing the backup command
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {

        // Check if backup directory is configured
        if (serverBackupDirPath != null) {

            // Make sure the deployment directory exists on the server
            if (sshService.isFileExist(serverDeployDirPath)) {
                // Ensure the backup directory exists
                sshService.ensureDirExists(serverBackupDirPath);

                // Copy the deployment directory to the backup directory
                final String copyCommand = String.format(CMD_COPY_TEMPLATE, serverDeployDirPath, serverBackupDirPath);
                sshService.execCommand(copyCommand);
            } else {
                // Log warning if deployment directory doesn't exist
                log.warn("Backup won't be taken for the deployment dir (`{}`), because it doesn't exist on the server `{}`", serverDeployDirPath, serverModel.getDomain());
            }
        } else {
            // Log warning if the backup directory is not configured
            log.warn("The backup dir isn't configured ('server.backupDir') for the server `{}`. Skipping the backup step", serverModel.getDomain());
        }
    }
}