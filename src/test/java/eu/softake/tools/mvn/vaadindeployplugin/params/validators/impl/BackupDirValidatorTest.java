package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackupDirValidatorTest {

    @Test
    void backupDirIsOptional() {
        ServerParam serverParam = new ServerParam();
        new BackupDirParamValidator().validate(serverParam);
    }

    @Test
    void backupDirIsEmpty() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("");
        assertThrows(IllegalArgumentException.class,
                () -> new AppsDataDirParamValidator().validate(serverParam));
    }

    @Test
    void backupDirStartsWithSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("mnt/backup");
        assertThrows(IllegalArgumentException.class,
                () -> new AppsDataDirParamValidator().validate(serverParam));
    }

    @Test
    void backupDirStartsWithNoSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("/mnt/backup");
        new AppsDataDirParamValidator().validate(serverParam);
    }
}