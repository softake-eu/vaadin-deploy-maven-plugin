package eu.softake.tools.mvn.vaadindeployplugin.params;

/**
 * Enum representing different types of servers in the deployment process.
 * <p>
 * This enum defines the various server types that are used in the deployment
 * pipeline, from local development environments to production.
 * </p>
 */
public enum ServerTypeParam {
    /**
     * Local server type, typically used for development and testing on a local machine.
     */
    LOCAL,

    /**
     * Test server type, used for testing purposes before staging or production deployment.
     */
    TEST,

    /**
     * QA (Quality Assurance) server type, used for quality checks and validation.
     */
    QA,

    /**
     * Staging server type, used as a final pre-production environment for testing with real data.
     */
    STG,

    /**
     * Production server type, used for the live environment where the application is accessible to end users.
     */
    PROD
}