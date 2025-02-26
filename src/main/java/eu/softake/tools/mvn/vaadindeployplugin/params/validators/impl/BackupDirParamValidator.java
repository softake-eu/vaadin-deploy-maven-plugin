package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;

/**
 * Validator for the backup directory configuration in the deployment plugin.
 * <p>
 * This validator ensures that the specified backup directory path starts with a forward slash (`/`),
 * which is required for Unix-based absolute paths.
 * </p>
 * <p>
 * If the backup directory does not follow the expected format, an {@link IllegalArgumentException} is thrown.
 * </p>
 */
public class BackupDirParamValidator implements ParamValidator<ServerParam> {

    /**
     * Error message template for invalid backup directory paths.
     */
    private static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration! Value of 'server.backupDir' must start with '/'. " +
            "For example: '/opt/backup'. Current value is `%s`";

    /**
     * Validates the backup directory path in the given {@link ServerParam} object.
     * <p>
     * Ensures that if a backup directory is specified, it must start with a forward slash (`/`).
     * If the validation fails, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param serverParam the {@link ServerParam} containing the backup directory path.
     * @throws IllegalArgumentException if the backup directory is not correctly formatted.
     */
    @Override
    public void validate(ServerParam serverParam) throws IllegalArgumentException {
        final String backupDir = serverParam.getBackupDir();

        if (backupDir != null && !backupDir.trim().startsWith("/")) {
            throw new IllegalArgumentException(buildExceptionMessage(backupDir));
        }
    }

    /**
     * Builds an exception message with the provided backup directory value.
     *
     * @param backupDir the invalid backup directory value.
     * @return a formatted exception message.
     */
    private String buildExceptionMessage(String backupDir) {
        return String.format(EXCEPTION_TEMPLATE, backupDir);
    }
}