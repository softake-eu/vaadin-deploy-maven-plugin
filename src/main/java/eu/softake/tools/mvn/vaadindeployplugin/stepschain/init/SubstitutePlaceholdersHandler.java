package eu.softake.tools.mvn.vaadindeployplugin.stepschain.init;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import eu.softake.tools.mvn.vaadindeployplugin.service.JarFileService;
import eu.softake.tools.mvn.vaadindeployplugin.service.PlaceholderReplacer;
import eu.softake.tools.mvn.vaadindeployplugin.stepschain.ChainStepHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Handles the substitution of placeholders in configuration files.
 * This step is used to replace placeholders in files with actual values
 * during the deployment process.
 */
@Slf4j
public class SubstitutePlaceholdersHandler implements ChainStepHandler<ServerParam> {
    // Constants
    public static final String DEFAULT_DB_USER = "user";
    public static final String DEFAULT_DB_PASSWORD = "P@ssword#5202";

    // Services for placeholder replacement and file handling
    private final PlaceholderReplacer placeholderReplacer;
    private final JarFileService fileService;

    // Data required for substitution
    private final String vaadinProjectAbsolutePath;
    private final String deployDirLocalPath;
    private final String appsDataDirLocalPath;
    private final String certbotEmail;
    private final String artifactId;

    /**
     * Constructs a {@code SubstitutePlaceholdersHandler} with the required dependencies.
     *
     * @param placeholderReplacer the service responsible for replacing placeholders
     * @param fileService the service for file handling
     * @param vaadinProjectAbsolutePath the absolute path of the Vaadin project
     * @param deployDirLocalPath the local path to the deployment directory
     * @param appsDataDirLocalPath the local path to the application data directory
     * @param certbotEmail the email for certbot
     * @param artifactId the artifact ID for the project
     */
    public SubstitutePlaceholdersHandler(PlaceholderReplacer placeholderReplacer,
                                         JarFileService fileService,
                                         String vaadinProjectAbsolutePath,
                                         String deployDirLocalPath,
                                         String appsDataDirLocalPath,
                                         String certbotEmail,
                                         String artifactId) {
        this.placeholderReplacer = placeholderReplacer;
        this.fileService = fileService;
        this.vaadinProjectAbsolutePath = vaadinProjectAbsolutePath;
        this.deployDirLocalPath = deployDirLocalPath;
        this.appsDataDirLocalPath = appsDataDirLocalPath;
        this.certbotEmail = certbotEmail;
        this.artifactId = artifactId;
    }

    /**
     * Provides a description of the current step being executed.
     *
     * @param dataObj the {@link ServerParam} object containing server configuration data
     * @return a string describing the step
     */
    @Override
    public String getStepDescription(ServerParam dataObj) {
        return "Substitute placeholders in config files...";
    }

    /**
     * Handles the actual substitution of placeholders in the files.
     * This method retrieves all the files in the deployment directory,
     * substitutes the placeholders with values from the map,
     * and updates the files accordingly.
     *
     * @param serverModel the {@link ServerParam} object containing server configuration
     * @throws Exception if an error occurs during placeholder substitution
     */
    @Override
    public void handle(ServerParam serverModel) throws Exception {
        final HashMap<String, String> keyValueMap = getSubstitutionMap(serverModel);
        final File deploymentDirFile = new File(deployDirLocalPath);
        final List<File> allFiles = fileService.getAllFiles(deploymentDirFile);

        for (File file : allFiles) {
            log.debug("Substitute placeholders in `{}`", file.getAbsolutePath());
            placeholderReplacer.substitute(file, keyValueMap);
        }
    }

    /**
     * Constructs a map of keys and values for placeholder substitution.
     * The map is populated with values based on the server configuration
     * and other required data.
     *
     * @param serverModel the {@link ServerParam} object containing server data
     * @return a map of placeholder keys and their corresponding values
     */
    private HashMap<String, String> getSubstitutionMap(ServerParam serverModel) {
        final ServerTypeParam serverType = serverModel.getType();

        // Determine deployment directories based on server type
        final String deploymentDir = ServerTypeParam.LOCAL.equals(serverType) ? deployDirLocalPath : serverModel.getDeployDir();
        final String appsDataDir = ServerTypeParam.LOCAL.equals(serverType) ? appsDataDirLocalPath : serverModel.getAppsDataDir();
        final String vaadinProjectDir = ServerTypeParam.LOCAL.equals(serverType) ? vaadinProjectAbsolutePath : deploymentDir;

        // Populate the map with placeholders and their values
        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("vaadinProjectDir", vaadinProjectDir);
        keyValueMap.put("deploymentDir", deploymentDir);
        keyValueMap.put("appsDataDir", appsDataDir);
        keyValueMap.put("server.domain", serverModel.getDomain());
        keyValueMap.put("database.userName", DEFAULT_DB_USER);
        keyValueMap.put("database.password", DEFAULT_DB_PASSWORD);
        keyValueMap.put("database.schema", artifactId);
        keyValueMap.put("certbot.email", certbotEmail);
        return keyValueMap;
    }
}