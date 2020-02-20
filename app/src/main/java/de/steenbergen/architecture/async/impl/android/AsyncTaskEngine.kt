package de.steenbergen.architecture.async.impl.android

import android.os.AsyncTask
import de.steenbergen.architecture.async.contract.*

class AsyncTaskEngine : AsyncEngine {

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: ErrorCallback,
        onSuccess: Callback<O>
    ): AsyncOperationInProgress {
        return AsyncTaskOperationInProgress(WorkerTask(operation, onError, onSuccess).apply { execute(input) })
    }

    private class WorkerTask<I, O>(
        private val operation: Operation<I, O>,
        private val onError: ErrorCallback,
        private val onSuccess: Callback<O>
    ) : AsyncTask<I, Unit, O?>() {
        override fun doInBackground(vararg params: I): O? {
            try {
                return operation(params.first())
            } catch (error: Throwable) {
                onError(error)
            }
            return null
        }

        override fun onPostExecute(result: O?) {
            result?.let(onSuccess)
        }
    }

    class AsyncTaskOperationInProgress(private val task: AsyncTask<*, *, *>) : AsyncOperationInProgress {
        override fun close() {
            task.cancel(true)
        }
    }
}
