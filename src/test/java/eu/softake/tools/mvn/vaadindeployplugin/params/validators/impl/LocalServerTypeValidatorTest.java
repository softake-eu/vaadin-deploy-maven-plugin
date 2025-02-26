package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.ServerTypeParam;
import eu.softake.tools.mvn.vaadindeployplugin.params.SshConnectionParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalServerTypeValidatorTest {

    @Test
    void localTypeHasNoSshConnectionDetails() {
        SshConnectionParamValidator localServerTypeValidator = new SshConnectionParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setType(ServerTypeParam.LOCAL);
        localServerTypeValidator.validate(serverParam);
    }

    @Test
    void localTypeHasSshConnectionDetails() {
        SshConnectionParamValidator localServerTypeValidator = new SshConnectionParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setType(ServerTypeParam.LOCAL);
        serverParam.setSshConnection(new SshConnectionParam());
        localServerTypeValidator.validate(serverParam);
    }

    @Test
    void qaTypeHasSshConnectionDetails() {
        checkTheRequiredSshConenction(ServerTypeParam.QA);
    }

    @Test
    void stgTypeHasSshConnectionDetails() {
        checkTheRequiredSshConenction(ServerTypeParam.STG);
    }

    @Test
    void testTypeHasSshConnectionDetails() {
        checkTheRequiredSshConenction(ServerTypeParam.TEST);
    }

    @Test
    void prodTypeHasSshConnectionDetails() {
        checkTheRequiredSshConenction(ServerTypeParam.PROD);
    }

    private void checkTheRequiredSshConenction(ServerTypeParam serverType) {
        SshConnectionParamValidator localServerTypeValidator = new SshConnectionParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setType(serverType);
        assertThrows(IllegalArgumentException.class, () -> localServerTypeValidator.validate(serverParam));
    }
}