package de.steenbergen.architecture.sample.ui.login

import androidx.lifecycle.*
import de.steenbergen.architecture.livedata.liveData
import de.steenbergen.architecture.livedata.switchMap
import de.steenbergen.architecture.sample.ui.login.LoginViewState.*
import de.steenbergen.architecture.sample.ui.login.di.injectLoginApi
import de.steenbergen.architecture.sample.ui.login.di.injectSessionRoomDb
import de.steenbergen.architecture.sample.ui.login.domain.Session
import de.steenbergen.architecture.sample.ui.login.domain.UserLoginPayload
import de.steenbergen.architecture.sample.ui.login.usecase.LoginOperation

class LoginViewModel : ViewModel() {
    var goToSession: (() -> Unit)? = null
    private val loginOperation = LoginOperation(injectLoginApi())

    val viewState: LiveData<LoginViewState>
    private val loginPayload = MutableLiveData<UserLoginPayload>()

    init {
        viewState = loginPayload.switchMap {
            if (it != null) {
                // this operation will clear itself when liveData observers are removed
                liveData {
                    emit(LoginStarted)
                    // we can take try catch out into another extension
                    try {
                        val response = loginOperation(it)
                        //should not do this in ViewModel
                        injectSessionRoomDb().store(Session(response.token))
                        emit(LoginSuccess)
                        goToSession?.invoke()
                    } catch (e: Exception) {
                        emit(LoginError(e.localizedMessage ?: ""))
                    }
                }
            } else {
                MutableLiveData<LoginViewState>(Initial)
            }
        }
    }

    fun login(userEmail: String, password: String) {
        loginPayload.value = UserLoginPayload(userEmail, password)
    }
}
