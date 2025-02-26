package eu.softake.tools.mvn.vaadindeployplugin;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.validators.PluginParamsValidator;
import eu.softake.tools.mvn.vaadindeployplugin.params.providers.LocalPathProvider;
import eu.softake.tools.mvn.vaadindeployplugin.params.providers.ServerPathProvider;
import eu.softake.tools.mvn.vaadindeployplugin.service.LocalFileService;
import eu.softake.tools.mvn.vaadindeployplugin.service.SshService;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainExecutor;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.CheckBuiltJarFileExistsHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.CopyAppsDataToServerHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.CopyDeploymentPackageToServerHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.CopyTargetJarToServerHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.DockerComposeBackupHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.DockerComposeStartHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.DockerComposeStopHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.deploy.DockerComposeVersionCheckHandler;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.init.CheckDockerfileExistsHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class representing a Maven Mojo for deploying Vaadin applications.
 * This class provides the deployment logic for applications running inside Docker Compose environments.
 * <p>
 * The deployment process includes:
 * <ul>
 *     <li>Validating plugin parameters</li>
 *     <li>Filtering servers by type</li>
 *     <li>Executing deployment workflows via SSH</li>
 *     <li>Handling Docker Compose stop, backup, and start operations</li>
 * </ul>
 * <p>
 * Subclasses must implement {@link #getServerTypeFilter()} to specify the type of servers to deploy to.
 */
@Slf4j
public abstract class AbstractDeployMojo extends AbstractMojo {

    /** Service for handling local file operations. */
    protected final LocalFileService localFileService = new LocalFileService();

    /** The base directory of the Maven project. */
    @Parameter(property = "projectBaseDir", defaultValue = "${project.basedir}")
    protected File projectBaseDir;

    /** The root directory of the Vaadin project. */
    @Parameter(property = "vaadinProjectDir", defaultValue = "${project.basedir}")
    protected File vaadinProjectDir;

    /** List of server configurations for deployment. */
    @Parameter(property = "servers")
    protected List<ServerParam> servers;

    /** Packaging type of the project (e.g., "jar"). */
    @Parameter(property = "packaging", defaultValue = "${project.packaging}")
    protected String packaging;

    /** Email address for Certbot SSL certificate registration. */
    @Parameter(property = "certbotEmail", required = true)
    private String certbotEmail;

    /**
     * Executes the deployment process.
     *
     * @throws MojoExecutionException if an error occurs during deployment.
     * @throws MojoFailureException   if the deployment fails due to a configuration issue.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        PluginParamsValidator.validate(certbotEmail, servers);

        // Find servers by type
        final ServerTypeParam serverTypeFilter = getServerTypeFilter();
        final List<ServerParam> servers = this.servers.stream()
                .filter(serverModel -> serverTypeFilter.equals(serverModel.getType()))
                .collect(Collectors.toList());

        if (servers.isEmpty()) {
            log.warn("No configurations are found for `{}` server type. Skipping deployment process", serverTypeFilter);
            return;
        }

        for (ServerParam server : servers) {
            log.info("***********************************************");
            log.info("Processing server: `{}`", server.getDomain());
            log.info("***********************************************");
            try (SshService sshService = new SshService(server.getSshConnection())) {
                ChainExecutor<ServerParam> deployWorkflow = initWorkflow(sshService, server);
                deployWorkflow.start(server);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new MojoExecutionException(e);
            }
        }
    }

    /**
     * Initializes the deployment workflow for a given server.
     * The workflow includes various deployment steps such as checking files, stopping Docker Compose, backing up data,
     * transferring files, and restarting Docker Compose.
     *
     * @param sshService  the SSH service for remote command execution.
     * @param serverParam the server parameters.
     * @return a {@link ChainExecutor} instance representing the deployment workflow.
     */
    protected ChainExecutor<ServerParam> initWorkflow(SshService sshService, ServerParam serverParam) {
        final LocalPathProvider localPathProvider = new LocalPathProvider(serverParam,
                projectBaseDir.getAbsolutePath(), vaadinProjectDir.getAbsolutePath());
        final ServerPathProvider serverPathProvider = new ServerPathProvider(serverParam);

        return new ChainExecutor<>(
                new CheckDockerfileExistsHandler(localPathProvider.getDeployDir()),
                new CheckBuiltJarFileExistsHandler(localPathProvider.getTargetDir(), packaging, localFileService),
                new DockerComposeVersionCheckHandler(sshService),
                new DockerComposeStopHandler(sshService, serverPathProvider.getDockerComposeFile()),
                new DockerComposeBackupHandler(sshService, serverPathProvider.getDeployDir(), serverPathProvider.getBackupDir()),
                new CopyDeploymentPackageToServerHandler(sshService, localPathProvider.getDeployDir(), serverPathProvider.getDeployDir()),
                new CopyAppsDataToServerHandler(sshService, localPathProvider.getAppsDataDir(), serverPathProvider.getAppsDataDir()),
                new CopyTargetJarToServerHandler(sshService, localFileService, packaging, serverPathProvider.getTargetDir(), localPathProvider.getTargetDir()),
                new DockerComposeStartHandler(sshService, serverPathProvider.getDockerComposeFile())
        );
    }

    /**
     * Gets the server type filter used to select which servers should be deployed.
     *
     * @return the server type filter.
     */
    public abstract ServerTypeParam getServerTypeFilter();
}