package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppsDataDirValidatorTest {

    @Test
    void appsDataDirIsRequired() {
        ServerParam serverParam = new ServerParam();
        assertThrows(IllegalArgumentException.class,
                () -> new AppsDataDirParamValidator().validate(serverParam));
    }

    @Test
    void appsDataDirStartsWithNoSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("mnt/data/vaadin");
        assertThrows(IllegalArgumentException.class,
                () -> new AppsDataDirParamValidator().validate(serverParam));
    }

    @Test
    void appsDataDirIsEmpty() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("");
        assertThrows(IllegalArgumentException.class,
                () -> new AppsDataDirParamValidator().validate(serverParam));
    }

    @Test
    void appsDataDirStartsWithSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("/mnt/data/vaadin");
        new AppsDataDirParamValidator().validate(serverParam);
    }

    @Test
    void appsDataDirEndsWithSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setAppsDataDir("/mnt/data/vaadin/");
        new AppsDataDirParamValidator().validate(serverParam);
    }
}