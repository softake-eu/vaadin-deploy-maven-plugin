package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;

/**
 * Validator for the {@link ServerTypeParam} in the context of {@link ServerParam}.
 * <p>
 * This class ensures that the {@code server.type} field in a {@link ServerParam} is correctly set.
 * The server type must not be {@code null} and should be one of the predefined values:
 * {@code LOCAL, TEST, QA, STG, PROD}.
 * </p>
 */
public class ServerTypeParamValidator implements ParamValidator<ServerParam> {

    /**
     * Error message template used when an invalid server type is encountered.
     */
    public static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration for server: `%s`! " +
            "Select one of the constraints [LOCAL, TEST, QA, STG, PROD] for 'server.type' field.";

    /**
     * Validates that the {@code server.type} field in the given {@link ServerParam} is not null.
     * <p>
     * If the {@code server.type} is missing, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param serverParam the {@link ServerParam} object to validate
     * @throws IllegalArgumentException if the {@code server.type} is null
     */
    @Override
    public void validate(ServerParam serverParam) throws IllegalArgumentException {
        final ServerTypeParam serverType = serverParam.getType();

        if (serverType == null) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEMPLATE, serverParam.getDomain()));
        }
    }
}