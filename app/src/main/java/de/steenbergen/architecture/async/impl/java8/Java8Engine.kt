package de.steenbergen.architecture.async.impl.java8

import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncOperationInProgress
import de.steenbergen.architecture.async.impl.UiThreadExecutor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.function.Supplier

class Java8Engine(
    private val workExecutor: Executor = workerThreadPool,
    private val callbackExecutor: Executor = UiThreadExecutor
) : AsyncEngine {

    companion object {
        private val workerThreadPool by lazy {
            Executors.newFixedThreadPool(5)
        }
    }

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: (Throwable) -> Unit,
        onSuccess: (O) -> Unit
    ): AsyncOperationInProgress {
        return Java8FutureAsyncOperationInProgress(
            CompletableFuture.supplyAsync(Supplier<O> {
                operation(input)
            }, workExecutor)
                .thenAcceptAsync(Consumer<O> { onSuccess(it) }, callbackExecutor)
                .exceptionally {
                    onError(it)
                    null
                }
        )
    }

    class Java8FutureAsyncOperationInProgress(private val future: CompletableFuture<*>) :
        AsyncOperationInProgress {

        override fun close() {
            future.cancel(true)
        }
    }
}
