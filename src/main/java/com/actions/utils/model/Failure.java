package com.actions.utils.model;

/**
 * Every enum that describes the possible failures that can happen should implement this interface.
 *
 * The api should evaluate the failure.
 */
public interface Failure {

    /**
     * Gives the description of the failure.
     *
     * @return a description of the failure
     */
    String getDescription();

    /**
     * Gives the error code of the failure.
     *
     * @return a error code that represents the failure
     */
    String getErrorCode();

}
