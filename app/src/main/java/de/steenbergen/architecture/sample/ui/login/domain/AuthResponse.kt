package de.steenbergen.architecture.sample.ui.login.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @Json(name = "token")
    val token: String,
    @Json(name = "contract_type")
    val contractType: String,
    @Json(name = "pubnub_subscribe_key")
    val subscribeKey: String?,
    @Json(name = "pubnub_publish_key")
    val publishKey: String?,
    @Json(name = "pubnub_auth_key")
    val authKey: String?
)
