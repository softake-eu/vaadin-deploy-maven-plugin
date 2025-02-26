package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Validator for the {@code server.database.provider} field in the {@link ServerParam} class.
 * Ensures that the provided database provider value is valid. The allowed database providers are:
 *  <ul>
 *     <li>{@code postgres}</li>
 *     <li>{@code mariadb}</li>
 *     <li>{@code nodatabase}</li>
 *  </ul>
 *  The value is case-insensitive and will be trimmed before validation.
 * <p>
 * If the database provider is not in the allowed list, an {@link IllegalArgumentException} is thrown.
 * </p>
 */
public class DatabaseProviderParamValidator implements ParamValidator<ServerParam> {

    /**
     * Error message template for invalid database provider configurations.
     */
    private static final String EXCEPTION_TEMPLATE = "Incorrect plugin configuration for server: `%s`! " +
            "If 'server.database.provider' must be one of the following: [`%s`]. The current value is `%s`";

    /**
     * Set of allowed database providers.
     */
    private final Set<String> allowedProviders = new HashSet<String>() {{
        add("postgres");
        add("mariadb");
        add("nodatabase");
    }};

    /**
     * Validates the database provider for the given {@link ServerParam}.
     * <p>
     * If the database provider is specified, it must be one of the allowed values.
     * If no provider is specified, it defaults to {@code nodatabase}.
     * </p>
     *
     * @param serverParam the {@link ServerParam} object containing the database provider configuration.
     * @throws IllegalArgumentException if the database provider is invalid.
     */
    @Override
    public void validate(ServerParam serverParam) throws IllegalArgumentException {
        String dbProvider = serverParam.getDbProvider();

        if (dbProvider != null) {
            dbProvider = dbProvider.toLowerCase(Locale.ROOT).trim();
            try {
                final Set<String> resourceChildrenNames = getAllowedProviders();
                if (!resourceChildrenNames.contains(dbProvider)) {
                    final String domain = serverParam.getDomain();
                    final String availableProviders = String.join(", ", resourceChildrenNames);

                    throw new IllegalArgumentException(String.format(EXCEPTION_TEMPLATE, domain, availableProviders, dbProvider));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } // If no database provider is specified, it defaults to "nodatabase".
    }

    /**
     * Retrieves the set of allowed database providers.
     * <p>
     * TODO: Replace hardcoded values with dynamic fetching from {@code resources/templates}.
     * </p>
     *
     * @return a {@link Set} of allowed database provider names.
     */
    public Set<String> getAllowedProviders() {
        return allowedProviders;
    }
}