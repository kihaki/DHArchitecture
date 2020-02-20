package de.steenbergen.architecture.async.contract

typealias Operation<I, O> = (I) -> O
typealias NoInputNoOutputOperation = Operation<Unit, Unit>
typealias NoInputOperation<O> = Operation<Any?, O>
typealias NoOutputOperation<I> = Operation<I, Unit>
typealias ErrorCallback = (Throwable) -> Unit
typealias Callback<O> = (O) -> Unit
