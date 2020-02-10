package de.steenbergen.architecture.async.contract

/**
 * Takes synchronous code and runs it asynchronously
 */
interface AsyncEngine {

    fun <I, O> doAsync(
        input: I,
        operation: Operation<I, O>,
        onError: ErrorCallback,
        onSuccess: Callback<O>
    ): AsyncWork
}

interface AsyncWork {
    fun cancel()
}
