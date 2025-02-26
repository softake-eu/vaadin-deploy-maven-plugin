package eu.softake.tools.mvn.vaadindeployplugin;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Mojo responsible for deploying the application to test servers.
 * <p>
 * This class extends {@link AbstractDeployMojo} and filters servers by the test type.
 * It is executed during the {@code package} phase of the Maven build lifecycle.
 * </p>
 */
@Mojo(name = "deploy-tst", defaultPhase = LifecyclePhase.PACKAGE)
public class DeployTstMojo extends AbstractDeployMojo {

    /**
     * Specifies the server type filter for deployment.
     *
     * @return {@link ServerTypeParam#TEST} indicating that only test servers are targeted.
     */
    public ServerTypeParam getServerTypeFilter() {
        return ServerTypeParam.TEST;
    }
}