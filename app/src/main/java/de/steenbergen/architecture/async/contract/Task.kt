package de.steenbergen.architecture.async.contract

/**
 * Combination of Operation with an AsyncEngine which enables running any Operation asynchronously
 * Separates the code from the way that it is executed
 */
class Task<in I, out O>(
    val work: Operation<I, O>,
    private val engine: AsyncEngine
) : AsyncOperation<I, O> {

    override fun execute(input: I, onError: ErrorCallback, onSuccess: Callback<O>): AsyncWork {
        return engine.doAsync(input, work, onError, onSuccess)
    }

    companion object {
        operator fun invoke(work: () -> Unit, engine: AsyncEngine): Task<Unit, Unit> =
            Task(work = { input: Any? -> work() }, engine = engine)
    }
}
