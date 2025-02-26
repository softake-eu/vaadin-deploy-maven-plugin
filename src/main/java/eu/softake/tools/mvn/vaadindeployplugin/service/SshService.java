package eu.softake.tools.mvn.vaadindeployplugin.service;

import eu.softake.tools.mvn.vaadindeployplugin.params.SshConnectionParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.io.nio2.Nio2ServiceFactoryFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.common.SftpConstants;
import org.apache.sshd.sftp.common.SftpHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Service class for handling SSH connections and file transfers via SFTP.
 * This class provides methods to upload files and directories to a remote server,
 * execute remote commands, and check if directories or files exist on the server.
 * It manages the SSH connection, SFTP client, and session lifecycle.
 * Implements AutoCloseable to automatically close the SSH session after use.
 */
@Slf4j
public class SshService implements AutoCloseable {

    // Services
    private final SshConnectionParam sshConnection;
    private final ClientSession clientSession;
    private final SftpClient sftpClient;

    /**
     * Constructor to initialize the SSH service with the provided connection parameters.
     *
     * @param sshConnection SSH connection parameters.
     * @throws Exception If an error occurs while establishing the SSH connection or initializing the SFTP client.
     */
    public SshService(SshConnectionParam sshConnection) throws Exception {
        this.sshConnection = sshConnection;
        this.clientSession = buildSshClientSession(sshConnection);
        this.sftpClient = SftpClientFactory.instance().createSftpClient(clientSession);
    }

    /**
     * Copies a local directory to the remote server.
     * It recursively walks through the local directory and uploads files to the specified remote directory.
     *
     * @param localDir  The path to the local directory to be uploaded.
     * @param remoteDir The path to the remote directory where files will be uploaded.
     * @throws Exception If an error occurs while walking the directory or copying files.
     */
    public void copyDirToServer(String localDir, String remoteDir) throws Exception {
        final Path localDirPath = Paths.get(localDir);

        Files.walk(localDirPath).forEach(localPath -> {
            try {
                if (Files.isRegularFile(localPath)) {
                    final Path relativePath = localDirPath.relativize(localPath);
                    final Path remoteFilePath = Paths.get(remoteDir + "/" + relativePath.toString().replace("\\", "/"));

                    copyFileToServer(localPath.toFile(), remoteFilePath.getParent().toString());
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload: " + localPath, e);
            }
        });
    }

    /**
     * Copies a single file from the local machine to the remote server.
     *
     * @param srcFile  The local file to be uploaded.
     * @param remoteDir The remote directory where the file will be uploaded.
     * @throws Exception If an error occurs while copying the file.
     */
    public void copyFileToServer(File srcFile, String remoteDir) throws Exception {
        ensureDirExists(remoteDir);

        final Path localFilePath = srcFile.toPath();
        final String remoteFilePath = remoteDir + "/" + srcFile.getName();
        final String hostname = this.sshConnection.getHostName();

        log.info("Copy file from `{}` to `{}:{}`", localFilePath, hostname, remoteFilePath);
        uploadFileToServer(localFilePath, remoteFilePath);
    }

    /**
     * Checks whether the specified remote directory is empty.
     * If the directory does not exist, it returns true.
     *
     * @param remoteDir The remote directory to check.
     * @return true if the directory is empty or does not exist, false if it contains files or subdirectories.
     * @throws Exception If an error occurs while checking the directory.
     */
    public boolean isDirEmpty(String remoteDir) throws Exception {
        try (SftpClient.CloseableHandle handle = sftpClient.openDir(remoteDir)) {
            Iterable<SftpClient.DirEntry> entries = sftpClient.listDir(handle);

            for (SftpClient.DirEntry entry : entries) {
                String filename = entry.getFilename();
                if (!".".equals(filename) && !"..".equals(filename)) {
                    return false;
                }
            }
        } catch (Exception e) {
            if (SftpConstants.SSH_FX_NO_SUCH_FILE == SftpHelper.resolveSubstatus(e)) {
                return true;
            }
            log.warn("Can't check whether the dir `{}` is empty", remoteDir, e);
            throw e;
        }

        return true;
    }

    /**
     * Checks whether a file exists on the remote server.
     *
     * @param remoteFilePath The remote file path to check.
     * @return true if the file exists, false otherwise.
     * @throws Exception If an error occurs while checking the file.
     */
    public boolean isFileExist(String remoteFilePath) throws Exception {
        try (SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(clientSession)) {
            final SftpClient.Attributes attributes = sftpClient.stat(remoteFilePath);
            return attributes != null;
        } catch (Exception e) {
            if (SftpConstants.SSH_FX_NO_SUCH_FILE == SftpHelper.resolveSubstatus(e)) {
                return false;
            }
            log.error("Couldn't check whether file `{}` exists on the server", remoteFilePath, e);
            throw e;
        }
    }

    /**
     * Executes a command on the remote server.
     *
     * @param command The command to execute.
     * @return The combined output (stdout and stderr) of the command.
     * @throws Exception If an error occurs while executing the command.
     */
    public String execCommand(String command) throws Exception {
        log.info("Execute command on the server: `{}`", command);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayOutputStream err = new ByteArrayOutputStream();
             ClientChannel channel = this.clientSession.createExecChannel(command)) {
            channel.setOut(out);
            channel.setErr(err);
            channel.open().verify();
            channel.waitFor(Collections.singleton(ClientChannelEvent.CLOSED), 0);
            int exitCode = channel.getExitStatus();
            String combinedOutput = out + err.toString();
            log.info("Result: `{}`", combinedOutput);
            if (exitCode == 0) {
                return combinedOutput;
            } else {
                throw new Exception("Command failed with exit code " + exitCode + ": " + combinedOutput);
            }
        }
    }

