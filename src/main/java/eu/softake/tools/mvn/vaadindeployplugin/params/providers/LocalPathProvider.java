package eu.softake.tools.mvn.vaadindeployplugin.params.providers;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import lombok.Getter;

import java.nio.file.Paths;

/**
 * Provides local file system paths for deploying a Vaadin project.
 * This class generates paths based on the given server parameters and project directories.
 */
@Getter
public class LocalPathProvider {

    /** The directory where the application will be deployed locally. */
    private final String deployDir;

    /** The root directory of the Vaadin project. */
    private final String vaadinProjectDir;

    /** The directory where application-specific data is stored. */
    private final String appsDataDir;

    /** The root directory for local deployments, derived from the project base directory and domain. */
    private final String localDeploymentRootDir;

    /** The target directory of the Vaadin project where compiled artifacts are stored. */
    private final String targetDir;

    /**
     * Constructs a LocalPathProvider instance using the provided server parameters and project directories.
     *
     * @param serverParam       the server parameters containing domain-specific configurations
     * @param projectBaseDir    the base directory of the project
     * @param vaadinProjectDir  the root directory of the Vaadin project
     */
    public LocalPathProvider(ServerParam serverParam, String projectBaseDir, String vaadinProjectDir) {
        final ServerPathProvider serverPathProvider = new ServerPathProvider(serverParam);
        final String domain = serverParam.getDomain();

        this.localDeploymentRootDir = projectBaseDir + "/_deployment/" + domain;
        this.deployDir = localDeploymentRootDir + serverPathProvider.getDeployDir();
        this.appsDataDir = localDeploymentRootDir + serverPathProvider.getAppsDataDir();
        this.vaadinProjectDir = vaadinProjectDir;
        this.targetDir = vaadinProjectDir + "/target";
    }

    /**
     * Checks whether the local deployment root directory exists.
     *
     * @return {@code true} if the local deployment root directory exists, {@code false} otherwise
     */
    public boolean exists() {
        return Paths.get(this.localDeploymentRootDir).toFile().exists();
    }
}