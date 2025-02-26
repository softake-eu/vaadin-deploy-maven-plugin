package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.AllArgsConstructor;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

/**
 * Handler that checks if the Dockerfile exists in the specified folder.
 * If the Dockerfile is missing, an exception is thrown with instructions to create one.
 */
@AllArgsConstructor
public class CheckDockerfileExistsHandler implements ChainStepHandler<ServerParam> {

    // Constants
    public static final String EXCEPTION_TEMPLATE = "Dockerfile doesn't exist by the path: `%s`. \n" +
            "Usually Dockerfile gets generated automatically by Vaadin Starter, " +
            "but if you created the project manually then you would need to execute a few extra steps: \n" +
            "1. Create file: `%s`\n" +
            "2. Put the following content inside:\n\n" +
            "FROM eclipse-temurin:17-jre\n" +
            "COPY target/*.jar app.jar\n" +
            "EXPOSE 8080\n" +
            "ENTRYPOINT [\"java\", \"-jar\", \"/app.jar\"]\n\n" +
            "3. Please pay attention to the first line of the Dockerfile. In case your project is based on java 21 you have to replace 17 with 21 (FROM eclipse-temurin:21-jre)";

    // Data
    private final String dockerfileParentDirPath;

    /**
     * Provides a description of the step: checking whether the Dockerfile exists.
     *
     * @param dataObj the server configuration object containing deployment parameters
     * @return the description of the step, including the directory where the Dockerfile should exist
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return String.format("Check whether Dockerfile exists in folder `%s`", dockerfileParentDirPath);
    }

    /**
     * Handles the check for the existence of the Dockerfile. If the Dockerfile is missing,
     * an exception is thrown with detailed instructions on how to create the Dockerfile manually.
     *
     * @param dataObj the server configuration object containing deployment parameters
     * @throws MojoExecutionException if the Dockerfile does not exist in the expected location
     */
    @Override
    public void handle(ServerParam dataObj) throws Exception {
        final String dockerfilePath = dockerfileParentDirPath + "/Dockerfile";
        final File file = new File(dockerfilePath);

        if(!file.exists()) {
            throw new MojoExecutionException(String.format(EXCEPTION_TEMPLATE, dockerfilePath, dockerfilePath));
        }
    }
}