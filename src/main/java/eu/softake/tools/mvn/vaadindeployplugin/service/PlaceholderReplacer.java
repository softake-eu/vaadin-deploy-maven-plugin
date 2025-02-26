package eu.softake.tools.mvn.vaadindeployplugin.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class for replacing placeholders in files with values from a provided map.
 * <p>
 * This class provides functionality to read a file, find placeholders in the format ${key},
 * replace them with the corresponding values from a map, and then write the updated content back
 * to the same file. The placeholders are replaced in all lines of the file.
 * </p>
 */
public class PlaceholderReplacer {

    /**
     * Reads a file, replaces placeholders with values from a map, and writes the result back to the same file.
     *
     * @param file         the file to process
     * @param keyValueMap  a map containing placeholders (keys) and their replacement values
     * @throws IOException if an I/O error occurs
     */
    public void substitute(File file, Map<String, String> keyValueMap) throws IOException {
        final Path filePath = file.toPath();

        // Read all lines, replace placeholders, and collect back to a list
        final List<String> updatedLines = Files.readAllLines(filePath).stream()
                .map(line -> {
                    for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
                        line = line.replace("${" + entry.getKey() + "}", entry.getValue());
                    }
                    return line;
                })
                .collect(Collectors.toList());

        // Write updated lines back to the file
        Files.write(filePath, updatedLines);
    }
}
