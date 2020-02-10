package de.steenbergen.architecture.sample.ui.login

sealed class SessionViewState {
    object LoadingSession : SessionViewState()
    data class Session(val token: String) : SessionViewState()
}
