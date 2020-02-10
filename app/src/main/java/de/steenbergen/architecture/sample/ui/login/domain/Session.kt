package de.steenbergen.architecture.sample.ui.login.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey val id: Int = 1,
    val token: String
) {
    companion object {
        operator fun invoke(token: String) =
            Session(
                token = token
            )
    }
}
