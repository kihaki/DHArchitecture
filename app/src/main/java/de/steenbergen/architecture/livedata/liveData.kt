package de.steenbergen.architecture.livedata

import androidx.lifecycle.LiveData
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.experimental.ExperimentalTypeInference

/**
 * This is copied from LiveData extensions library and altered to fit our needs.
 *
 * It uses coroutines internally but it can be changed without affecting the client.
 * The only "hard dependency" it has is on [LiveData]
 *
 * @param timeoutInMs The timeout in ms before cancelling the block if there are no active observers
 * ([LiveData.hasActiveObservers]. Defaults to [DEFAULT_TIMEOUT].
 */
@UseExperimental(ExperimentalTypeInference::class)
fun <T> liveData(
    timeoutInMs: Long = DEFAULT_TIMEOUT,
    @BuilderInference block: suspend AsyncScope<T>.() -> Unit
): LiveData<T> = CoroutineLiveData(EmptyCoroutineContext, timeoutInMs, block)

