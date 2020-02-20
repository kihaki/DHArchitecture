package de.steenbergen.architecture.async.impl.coroutines

import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncOperationInProgress
import kotlinx.coroutines.*

class CoroutinesEngine(
    private val scope: CoroutineScope = GlobalScope,
    private val workDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val callbackDispatcher: CoroutineDispatcher = Dispatchers.Main
) : AsyncEngine {

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: (Throwable) -> Unit,
        onSuccess: (O) -> Unit
    ): AsyncOperationInProgress {
        return CoroutinesAsyncOperationInProgress(
            (scope + workDispatcher).launch {
                try {
                    val output = operation(input)
                    withContext(callbackDispatcher) {
                        if (isActive) {
                            onSuccess(output)
                        }
                    }
                } catch (error: Throwable) {
                    if (error !is CancellationException) {
                        withContext(callbackDispatcher) {
                            onError(error)
                        }
                    }
                }
            }
        )
    }

    class CoroutinesAsyncOperationInProgress(private val job: Job) : AsyncOperationInProgress {
        override fun close() {
            job.cancel()
        }
    }
}
