package de.steenbergen.architecture.async.contract

class CompoundAsyncOperation<in I, out I2, out O>(
    private val first: AsyncOperation<I, I2>,
    private val second: AsyncOperation<I2, O>
) : AsyncOperation<I, O> {

    private var asyncWork = CompoundAsyncOperationInProgress(null, null)

    override fun execute(input: I, onError: ErrorCallback, onSuccess: Callback<O>): AsyncOperationInProgress {
        asyncWork.first = first.execute(input, onError) { output ->
            asyncWork.second = second.execute(output, onError, onSuccess)
        }
        return asyncWork
    }

    class CompoundAsyncOperationInProgress(
        internal var first: AsyncOperationInProgress?,
        internal var second: AsyncOperationInProgress?
    ) : AsyncOperationInProgress {
        override fun close() {
            first?.close()
            second?.close()
        }
    }
}
