package de.steenbergen.architecture.sample.ui.login.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserLoginPayload(
    val email: String,
    val password: String
)
