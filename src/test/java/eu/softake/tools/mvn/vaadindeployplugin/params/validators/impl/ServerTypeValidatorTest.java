package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTypeValidatorTest {

    @Test
    void serverTypeIsRequired() {
        ServerTypeParamValidator serverTypeValidator = new ServerTypeParamValidator();
        ServerParam serverParam = new ServerParam();
        assertThrows(IllegalArgumentException.class, () -> serverTypeValidator.validate(serverParam));
    }
}