package de.steenbergen.architecture.sample.ui.login.usecase

import de.steenbergen.architecture.sample.ui.login.domain.AuthResponse
import de.steenbergen.architecture.sample.ui.login.domain.UserAuthPayload
import de.steenbergen.architecture.sample.ui.login.domain.UserLoginPayload
import de.steenbergen.architecture.sample.ui.login.net.LoginApi
import de.steenbergen.architecture.sample.ui.login.net.toThrowable

class LoginOperation(private val loginApi: LoginApi) :
        (UserLoginPayload) -> AuthResponse {

    override fun invoke(input: UserLoginPayload): AuthResponse {
        val authResponse = loginApi.postUser(UserAuthPayload(input)).execute()
        if (authResponse.isSuccessful) {
            return authResponse.body()!!
        } else {
            throw authResponse.toThrowable()
        }
    }
}
