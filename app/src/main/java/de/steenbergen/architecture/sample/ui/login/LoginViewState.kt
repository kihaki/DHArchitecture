package de.steenbergen.architecture.sample.ui.login

sealed class LoginViewState {
    object Initial : LoginViewState()
    object LoginStarted : LoginViewState()
    data class LoginError(
        val errorMessage: String,
        val emailError: String? = null,
        val passwordError: String? = null
    ) : LoginViewState()

    object LoginSuccess : LoginViewState()
}
