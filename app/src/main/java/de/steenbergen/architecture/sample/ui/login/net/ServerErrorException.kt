package de.steenbergen.architecture.sample.ui.login.net

import retrofit2.Response

data class ServerErrorException(val response: Response<*>) : Throwable() {
    override fun getLocalizedMessage(): String? {
        return response.errorBody()?.string()
    }
}
