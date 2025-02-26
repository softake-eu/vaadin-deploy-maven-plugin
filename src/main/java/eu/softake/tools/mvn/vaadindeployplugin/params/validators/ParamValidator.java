package eu.softake.tools.mvn.vaadindeployplugin.params.validators;

/**
 * Interface for validating parameters of type {@code T}.
 * <p>
 * Implementations of this interface are responsible for validating specific fields or
 * objects of type {@code T}. The {@code validate} method should throw an exception
 * if the validation fails.
 * </p>
 *
 * @param <T> the type of the object that will be validated
 */
public interface ParamValidator<T> {

    /**
     * Validates the provided object.
     * <p>
     * Implementations of this method should define the validation rules for the given object.
     * If validation fails, an exception should be thrown.
     * </p>
     *
     * @param obj the object to validate
     * @throws IllegalArgumentException if the object does not pass validation
     */
    void validate(T obj) throws IllegalArgumentException;
}