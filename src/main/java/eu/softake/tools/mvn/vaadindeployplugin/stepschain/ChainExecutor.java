package eu.softake.tools.mvn.vaadindeployplugin.stepschain;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Executes a sequence of {@link ChainStepHandler} instances in a defined order.
 *
 * @param <T> the type of data object that the steps process
 */
@Slf4j
public class ChainExecutor<T> {

    // List of steps in the execution chain
    private final LinkedList<ChainStepHandler<T>> steps;

    /**
     * Constructs a {@code ChainExecutor} with a given list of steps.
     *
     * @param steps the list of steps to be executed; if null, an empty list is initialized
     */
    public ChainExecutor(LinkedList<ChainStepHandler<T>> steps) {
        this.steps = steps != null ? steps : new LinkedList<>();
    }

    /**
     * Constructs a {@code ChainExecutor} with a variable number of step handlers.
     *
     * @param stepHandlers the step handlers to be added to the execution chain
     */
    @SafeVarargs
    public ChainExecutor(ChainStepHandler<T>... stepHandlers) {
        this.steps = new LinkedList<>(Arrays.asList(stepHandlers));
    }

    /**
     * Constructs a {@code ChainExecutor} with an empty step list.
     */
    public ChainExecutor() {
        this.steps = new LinkedList<>();
    }

    /**
     * Starts the execution of the defined steps.
     *
     * @param dataObj the data object to be processed by each step
     * @throws Exception if an error occurs in any step execution
     * @throws IllegalStateException if there are no steps to execute
     */
    public void start(T dataObj) throws Exception {
        if (steps.isEmpty()) {
            throw new IllegalStateException("No steps to execute!");
        }
        for (ChainStepHandler<T> step : steps) {
            log.info(step.getStepDescription(dataObj));
            step.handle(dataObj);
            log.info("OK");
        }
    }

    /**
     * Adds a new step to the execution chain.
     *
     * @param flowStepHandler the step handler to be added
     */
    public void addStep(ChainStepHandler<T> flowStepHandler) {
        steps.add(flowStepHandler);
    }
}