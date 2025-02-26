package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.SshConnectionParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler that checks the validity of the SSH session before proceeding with the deployment process.
 * This ensures that the SSH connection is properly established with the remote server.
 *
 * <p>The handler will validate the SSH session by calling the {@link SshService#isSshSessionValid()} method.
 * If the SSH session is invalid, an {@link IllegalStateException} is thrown with a specific message.</p>
 *
 * <p>This step helps ensure that the SSH connection is working properly before performing any deployment
 * tasks that require SSH access to the target server, such as copying files or executing commands.</p>
 */
@AllArgsConstructor
@Slf4j
public class CheckSshSessionHandler implements ChainStepHandler<ServerParam> {

    // Constants
    /**
     * The error message template that is used when the SSH session is invalid.
     */
    public static final String EXCEPTION_TEMPLATE = "Ssh configurations are incorrect. Please double check...";

    // Services
    /**
     * The service used to check the validity of the SSH session.
     */
    private final SshService sshService;

    /**
     * Provides a description of the step that will be executed in the chain.
     *
     * @param serverModel the server model containing the SSH connection details.
     * @return a string describing the step, including the SSH user and hostname.
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        final SshConnectionParam sshConnection = serverModel.getSshConnection();
        return String.format("Check SSH session: `%s@%s`...", sshConnection.getUser(), sshConnection.getHostName());
    }

    /**
     * Handles the SSH session check by verifying the validity of the SSH connection.
     *
     * @param serverModel the server model containing the SSH connection details.
     * @throws IllegalStateException if the SSH session is invalid.
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        final boolean isSshSessionValid = sshService.isSshSessionValid();

        if (!isSshSessionValid) {
            throw new IllegalStateException(EXCEPTION_TEMPLATE);
        }
    }
}