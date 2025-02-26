package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.ServerParam;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseProviderValidatorTest {

    private List<String> validDbProviders = new ArrayList<String>() {{
        add("postgres");
        add("postgreS");
        add("Postgres");
        add("POSTGRES");
        add("POSTGREs");
        add("mariadb");
        add("Mariadb");
        add("mariadB");
        add("mariaDB");
        add("mariaDb");
        add("MariaDb");
        add("MariaDb ");
        add("MARIADB");
        add("MARIADb");
        add("nodatabase");
        add("NODATABASE");
        add("NOdatabase");
        add("NoDatabase");
        add("noDatabase");
        add("noDatabase ");
    }};

    @Test
    void checkValidDbProviders() {
        DatabaseProviderParamValidator databaseProviderValidator = new DatabaseProviderParamValidator();
        ServerParam serverParam = new ServerParam();
        for (String validDbProvider : validDbProviders) {
            serverParam.setDbProvider(validDbProvider);
            databaseProviderValidator.validate(serverParam);
        }
    }

    @Test
    void dbProviderIsOptional() {
        DatabaseProviderParamValidator databaseProviderValidator = new DatabaseProviderParamValidator();
        ServerParam serverParam = new ServerParam();
        databaseProviderValidator.validate(serverParam);
    }

    @Test
    void emptyDbProviderIsForbidden() {
        DatabaseProviderParamValidator databaseProviderValidator = new DatabaseProviderParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDbProvider("");
        assertThrows(IllegalArgumentException.class, () -> databaseProviderValidator.validate(serverParam));
    }

    @Test
    void oracleDbProviderIsForbidden() {
        DatabaseProviderParamValidator databaseProviderValidator = new DatabaseProviderParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDbProvider("oracle");
        assertThrows(IllegalArgumentException.class, () -> databaseProviderValidator.validate(serverParam));
    }

    @Test
    void mysqlDbProviderIsForbidden() {
        DatabaseProviderParamValidator databaseProviderValidator = new DatabaseProviderParamValidator();
        ServerParam serverParam = new ServerParam();
        serverParam.setDbProvider("mysql");
        assertThrows(IllegalArgumentException.class, () -> databaseProviderValidator.validate(serverParam));
    }
}