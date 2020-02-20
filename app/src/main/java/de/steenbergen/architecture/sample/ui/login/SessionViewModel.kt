package de.steenbergen.architecture.sample.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.steenbergen.architecture.async.contract.plus
import de.steenbergen.architecture.sample.ui.login.SessionViewState.LoadingSession
import de.steenbergen.architecture.sample.ui.login.di.injectAsyncEngine
import de.steenbergen.architecture.sample.ui.login.di.injectSessionObserver
import de.steenbergen.architecture.sample.ui.login.di.injectSessionRoomDb
import de.steenbergen.architecture.storage.contract.Try

class SessionViewModel : ViewModel() {

    var closeView: (() -> Unit)? = null

    private val ioEngine = injectAsyncEngine()
    private val logoutProcess = { injectSessionRoomDb().nuke() } + ioEngine

    private val sessionObserver = injectSessionObserver { result ->
        when (result) {
            is Try.Success -> {
                Log.i("LoginSample", "Received new session!: ${result.value}")
                if (result.value != null) {
                    _viewState.value = SessionViewState.Session(token = result.value.token)
                } else {
                    _viewState.value = LoadingSession
                }
            }
            is Try.Error -> {
                Log.e("LoginSample", "SessionError: ${result.error}")
                result.error.printStackTrace()
                // database connection broken, TODO: Fix
                _viewState.value = LoadingSession
            }
        }
    }

    private val _viewState: MutableLiveData<SessionViewState> =
        MutableLiveData(LoadingSession)
    val viewState: LiveData<SessionViewState> = _viewState

    fun logout() {
        logoutProcess.execute(
            input = Unit,
            onError = { closeView() },
            onSuccess = { closeView() }
        )
    }

    private fun closeView() {
        closeView?.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        sessionObserver.stop()
    }
}

