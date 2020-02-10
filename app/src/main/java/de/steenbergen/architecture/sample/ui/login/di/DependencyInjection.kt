package de.steenbergen.architecture.sample.ui.login.di

import de.steenbergen.architecture.App
import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.Callback
import de.steenbergen.architecture.async.contract.Operation
import de.steenbergen.architecture.async.impl.coroutines.CoroutinesEngine
import de.steenbergen.architecture.sample.ui.login.domain.Session
import de.steenbergen.architecture.sample.ui.login.db.SessionDao
import de.steenbergen.architecture.sample.ui.login.net.LoginApi
import de.steenbergen.architecture.storage.contract.ObservableValue
import de.steenbergen.architecture.storage.impl.rx.RxNullableObservableValue
import de.steenbergen.architecture.storage.contract.Try
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


fun injectSessionRoomDb(): SessionDao {
    return App.instance.db.sessionDao()
}

fun injectSessionObserver(callback: Callback<Try<Session?>>): ObservableValue<Session?> =
    RxNullableObservableValue(
        observable = injectSessionRoomDb().observe(),
        callback = callback
    )

fun injectLoginApi(): LoginApi {
    return Retrofit.Builder()
        .baseUrl("https://de-st.usehurrier.com/api/mobile/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(LoginApi::class.java)
}

fun injectCoroutinesEngine(scope: CoroutineScope): AsyncEngine {
    return CoroutinesEngine(scope)
}

fun <I> injectArtificialWait(sleepTimeMillis: Long): Operation<I, I> = { passThroughData ->
    Thread.sleep(sleepTimeMillis)
    passThroughData
}
