package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.LocalFileService;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * Handler responsible for copying the compiled package (e.g., JAR file) from the local machine to the remote server.
 * It retrieves files with the specified packaging type from the local target directory and copies them to the specified
 * remote target directory on the server.
 */
@AllArgsConstructor
@Slf4j
public class CopyTargetJarToServerHandler implements ChainStepHandler<ServerParam> {

    // Services
    private final SshService sshService;  // Service for SSH communication with the server
    private final LocalFileService localFileService;  // Service to interact with local files

    // Data
    private final String packaging;  // The packaging type (e.g., "jar")
    private final String remoteTargetDir;  // The target directory on the remote server
    private final String localTargetDir;  // The source directory on the local machine

    /**
     * Provides the description of the step, including the packaging type and local target directory.
     *
     * @param dataObj the server configuration object
     * @return the description of the copy step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return String.format("Copy the compiled package (`%s`) from `%s`", packaging, localTargetDir);
    }

    /**
     * Handles the process of copying the compiled package from the local machine to the remote server.
     * It looks for files in the local target directory that match the packaging type (e.g., `.jar`)
     * and copies them to the remote target directory on the server.
     *
     * @param serverModel the server configuration object
     * @throws Exception if an error occurs during the file copy process
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        // Get the list of files from the local directory that match the packaging type (e.g., "*.jar")
        List<File> children = localFileService.getChildren(new File(localTargetDir), ".*\\." + packaging + "$");

        // Copy each file found to the remote server
        for (File childFile : children) {
            sshService.copyFileToServer(childFile, remoteTargetDir);
        }
    }
}