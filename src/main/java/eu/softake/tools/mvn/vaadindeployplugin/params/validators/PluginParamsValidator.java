package eu.softake.tools.mvn.vaadindeployplugin.params.validators;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.AppsDataDirParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.BackupDirParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.CertbotEmailParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.DatabaseProviderParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.DeployDirParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.DomainParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.SshConnectionParamValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl.ServerTypeParamValidator;
import lombok.AllArgsConstructor;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator class responsible for validating the plugin parameters.
 * <p>
 * The class ensures that various server and configuration parameters, such as domain,
 * deployment directories, database provider, etc., are correctly set and conform to required rules.
 * </p>
 */
@AllArgsConstructor
public class PluginParamsValidator {

    // Validator for Certbot email
    private static final CertbotEmailParamValidator certbotEmailValidator = new CertbotEmailParamValidator();

    // List of validators for ServerParam
    private static final List<ParamValidator<ServerParam>> serverParamValidators =
            new ArrayList<ParamValidator<ServerParam>>() {{
                add(new DomainParamValidator());
                add(new ServerTypeParamValidator());
                add(new SshConnectionParamValidator());
                add(new DeployDirParamValidator());
                add(new AppsDataDirParamValidator());
                add(new BackupDirParamValidator());
                add(new DatabaseProviderParamValidator());
            }};

    /**
     * Validates the Certbot email and server parameters.
     * <p>
     * The method first validates the Certbot email, then iterates through the provided
     * list of servers and validates each server's parameters using a set of pre-defined validators.
     * </p>
     *
     * @param certbotEmail the email to be validated for Certbot usage
     * @param servers the list of servers to be validated
     * @throws MojoExecutionException if any validation fails
     */
    public static void validate(String certbotEmail, List<ServerParam> servers) throws MojoExecutionException {
        // Validate Certbot email
        certbotEmailValidator.validate(certbotEmail);

        // Validate each server's parameters
        for (ServerParam server : servers) {
            for (ParamValidator<ServerParam> validator : serverParamValidators) {
                validator.validate(server);
            }
        }
    }
}