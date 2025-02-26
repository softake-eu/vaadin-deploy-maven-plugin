package eu.softake.tools.mvn.vaadindeployplugin;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Mojo responsible for deploying the application to staging servers.
 * <p>
 * This class extends {@link AbstractDeployMojo} and filters servers by the staging type.
 * It is executed during the {@code package} phase of the Maven build lifecycle.
 * </p>
 */
@Mojo(name = "deploy-stg", defaultPhase = LifecyclePhase.PACKAGE)
public class DeployStgMojo extends AbstractDeployMojo {

    /**
     * Specifies the server type filter for deployment.
     *
     * @return {@link ServerTypeParam#STG} indicating that only staging servers are targeted.
     */
    public ServerTypeParam getServerTypeFilter() {
        return ServerTypeParam.STG;
    }
}