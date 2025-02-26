package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CertbotEmailValidatorTest {

    private static final List<String> validEmails = new ArrayList<String>() {{
        add("test@gmail.com");
        add("antonio+banderas@outlook.com");
        add("a.banderas@yahoo.com");
        add("x.y.z.banderas@yahoo.com");
        add("a-banderas@mail.outlook.com");
        add("a-banderas@dns-server.mail.outlook.com");
    }};

    private static final List<String> invalidEmails = new ArrayList<String>() {{
        add("test!@gmail.com");
        add("antonio+banderas@@outlook.com");
        add("a.banderas@yahoo.com ");
        add("a#banderas@mail.com.ua");
        add("a-banderas@dns-server..mail.outlook.com");
        add(" a-banderas@dns-server.mail.outlook.com");
        add("a-banderas@dns-server.mail.outlook.com ");
    }};

    @Test
    void certbotEmailIsValid() {

        CertbotEmailParamValidator certbotEmailValidator = new CertbotEmailParamValidator();
        for (String validEmail : validEmails) {
            certbotEmailValidator.validate(validEmail);
        }
    }

    @Test
    void certbotEmailIsInvalid() {

        CertbotEmailParamValidator certbotEmailValidator = new CertbotEmailParamValidator();
        for (String validEmail : invalidEmails) {
            assertThrows(IllegalArgumentException.class,
                    () -> certbotEmailValidator.validate(validEmail));
        }
    }
}