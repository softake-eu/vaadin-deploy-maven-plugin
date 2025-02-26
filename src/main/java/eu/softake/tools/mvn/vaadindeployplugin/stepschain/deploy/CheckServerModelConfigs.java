package eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;

import java.util.Locale;

/**
 * Handler that checks the validity of the plugin configuration for the provided server model.
 * Specifically, it verifies that the server domain is correctly set and not configured to "localhost" or "127.0.0.1".
 *
 * <p>If the domain is invalid (i.e., it is not set or is set to localhost/127.0.0.1), an {@link IllegalStateException}
 * is thrown with an appropriate error message.</p>
 *
 * <p>This step helps ensure that the server's domain is correctly configured, which is crucial for
 * proper deployment and communication between systems.</p>
 */
public class CheckServerModelConfigs implements ChainStepHandler<ServerParam> {

    /**
     * Provides a description of the step that will be executed in the chain.
     *
     * @param serverModel the server model containing the server configuration.
     * @return a string describing the step, including the server domain.
     */
    @Override
    public String getStepDescription(ServerParam serverModel) {
        return String.format("Check plugin settings for `%s`", serverModel.getDomain());
    }

    /**
     * Validates the server domain configuration in the server model. If the domain is not set or is set
     * to "localhost" or "127.0.0.1", an exception is thrown.
     *
     * @param serverModel the server model containing the server configuration.
     * @throws IllegalStateException if the domain is not properly configured.
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        String domain = serverModel.getDomain();
        if (domain == null
                || "localhost".equals(domain.toLowerCase(Locale.ROOT).trim())
                || "127.0.0.1".equals(domain.toLowerCase(Locale.ROOT).trim())) {
            throw new IllegalStateException("Incorrect plugin configuration! server.domain must be properly set. For example: 'www.softake.eu'");
        }
    }
}