package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DeployDirValidatorTest {

    @Test
    void deployDirIsRequired() {
        ServerParam serverParam = new ServerParam();
        assertThrows(IllegalArgumentException.class,
                () -> new DeployDirParamValidator().validate(serverParam));
    }

    @Test
    void deployDirStartsWithNoSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("opt/applications/vaadin");
        assertThrows(IllegalArgumentException.class,
                () -> new DeployDirParamValidator().validate(serverParam));
    }

    @Test
    void deployDirIsEmpty() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("");
        assertThrows(IllegalArgumentException.class,
                () -> new DeployDirParamValidator().validate(serverParam));
    }

    @Test
    void deployDirStartsWithSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("/opt/applications/vaadin");
        new DeployDirParamValidator().validate(serverParam);
    }

    @Test
    void deployDirEndsWithSlash() {
        ServerParam serverParam = new ServerParam();
        serverParam.setDeployDir("/opt/applications/vaadin/");
        new DeployDirParamValidator().validate(serverParam);
    }
}