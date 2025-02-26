package eu.softake.tools.mvn.vaadindeployplugin.params.providers;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides server-related file system paths for deployment.
 * This class generates paths based on the given server parameters.
 */
@Getter
public class ServerPathProvider {

    /** Formatter for naming backup directories with timestamped folder names. */
    private static final DateTimeFormatter folderNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /** The directory where the application will be deployed on the server. */
    private final String deployDir;

    /** The directory where application-specific data is stored on the server. */
    private final String appsDataDir;

    /** The path to the Docker Compose configuration file within the deployment directory. */
    private final String dockerComposeFile;

    /** The directory where application backups will be stored, with a timestamped subdirectory. */
    private final String backupDir;

    /** The target directory within the deployment directory where compiled artifacts are stored. */
    private final String targetDir;

    /**
     * Constructs a ServerPathProvider instance using the provided server parameters.
     *
     * @param serverParam the server parameters containing deployment configurations
     */
    public ServerPathProvider(ServerParam serverParam) {
        this.deployDir = serverParam.getDeployDir();
        this.appsDataDir = serverParam.getAppsDataDir();
        this.dockerComposeFile = this.deployDir + "/docker-compose.yaml";
        this.targetDir = this.deployDir + "/target";

        if (serverParam.getBackupDir() != null) {
            this.backupDir = serverParam.getBackupDir() +
                    "/" + folderNameFormatter.format(LocalDateTime.now());
        } else {
            this.backupDir = null;
        }
    }
}