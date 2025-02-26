package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.SshConnectionParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;
import lombok.extern.slf4j.Slf4j;

/**
 * Validator for the {@link SshConnectionParam} in the context of {@link ServerParam}.
 * <p>
 * This class validates the configuration of the {@code sshConnection} property within a {@code ServerParam}.
 * It ensures that the {@code sshConnection} property is set only for servers that are not of type {@code LOCAL}.
 * If the server type is {@code LOCAL}, the {@code sshConnection} property should not be set.
 * </p>
 * <p>
 * Additionally, a warning is logged if the {@code sshConnection} property is incorrectly set for a {@code LOCAL} server.
 * </p>
 */
@Slf4j
public class SshConnectionParamValidator implements ParamValidator<ServerParam> {

    private static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration for server: `%s`! "
            + "If 'server.type' is not LOCAL then sshConnection must be set!";

    /**
     * Validates the {@code ServerParam} to ensure the correct configuration of the {@code sshConnection} property.
     * <p>
     * If the {@code ServerTypeParam} is not {@code LOCAL}, the {@code sshConnection} must be set.
     * If the server type is {@code LOCAL}, the {@code sshConnection} should not be set, and a warning is logged.
     * </p>
     *
     * @param serverParam the {@link ServerParam} object to validate
     * @throws IllegalArgumentException if the configuration is incorrect
     */
    @Override
    public void validate(ServerParam serverParam) throws IllegalArgumentException {
        final ServerTypeParam serverType = serverParam.getType();
        final SshConnectionParam sshConnection = serverParam.getSshConnection();

        if (!ServerTypeParam.LOCAL.equals(serverType) && sshConnection == null) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEMPLATE, serverParam.getDomain()));
        }

        if (ServerTypeParam.LOCAL.equals(serverType) && sshConnection != null) {
            log.warn("'sshConnection' property is redundant for servers of type 'LOCAL'. It doesn't affect functionality,"
                    + "but the configuration is incorrect. Remove 'sshConnection' from `{}` server!", serverParam.getDomain());
        }
    }
}