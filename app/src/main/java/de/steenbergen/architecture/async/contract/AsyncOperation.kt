package de.steenbergen.architecture.async.contract

/**
 * Represents an operation that runs in the background and then completes with a callback
 */
interface AsyncOperation<in I, out O> {
    fun execute(input: I, onError: ErrorCallback, onSuccess: Callback<O>): AsyncOperationInProgress
}
