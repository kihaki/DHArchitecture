package de.steenbergen.architecture.async.contract

/**
 * Combination of Operation with an AsyncEngine which enables running any Operation asynchronously
 * Separates the code from the way that it is executed
 */
class AsyncOperationImpl<in I, out O>(
    val work: Operation<I, O>,
    private val engine: AsyncEngine
) : AsyncOperation<I, O> {

    override fun execute(input: I, onError: ErrorCallback, onSuccess: Callback<O>): AsyncOperationInProgress {
        return engine.doAsync(input, work, onError, onSuccess)
    }

    companion object {
        operator fun invoke(work: () -> Unit, engine: AsyncEngine): AsyncOperationImpl<Unit, Unit> =
            AsyncOperationImpl(work = { work() }, engine = engine)
    }
}
