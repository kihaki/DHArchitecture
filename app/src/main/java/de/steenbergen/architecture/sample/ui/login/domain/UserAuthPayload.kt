package de.steenbergen.architecture.sample.ui.login.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAuthPayload(
    @Json(name = "user")
    val userLoginPayload: UserLoginPayload
)
