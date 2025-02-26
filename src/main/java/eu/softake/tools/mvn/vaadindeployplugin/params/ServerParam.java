package eu.softake.tools.mvn.vaadindeployplugin.params;

import lombok.Getter;

/**
 * Represents the parameters related to a server in the deployment process.
 * <p>
 * This class holds information about the type of server, SSH connection details,
 * domain, database provider, and directories related to deployment, application
 * data, and backups.
 * </p>
 */
@Getter
public class ServerParam extends AbstractPluginParam {

    /**
     * The type of the server (e.g., local, test, production).
     */
    private ServerTypeParam type;

    /**
     * SSH connection parameters for connecting to the server.
     */
    private SshConnectionParam sshConnection;

    /**
     * The domain name associated with the server.
     */
    private String domain;

    /**
     * The database provider used on the server.
     */
    private String dbProvider;

    /**
     * The directory for application data on the server.
     */
    private String appsDataDir;

    /**
     * The directory for deployment on the server.
     */
    private String deployDir;

    /**
     * The directory for backups on the server.
     */
    private String backupDir;

    /**
     * Sets the server type.
     *
     * @param type the type of the server (e.g., LOCAL, TEST, PROD)
     */
    public void setType(ServerTypeParam type) {
        this.type = type;
    }

    /**
     * Sets the SSH connection parameters.
     *
     * @param sshConnection the SSH connection details
     */
    public void setSshConnection(SshConnectionParam sshConnection) {
        this.sshConnection = sshConnection;
    }

    /**
     * Sets the domain for the server.
     *
     * @param domain the domain name
     */
    public void setDomain(String domain) {
        this.domain = trim(domain);
    }

    /**
     * Sets the database provider for the server.
     *
     * @param dbProvider the name of the database provider
     */
    public void setDbProvider(String dbProvider) {
        this.dbProvider = lowercaseAndTrim(dbProvider);
    }

    /**
     * Sets the directory for application data on the server.
     *
     * @param appsDataDir the path to the application data directory
     */
    public void setAppsDataDir(String appsDataDir) {
        this.appsDataDir = decoratePath(appsDataDir);
    }

    /**
     * Sets the directory for deployment on the server.
     *
     * @param deployDir the path to the deployment directory
     */
    public void setDeployDir(String deployDir) {
        this.deployDir = decoratePath(deployDir);
    }

    /**
     * Sets the directory for backups on the server.
     *
     * @param backupDir the path to the backup directory
     */
    public void setBackupDir(String backupDir) {
        this.backupDir = decoratePath(backupDir);
    }
}