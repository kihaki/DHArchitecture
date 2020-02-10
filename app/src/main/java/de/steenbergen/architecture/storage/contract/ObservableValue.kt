package de.steenbergen.architecture.storage.contract

import androidx.annotation.CallSuper
import de.steenbergen.architecture.async.contract.Callback

abstract class ObservableValue<O>(
    private var callback: Callback<Try<O>>?
) {
    protected fun emit(value: Try<O>) {
        callback?.invoke(value)
    }

    @CallSuper
    open fun stop() {
        callback = null
    }
}
