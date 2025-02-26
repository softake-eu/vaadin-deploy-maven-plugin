package eu.softake.tools.mvn.vaadindeployplugin.params;

import lombok.Getter;

/**
 * Represents the parameters required for establishing an SSH connection.
 * <p>
 * This class holds the configuration details needed for connecting to a remote server via SSH,
 * including the hostname, username, identity file, passphrase, password, and port.
 * </p>
 */
@Getter
public class SshConnectionParam extends AbstractPluginParam {

    private String hostName;
    private String user = "root";
    private String identityFile;
    private String passPhrase;
    private String password;
    private int port = 22;

    /**
     * Sets the hostname for the SSH connection.
     *
     * @param hostName the hostname of the remote server
     */
    public void setHostName(String hostName) {
        this.hostName = trim(hostName);
    }

    /**
     * Sets the username for the SSH connection. Default is "root".
     *
     * @param user the username for the SSH connection
     */
    public void setUser(String user) {
        this.user = trim(user);
    }

    /**
     * Sets the path to the identity file for the SSH connection.
     *
     * @param identityFile the path to the identity file (private key)
     */
    public void setIdentityFile(String identityFile) {
        this.identityFile = trim(identityFile);
    }

    /**
     * Sets the passphrase for the identity file (if applicable).
     *
     * @param passPhrase the passphrase for the identity file
     */
    public void setPassPhrase(String passPhrase) {
        this.passPhrase = trim(passPhrase);
    }

    /**
     * Sets the password for the SSH connection (if identity file is not used).
     *
     * @param password the password for the SSH connection
     */
    public void setPassword(String password) {
        this.password = trim(password);
    }

    /**
     * Sets the port for the SSH connection. Default is 22.
     *
     * @param port the port number for the SSH connection
     */
    public void setPort(int port) {
        this.port = port;
    }
}