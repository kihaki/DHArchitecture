package de.steenbergen.architecture.sample.ui.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.EditorInfo.IME_NULL
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.steenbergen.architecture.R
import de.steenbergen.architecture.sample.LoginActivity
import de.steenbergen.architecture.sample.ui.login.LoginViewState.*
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment(R.layout.login_fragment) {

    private val onEnterStartLogin =
        TextView.OnEditorActionListener { _: TextView?, action: Int?, event: KeyEvent? ->
            when {
                event?.action == IME_NULL -> {
                    loginView_loginButton.callOnClick()
                    true
                }
                action == IME_ACTION_NEXT -> {
                    loginView_loginButton.callOnClick()
                    true
                }
                else -> {
                    false
                }
            }
        }

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java).apply {
            goToSession = { (activity as? LoginActivity)?.goToSession() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is Initial -> {
                    loginView_loginProgress.visibility = View.GONE

                    loginView_userEmailInput.isEnabled = true
                    loginView_passwordInput.isEnabled = true
                    loginView_loginButton.isEnabled = true

                    loginView_userEmailInput.error = null
                    loginView_passwordInput.error = null
                }
                is LoginStarted -> {
                    loginView_loginProgress.visibility = View.VISIBLE

                    loginView_userEmailInput.isEnabled = false
                    loginView_passwordInput.isEnabled = false
                    loginView_loginButton.isEnabled = false

                    loginView_userEmailInput.error = null
                    loginView_passwordInput.error = null
                }
                is LoginSuccess -> {
                    loginView_loginProgress.visibility = View.GONE

                    loginView_userEmailInput.isEnabled = false
                    loginView_passwordInput.isEnabled = false
                    loginView_loginButton.isEnabled = false

                    loginView_userEmailInput.error = null
                    loginView_passwordInput.error = null
                }
                is LoginError -> {
                    loginView_loginProgress.visibility = View.GONE

                    loginView_userEmailInput.isEnabled = true
                    loginView_passwordInput.isEnabled = true
                    loginView_loginButton.isEnabled = true

                    loginView_userEmailInput.error = state.emailError
                    loginView_passwordInput.error = state.passwordError

                    Toast.makeText(
                        context,
                        "Login failed: ${state.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        loginView_loginButton.setOnClickListener {
            viewModel.login(
                loginView_userEmailInput.text.toString(),
                loginView_passwordInput.text.toString()
            )
        }

        loginView_loginButton.setOnEditorActionListener(onEnterStartLogin)
        loginView_passwordInput.setOnEditorActionListener(onEnterStartLogin)
    }
}
