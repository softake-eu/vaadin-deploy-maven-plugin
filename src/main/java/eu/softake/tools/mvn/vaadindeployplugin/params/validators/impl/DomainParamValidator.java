package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;

/**
 * Validator for the {@code server.domain} field in the {@link ServerParam} class.
 * <p>
 * Ensures that the domain is properly set and does not contain a protocol prefix
 * such as {@code http://} or {@code https://}. The domain should not be {@code null} or empty.
 * </p>
 */
public class DomainParamValidator implements ParamValidator<ServerParam> {

    /**
     * Error message template used when an invalid domain is encountered.
     */
    private static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration! 'server.domain' must be properly set. " +
            "For example: 'www.softake.eu'. Current value is `%s`";

    /**
     * Validates the {@code server.domain} field in the given {@link ServerParam}.
     * <p>
     * The domain must not be {@code null}, empty, or contain an HTTP/HTTPS prefix.
     * If any of these conditions are met, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param serverParam the {@link ServerParam} object to validate
     * @throws IllegalArgumentException if the {@code server.domain} is invalid
     */
    @Override
    public void validate(ServerParam serverParam) throws IllegalArgumentException {
        final String domain = serverParam.getDomain();

        if (domain == null
                || domain.isEmpty()
                || domain.startsWith("http://")
                || domain.startsWith("https://")
        ) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEMPLATE, domain));
        }
    }
}