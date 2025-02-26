package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;

/**
 * Validator for the applications data directory configuration in the deployment plugin.
 * <p>
 * This validator ensures that the specified applications data directory path starts with a forward slash (`/`),
 * which is required for Unix-based absolute paths.
 * </p>
 * <p>
 * If the applications data directory does not follow the expected format, an exception is thrown
 * via the parent class {@link AbstractFolderConfigurationValidator}.
 * </p>
 */
public class AppsDataDirParamValidator extends AbstractFolderConfigurationValidator {

    /**
     * Error message template for invalid applications data directory paths.
     */
    private static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration! 'server.appsDataDir' must be set " +
            "and start with '/'. For example: '/opt/apps_data'. Current value is `%s`";

    /**
     * Retrieves the applications data directory path from the {@link ServerParam} object.
     * <p>
     * This method is used by the parent class to perform validation.
     * </p>
     *
     * @param serverParam the {@link ServerParam} containing the applications data directory path.
     * @return the applications data directory path as a string.
     */
    @Override
    public String getDirPath(ServerParam serverParam) {
        return serverParam.getAppsDataDir();
    }

    /**
     * Builds an exception message with the provided invalid directory value.
     *
     * @param appsDataDir the invalid applications data directory value.
     * @return a formatted exception message.
     */
    public String buildExceptionMessage(String appsDataDir) {
        return String.format(EXCEPTION_TEMPLATE, appsDataDir);
    }
}