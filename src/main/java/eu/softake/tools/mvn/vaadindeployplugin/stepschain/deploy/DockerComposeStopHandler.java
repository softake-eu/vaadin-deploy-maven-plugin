package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler that stops the Docker Compose services by executing the 'docker compose down' command on the server.
 * If the Docker Compose file doesn't exist on the server, the operation is skipped.
 */
@AllArgsConstructor
@Slf4j
public class DockerComposeStopHandler implements ChainStepHandler<ServerParam> {

    // Constants
    public static final String CMD_DOCKER_DOWN_TEMPLATE = "docker compose -f %s down -v";

    // Service
    private final SshService sshService;
    private final String dockerComposeFilePath;

    /**
     * Provides a description of the step: stopping the Docker Compose services on the server.
     *
     * @param dataObj the server configuration object containing deployment parameters
     * @return the description of the step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return "Stop DockerCompose...";
    }

    /**
     * Handles the stopping of Docker Compose services.
     * It checks if the Docker Compose file exists on the server and executes the 'docker compose down' command if the file is present.
     * If the file doesn't exist, the step is skipped.
     *
     * @param serverModel the server configuration object containing deployment parameters
     * @throws Exception if an error occurs during the execution of the command
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {

        final boolean fileExist = sshService.isFileExist(dockerComposeFilePath);

        if (fileExist) {
            final String dockerDownCommand = String.format(CMD_DOCKER_DOWN_TEMPLATE, dockerComposeFilePath);
            sshService.execCommand(dockerDownCommand);
        } else {
            log.info("DockerCompose file doesn't exist on the server. No need to stop");
        }
    }
}