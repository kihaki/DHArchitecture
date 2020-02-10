package de.steenbergen.architecture.async.impl.coroutines

import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncWork
import kotlinx.coroutines.*

class CoroutinesEngine(
    private val scope: CoroutineScope,
    private val workDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val callbackDispatcher: CoroutineDispatcher = Dispatchers.Main
) : AsyncEngine {

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: (Throwable) -> Unit,
        onSuccess: (O) -> Unit
    ): AsyncWork {
        return CoroutinesAsyncWork(
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

    class CoroutinesAsyncWork(private val job: Job) : AsyncWork {
        override fun cancel() {
            job.cancel()
        }
    }
}
