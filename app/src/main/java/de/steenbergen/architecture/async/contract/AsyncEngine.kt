package de.steenbergen.architecture.async.contract

/**
 * Takes synchronous code and runs it asynchronously
 */
interface AsyncEngine {

    fun <I, O> doAsync(
        input: I,
        operation: ((I) -> O),
        onError: ErrorCallback,
        onSuccess: Callback<O>
    ): AsyncOperationInProgress
}
