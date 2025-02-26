package eu.softake.tools.mvn.vaadindeployplugin.params.validators.impl;

import eu.softake.tools.mvn.vaadindeployplugin.params.validators.ParamValidator;

import java.util.regex.Pattern;

/**
 * Validator for email addresses used in Certbot configurations.
 * This validator ensures that the provided email address follows a valid format based on the
 * OWASP recommended email regex pattern. The email must contain:
 *  <ul>
 *     <li>A valid username with alphanumeric characters, underscores, plus, ampersand, and hyphen.</li>
 *     <li>A domain with at least one dot separator.</li>
 *     <li>A valid top-level domain (TLD) with a minimum of two and a maximum of seven characters.</li>
 *  </ul>
 * <p>
 * If the email format is incorrect, an {@link IllegalArgumentException} is thrown.
 * </p>
 */
public class CertbotEmailParamValidator implements ParamValidator<String> {

    /**
     * Regular expression pattern for validating email addresses.
     * <p>
     * This pattern follows the OWASP recommendation to ensure a proper email format.
     * </p>
     */
    private static final String OWASP_EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * Error message template for invalid email addresses.
     */
    private static final String EXCEPTION_TEMPLATE = "Email value `%s` is invalid. Please specify the correct email address.";

    /**
     * Validates the given email address.
     * <p>
     * The email is checked against a regex pattern to ensure it adheres to a proper format.
     * If the email does not match the expected pattern, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param email the email address to validate.
     * @throws IllegalArgumentException if the email format is invalid.
     */
    @Override
    public void validate(String email) throws IllegalArgumentException {
        if (!Pattern.matches(OWASP_EMAIL_PATTERN, email)) {
            throw new IllegalArgumentException(String.format(EXCEPTION_TEMPLATE, email));
        }
    }
}