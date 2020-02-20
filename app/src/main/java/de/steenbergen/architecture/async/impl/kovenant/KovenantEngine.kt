package de.steenbergen.architecture.async.impl.kovenant

import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncOperationInProgress
import de.steenbergen.architecture.async.impl.UiThreadExecutor
import nl.komponents.kovenant.CancelablePromise
import nl.komponents.kovenant.Context
import nl.komponents.kovenant.Kovenant
import nl.komponents.kovenant.jvm.asDispatcher
import nl.komponents.kovenant.task
import java.util.concurrent.Executors

class KovenantEngine(
    private val context: Context = Kovenant.createContext {
        workerContext {
            dispatcher = Executors.newFixedThreadPool(2).asDispatcher()
        }
        callbackContext {
            dispatcher = UiThreadExecutor.asDispatcher()
        }
    }
) : AsyncEngine {

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: (Throwable) -> Unit,
        onSuccess: (O) -> Unit
    ): AsyncOperationInProgress {
        return KovenantAsyncOperationInProgress(
            (task(context) {
                operation(input)
            } success {
                onSuccess(it)
            } fail {
                if (!it.isCancellationByUser()) {
                    // getting cancelled is not an error
                    onError(it)
                }
            }) as CancelablePromise<O, Exception>
        )
    }

    private fun Throwable.isCancellationByUser() = this is KovenantEngineCancelledException

    private class KovenantEngineCancelledException : Exception()

    class KovenantAsyncOperationInProgress(private val promise: CancelablePromise<*, Exception>) :
        AsyncOperationInProgress {

        override fun close() {
            promise.cancel(KovenantEngineCancelledException())
        }
    }
}
