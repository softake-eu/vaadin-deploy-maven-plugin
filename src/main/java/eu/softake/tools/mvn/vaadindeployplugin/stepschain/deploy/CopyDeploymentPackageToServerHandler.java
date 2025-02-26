package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CopyDeploymentPackageToServerHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final SshService sshService;  // SSH service to interact with the remote server

    // Data
    private final String deployDirLocalPath;  // Local directory containing deployment files
    private final String deployDirRemotePath;  // Remote directory on the server where files will be copied

    /**
     * Provides a description of the step, including the target domain and hostname.
     *
     * @param dataObj the server configuration object
     * @return the description of the copy step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return String.format("Copy the deployment settings `%s` to the server `%s`...",
                dataObj.getDomain(),
                dataObj.getSshConnection().getHostName());
    }

    /**
     * Handles the process of copying the deployment package from the local machine to the remote server.
     *
     * @param serverModel the server configuration object
     * @throws Exception if an error occurs during the file copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        // Use SSH service to copy the deployment directory from the local path to the remote server
        sshService.copyDirToServer(deployDirLocalPath, deployDirRemotePath);
    }
}