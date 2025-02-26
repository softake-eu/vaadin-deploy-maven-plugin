package eu.softake.tools.mvn.vaadindeployplugin.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A utility service for performing file-related operations, such as retrieving files from a directory
 * and copying files between paths.
 * <p>
 * This class provides methods to get child files from a directory that match a given pattern and to copy files
 * from a source path to a destination path.
 * </p>
 */
public class LocalFileService {

    /**
     * Error message to be used when a provided file path is not a directory.
     */
    public static final String EXCEPTION_NOT_DIRECTORY = "The file must point to a directory! Current value: `%s`";

    /**
     * Error message to be used when a provided file path is not a valid file.
     */
    public static final String EXCEPTION_WRONG_PATH = "The path must point to a file! Current value: `%s`";

    /**
     * Retrieves a list of files in the specified directory that match the provided filter regex.
     * <p>
     * This method checks if the given parent folder is a valid directory and then filters files based on the provided
     * regular expression. It returns a list of matching files.
     * </p>
     *
     * @param parentFolder    the parent directory to search for files
     * @param fileFilterRegex the regular expression to match file names
     * @return a list of files that match the filter regex
     * @throws IllegalArgumentException if the provided path is not a directory
     */
    public List<File> getChildren(File parentFolder, String fileFilterRegex) {
        // Check whether provided file is a directory
        if (!parentFolder.isDirectory()) {
            throw new IllegalArgumentException(String.format(EXCEPTION_NOT_DIRECTORY, parentFolder.getAbsolutePath()));
        }
        // Get list of files that match the fileFilterRegex
        File[] foundFiles = parentFolder.listFiles(((dir, name) -> name.matches(fileFilterRegex)));
        // Convert array to list
        return Arrays.asList(Optional.ofNullable(foundFiles).orElse(new File[0]));
    }

    /**
     * Copies a file from the source path to the destination path.
     * <p>
     * This method checks if the source file exists and is not a directory, creates the target directory if it does not
     * exist, and copies the source file to the destination path, replacing any existing file.
     * </p>
     *
     * @param srcPathString the source file path as a string
     * @param destPathString the destination file path as a string
     * @throws IOException if an I/O error occurs during file copying
     * @throws IllegalArgumentException if the source path is not valid or points to a directory
     */
    public void copyFile(String srcPathString, String destPathString) throws IOException {
        // Convert String to Path
        final Path srcPath = Paths.get(srcPathString);
        final Path destPath = Paths.get(destPathString);
        // Validate srcPath exists
        if (!srcPath.toFile().exists() || srcPath.toFile().isDirectory()) {
            throw new IllegalArgumentException(String.format(EXCEPTION_WRONG_PATH, srcPathString));
        }
        // Ensure the target directory exists
        Files.createDirectories(destPath.getParent());
        // Copy file to destination
        Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
    }
}