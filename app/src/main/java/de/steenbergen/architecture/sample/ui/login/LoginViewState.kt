package de.steenbergen.architecture.sample.ui.login

sealed class LoginViewState {
    object Initial : LoginViewState()
    object LoginStarted : LoginViewState()
    data class Error(
        val errorMessage: String,
        val emailError: String?,
        val passwordError: String?
    ) : LoginViewState()

    object LoginSuccess : LoginViewState()
}
