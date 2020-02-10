package de.steenbergen.architecture.sample.ui.login.net

import retrofit2.Response

fun <T> Response<T>.toThrowable(): ServerErrorException =
    ServerErrorException(this)
