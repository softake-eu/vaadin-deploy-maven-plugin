package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;

/**
 * Validator for the {@code server.deployDir} field in the {@link ServerParam} class.
 * <p>
 * Ensures that the deployment directory is properly configured and follows the required format.
 * The directory path must not be {@code null} and must start with {@code '/'}.
 * </p>
 * <p>
 * Example of a valid deployment directory: {@code /opt/vaadin-app}.
 * </p>
 */
public class DeployDirParamValidator extends AbstractFolderConfigurationValidator {

    /**
     * Error message template used when an invalid deployment directory is encountered.
     */
    private static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration! 'server.deployDir' must be set and start with '/'. " +
            "For example: '/opt/vaadin-app'. Current value is `%s`";

    /**
     * Retrieves the deployment directory path from the given {@link ServerParam}.
     *
     * @param serverParam the {@link ServerParam} object containing the deployment directory
     * @return the deployment directory path
     */
    @Override
    public String getDirPath(ServerParam serverParam) {
        return serverParam.getDeployDir();
    }

    /**
     * Builds an error message indicating an incorrect deployment directory configuration.
     *
     * @param deployDir the invalid deployment directory value
     * @return a formatted error message describing the issue
     */
    public String buildExceptionMessage(String deployDir) {
        return String.format(EXCEPTION_TEMPLATE, deployDir);
    }
}