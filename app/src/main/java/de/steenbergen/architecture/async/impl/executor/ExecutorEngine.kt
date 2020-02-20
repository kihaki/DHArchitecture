package de.steenbergen.architecture.async.impl.executor

import android.os.Handler
import android.os.Looper
import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncOperationInProgress
import de.steenbergen.architecture.async.contract.Callback
import de.steenbergen.architecture.async.contract.ErrorCallback
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ExecutorEngine(
    private val workExecutor: Executor = threadPoolWorkExecutor,
    private val callbackExecutor: Executor = uiWorkExecutor
) : AsyncEngine {

    companion object {
        private val threadPoolWorkExecutor: Executor = Executors.newFixedThreadPool(5)
        private val uiWorkExecutor: Executor = UiThreadExecutor()

        private class UiThreadExecutor : Executor {
            private val uiHandler: Handler =
                Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                uiHandler.post(command)
            }
        }
    }

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: ErrorCallback,
        onSuccess: Callback<O>
    ): AsyncOperationInProgress {
        return RunnableWorker(input, operation, onError, onSuccess, workExecutor, callbackExecutor)
    }

    class RunnableWorker<I, O>(
        input: I,
        operation: (I) -> O,
        private val onError: ErrorCallback,
        private val onSuccess: Callback<O>,
        workExecutor: Executor,
        private val callbackExecutor: Executor
    ) : AsyncOperationInProgress {
        @Volatile
        private var isCancelled: Boolean = false

        init {
            workExecutor.execute {
                try {
                    val result = operation(input)
                    if (!isCancelled) {
                        callbackExecutor.execute { onSuccess(result) }
                    }
                } catch (error: Throwable) {
                    if (!isCancelled) {
                        callbackExecutor.execute { onError(error) }
                    }
                }
            }
        }

        override fun close() {
            isCancelled = true
        }
    }
}
