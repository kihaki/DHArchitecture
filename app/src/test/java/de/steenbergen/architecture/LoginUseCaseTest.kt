package de.steenbergen.architecture

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import de.steenbergen.architecture.sample.ui.login.usecase.LoginUseCase
import de.steenbergen.architecture.sample.ui.login.domain.AuthResponse
import de.steenbergen.architecture.sample.ui.login.domain.UserLoginPayload
import de.steenbergen.architecture.sample.ui.login.net.LoginApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.mock.Calls

class LoginUseCaseTest {

    private val loginApi = mock<LoginApi>()
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        loginUseCase =
            LoginUseCase(
                loginApi
            )
    }

    @Test
    fun verifySuccess() {
        // Desired results
        val desiredResponse = AuthResponse(
            "token",
            "contractType",
            null,
            null,
            null
        )

        // Setup
        whenever(loginApi.postUser(any())).thenReturn(Calls.response(desiredResponse))

        // Test
        val response = loginUseCase.invoke(UserLoginPayload("someEmail", "somePassword"))

        // Verification
        verify(loginApi).postUser(any())
        Assert.assertEquals(desiredResponse, response)
    }

    @Test
    fun verifyFailure() {
        // Desired results
        val desiredError = RuntimeException()

        // Setup
        var response: AuthResponse? = null
        var thrownError: Throwable? = null
        whenever(loginApi.postUser(any())).thenReturn(Calls.failure(desiredError))

        // Test
        try {
            response = loginUseCase.invoke(UserLoginPayload("someEmail", "somePassword"))
        } catch (error: Throwable) {
            thrownError = error
        }

        // Verification
        verify(loginApi).postUser(any())
        Assert.assertEquals(null, response)
        Assert.assertEquals(desiredError, thrownError)
    }
}
