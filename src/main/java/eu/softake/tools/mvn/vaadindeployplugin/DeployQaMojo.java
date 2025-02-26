package eu.softake.tools.mvn.vaadindeployplugin;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Mojo responsible for deploying the application to QA servers.
 * <p>
 * This class extends {@link AbstractDeployMojo} and filters servers by the QA type.
 * It is executed during the {@code package} phase of the Maven build lifecycle.
 * </p>
 */
@Mojo(name = "deploy-qa", defaultPhase = LifecyclePhase.PACKAGE)
public class DeployQaMojo extends AbstractDeployMojo {

    /**
     * Specifies the server type filter for deployment.
     *
     * @return {@link ServerTypeParam#QA} indicating that only QA servers are targeted.
     */
    public ServerTypeParam getServerTypeFilter() {
        return ServerTypeParam.QA;
    }
}