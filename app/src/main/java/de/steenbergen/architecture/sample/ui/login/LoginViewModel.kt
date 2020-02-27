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
    private val loginPayload = MutableLiveData<UserLoginPayload>(null)

    init {
        viewState = loginPayload.switchMap {
            if (it != null) {
                loginLiveData(it)
            } else {
                MutableLiveData<LoginViewState>(Initial)
            }
        }
    }

    /**
     * This login operation uses [liveData] extension to perform long running tasks.
     * this operation will be cleared automatically when the LiveData observers are removed
     *
     *  - the database operation should be moved to a repository and should be accessed via a use case
     *  - we can extract try catch into another extension if we want to provide success and error callbacks separately
     * */
    private fun loginLiveData(userLoginPayload: UserLoginPayload): LiveData<LoginViewState> {
        return liveData {
            emit(LoginStarted)
            try {
                val response = loginOperation(userLoginPayload)
                // will not run if loginOperation resulted in error
                injectSessionRoomDb().store(Session(response.token))
                emit(LoginSuccess)
                goToSession?.invoke()
            } catch (e: Throwable) {
                emit(LoginError(e.localizedMessage))
            }
            emit(Initial)
        }
    }

    fun login(userEmail: String, password: String) {
        loginPayload.value = UserLoginPayload(userEmail, password)
    }
}
