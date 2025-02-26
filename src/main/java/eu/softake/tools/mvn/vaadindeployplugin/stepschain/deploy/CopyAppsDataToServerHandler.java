package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CopyAppsDataToServerHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final SshService sshService;  // SSH service for file and directory management on the server

    // Data
    private final String appsDataDirLocalPath;  // Local directory containing application data
    private final String appsDataDirRemotePath;  // Remote directory on the server to copy the data to

    /**
     * Provides a description of the step, which involves copying application data.
     *
     * @param dataObj the server configuration object
     * @return a description of the copy operation
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return String.format("Copy Apps Data dir from `%s` to the server `%s`...",
                dataObj.getDomain(),
                dataObj.getSshConnection().getHostName());
    }

    /**
     * Handles the process of copying the application data from the local machine to the remote server.
     * Before copying, it ensures that the target directory is either empty or ready to accept new data.
     *
     * @param serverModel the server configuration object
     * @throws Exception if any error occurs during the directory copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        // Ensure the remote directory exists before proceeding
        sshService.ensureDirExists(appsDataDirRemotePath);

        // Check if the remote directory is empty
        final boolean isDirEmpty = sshService.isDirEmpty(appsDataDirRemotePath);

        if (!isDirEmpty) {
            // If the directory is not empty, skip the copying operation
            final String domain = serverModel.getDomain();
            log.warn("Apps Data dir `{}:{}` is NOT empty. Skip copying local folder `{}`!", domain, appsDataDirRemotePath, appsDataDirLocalPath);
            return;
        }

        // If the directory is empty, copy the data from the local machine to the server
        sshService.copyDirToServer(appsDataDirLocalPath, appsDataDirRemotePath);
    }
}