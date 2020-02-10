package de.steenbergen.architecture.sample.ui.login.net

import de.steenbergen.architecture.sample.ui.login.domain.AuthResponse
import de.steenbergen.architecture.sample.ui.login.domain.UserAuthPayload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/mobile/auth")
    fun postUser(
        @Body authenticationResponse: UserAuthPayload
    ): Call<AuthResponse>
}