    /**
     * Ensures that the specified remote directory exists, creating any necessary subdirectories.
     *
     * @param remoteDir The remote directory to ensure exists.
     * @throws IOException If an error occurs while checking or creating the directory.
     */
    public void ensureDirExists(String remoteDir) throws IOException {
        final String[] folders = remoteDir.split("/");
        final StringBuilder pathBuilder = new StringBuilder();

        for (String folder : folders) {
            if (folder.isEmpty()) continue;
            pathBuilder.append('/').append(folder);

            final String currentPath = pathBuilder.toString();
            try {
                this.sftpClient.stat(currentPath);
            } catch (IOException e) {
                if (SftpConstants.SSH_FX_NO_SUCH_FILE == SftpHelper.resolveSubstatus(e)) {
                    log.info("Create folder: `{}`", currentPath);
                    this.sftpClient.mkdir(currentPath);
                } else {
                    throw e;
                }
            }
        }
    }

    /**
     * Checks if the current SSH session is valid (open and authenticated).
     *
     * @return true if the SSH session is valid, false otherwise.
     */
    public boolean isSshSessionValid() {
        return clientSession.isOpen() && clientSession.isAuthenticated();
    }

    /**
     * Uploads a file from the local machine to the remote server.
     *
     * @param localFilePath the path to the local file to be uploaded
     * @param remoteFilePath the path where the file will be uploaded on the remote server
     * @throws Exception if an error occurs during file upload
     */
    private void uploadFileToServer(Path localFilePath, String remoteFilePath) throws Exception {
        this.sftpClient.put(localFilePath, remoteFilePath);
    }

    /**
     * Establishes an SSH client session using the provided SSH connection parameters.
     *
     * @param sshConnection the connection parameters containing host, port, username, etc.
     * @return the created {@link ClientSession} instance
     * @throws Exception if an error occurs during session creation or authentication
     */
    private ClientSession buildSshClientSession(SshConnectionParam sshConnection) throws Exception {
        final int port = sshConnection.getPort();
        final String host = sshConnection.getHostName();
        final String userName = sshConnection.getUser();
        final String identityFile = sshConnection.getIdentityFile();
        final String passphrase = sshConnection.getPassPhrase();
        final String password = sshConnection.getPassword();

        SshClient client = SshClient.setUpDefaultClient();
        client.setIoServiceFactoryFactory(new Nio2ServiceFactoryFactory());
        client.setServerKeyVerifier((clientSession, remoteAddress, serverKey) -> true);
        client.start();
        log.info("Connecting to the server {}@{}:{}", userName, host, port);
        ClientSession session = client.connect(userName, host, port).verify().getSession();
        authenticateUserSession(identityFile, passphrase, password, session);
        log.info("SSH session has been established");
        return session;
    }

    /**
     * Authenticates the user session using either public key or password-based authentication.
     *
     * @param identityFile the path to the identity file for public key authentication, or null for password authentication
     * @param passphrase the passphrase for the identity file, if applicable
     * @param password the password for password-based authentication
     * @param session the {@link ClientSession} to authenticate
     * @throws Exception if authentication fails
     */
    private void authenticateUserSession(String identityFile, String passphrase, String password, ClientSession session) throws Exception {
        if (identityFile != null) {
            FileKeyPairProvider keyPairProvider = new FileKeyPairProvider(Paths.get(identityFile));
            if (passphrase != null) {
                keyPairProvider.setPasswordFinder(FilePasswordProvider.of(passphrase));
            }
            session.addPublicKeyIdentity(keyPairProvider.loadKeys(null).iterator().next());
        } else {
            session.addPasswordIdentity(password);
        }
        session.auth().verify();
        log.info("Authenticated successfully");
    }

    /**
     * Closes the SSH session and releases resources.
     *
     * @throws Exception If an error occurs while closing the session.
     */
    @Override
    public void close() throws Exception {
        clientSession.close();
        log.info("SSH session has been closed");
    }
}