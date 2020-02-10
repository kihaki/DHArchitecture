package de.steenbergen.architecture.async.contract

inline operator fun <I, O, T> Operation<I, O>.plus(crossinline other: Operation<O, T>): Operation<I, T> =
    this.then(other)

inline fun <I, O, T> Operation<I, O>.then(crossinline other: Operation<O, T>): Operation<I, T> =
    { input ->
        other(this(input))
    }

inline operator fun <O, T> (() -> O).plus(crossinline other: Operation<O, T>): Operation<Unit, T> =
    { other(this()) }
