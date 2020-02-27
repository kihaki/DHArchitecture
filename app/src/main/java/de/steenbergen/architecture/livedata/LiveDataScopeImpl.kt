package de.steenbergen.architecture.livedata

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AsyncScopeImpl<T>(
    private var target: CoroutineLiveData<T>,
    context: CoroutineContext
) : AsyncScope<T> {

    override val latestValue: T?
        get() = target.value

    // use `liveData` provided context + main dispatcher to communicate with the target
    // LiveData. This gives us main thread safety as well as cancellation cooperation
    private val coroutineContext = context + Dispatchers.Main.immediate

    override suspend fun emit(value: T) = withContext(coroutineContext) {
        target.value = value
    }
}
