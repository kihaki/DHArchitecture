package de.steenbergen.architecture.async.contract

inline fun <A, B, C> ((A) -> B).then(crossinline other: ((B) -> C)): ((A) -> C) {
    return { param: A -> other(this(param)) }
}

inline operator fun <A, B, C> ((A) -> B).plus(crossinline other: ((B) -> C)): ((A) -> C) {
    return this.then(other)
}

inline fun <B, C> (() -> B).then(crossinline other: ((B) -> C)): (() -> C) {
    return { other(this()) }
}

inline operator fun <B, C> (() -> B).plus(crossinline other: ((B) -> C)): (() -> C) {
    return this.then(other)
}
