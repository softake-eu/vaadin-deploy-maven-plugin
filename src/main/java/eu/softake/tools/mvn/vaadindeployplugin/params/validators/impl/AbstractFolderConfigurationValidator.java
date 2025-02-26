package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;

/**
 * Abstract base class for folder configuration validators.
 * <p>
 * This class provides a common validation mechanism for parameters that represent directory paths.
 * The directory path must be specified and start with a forward slash (`/`) to indicate a valid absolute path.
 * </p>
 * <p>
 * Concrete subclasses must implement the {@link #getDirPath(ServerParam)} method to retrieve the directory
 * path from the {@link ServerParam}, and the {@link #buildExceptionMessage(String)} method to create
 * a custom exception message when validation fails.
 * </p>
 */
public abstract class AbstractFolderConfigurationValidator implements ParamValidator<ServerParam> {

    /**
     * Validates the directory path of a given {@link ServerParam}.
     * <p>
     * This method checks whether the directory path is non-null, non-empty, and starts with a forward slash (`/`).
     * If any of these conditions is violated, an {@link IllegalArgumentException} is thrown with a custom message.
     * </p>
     *
     * @param obj the {@link ServerParam} object containing the folder configuration to be validated.
     * @throws IllegalArgumentException if the directory path is invalid.
     */
    @Override
    public void validate(ServerParam obj) throws IllegalArgumentException {
        final String dirPath = getDirPath(obj);

        if (dirPath == null || !dirPath.trim().startsWith("/")) {
            throw new IllegalArgumentException(buildExceptionMessage(dirPath));
        }
    }

    /**
     * Retrieves the directory path from the given {@link ServerParam}.
     * <p>
     * This method must be implemented by subclasses to extract the specific directory path from
     * the {@link ServerParam} object.
     * </p>
     *
     * @param serverParam the {@link ServerParam} object containing the directory path.
     * @return the directory path as a string.
     */
    public abstract String getDirPath(ServerParam serverParam);

    /**
     * Builds an exception message with the provided invalid directory path.
     * <p>
     * This method must be implemented by subclasses to return a custom error message specific
     * to the folder being validated.
     * </p>
     *
     * @param folderPath the invalid folder path.
     * @return a formatted exception message indicating the error.
     */
    public abstract String buildExceptionMessage(String folderPath);
}