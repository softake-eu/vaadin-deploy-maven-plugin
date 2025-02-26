package eu.softake.tools.mvn.vaadindeployplugin.service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Provides utility methods for working with JAR files and resources, including copying files and folders from
 * resources, as well as recursively collecting all files from a directory.
 */
@Slf4j
public class JarFileService {

    /**
     * Copies a file from the resources folder to the specified destination.
     * <p>
     * This method loads a file from the resources folder using the class loader and copies it to the given destination.
     * If the resource is not found, an exception is thrown.
     * </p>
     *
     * @param srcPath the path to the resource in the classpath
     * @param destPath the destination path where the file should be copied
     * @throws IOException if an error occurs while reading the resource or writing to the destination
     */
    public void copyFile(String srcPath, String destPath) throws Exception {
        log.debug("Copy the file from the resources folder: {} to the destination: {}", srcPath, destPath);
        Path dockerComposeTargetPath = Paths.get(destPath);
        // Ensure the target directory exists
        Files.createDirectories(dockerComposeTargetPath.getParent());
        // Get file from resources
        InputStream dockerComposeTemplateIS = getClass().getClassLoader().getResourceAsStream(srcPath);
        if (dockerComposeTemplateIS == null) {
            throw new IOException("Resource not found: " + srcPath);
        }
        // Copy to the destination
        Files.copy(dockerComposeTemplateIS, dockerComposeTargetPath, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Copied successfully");
    }

    /**
     * Copies an entire folder (including all its files) from the resources folder to the specified target directory.
     * <p>
     * This method retrieves the folder from the resources as a JAR entry and copies all the files inside it
     * to the target directory, maintaining the folder structure.
     * </p>
     *
     * @param srcDir the source directory path inside the resources folder
     * @param targetDir the target directory where the files should be copied
     * @throws IOException if an error occurs while reading the resources or writing to the target directory
     */
    public void copyFolder(String srcDir, String targetDir) throws Exception {
        final URL resourceURL = this.getClass().getClassLoader().getResource(srcDir);
        final String resourceUrlPath = resourceURL.getPath();
        // Get the JAR file path
        final String jarPath = resourceUrlPath.substring(5, resourceUrlPath.indexOf("!"));

        try (JarFile jarFile = new JarFile(jarPath)) {
            // Iterate over entries in the JAR file
            for (JarEntry entry : jarFile.stream().collect(Collectors.toList())) {
                if (entry.getName().startsWith(srcDir + "/") && !entry.isDirectory()) {
                    // Calculate relative path and target path
                    String relativePath = entry.getName().substring(srcDir.length() + 1);
                    Path targetPath = Paths.get(targetDir).resolve(relativePath);

                    // Ensure the target directory exists
                    Files.createDirectories(targetPath.getParent());

                    // Copy file content
                    try (InputStream inputStream = jarFile.getInputStream(entry)) {
                        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    /**
     * Recursively collects all files from a folder and its subfolders.
     * <p>
     * This method walks through the provided directory and all of its subdirectories to collect a list of all files.
     * </p>
     *
     * @param folder the root folder to start the iteration
     * @return a list of all files from the folder and subfolders
     * @throws IllegalArgumentException if the provided folder does not exist or is null
     */
    public List<File> getAllFiles(File folder) {
        List<File> fileList = new ArrayList<>();

        if (folder == null || !folder.exists()) {
            throw new IllegalArgumentException("The folder must exist and cannot be null.");
        }

        File[] files = folder.listFiles();
        if (files == null) return fileList;

        for (File file : files) {
            if (file.isDirectory()) {
                // Recurse into subdirectories
                fileList.addAll(getAllFiles(file));
            } else {
                // Add the file to the list
                fileList.add(file);
            }
        }

        return fileList;
    }
}