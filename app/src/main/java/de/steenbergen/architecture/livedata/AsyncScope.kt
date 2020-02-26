package de.steenbergen.architecture.livedata

interface AsyncScope<T> {

    suspend fun emit(value: T)

    val latestValue: T?
}
