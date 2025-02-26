package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * Handler that checks if Docker Compose is installed and has a valid version on the server.
 * If Docker Compose is not installed or the version is invalid, an exception is thrown.
 */
@AllArgsConstructor
@Slf4j
public class DockerComposeVersionCheckHandler implements ChainStepHandler<ServerParam> {

    // Constants
    private static final String EXCEPTION_TEMPLATE = "Docker Compose is not installed or isn't available on the server. " +
            "The command 'docker compose version' has returned the following result: `%s`";
    private static final String regex = "Docker Compose version v\\d+\\.\\d+\\.\\d+\\s*";

    // Service
    private final SshService sshService;

    /**
     * Provides a description of the step: checking whether Docker Compose is installed on the server.
     *
     * @param dataObj the server configuration object containing deployment parameters
     * @return the description of the step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return "Check whether docker compose is installed on the server...";
    }

    /**
     * Handles the check for Docker Compose installation and version.
     * If Docker Compose is not installed or the version is invalid, an exception is thrown.
     *
     * @param serverModel the server configuration object containing deployment parameters
     * @throws IllegalStateException if Docker Compose is not installed or the version is invalid
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {

        final String execResult = sshService.execCommand("docker compose version");
        final boolean correctDockerVersion = Pattern.matches(regex, execResult);

        if (!correctDockerVersion) {
            throw new IllegalStateException(String.format(EXCEPTION_TEMPLATE, execResult));
        }
    }
}