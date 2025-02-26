package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Handler that starts Docker Compose services by executing the 'docker compose up' command on the server.
 * If the Docker Compose file doesn't exist on the server, an exception is thrown.
 */
@AllArgsConstructor
@Slf4j
public class DockerComposeStartHandler implements ChainStepHandler<ServerParam> {

    // Constants
    public static final String CMD_DOCKER_UP_TEMPLATE = "docker compose -f %s up -d --build";
    public static final String EXCEPTION_TEMPLATE = "Something went wrong! File `%s` doesn't exist on the server.";

    // Service
    private final SshService sshService;

    // Data
    private final String dockerComposeFileRemotePath;

    /**
     * Provides a description of the step: starting Docker Compose services on the server.
     *
     * @param dataObj the server configuration object containing deployment parameters
     * @return the description of the step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return "Start DockerCompose...";
    }

    /**
     * Handles the starting of Docker Compose services.
     * It checks if the Docker Compose file exists on the server and executes the 'docker compose up' command if the file is present.
     * If the file doesn't exist, an exception is thrown.
     * It also adds a 2-second sleep to ensure that any previous operation, such as uploading a JAR file, has completed fully.
     *
     * @param serverModel the server configuration object containing deployment parameters
     * @throws Exception if an error occurs during the execution of the command or if the Docker Compose file doesn't exist
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        // Workaround to prevent JAR file corruption if 'docker compose up' is executed immediately after uploading
        log.info("Sleep for 2 sec to allow the previous operation finish completely");
        Thread.sleep(2000); // Sleep for 2 seconds to ensure previous operation completes
        log.info("Resume thread");

        boolean fileExist = sshService.isFileExist(dockerComposeFileRemotePath);
        if (fileExist) {
            final String dockerComposeUpCommand = String.format(CMD_DOCKER_UP_TEMPLATE, dockerComposeFileRemotePath);
            sshService.execCommand(dockerComposeUpCommand);
        } else {
            throw new MojoExecutionException(String.format(EXCEPTION_TEMPLATE, dockerComposeFileRemotePath));
        }
    }
}