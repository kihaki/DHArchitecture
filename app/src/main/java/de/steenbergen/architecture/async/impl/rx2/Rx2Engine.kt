package de.steenbergen.architecture.async.impl.rx2

import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncOperationInProgress
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Rx2Engine(
    private val workScheduler: Scheduler = Schedulers.io(),
    private val callbackScheduler: Scheduler = AndroidSchedulers.mainThread()
) : AsyncEngine {

    override fun <I, O> doAsync(
        input: I,
        operation: (I) -> O,
        onError: (Throwable) -> Unit,
        onSuccess: (O) -> Unit
    ): AsyncOperationInProgress {
        return Rx2AsyncOperationInProgress(
            Single.create<O> { emitter ->
                try {
                    val result = operation(input)
                    emitter.onSuccess(result)
                } catch (exception: Throwable) {
                    emitter.onError(exception)
                }
            }
                .subscribeOn(workScheduler)
                .observeOn(callbackScheduler)
                .subscribe(onSuccess, onError)
        )
    }

    class Rx2AsyncOperationInProgress(private val subscription: Disposable) :
        AsyncOperationInProgress {
        override fun close() {
            subscription.dispose()
        }
    }
}
