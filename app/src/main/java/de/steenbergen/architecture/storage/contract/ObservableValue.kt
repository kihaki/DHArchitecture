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

//abstract class OVI<O> : OV<O>, RemoveObs {
//
//    override fun observe(onError: ErrorCallback, onValue: Callback<O>): Observation {
//        observations.add()
//    }
//
//    override fun onStopOps(obs: Observation) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
//
//interface OV<O> {
//    fun observe(onError: ErrorCallback, onValue: Callback<O>): Observation
//}
//
//interface RemoveObs {
//    fun onStopOps(obs: Observation)
//}
//
//interface ObservationRegistry<O> {
//    fun notify(data: O)
//    fun addObservation(observation: Observation<O>)
//    fun removeObservation(observation: Observation<O>)
//}
//
//class ObservationRegistryImpl<O> : ObservationRegistry<O> {
//    private val observations = mutableListOf<Observation<O>>()
//
//    override fun notify(data: O) {
//        observations.forEach {
//            observation
//        }
//    }
//
//    override fun addObservation(observation: Observation<O>) {
//        observations.add(observation)
//    }
//
//    override fun removeObservation(observation: Observation<O>) {
//        observations.remove(observation)
//    }
//
//}
//
//interface Observation {
//    fun stop()
//}
//
//class ObservationImpl(private val parent: RemoveObs, private val successCallback: Callback) : Observation {
//    override fun stop() {
//        parent.onStopOps(this)
//    }
//}
