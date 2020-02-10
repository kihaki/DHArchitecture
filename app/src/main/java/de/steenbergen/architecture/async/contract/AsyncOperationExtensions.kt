package de.steenbergen.architecture.async.contract

@JvmName("plusNoInputNoOutputMethod")
operator fun (() -> Unit).plus(engine: AsyncEngine): Task<Unit, Unit> =
    this.runWith(engine)

@JvmName("plusNoInputNoOutput")
operator fun NoInputNoOutputOperation.plus(engine: AsyncEngine): Task<Unit, Unit> =
    this.runWith(engine)

@JvmName("plusNoOutput")
operator fun <I> NoOutputOperation<I>.plus(engine: AsyncEngine): Task<I, Unit> =
    this.runWith(engine)

//@JvmName("plusNoInput")
//operator fun <O> NoInputOperation<O>.plus(engine: AsyncEngine): Task<Any?, O> =
//    this.runWith(engine)

@JvmName("plusOperation")
operator fun <I, O> Operation<I, O>.plus(engine: AsyncEngine): Task<I, O> =
    this.runWith(engine)

@JvmName("runWithNoInputNoOutput")
fun NoInputNoOutputOperation.runWith(engine: AsyncEngine): Task<Unit, Unit> =
    this.runWith(engine)

@JvmName("runWithNoInputNoOutputMethod")
fun (() -> Unit).runWith(engine: AsyncEngine): Task<Unit, Unit> =
    Task(work = this, engine = engine)

@JvmName("runWithNoOutput")
fun <I> NoOutputOperation<I>.runWith(engine: AsyncEngine): Task<I, Unit> =
    Task(work = this, engine = engine)

//@JvmName("runWithWithNoInput")
//fun <O> NoInputOperation<O>.runWith(engine: AsyncEngine): Task<Any?, O> =
//    Task(work = this, engine = engine)

@JvmName("runWithWithOperation")
fun <I, O> Operation<I, O>.runWith(engine: AsyncEngine): Task<I, O> =
    Task(work = this, engine = engine)

operator fun <I, O, T> AsyncOperation<I, O>.plus(other: AsyncOperation<O, T>): CompoundAsyncOperation<I, O, T> =
    this.then(other)

fun <I, O, T> AsyncOperation<I, O>.then(other: AsyncOperation<O, T>): CompoundAsyncOperation<I, O, T> =
    CompoundAsyncOperation(this, other)
