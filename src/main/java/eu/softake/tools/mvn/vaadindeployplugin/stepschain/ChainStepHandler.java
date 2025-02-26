package eu.softake.tools.mvn.vaadindeployplugin.stepschain;

/**
 * Defines a handler for processing a step in a deployment chain.
 * <p>
 * Implementations of this interface should provide a description of the step
 * and execute the necessary logic for handling the provided data object.
 * </p>
 *
 * @param <T> the type of data object that the handler processes
 */
public interface ChainStepHandler<T> {

    /**
     * Returns a description of the step being executed.
     *
     * @param dataObj the data object related to this step
     * @return a string describing the step
     */
    String getStepDescription(T dataObj);

    /**
     * Executes the logic associated with this step.
     *
     * @param dataObj the data object related to this step
     * @throws Exception if an error occurs during execution
     */
    void handle(T dataObj) throws Exception;
}