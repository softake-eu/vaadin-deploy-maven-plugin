package eu.softake.tools.mvn.vaadindeployplugin;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.PluginParamsValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.providers.LocalPathProvider;
import eu.softake.tools.mvn.vaadindeployplugin.service.JarFileService;
import eu.softake.tools.mvn.vaadindeployplugin.service.LocalFileService;
import eu.softake.tools.mvn.vaadindeployplugin.service.PlaceholderReplacer;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainExecutor;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CheckDockerfileExistsHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CreateAppsDataHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CreateConfigsHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CreateDockerComposeFileHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CreateDockerFileHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CreateEnvFileHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.SubstitutePlaceholdersHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

/**
 * Maven plugin Mojo for initializing a Vaadin project deployment.
 * This class handles the initialization of server-specific configurations,
 * including Dockerfile, environment files, Docker Compose, application data,
 * and placeholder substitution.
 */
@Slf4j
@Mojo(name = "init")
public class InitProjectMojo extends AbstractMojo {

    // Services
    private final JarFileService jarFileService = new JarFileService();
    private final LocalFileService localFileService = new LocalFileService();
    private final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer();

    // Plugin configs

    /** The base directory of the project. */
    @Parameter(property = "projectBaseDir", defaultValue = "${project.basedir}", required = true)
    private File projectBaseDir;

    /** The directory containing the Vaadin project. */
    @Parameter(property = "vaadinProjectDir", defaultValue = "${project.basedir}", required = true)
    private File vaadinProjectDir;

    /** The artifact ID of the project. */
    @Parameter(property = "artifactId", defaultValue = "${project.artifactId}", required = true)
    private String artifactId;

    /** The email used for Certbot SSL certificate generation. */
    @Parameter(property = "certbotEmail", required = true)
    private String certbotEmail;

    /** A list of server parameters for each deployment target. */
    @Parameter(property = "servers", required = true)
    private List<ServerParam> servers;

    /**
     * Executes the Mojo to initialize the project deployment for each server configuration.
     *
     * @throws MojoExecutionException if an unexpected error occurs during execution
     * @throws MojoFailureException   if the plugin fails to complete successfully
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Validate plugin parameters
        PluginParamsValidator.validate(certbotEmail, servers);

        for (ServerParam serverModel : servers) {

            final LocalPathProvider localPathProvider = new LocalPathProvider(serverModel,
                    projectBaseDir.getAbsolutePath(), vaadinProjectDir.getAbsolutePath());

            if (localPathProvider.exists()) {
                log.warn("Skipping server `{}` because it has been already initialized.", serverModel.getDomain());
                continue;
            }

            log.info("***********************************************");
            log.info("Processing server: `{}`", serverModel.getDomain());
            log.info("***********************************************");

            try {
                // Initialize project setup chain
                ChainExecutor<ServerParam> initProjectFlow = new ChainExecutor<>(
                        new CheckDockerfileExistsHandler(localPathProvider.getVaadinProjectDir()),
                        new CreateEnvFileHandler(jarFileService, localPathProvider.getDeployDir()),
                        new CreateDockerComposeFileHandler(jarFileService, localPathProvider.getDeployDir()),
                        new CreateConfigsHandler(jarFileService, localPathProvider.getDeployDir()),
                        new CreateAppsDataHandler(jarFileService, localPathProvider.getAppsDataDir()),
                        new CreateDockerFileHandler(localFileService, localPathProvider.getDeployDir(), localPathProvider.getVaadinProjectDir()),
                        new SubstitutePlaceholdersHandler(placeholderReplacer,
                                jarFileService,
                                localPathProvider.getVaadinProjectDir(),
                                localPathProvider.getDeployDir(),
                                localPathProvider.getAppsDataDir(),
                                certbotEmail,
                                artifactId)
                );

                // Start the initialization flow
                initProjectFlow.start(serverModel);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new MojoExecutionException(e);
            }
        }
    }
}