package de.steenbergen.architecture.sample.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScoped
import de.steenbergen.architecture.async.contract.AsyncEngine
import de.steenbergen.architecture.async.contract.AsyncOperation
import de.steenbergen.architecture.async.contract.AsyncOperationInProgress
import de.steenbergen.architecture.async.contract.plus
import de.steenbergen.architecture.sample.ui.login.LoginViewState.*
import de.steenbergen.architecture.sample.ui.login.di.injectAsyncEngine
import de.steenbergen.architecture.sample.ui.login.di.injectLoginApi
import de.steenbergen.architecture.sample.ui.login.di.injectSessionRoomDb
import de.steenbergen.architecture.sample.ui.login.domain.AuthResponse
import de.steenbergen.architecture.sample.ui.login.domain.Session
import de.steenbergen.architecture.sample.ui.login.domain.UserLoginPayload
import de.steenbergen.architecture.sample.ui.login.net.ServerErrorException
import de.steenbergen.architecture.sample.ui.login.usecase.LoginOperation

class LoginViewModel : ViewModel() {

    var goToSession: (() -> Unit)? = null

    private val _viewState: MutableLiveData<LoginViewState> = MutableLiveData(Initial)
    val viewState: LiveData<LoginViewState> = _viewState

    private val engine: AsyncEngine = injectAsyncEngine()

    private val storeSessionOp: AsyncOperation<Session, Unit> =
        { session: Session ->
            injectSessionRoomDb().store(session)
        } + engine

    private val loginOp: AsyncOperation<UserLoginPayload, AuthResponse> =
        LoginOperation(injectLoginApi()) + engine

    private var loginProcess: AsyncOperationInProgress? = null

    fun login(userEmail: String, password: String) {
        _viewState.value = LoginStarted
        loginProcess?.close()
        loginProcess = viewModelScoped(
            loginOp.execute(
                input = UserLoginPayload(
                    userEmail,
                    password
                ),
                onSuccess = ::onLoginSuccess,
                onError = ::onLoginError
            )
        )
    }

    private fun onLoginSuccess(result: AuthResponse) {
        Log.i("LoginSample", "Login success: $result")
        storeSessionOp.execute(
            input = Session(
                result.token
            ),
            onSuccess = {
                _viewState.value = LoginSuccess
                goToSession?.invoke()
                _viewState.value = Initial
            },
            onError = {
                _viewState.value = Initial
                // We can do something here or not
                it.printStackTrace()
            }
        )
    }

    private fun onLoginError(throwable: Throwable) {
        _viewState.value =
            Error(throwable.localizedMessage ?: "No error message", "Check email", "Check password")
        Log.e(
            "LoginSample",
            "Received error ${throwable.localizedMessage} on ${Thread.currentThread()}"
        )
        throwable.printStackTrace()
        if (throwable is ServerErrorException) {
            Log.e("LoginSample", "ErrorBody: ${throwable.response.errorBody()?.string()}")
        }
    }

    // TODO: Test if loginProcess is really automatically closed
//    override fun onCleared() {
//        super.onCleared()
//        loginProcess?.close()
//    }
}
