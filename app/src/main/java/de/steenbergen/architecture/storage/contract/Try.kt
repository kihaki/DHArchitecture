package de.steenbergen.architecture.storage.contract

sealed class Try<out T> {
    data class Success<T>(val value: T) : Try<T>()
    data class Error<T>(val error: Throwable) : Try<T>()

    fun onValue(onValue: (T) -> Unit) = fold(onValue = onValue, onError = {})

    fun onError(onError: (Throwable) -> Unit) = fold(onValue = {}, onError = onError)

    fun fold(
        onValue: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        when (this) {
            is Success -> onValue(value)
            is Error -> onError(error)
        }
    }

    companion object {
        operator fun <T> invoke(operation: () -> T): Try<T> = try {
            Success(operation())
        } catch (error: Throwable) {
            Error(error)
        }
    }
}
