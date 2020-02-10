package de.steenbergen.architecture.storage.impl.rx

import de.steenbergen.architecture.async.contract.Callback
import de.steenbergen.architecture.storage.contract.ObservableValue
import de.steenbergen.architecture.storage.contract.Try
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxNullableObservableValue<T>(
    workScheduler: Scheduler = Schedulers.io(),
    callbackScheduler: Scheduler = AndroidSchedulers.mainThread(),
    observable: Observable<List<T>>,
    callback: Callback<Try<T?>>
) : ObservableValue<T?>(callback) {

    private val connection = observable
        .subscribeOn(workScheduler)
        .observeOn(callbackScheduler)
        .subscribe(
            {
                if (it.isEmpty()) {
                    emit(Try.Success(null))
                } else {
                    emit(Try.Success(it.first()))
                }
            },
            { emit(Try.Error(it)) }
        )

    override fun stop() {
        super.stop()
        connection.dispose()
    }
}
