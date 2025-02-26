package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


class DomainValidatorTest {

    private final static List<String> validDomains = new ArrayList<String>() {{
        add("google.com");
        add(" google.com");
        add("softake.eu");
        add("finance.yahoo.com");
        add("us.finance.yahoo.com");
        add("us-finance.yahoo.com");
        add("us1-finance.yahoo.com");
        add("127.0.0.1");
        add("localhost");
        add("localhost ");
    }};

    @Test
    void checkValidDomains() {
        DomainParamValidator domainValidator = new DomainParamValidator();
        ServerParam serverParam = new ServerParam();

        for (String validDomain : validDomains) {
            serverParam.setDomain(validDomain);
            domainValidator.validate(serverParam);
        }
    }

    @Test
    void domainIsRequired() {
        DomainParamValidator domainValidator = new DomainParamValidator();
        ServerParam serverParam = new ServerParam();

        assertThrows(IllegalArgumentException.class, () -> domainValidator.validate(serverParam));
    }

    @Test
    void domainIsEmpty() {
        DomainParamValidator domainValidator = new DomainParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDomain("");
        assertThrows(IllegalArgumentException.class, () -> domainValidator.validate(serverParam));
    }

    @Test
    void domainIsEmptyTrimmed() {
        DomainParamValidator domainValidator = new DomainParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDomain("    ");
        assertThrows(IllegalArgumentException.class, () -> domainValidator.validate(serverParam));
    }

    @Test
    void domainStartsWithHttp() {
        DomainParamValidator domainValidator = new DomainParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDomain("http://");
        assertThrows(IllegalArgumentException.class, () -> domainValidator.validate(serverParam));
    }

    @Test
    void domainStartsWithHttps() {
        DomainParamValidator domainValidator = new DomainParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDomain("https://");
        assertThrows(IllegalArgumentException.class, () -> domainValidator.validate(serverParam));
    }
}